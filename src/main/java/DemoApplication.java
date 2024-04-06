import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Import({
		// swagger config@local
		com.my.api.config.SwaggerConfig.class,

		// config@tom-db-service
		com.my.db.config.EntityServiceConfig.class,
})

@ComponentScan	(basePackages = {
		// controller@local
		"com.my.api.controller",

		// aop@local
		"com.my.api.aop",

		// aop@my-db-service
		"com.my.db.service.aop",

		// table access service@tom-db-service
		"com.my.db.service.provider" ,
})

@EnableJpaRepositories(
		// repositiry@tom-db-service
		"com.my.db.repository"
)

@EntityScan(basePackages = {
		// entity@my-db-entity
		"com.my.db.entity"
})
@SpringBootApplication
public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}


