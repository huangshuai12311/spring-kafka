package com.example;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huangshuai
 * @create 2023-08-02-15:30
 */
@Configuration
public class KafkaConsumerConfiguration {

	@Value("${spring.kafka.bootstrap-servers}")
	private String servers;

	@Value("${spring.kafka.consumer.group-id}")
	private String groupId;

	@Value("${spring.kafka.consumer.enable-auto-commit}")
	private boolean autoCommit;

	@Value("${spring.kafka.consumer.auto-offset-reset}")
	private String latest;

	@Value("${spring.kafka.consumer.max-poll-records}")
	private String maxPollRecords;

	/**
	 * 自定义配置
	 */
	public Map<String, Object> consumerConfigs() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, latest);
		props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put("apollo.namespace","TEST2.my_namespace");
		return props;
	}

	/**
	 * 消费者工厂
	 */
	@Bean
	public ConsumerFactory<String, Object> consumerFactory() {
		return new DefaultKafkaConsumerFactory<>(consumerConfigs());
	}

	/**
	 * 注入KafkaListenerContainerFactory
	 *
	 * @return KafkaListenerContainerFactory
	 */
	@Bean
	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Object>> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, Object> factory =
				new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		// 设置并发消费的线程数，不能超过partitions的大小
		factory.setConcurrency(1);
		// 设置是否是批量消费
		factory.setBatchListener(true);
		// 设置poll超时时间
		factory.getContainerProperties().setPollTimeout(30000);
		// 设置ACK模式
		factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
		// 设置缺少topic是否启动失败
		factory.setMissingTopicsFatal(false);
		return factory;
	}


}
