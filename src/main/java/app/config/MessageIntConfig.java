package app.config;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Queue;

import org.aopalliance.aop.Advice;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageSources;
import org.springframework.integration.dsl.channel.DirectChannelSpec;
import org.springframework.integration.dsl.channel.MessageChannelSpec;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.dsl.jms.Jms;
import org.springframework.integration.handler.advice.ErrorMessageSendingRecoverer;
import org.springframework.integration.handler.advice.RequestHandlerRetryAdvice;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandlingException;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
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
	
	@Autowired
	private ErrorMessageHandler errorMessageHandler;

	private String incomingMsgChannel = "incomingMsgChannel";
	
	private String errorMessageChannel = "errorMessageChannel";
	
	@Bean
	public IntegrationFlow readMesageFlow() {
	    return IntegrationFlows.from((MessageSources s) -> s.jms(this.jmsConnectionFactory).destination(requestQueue))
	    //		Jms.inboundAdapter(jmsConnectionFactory).destination(requestQueue))
	    		.channel(incomingMsg())
	                .get();
	}
	
	@Bean
	public IntegrationFlow processMessage() {
		return IntegrationFlows.from(incomingMsgChannel)
				.handle(messageProcessService,"processMessage",e->e.advice(retryAdvice()))
				.resequence()
				.get();
	}
	
	@Bean
	public IntegrationFlow handleErrorMessageFlow() {
		return IntegrationFlows.from(errorMsgChannel())
				.handle(errorMessageHandler)
				.get();
	}
	
	@Bean
	public Advice retryAdvice() {
		 FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
		 backOffPolicy.setBackOffPeriod(1000);
		
		 SimpleRetryPolicy policy = new SimpleRetryPolicy();
		 policy.setMaxAttempts(5);
		 
		   
		 RetryTemplate retryTemplate = new RetryTemplate();
		 retryTemplate.setBackOffPolicy(backOffPolicy);
		 retryTemplate.setRetryPolicy(policy);

		 RequestHandlerRetryAdvice advice = new RequestHandlerRetryAdvice();
		 advice.setRetryTemplate(retryTemplate);
		 
		 advice.setRecoveryCallback(new ErrorMessageSendingRecoverer(errorMsgChannel()));
		 		 
		return advice;
	}

	@Bean(name = PollerMetadata.DEFAULT_POLLER)
	public PollerMetadata poller() {
		return Pollers.fixedRate(0).get();
	}
	
	@Autowired
	private TaskExecutor taskExecutor;
	
	@Bean
	public MessageChannelSpec incomingMsg() {
		//return MessageChannels.direct(incomingMsgChannel);
		return MessageChannels.executor(incomingMsgChannel,taskExecutor);
	}
	
	@Bean
	public MessageChannel errorMsgChannel() {
		return MessageChannels.direct(errorMessageChannel).get();
	}
	
	@Service("errorMessageHandler")
	private static class ErrorMessageHandler {
		
		@ServiceActivator
		public void handleErrorMessage(MessageHandlingException msg) {
			logger.error("handle error msg:"+msg.getMessage());
			logger.error("payload of error msg:"+msg.getFailedMessage().getPayload());

		}
	}
}
