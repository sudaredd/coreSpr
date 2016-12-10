package app;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.stereotype.Component;

import app.config.DbConfig;
import app.config.MessageConfig;
import app.config.MessageIntConfig;

@Configuration
@ComponentScan(basePackages="app.util,app.config,app.service")
@Component
@EnableJms
@Import({MessageConfig.class,DbConfig.class,MessageIntConfig.class})
@PropertySource("classpath:app.properties")
public class Application {

}
