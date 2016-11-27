package com.config;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.sql.DataSource;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.MessageListenerContainer;

import com.util.MessageReceiver;
import com.util.MessageSender;

@Configuration
public class MessageConfig {

	protected final Log logger = LogFactory.getLog(MessageConfig.class);

	@Autowired
	private Environment env;

	@Autowired
	private MessageReceiver messageReceiver;
	
	@Bean
	public ConnectionFactory activeMqConnectionFactory() {
		String url = env.getProperty("mq.brokerURL");
		String uname = env.getProperty("mq.userName");
		String password = env.getProperty("mq.password");
	//	return new ActiveMQConnectionFactory(uname, password, url);
		return new ActiveMQConnectionFactory(url);
	}

	@Bean
	public ConnectionFactory connectionFactory() {
		return new CachingConnectionFactory(activeMqConnectionFactory());
	}
	

	@Bean
	public Queue requestQueue() {
		return new ActiveMQQueue("q.dropcopy");
	}
	
	@Bean
	public MessageSender messageSender() {
		MessageSender messageSender = new MessageSender();
		messageSender.setConnectionFactory(connectionFactory());
		messageSender.setDestination(requestQueue());
		messageSender.setSessionTransacted(true);
		
		return messageSender;
	}
	
	
	@Bean
	public MessageListenerContainer messageListener() {
		DefaultMessageListenerContainer listenerContainer = 
				new DefaultMessageListenerContainer();
		listenerContainer.setConnectionFactory(connectionFactory());
		listenerContainer.setDestination(requestQueue());
		listenerContainer.setMessageListener(messageReceiver);
		return listenerContainer;
	}
}
