package com.ifeng.comment.main;

import com.ifeng.comment.hdfs.CountSyncPolicy;
import com.ifeng.comment.hdfs.DefaultFileNameFormat;
import com.ifeng.comment.hdfs.TimedRotationPolicy;
import com.ifeng.comment.kafka.*;
import com.ifeng.comment.processer.VdnLog;
import com.ifeng.comment.util.DateUtil;

import java.util.Date;

/**
 * Created by duanyb on 2017/7/7.
 */
public class MainKafkaToHdfs {
    public static void main(String[] args) {
        String zookeeper = "10.90.34.11:2181,10.90.34.12:2181,10.90.34.13:2181,10.90.34.14:2181,10.90.34.15:2181";
        String groupId = "dybteswrq111tt";
        String topic = "ifeng_logs";
        int threads = 20;

        SplitFileInput splitFileInput = new VdnLog();
        JavaKafkaConsumerHighAPI example = new JavaKafkaConsumerHighAPI(topic, threads, zookeeper, groupId);
        ConsumerKafkaStreamProcesser consumerKafkaStreamProcesser = new KafKaToHdfs("hdfs://10.90.13.82:8020",new CountSyncPolicy(100),
                new TimedRotationPolicy(10.0f, TimedRotationPolicy.TimeUnit.MINUTES),
                new DefaultFileNameFormat().withPath("/dyb/dyb/pc/" + DateUtil.formatDate(new Date())).withPrefix("PC_").withExtension(".log")
                ,splitFileInput);

        example.setConsumerKafkaStreamProcesser(consumerKafkaStreamProcesser);
        new Thread(example).start();
        // 执行10秒后结束
        int sleepMillis = 600000890;
        try {
            Thread.sleep(sleepMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 关闭
        example.shutdown();
    }
}
