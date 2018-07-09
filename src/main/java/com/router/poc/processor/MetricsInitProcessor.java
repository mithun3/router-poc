package com.router.poc.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(value="metricsInitProcessor")
public class MetricsInitProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		exchange.setProperty("START_TIME", System.currentTimeMillis());
		log.info("Started processing message {}", exchange.getProperty("START_TIME"));
	}

}
