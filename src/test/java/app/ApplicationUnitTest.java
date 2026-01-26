package app;

import jakarta.jms.JMSException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import app.util.MessageSender;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ApplicationMain.class})
public class ApplicationUnitTest {

	protected static final Log logger = LogFactory.getLog(ApplicationUnitTest.class);

	
	@Test
	public void test() throws JMSException {
		
	}
}
