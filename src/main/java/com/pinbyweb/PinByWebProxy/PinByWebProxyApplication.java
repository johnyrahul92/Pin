package com.pinbyweb.PinByWebProxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.PropertySource;



@SpringBootApplication
//@PropertySource("${pinbyweb/services-config}")
@PropertySource(value = { "file:/Users/gulfbank/Desktop/GBK/PinByWeb/Attributes.properties"})
public class PinByWebProxyApplication extends SpringBootServletInitializer {	
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(PinByWebProxyApplication.class);
	}
	
	public static void main(String[] args) {		
		SpringApplication.run(PinByWebProxyApplication.class, args);
	}
}
