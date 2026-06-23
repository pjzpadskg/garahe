package com.pj.garahe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class GaraheApplication {

	public static void main(String[] args) {
		SpringApplication.run(GaraheApplication.class, args);
	}

}
