import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Import({
		// swagger config@local
		com.my.api.config.SwaggerConfig.class,

		// jwt config@my-jwt
		com.my.jwt.config.WebSecurityConfig.class,

		// config@my-db-service
		com.my.db.config.EntityServiceConfig.class,

		//config@redis
		com.my.redis.config.RedisConfig.class,
		com.my.redis.config.RedisServiceConfig.class,
		com.my.redis.config.RedisMessageConfig.class,

})

@ComponentScan	(basePackages = {
		// controller@local
		"com.my.api.controller",

		// controller aop@local
		"com.my.api.aop",

		//Jwt@local
		"com.my.jwt.service",

		// JWT@local
		"com.my.jwt",

		// aop@my-db-service
		"com.my.db.service.aop",
		// db access service@my-db-service
		"com.my.db.service.provider",

		// redis service@my-redis-service
		"com.my.redis.service.provider"
})

@EnableJpaRepositories(
		// repositiry@tom-db-service
		"com.my.db.repository"
)

@EntityScan(basePackages = {
		// entity@my-db-entity
		"com.my.db.entity",
})

@SpringBootApplication
public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}


