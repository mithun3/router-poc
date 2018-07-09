package com.router.poc.routes;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.XPathBuilder;
import org.apache.camel.model.dataformat.XmlJsonDataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.router.poc.processor.ValidationProcessor;

/**
 * Magic Route Builder
 */
@Component
public class CustomRouteBuilder  extends RouteBuilder implements RoutesBuilder {
	
	public static String splitXpath = "//orders/order";
	
	@Autowired
	@Qualifier("validationProcessor")
	private ValidationProcessor validationProcessor;
	
    public void configure() {
    	
    	/*
    	 * Route errors to DLQ after one retry and one second delay
    	 */
    	//errorHandler(deadLetterChannel("activemq:queue:emagic.dead"));
    	
		XPathBuilder splitXPath = new XPathBuilder (splitXpath);
		
    	/*
    	 * Splitter - xpath expression
    	 */
		/*from("activemq:emagic.orders").
			split(splitXPath).parallelProcessing().
		to("activemq:emagic.order");*/
    	
    	/*
    	 * Content Based Routing - simple expression
    	 */
//    	from("activemq:emagic.order").
//    	choice().
//    		when().simple("${in.body} contains 'Houdini'").
//    			to("activemq:priority.order").
//    		otherwise().
//    			to("activemq:magic.order");
    	
    	/*
    	 * Content Based Routing - Mediation, simple expression
    	 * http://camel.apache.org/maven/camel-2.15.0/camel-core/apidocs/org/apache/camel/Processor.html 
    	 */
    	 
		XmlJsonDataFormat xmlJsonFormat = new XmlJsonDataFormat();
		xmlJsonFormat.setForceTopLevelObject(true);
		
//		from("activemq:payment.request")
//		.log("Some stupid log")
//		.setProperty("ROUTE_PATH", constant("REQUEST"))//SET PATH here
////		.process(metricsInitProcessor)
//		//do some more processing if possible
//		.wireTap("activemq:payment.request.wiretap")
////		.log("Schema Validation")
//		.process(validationProcessor)
//		.end()
//		.multicast()
////		.process(metricsCompleteProcessor)
//		.to("activemq:payment.request.processed")
//		.to("activemq:payment.request.processed.mirror");
//		
    	/*from("activemq:emagic.order").
    	choice().
    		when().simple("${in.body} contains 'Houdini'").
				process((exchange) -> exchange.getIn().setHeader("VIP", "true")).
    			to("activemq:priority.order").
    		otherwise().
    			marshal(xmlJsonFormat).
    			transform(body().regexReplaceAll("@", "")).
    			to("activemq:magic.order"); */
		
    	/*
    	 * Content Based Routing - Wire-Tap to ActiveMQ Topic
    	 * Requires updating Splitter Route and Uncomment wireTap 
    	 
		from("activemq:emagic.order").
			wireTap("direct:ministry").
		to("activemq:magic.order");
		
    	from("direct:ministry").
			choice().
				when().simple("${in.body} contains 'Elder'").
					log("ILLEGAL MAGIC ALERT").
					to("activemq:topic:magic.alerts").		
				otherwise().
					log("...off into the ether");*/
    }
}