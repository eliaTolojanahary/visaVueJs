package visa.backoffice.backoffice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "visa.backoffice")
@EntityScan(basePackages = "visa.backoffice.entity")
@EnableJpaRepositories(basePackages = "visa.backoffice.repository")
public class BackofficeApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackofficeApplication.class, args);
    }
}