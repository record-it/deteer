package pl.recordit.deteer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import pl.recordit.deteer.rest.service.ProductRestService;
import pl.recordit.deteer.rest.service.ProductRestUpdateService;
import pl.recordit.deteer.service.ProductService;
import pl.recordit.deteer.storage.StorageProperties;
import pl.recordit.deteer.storage.StorageService;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
@EnableWebMvc
public class DeteerApplication {
	public static void main(String[] args) {
		SpringApplication.run(DeteerApplication.class, args);
	}

	@Bean
	CommandLineRunner init(StorageService storageService, ProductRestUpdateService productRestUpdateService, ProductService productService) {
		return (args) -> {
			//storageService.deleteAll();
			storageService.init();
		};
	}
}
