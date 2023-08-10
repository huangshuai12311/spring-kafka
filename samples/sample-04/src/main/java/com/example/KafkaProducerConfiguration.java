package com.example;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huangshuai
 * @create 2023-08-02-11:20
 */
@Configuration
public class KafkaProducerConfiguration {
	public static final String NAMESPACE_KEY = "apollo.namespace";

	// 服务器地址
	@Value("${spring.kafka.bootstrap-servers}")
	private String servers;

	// ack类型，可选0、1、all/-1
	@Value("${spring.kafka.producer.acks}")
	private String ack;

	// 重试次数
	@Value("${spring.kafka.producer.retries}")
	private String retries;

	// 批量大小（单位字节）
	@Value("${spring.kafka.producer.batch-size}")
	private String batchSize;

	// 提交延时（单位ms）
	@Value("${spring.kafka.producer.linger}")
	private String linger;

	// 缓冲区大小（默认32M）
	@Value("${spring.kafka.producer.buffer-memory}")
	private String bufferMemory;

	/**
	 * 自定义配置
	 */
	@Bean
	public Map<String, Object> producerConfigs() {
		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
		props.put(ProducerConfig.ACKS_CONFIG, ack);
		props.put(ProducerConfig.RETRIES_CONFIG, retries);
		props.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
		props.put(ProducerConfig.LINGER_MS_CONFIG, linger);
		props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemory);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		props.put(NAMESPACE_KEY,"TEST2.my_namespace");
		return props;
	}

	/**
	 * 生产者工厂
	 */
	@Bean
	public ProducerFactory<String, Object> producerFactory() {
		return new DefaultKafkaProducerFactory<>(producerConfigs());
	}

	/**
	 * 注入kafkaTemplate
	 *
	 * @return KafkaTemplate
	 */
	@Bean
	public KafkaTemplate<String, Object> kafkaTemplate1() {
		return new KafkaTemplate<>(producerFactory());
	}

	/**
	 * 初始化一个Topic
	 *
	 * @return NewTopic
	 */
	@Bean
	public NewTopic initialTopic() {
		// 设置Topic名称、分区数、副本数，这里是创建了3个分区
		return new NewTopic("test_topic", 3, (short) 1);
	}
}

