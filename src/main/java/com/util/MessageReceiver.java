package com.util;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
public class MessageReceiver implements MessageListener {

	protected static final Log logger = LogFactory.getLog(MessageReceiver.class);
	
	public void onMessage(Message msg) {
		TextMessage txtMsg = (TextMessage)msg;
		try {
			logger.info("message received:"+txtMsg.getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
	}
	
	@JmsListener(containerFactory="jmsListenerContainerFactory",destination="q.dropcopy")
	public void onMsg(String msg) {
		logger.info("message received jmslistener:"+msg);
	}

}
