package com.ifeng.comment.kafka;

import com.ifeng.comment.hdfs.*;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;
import org.apache.commons.collections.map.HashedMap;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Created by duanyb on 2017/7/10.
 */
public class KafKaToHdfs extends ConsumerKafkaStreamProcesser {
    private String hdfs;
    private FileNameFormat fileNameFormat;
    private FileRotationPolicy rotationPolicy;
    private SyncPolicy syncPolicy;
    private SplitFileInput splitFileInput;

    public KafKaToHdfs(String hdfs, SyncPolicy syncPolicy, FileRotationPolicy rotationPolicy, FileNameFormat fileNameFormat_pc, SplitFileInput splitFileInput) {
        this.hdfs = hdfs;
        this.syncPolicy = syncPolicy;
        this.rotationPolicy = rotationPolicy;
        this.fileNameFormat = fileNameFormat_pc;
        this.splitFileInput = splitFileInput;
    }

    public KafKaToHdfs() {
    }

    /**
     * @param syncPolicy
     * @return
     */
    public KafKaToHdfs syncPolicy(SyncPolicy syncPolicy) {
        this.syncPolicy = checkNotNull(syncPolicy, "syncPolicy");
        return this;
    }

    /**
     * @param hdfs
     * @return
     */
    public KafKaToHdfs hdfs(String hdfs) {
        this.hdfs = checkNotNull(hdfs, "hdfs");
        return this;
    }

    /**
     * @param rotationPolicy
     * @return
     */
    public KafKaToHdfs rotationPolicy(FileRotationPolicy rotationPolicy) {
        this.rotationPolicy = checkNotNull(rotationPolicy, "rotationPolicy");
        return this;
    }

    /**
     * @param fileNameFormat
     * @return
     */
    public KafKaToHdfs fileNameFormat(FileNameFormat fileNameFormat) {
        this.fileNameFormat = checkNotNull(fileNameFormat, "fileNameFormat");
        return this;
    }

    /**
     * @param splitFileInput
     * @return
     */
    public KafKaToHdfs splitFileInput(SplitFileInput splitFileInput) {
        this.splitFileInput = checkNotNull(splitFileInput, "splitFileInput");
        return this;
    }


    @Override
    public ConsumerKafkaStreamProcesser copyKafkaStreamProcesser() {
        return new KafKaToHdfs(hdfs, syncPolicy, rotationPolicy, fileNameFormat, splitFileInput);
    }

    @Override
    public void prepare() {
        hdfsBolt_pc = new HdfsUtile().withFsUrl(hdfs).withFileNameFormat(fileNameFormat)
                .withRotationPolicy(rotationPolicy)
                .withSyncPolicy(syncPolicy);
        hdfsBolt_pc.prepare(new HashedMap());
    }

    @Override
    public void execute(KafkaStream<String, String> stream) {
        try {
            ConsumerIterator<String, String> iter = null;
            try {
                iter = stream.iterator();
            } catch (Exception e) {
                e.printStackTrace();
            }
            int count = 0;
            StringBuilder stringBuilder = new StringBuilder();
            while (iter.hasNext()) {
                try {
                    MessageAndMetadata value = iter.next();
                    if (value == null) {
                        Thread.sleep(1);
                    } else {
                        long offset = value.offset();
                        String key = (String) value.key();
                        String message = (String) value.message();
                        if (count % 1000 == 0) {
                            StringBuilder stringBuilder1 = stringBuilder;
                            try {
                                hdfsBolt_pc.execute(stringBuilder1.toString().getBytes());
                                stringBuilder = new StringBuilder();
                            } catch (Exception e) {
                                hdfsBolt_pc.createOutputFile();
                            }
                        } else {
                            String str_tmp = splitFileInput.splitLine(message);
                            if (str_tmp != null) {
                                stringBuilder.append(splitFileInput.splitLine(message));
                                stringBuilder.append("\r\n");
                            }
                        }
                        count++;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                hdfsBolt_pc.execute(stringBuilder.toString().getBytes());
            } catch (Exception e) {
                e.printStackTrace();
                hdfsBolt_pc.createOutputFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getHdfs() {
        return hdfs;
    }

    public FileNameFormat getFileNameFormat() {
        return fileNameFormat;
    }

    public void setFileNameFormat(FileNameFormat fileNameFormat) {
        this.fileNameFormat = fileNameFormat;
    }

    public FileRotationPolicy getRotationPolicy() {
        return rotationPolicy;
    }

    public void setRotationPolicy(FileRotationPolicy rotationPolicy) {
        this.rotationPolicy = rotationPolicy;
    }

    public SyncPolicy getSyncPolicy() {
        return syncPolicy;
    }

    public void setSyncPolicy(SyncPolicy syncPolicy) {
        this.syncPolicy = syncPolicy;
    }


    public SplitFileInput getSplitFileInput() {
        return splitFileInput;
    }

    public void setSplitFileInput(SplitFileInput splitFileInput) {
        this.splitFileInput = splitFileInput;
    }
}
