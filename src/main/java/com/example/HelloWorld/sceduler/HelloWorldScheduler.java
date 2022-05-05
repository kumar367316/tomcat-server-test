package com.example.HelloWorld.sceduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class HelloWorldScheduler {

	@Scheduled(cron="0 * * ? * *")
	public void testScheduler() {
		System.out.print("scheduler running");
	}
	
}
