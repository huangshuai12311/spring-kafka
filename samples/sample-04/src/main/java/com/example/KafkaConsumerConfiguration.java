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

	@Value("${spring.kafka.consumer.group-id}")
	private String groupId;


	/**
	 * 自定义配置
	 */
	public Map<String, Object> consumerConfigs() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(CommonConfiguration.NAMESPACE_KEY, new CommonConfiguration().getApolloNamespace());
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
