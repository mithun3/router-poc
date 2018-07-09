package com.router.poc.routes;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.router.poc.exception.RouterException;
import com.router.poc.processor.MetricsCompleteProcessor;
import com.router.poc.processor.MetricsInitProcessor;
import com.router.poc.processor.ValidationProcessor;

import lombok.extern.slf4j.Slf4j;

/**
 * Magic Route Builder
 */
@Component
@Slf4j
public class CustomRequestRoutesBuilder extends RouteBuilder implements RoutesBuilder {
	
	@Autowired
	@Qualifier("validationProcessor")
	private ValidationProcessor validationProcessor;
	
	@Autowired
	private MetricsInitProcessor metricsInitProcessor;
	
	@Autowired
	private MetricsCompleteProcessor metricsCompleteProcessor;
	
    public void configure() {
		
				onException(Exception.class)
					.handled(true)
//					.maximumRedeliveries(getRetryCount())
//					.maximumRedeliveryDelay(getRetryDelay())
					.end()
					.to("activemq:payment.request.failed");
				
				onException(RouterException.class)
					.handled(true)
//					.maximumRedeliveries(getRetryCount())
//					.maximumRedeliveryDelay(getRetryDelay())
					.end()
					.to("activemq:payment.request.failed");
				
				from("activemq:payment.request")
					.log("Obtained the message from payment.request queue")
					.setProperty("ROUTE_PATH", constant("REQUEST"))
					.process(metricsInitProcessor)
					.wireTap("activemq:payment.request.wiretap")
					.log("Dropped the message to the wiretap queue")
					.log("Schema Validation Started")
					.process(validationProcessor)
					.end()
					.multicast()
					.process(metricsCompleteProcessor)
					.to("activemq:payment.request.acknowledge") //write some logic to carryout acknowledgement
					.to("activemq:payment.request.processed.mirror");
		
	}
}