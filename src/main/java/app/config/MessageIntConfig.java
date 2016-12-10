package app.config;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Queue;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.DirectChannelSpec;
import org.springframework.integration.dsl.channel.MessageChannelSpec;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.dsl.jms.Jms;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

import sun.util.logging.resources.logging;
import app.service.MessageProcessService;
import app.util.MessageReceiver;
import app.util.MessageSender;

@EnableIntegration
@Configuration
public class MessageIntConfig {

	protected static final Log logger = LogFactory.getLog(MessageIntConfig.class);

	@Autowired
	private Environment env;

	@Autowired
	private ConnectionFactory jmsConnectionFactory;
	
	@Autowired
	private Destination requestQueue;
	
	@Autowired
	private MessageProcessService messageProcessService;

	private String incomingMsgChannel = "incomingMsgChannel";
	
	@Bean
	public IntegrationFlow readMesageFlow() {
	    return IntegrationFlows.from(Jms.inboundAdapter(jmsConnectionFactory).destination(requestQueue))
	    		.channel(incomingMsg())
	                .get();
	}
	
	@Bean
	public IntegrationFlow processMessage() {
		return IntegrationFlows.from(incomingMsgChannel)
				.handle(messageProcessService)
				.get();
	}
	
	@Bean(name = PollerMetadata.DEFAULT_POLLER)
	public PollerMetadata poller() {
		return Pollers.fixedDelay(0).get();
	}
	
	@Bean
	public MessageChannelSpec incomingMsg() {
		return MessageChannels.direct(incomingMsgChannel);
	}
/*	
	@Bean
	public MessageListenerContainer messageListener() {
		DefaultMessageListenerContainer listenerContainer = 
				new DefaultMessageListenerContainer();
		listenerContainer.setConnectionFactory(connectionFactory());
		listenerContainer.setDestination(requestQueue());
		listenerContainer.setMessageListener(messageReceiver);
		return listenerContainer;
	}*/
}
