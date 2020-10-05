package pl.recordit.deteer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import pl.recordit.deteer.dto.NewProductDto;
import pl.recordit.deteer.entity.User;
import pl.recordit.deteer.repository.UserRepository;
import pl.recordit.deteer.rest.service.ProductRestService;
import pl.recordit.deteer.rest.service.ProductRestUpdateService;
import pl.recordit.deteer.service.ProductService;
import pl.recordit.deteer.service.UserService;
import pl.recordit.deteer.storage.StorageProperties;
import pl.recordit.deteer.storage.StorageService;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
@EnableWebMvc
public class DeteerApplication {
	public static void main(String[] args) {
		SpringApplication.run(DeteerApplication.class, args);
	}

	@Bean
	CommandLineRunner init(StorageService storageService, ProductService productService, UserRepository userRepository) {
		return (args) -> {

			User defaultUSer = userRepository.save(User.builder().email("test@test.pl").password("$2a$10$N/.9OHMMRTa8wnZ1kyq8d.tXRCrTArlb76snZcsIk/Cqe4wBZdq/6").enabled(true).verified(true).registered(LocalDateTime.now()).build());
			productService.create(NewProductDto.builder().isPublic(true).name("PRIMA").isPublic(true).parentId(null).properties("{\"flue size\": 200, \"fuel\": \"dry wood\"}").publisher(defaultUSer).build());
			productService.create(NewProductDto.builder().isPublic(true).name("PRIMA XSM").isPublic(true).parentId(1L).properties("{\"power\": 8, \"CO2 emmission\": \"0,109%\"}").publisher(defaultUSer).build());
			productService.create(NewProductDto.builder().isPublic(true).name("PRIMA ME").isPublic(true).parentId(1L).properties("{\"power\": 16, \"CO2 emmission\": \"0,119%\"}").publisher(defaultUSer).build());
			productService.create(NewProductDto.builder().isPublic(true).name("PRIMA SM").isPublic(true).parentId(1L).properties("{\"power\": 12, \"CO2 emmission\": \"0,129%\"}").publisher(defaultUSer).build());
			storageService.init();
		};
	}
}
