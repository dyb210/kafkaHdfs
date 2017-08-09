package com.ifeng.comment.main;

import com.ifeng.comment.hdfs.CountSyncPolicy;
import com.ifeng.comment.hdfs.DefaultFileNameFormat;
import com.ifeng.comment.hdfs.TimedRotationPolicy;
import com.ifeng.comment.kafka.ConsumerKafkaStreamProcesser;
import com.ifeng.comment.kafka.JavaKafkaConsumerHighAPI;
import com.ifeng.comment.kafka.KafKaToHdfs;
import com.ifeng.comment.kafka.SplitFileInput;
import com.ifeng.comment.processer.Pgc_stat_first;
import com.ifeng.comment.processer.VdnLog;
import com.ifeng.comment.util.DateUtil;

import java.util.Date;

/**
 * Created by duanyb on 2017/7/7.
 */
public class MainPgc_stat_first_stage {
    public static void main(String[] args) {
        String zookeeper = "10.90.34.11:2181,10.90.34.12:2181,10.90.34.13:2181,10.90.34.14:2181,10.90.34.15:2181";
        String groupId = "dybteswrq111tt";
        String topic = "pgc_stat_first_stage";
        int threads = 10;

        SplitFileInput splitFileInput = new Pgc_stat_first();
        JavaKafkaConsumerHighAPI javaKafkaConsumerHighAPI = new JavaKafkaConsumerHighAPI(topic, threads, zookeeper, groupId);
        ConsumerKafkaStreamProcesser consumerKafkaStreamProcesser = new KafKaToHdfs()
                .hdfs("hdfs://10.90.13.82:8020")
                .fileNameFormat(new DefaultFileNameFormat().withPath("/dyb/dyb/pgc_stat_first_stage/"+DateUtil.formatDate(new Date())).withPrefix("PGC_").withExtension(".log"))
                .syncPolicy(new CountSyncPolicy(100))
                .rotationPolicy(new TimedRotationPolicy(10.0f, TimedRotationPolicy.TimeUnit.MINUTES))
                .splitFileInput(splitFileInput);
        javaKafkaConsumerHighAPI.setConsumerKafkaStreamProcesser(consumerKafkaStreamProcesser);
        try {
            new Thread(javaKafkaConsumerHighAPI).start();
        } catch (Exception er) {
            er.printStackTrace();
        }
        // 执行10秒后结束
        int sleepMillis = 600000890;
        try {
            Thread.sleep(sleepMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 关闭
        javaKafkaConsumerHighAPI.shutdown();
    }
}
