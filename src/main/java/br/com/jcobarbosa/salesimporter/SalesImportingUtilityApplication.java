package br.com.jcobarbosa.salesimporter;

import br.com.jcobarbosa.salesimporter.config.SalesFileProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(SalesFileProperties.class)
public class SalesImportingUtilityApplication {

	private static final Logger logger = LoggerFactory.getLogger(SalesImportingUtilityApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SalesImportingUtilityApplication.class, args);
	}
}
