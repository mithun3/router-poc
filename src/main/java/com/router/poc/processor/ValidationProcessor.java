package com.router.poc.processor;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import com.router.poc.exception.RouterException;

import lombok.extern.slf4j.Slf4j;

@Component(value="validationProcessor")
@Slf4j
public class ValidationProcessor implements Processor {
	
	private Map<String, Schema> schemaMap = new HashMap<>();
	
	private ResourceLoader resourceLoader;

	@Override
	public void process(Exchange exchange) throws RouterException, SAXException, IOException {
		
		final String incomingMessage = exchange.getIn().getBody(String.class);
		final String channelId = exchange.getProperty("SOURCE_ID", String.class);
		final String reqId = exchange.getProperty("REQUEST_ID", String.class);
		log.info("Started Validation of XML - Inocming Message : {}, channelId : {}, reqId : {}", incomingMessage, channelId, reqId);
		try {
			if(StringUtils.isBlank(incomingMessage)) {
				log.error("Incoming Message is blank");
				throw new RouterException("Incoming Message is blank");
			}
		} catch (RouterException e) {
			exchange.setProperty("SOME_FAIL", "SOME_FAIL");
			log.error("Some exception has occured {}", e.getMessage());
			throw e;
		}
		try {
			validateSchema(incomingMessage, exchange, "/resources/sample/test.xml");
		} catch (Exception e) {
			log.error("Exception has occured, during validateSchema(), {}", e.getMessage());
			throw new RouterException("Exception has occured, during validateSchema()");
		}
	}
	
	public void validateSchema (String message, Exchange exchange, String schemaName) throws SAXException, IOException {
		try {
			Schema schema = getSchema(schemaName);
			Source source = new StreamSource(new StringReader(message));
			schema.newValidator().validate(source);
		} catch (RouterException e) {
			log.error("An exception has occured during validation of xml with xsd schema, {}", e.getMessage());
		}
	}
	
	public Schema getSchema(String schemaName) throws SAXException, IOException, RouterException {
		
		if(schemaMap.containsKey(schemaName)) return schemaMap.get(schemaName);
		
		final Resource resource = new ClassPathResource("sample/test.xsd");//resourceLoader.getResource(schemaName);
		
		if(resource != null) {
			log.info("validating the xml against the xsd");
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = schemaFactory.newSchema(new StreamSource(resource.getInputStream()));
			schemaMap.put(schemaName, schema);
			return schema;
		} else {
			log.error("Error while returning a schema");
			throw new RouterException("Error while returning a schema");
		}
		
		
	}
}
