package com.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * @author huangshuai
 * @create 2023-08-02-15:35
 */
@Slf4j
@Component
public class ConsumerListener {
	@KafkaListener(topics = {"test-topic"})
	public void consumer(String record, Acknowledgment ack) throws InterruptedException {
		Thread.sleep(2000);
		String threadName = Thread.currentThread().getName();
		try {
			log.info("线程: {} 接收到消息: {}", threadName, record);
		} finally {
			log.info("线程: {} 异步提交", threadName);
			ack.acknowledge();
		}
	}
}

