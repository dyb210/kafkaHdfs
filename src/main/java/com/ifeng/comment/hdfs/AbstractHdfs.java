/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ifeng.comment.hdfs;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

public abstract class AbstractHdfs {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractHdfs.class);
    private static final Integer DEFAULT_RETRY_COUNT = 3;
    /**
     * Half of the default Config.TOPOLOGY_MESSAGE_TIMEOUT_SECS
     */
    private static final int DEFAULT_TICK_TUPLE_INTERVAL_SECS = 15;
    protected SyncPolicy syncPolicy;
    private Path currentFile;
    protected transient FileSystem fs;
    protected FileNameFormat fileNameFormat;
    protected int rotation = 0;
    protected String fsUrl;
    protected String configKey;
    protected transient Object writeLock;
    protected long offset = 0;
    protected Integer fileRetryCount = DEFAULT_RETRY_COUNT;
    protected Integer tickTupleInterval = DEFAULT_TICK_TUPLE_INTERVAL_SECS;
    protected FileRotationPolicy rotationPolicy;

    protected transient Configuration hdfsConfig;
    protected transient Timer rotationTimer;

    protected void rotateOutputFile() throws IOException {
        LOG.info("Rotating output file...");
        long start = System.currentTimeMillis();
        synchronized (this.writeLock) {
            closeOutputFile();
            this.rotation++;
            Path newFile = createOutputFile();
            this.currentFile = newFile;
        }
        long time = System.currentTimeMillis() - start;
        LOG.info("File rotation took {} ms.", time);
    }

    /**
     * Marked as final to prevent override. Subclasses should implement the doPrepare() method.
     * @param conf
     */
    public final void prepare(Map conf) {
        this.writeLock = new Object();
        if (this.syncPolicy == null) throw new IllegalStateException("SyncPolicy must be specified.");
        if (this.fsUrl == null) {
            throw new IllegalStateException("File system URL must be specified.");
        }

        this.fileNameFormat.prepare();
        this.hdfsConfig = new Configuration();
        hdfsConfig.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
        Map<String, Object> map = (Map<String, Object>) conf.get(this.configKey);
        if (map != null) {
            for (String key : map.keySet()) {
                this.hdfsConfig.set(key, String.valueOf(map.get(key)));
            }
        }
        try {
            //HdfsSecurityUtil.login(conf, hdfsConfig);
            doPrepare();
            this.currentFile = createOutputFile();

        } catch (Exception e) {
            throw new RuntimeException("Error preparing HdfsBolt: " + e.getMessage(), e);
        }

        if (this.rotationPolicy instanceof TimedRotationPolicy) {
            long interval = ((TimedRotationPolicy) this.rotationPolicy).getInterval();
            this.rotationTimer = new Timer(true);
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    try {
                        rotateOutputFile();
                    } catch (IOException e) {
                        LOG.warn("IOException during scheduled file rotation.", e);
                    }
                }
            };
            this.rotationTimer.scheduleAtFixedRate(task, interval, interval);
        }
    }

    public final void execute(byte[] tuple) {

        synchronized (this.writeLock) {
            boolean forceSync = false;
                try {
                    writeTuple(tuple);
                } catch (IOException e) {
                    //If the write failed, try to sync anything already written
                    LOG.info("Tuple failed to write, forcing a flush of existing data.");
                }
            }
            if (this.syncPolicy.mark(tuple, this.offset)) {
                int attempts = 0;
                boolean success = false;
                IOException lastException = null;
                // Make every attempt to sync the data we have.  If it can't be done then kill the bolt with
                // a runtime exception.  The filesystem is presumably in a very bad state.
                while (success == false && attempts < fileRetryCount) {
                    attempts += 1;
                    try {
                        syncTuples();

                        syncPolicy.reset();
                        success = true;
                    } catch (IOException e) {
                        LOG.warn("Data could not be synced to filesystem on attempt [{}]", attempts);
                        lastException = e;
                    }
                }
                // If unsuccesful fail the pending tuples
                if (success == false) {
                    throw new RuntimeException("Sync failed [" + attempts + "] times.", lastException);
                }
            }
            if (this.rotationPolicy.mark(tuple, this.offset)) {
                try {
                    rotateOutputFile();
                    this.rotationPolicy.reset();
                    this.offset = 0;
                } catch (IOException e) {
                    LOG.warn("File could not be rotated");
                    //At this point there is nothing to do.  In all likelihood any filesystem operations will fail.
                    //The next tuple will almost certainly fail to write and/or sync, which force a rotation.  That
                    //will give rotateAndReset() a chance to work which includes creating a fresh file handle.
                }
            }
        }


    /**
     * writes a tuple to the underlying filesystem but makes no guarantees about syncing data.
     *
     * this.offset is also updated to reflect additional data written
     *
     * @throws IOException
     */
    abstract protected void writeTuple(byte[] bytes) throws IOException;

    public abstract void doPrepare() throws IOException;

    /**
     * Make the best effort to sync written data to the underlying file system.  Concrete classes should very clearly
     * state the file state that sync guarantees.  For example, HdfsBolt can make a much stronger guarantee than
     * SequenceFileBolt.
     *
     * @throws IOException
     */
    abstract protected void syncTuples() throws IOException;


    abstract protected void closeOutputFile() throws IOException;

    abstract protected Path createOutputFile() throws IOException;


}
