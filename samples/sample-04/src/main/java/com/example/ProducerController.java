package com.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author huangshuai
 * @create 2023-08-02-11:25
 */
@Slf4j
@RestController
public class ProducerController {

	@Resource
	private KafkaTemplate<String, Object> kafkaTemplate;


	@RequestMapping("/send")
	public String sendMessage(@RequestParam("value") String value) {
		System.out.println("dffff");
		try {
			kafkaTemplate.send("test-topic", value);
			return "发送成功";
		} catch (Exception e) {
			log.error("Failed to send message caused by {}", e.getMessage());
			return "发送失败";
		}
	}
}

