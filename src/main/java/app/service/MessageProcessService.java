package app.service;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Service;

@Service
public class MessageProcessService {

	protected static final Log logger = LogFactory.getLog(MessageProcessService.class);

	private AtomicInteger counter = new AtomicInteger();
	
	@ServiceActivator
	public void processMessage(String message) {
		logger.info("process message no:"+counter.getAndIncrement()+ " ,message:"+  message);
		//test for error handling
		/*if(counter.get()>1) {
			throw new RuntimeException("exception while procesing message");
		}*/

	}
}
