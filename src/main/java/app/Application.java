package app;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.config.DbConfig;
import com.config.MessageConfig;

@Configuration
@ComponentScan(basePackages="com.util,com.config")
@Component
@Import({MessageConfig.class,DbConfig.class})
@PropertySource("classpath:app.properties")
public class Application {

}
