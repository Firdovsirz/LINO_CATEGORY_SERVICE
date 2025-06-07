package m.example.lino_category_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class LinoCategoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LinoCategoryServiceApplication.class, args);
	}

}
