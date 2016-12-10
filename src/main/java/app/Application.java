package app;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.stereotype.Component;

import com.config.DbConfig;
import com.config.MessageConfig;
import com.config.MessageIntConfig;

@Configuration
@ComponentScan(basePackages="com.util,com.config")
@Component
@EnableJms
@Import({MessageConfig.class,DbConfig.class,MessageIntConfig.class})
@PropertySource("classpath:app.properties")
public class Application {

}
