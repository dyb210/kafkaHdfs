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

import com.ifeng.comment.util.DateUtil;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.client.HdfsDataOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HdfsUtile extends AbstractHdfs {
    private static final Logger LOG = LoggerFactory.getLogger(HdfsUtile.class);

    private transient FSDataOutputStream out;

    public HdfsUtile withFsUrl(String fsUrl) {
        this.fsUrl = fsUrl;
        return this;
    }

    public HdfsUtile withConfigKey(String configKey) {
        this.configKey = configKey;
        return this;
    }

    public HdfsUtile withFileNameFormat(FileNameFormat fileNameFormat) {
        this.fileNameFormat = fileNameFormat;
        return this;
    }


    public HdfsUtile withSyncPolicy(SyncPolicy syncPolicy) {
        this.syncPolicy = syncPolicy;
        return this;
    }

    public HdfsUtile withRotationPolicy(FileRotationPolicy rotationPolicy) {
        this.rotationPolicy = rotationPolicy;
        return this;
    }

    public HdfsUtile withTickTupleIntervalSeconds(int interval) {
        this.tickTupleInterval = interval;
        return this;
    }

    public HdfsUtile withRetryCount(int fileRetryCount) {
        this.fileRetryCount = fileRetryCount;
        return this;
    }

    @Override
    public void doPrepare() throws IOException {
        LOG.info("Preparing HDFS Bolt...");
        this.fs = FileSystem.get(URI.create(this.fsUrl), hdfsConfig);
    }

    @Override
    protected void syncTuples() throws IOException {
        LOG.debug("Attempting to sync all data to filesystem");
        if (this.out instanceof HdfsDataOutputStream) {
            ((HdfsDataOutputStream) this.out).hsync(EnumSet.of(HdfsDataOutputStream.SyncFlag.UPDATE_LENGTH));
        } else {
            this.out.hsync();
        }
    }

    @Override
    protected void writeTuple(byte[] bytes) throws IOException {
        try {
            //byte[] bytes = this.format.format(tuple);
            out.write(bytes);
            this.offset += bytes.length;
        } catch (Exception e) {
        }
    }

    @Override
    protected void closeOutputFile() throws IOException {
        this.out.close();
    }

    @Override
    public Path createOutputFile() throws IOException {
        LOG.info("start createOutputFile" + this.fileNameFormat.getPath());
        Pattern pattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
        Matcher math = pattern.matcher(this.fileNameFormat.getPath());
        String realPath = this.fileNameFormat.getPath();
        if (math.find()) {
            realPath = realPath.substring(0, realPath.lastIndexOf("/"));
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(new Date());
//            calendar.add(Calendar.DATE,-1);
            realPath = realPath + "/" + DateUtil.formatDate(new Date());
        }
        Path path = new Path(realPath, this.fileNameFormat.getName(this.rotation, System.currentTimeMillis()));
        this.out = this.fs.create(path);
        LOG.info("end createOutputFile" + realPath);
        LOG.info("end createOutputFile" + path.toString());
        return path;
    }
}
