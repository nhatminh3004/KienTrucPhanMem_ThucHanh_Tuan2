package nhatminh1.springboot_activeMQ.config;

import javax.jms.ConnectionFactory;

import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@Configuration
@ComponentScan(basePackages = "nhatminh1.springboot_activeMQ")
public class JMSConfiguration {
	@Bean
	public JmsListenerContainerFactory<?> myFactory (ConnectionFactory connectionFactory,DefaultJmsListenerContainerFactoryConfigurer configurer){
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		configurer.configure(factory, connectionFactory);
		return factory;
	}
	@Bean
	public MessageConverter jacksonJmsMessConvert() {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setTargetType(MessageType.TEXT);
		converter.setTypeIdPropertyName("_type");
		return converter;
	}
	
}
