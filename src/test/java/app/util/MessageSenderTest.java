package app.util;

import javax.jms.JMSException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import app.config.MessageConfig;
import app.util.MessageSender;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource("classpath:app_test.properties")
@ContextConfiguration(classes = {MessageConfig.class})
public class MessageSenderTest {

	protected static final Log logger = LogFactory.getLog(MessageSenderTest.class);

	@Autowired
	private MessageSender messageSender;
	
	@Test
	public void testSendMsg() throws JMSException {
		final String msg = "8=FIX.4.29=37735=849=SCI56=TCI34=7352=20140526-03:22:00100=XSFE1=Y08A016=614311=2594214=115=AUD17=3580-20970820=031=614332=137=tk-20140526-000000000550538=140=244=614347=A528=A48=YAPZ454=155=APZ4666=AP59=060=20140526-03:22:15109=SCOTTISH WIDOWS110=0120=AUD150=2151=0167=FUT200=201412207=XSFE5054=PARENT5035=259425055=2594270129=2594274000=ULLINK82900=ULLINK10=034";
		int size=100;
		logger.info("send "+size+" msgs");
		for(int i=0;i<size;i++)
			messageSender.sendMessage(msg);
		logger.info("sent "+size+" msgs");
	}
}
