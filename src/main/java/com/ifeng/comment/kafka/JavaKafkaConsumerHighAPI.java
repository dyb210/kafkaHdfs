package com.ifeng.comment.kafka;

import kafka.consumer.*;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import kafka.serializer.StringDecoder;
import kafka.utils.VerifiableProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 自定义简单Kafka消费者， 使用高级API
 */
public class JavaKafkaConsumerHighAPI implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(JavaKafkaConsumerHighAPI.class);
    /**
     * Kafka数据消费对象
     */
    private ConsumerConnector consumer;

    /**
     * Kafka Topic名称
     */
    private String topic;

    /**
     * 线程数量，一般就是Topic的分区数量
     */
    private int numThreads;

    /**
     * 线程池
     */
    private ExecutorService executorPool;
    private ConsumerKafkaStreamProcesser consumerKafkaStreamProcesser;

    /**
     * 构造函数
     *
     * @param topic      Kafka消息Topic主题
     * @param numThreads 处理数据的线程数/可以理解为Topic的分区数
     * @param zookeeper  Kafka的Zookeeper连接字符串
     * @param groupId    该消费者所属group ID的值
     */
    public JavaKafkaConsumerHighAPI(String topic, int numThreads, String zookeeper, String groupId) {
        this.consumer = Consumer.createJavaConsumerConnector(createConsumerConfig(zookeeper, groupId));
        this.topic = topic;
        this.numThreads = numThreads;
    }

    @Override
    public void run() {
        try {
            Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
            topicCountMap.put(this.topic, this.numThreads);
            StringDecoder keyDecoder = new StringDecoder(new VerifiableProperties());
            StringDecoder valueDecoder = new StringDecoder(new VerifiableProperties());
            /**
             * Key: Topic主题
             * Value: 对应Topic的数据流读取器，大小是topicCountMap中指定的topic大小
             */
            Map<String, List<KafkaStream<String, String>>> consumerMap = this.consumer.createMessageStreams(topicCountMap, keyDecoder, valueDecoder);
            List<KafkaStream<String, String>> streams = consumerMap.get(this.topic);
            this.executorPool = Executors.newFixedThreadPool(this.numThreads);
            int threadNumber = 0;
            for (final KafkaStream<String, String> stream : streams) {
                consumerKafkaStreamProcesser = copyKafkaStreamProcesser();
                consumerKafkaStreamProcesser.setStream(stream);
                consumerKafkaStreamProcesser.setThreadNumber(threadNumber);
                this.executorPool.submit(consumerKafkaStreamProcesser);
//                ConsumerIterator consumerIterator = stream.iterator();
//                while (consumerIterator.hasNext()) {
//                    MessageAndMetadata value = consumerIterator.next();
//                    System.out.println(value.message());
//                }
                threadNumber++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        if (this.consumer != null) {
            this.consumer.shutdown();
        }
        if (this.executorPool != null) {
            this.executorPool.shutdown();
            try {
                if (!this.executorPool.awaitTermination(5, TimeUnit.SECONDS)) {
                    logger.info("Timed out waiting for consumer threads to shut down, exiting uncleanly!!");
                }
            } catch (InterruptedException e) {
                logger.info("Interrupted during shutdown, exiting uncleanly!!");
            }
        }
    }

    /**
     * 根据传入的zk的连接信息和groupID的值创建对应的ConsumerConfig对象
     *
     * @param zookeeper zk的连接信息，类似于：<br/>
     *                  hadoop-senior01.ibeifeng.com:2181,hadoop-senior02.ibeifeng.com:2181/kafka
     * @param groupId   该kafka consumer所属的group id的值， group id值一样的kafka consumer会进行负载均衡
     * @return Kafka连接信息
     */
    private ConsumerConfig createConsumerConfig(String zookeeper, String groupId) {
        Properties prop = new Properties();
        prop.put("group.id", groupId); // 指定分组id
        prop.put("zookeeper.connect", zookeeper); // 指定zk的连接url
        prop.put("zookeeper.session.timeout.ms", "1000"); //
        prop.put("zookeeper.sync.time.ms", "200");
        prop.put("auto.commit.interval.ms", "1000");
        prop.put("rebalance.backoff.ms", "2000");
        prop.put("rebalance.max.retries", "10");
        return new ConsumerConfig(prop);
    }

    public ConsumerKafkaStreamProcesser copyKafkaStreamProcesser(){
       return  consumerKafkaStreamProcesser.copyKafkaStreamProcesser();
    }

    public ConsumerKafkaStreamProcesser getConsumerKafkaStreamProcesser() {
        return consumerKafkaStreamProcesser;
    }

    public void setConsumerKafkaStreamProcesser(ConsumerKafkaStreamProcesser consumerKafkaStreamProcesser) {
        this.consumerKafkaStreamProcesser = consumerKafkaStreamProcesser;
    }
}