package com.ifeng.comment.kafka;

import com.ifeng.comment.hdfs.HdfsUtile;
import kafka.consumer.KafkaStream;

/**
 * ConsumerKafkaStreamProcesser 线程负责
 *
 */
public class ConsumerKafkaStreamProcesser implements Runnable {
    protected KafkaStream<String, String> stream;
    protected int threadNumber;
    protected HdfsUtile hdfsBolt_pc;

    public ConsumerKafkaStreamProcesser(){

    }
    public ConsumerKafkaStreamProcesser(KafkaStream<String, String> stream, int threadNumber) {
        this.stream = stream;
        this.threadNumber = threadNumber;

    }
    public void prepare() {
    }
    public void execute(KafkaStream<String, String> stream) {
    }

    public void setStream(KafkaStream<String, String> stream) {
        this.stream = stream;
    }

    public void setThreadNumber(int threadNumber) {
        this.threadNumber = threadNumber;
    }

    public void setHdfsBolt_pc(HdfsUtile hdfsBolt_pc) {
        this.hdfsBolt_pc = hdfsBolt_pc;
    }

    @Override
    public void run() {
        try {
            prepare();
            execute(stream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ConsumerKafkaStreamProcesser copyKafkaStreamProcesser() {
        return this;
    }
}