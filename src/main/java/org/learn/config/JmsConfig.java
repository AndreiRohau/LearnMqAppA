package org.learn.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;


@Configuration
@EnableJms
public class JmsConfig {

    @Value("${spring.activemq.broker-url}")
    private String brokerUrl;
    @Value("${spring.activemq.user}")
    private String brokerUsername;
    @Value("${spring.activemq.password}")
    private String brokerPassword;

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(brokerUrl);
        connectionFactory.setUserName(brokerUsername);
        connectionFactory.setPassword(brokerPassword);
        return connectionFactory;
    }

    @Bean
    public JmsTemplate jmsQueueTemplate(ActiveMQConnectionFactory connectionFactory) {
        return getJmsTemplate(connectionFactory, false);
    }

    @Bean
    public JmsTemplate jmsTopicTemplate(ActiveMQConnectionFactory connectionFactory) {
        return getJmsTemplate(connectionFactory, true);
    }

    private JmsTemplate getJmsTemplate(ActiveMQConnectionFactory connectionFactory, boolean isPubSumDomain) {
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(connectionFactory);
        template.setPubSubDomain(isPubSumDomain);
        return template;
    }

    @Bean
    public DefaultJmsListenerContainerFactory queueResponseListenerFactory(ActiveMQConnectionFactory connectionFactory) {
        return getJmsListener(connectionFactory, false, false, "q-client-response");
    }

    private DefaultJmsListenerContainerFactory getJmsListener(ActiveMQConnectionFactory connectionFactory, boolean isPubSubDomain, boolean isSubscriptionDurable, String clientId) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPubSubDomain(isPubSubDomain);
        factory.setSubscriptionDurable(isSubscriptionDurable);
        factory.setClientId(clientId);
        return factory;
    }
}
