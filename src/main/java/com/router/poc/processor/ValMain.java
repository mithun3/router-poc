package com.router.poc.processor;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.xml.sax.SAXException;

public class ValMain {
	public void main(String[] args) throws SAXException, IOException {
		final Resource resource = new ClassPathResource("sample/test.xsd");//resourceLoader.getResource("classpath:sample/test.xsd");
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = schemaFactory.newSchema(new StreamSource(resource.getInputStream()));
		Source source = new StreamSource(new File("C:\\code\\anz\\router-poc\\src\\main\\resources\\sample\\test.xml"));
		schema.newValidator().validate(source);
		System.out.print("success");
	}
	
}
