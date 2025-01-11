package sesac_3rd.sesac_3rd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class Sesac3rdApplication {

	public static void main(String[] args) {
		SpringApplication.run(Sesac3rdApplication.class, args);
	}

}
