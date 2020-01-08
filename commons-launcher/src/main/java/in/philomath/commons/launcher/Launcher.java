package in.philomath.commons.launcher;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author bhagath
 * @since 1.0.0
 */
@Data
@Slf4j
@EnableAutoConfiguration
@SpringBootApplication(scanBasePackages = {"in.philomath.**.config"})
public class Launcher {

	public static void main(String[] args) {

		SpringApplication application = new SpringApplication(Launcher.class);
		ConfigurableEnvironment env = application.run(args).getEnvironment();
	}

}
