package app.util;


import jakarta.jms.ConnectionFactory;
import jakarta.jms.Destination;
import jakarta.jms.JMSException;

import org.springframework.jms.core.JmsTemplate;

public class MessageSender {
	private ConnectionFactory connectionFactory;
    private Destination destination;
    private int messagesSent;
    private JmsTemplate jmsTemplate;
    public boolean send = true;
    public boolean sessionTransacted = false;
    
    
	public ConnectionFactory getConnectionFactory() {
		return connectionFactory;
	}

	public void setConnectionFactory(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public Destination getDestination() {
		return destination;
	}

	public void setDestination(Destination destination) {
		this.destination = destination;
	}

	public int getMessagesSent() {
		return messagesSent;
	}

	public void setMessagesSent(int messagesSent) {
		this.messagesSent = messagesSent;
	}

	public JmsTemplate getJmsTemplate() {
		if(jmsTemplate == null)
			jmsTemplate = new JmsTemplate(connectionFactory);
		return jmsTemplate;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public boolean isSend() {
		return send;
	}

	public void setSend(boolean send) {
		this.send = send;
	}

	public boolean isSessionTransacted() {
		return sessionTransacted;
	}

	public void setSessionTransacted(boolean sessionTransacted) {
		this.sessionTransacted = sessionTransacted;
	}
    public void sendMessage(String xmlMessage) throws JMSException {
    	getJmsTemplate().convertAndSend(destination, xmlMessage);
  	  	    	  
	 }
	    
	    
}
