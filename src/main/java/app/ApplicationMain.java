package app;

import jakarta.jms.JMSException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import app.util.MessageSender;

public class ApplicationMain {
	protected static final Log logger = LogFactory.getLog(ApplicationContext.class);

	public static void main(String[] args) throws JMSException {

		logger.info("start application");
		ApplicationContext context = new AnnotationConfigApplicationContext(Application.class);
		
	}
}
