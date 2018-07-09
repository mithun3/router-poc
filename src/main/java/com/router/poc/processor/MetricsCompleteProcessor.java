package com.router.poc.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(value="metricsCompleteProcessor")
public class MetricsCompleteProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		final long startTime = exchange.getProperty("START_TIME", Long.class);
		final long execTime = System.currentTimeMillis() - startTime;
		final String routePath = null; //TODO exchange.getProperty("START_TIME", Long.class);
		log.info("Metrics after nessage processing is complete. {} - {}", execTime, routePath);
	}

}
