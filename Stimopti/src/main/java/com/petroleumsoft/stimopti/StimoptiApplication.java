package com.petroleumsoft.stimopti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class StimoptiApplication {

	public static void main(String[] args) {
		SpringApplication.run(StimoptiApplication.class, args);
	}
	
//	@Bean
//	public Docket api() {
//		return new Docket(DocumentationType.SWAGGER_2).select()
//				.apis(RequestHandlerSelectors.basePackage("com.petroleumsoft.stimopti.controller")).paths(PathSelectors.any())
//				.build();
//	}

}
