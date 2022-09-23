package nhatminh1.springboot_activeMQ;

import java.util.Date;
import java.util.Iterator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;

import nhatminh1.springboot_activeMQ.data.Person;

@SpringBootApplication
@EnableJms
public class SpringbootActiveMqApplication {
	private static final String MESSAGE_QUEUE = "lamnhatminh";

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(SpringbootActiveMqApplication.class, args);

		// get JMS template bean reference
		JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
		Person p = new Person();
		p.setMssv(19436631);
		p.setHoten("Nguyễn Lâm Nhật Minh");
		p.setNgaysinh(new Date("30/4/2001"));
		System.out.println("Gửi thông tin đến người nhận.... finish...");
		jmsTemplate.convertAndSend(MESSAGE_QUEUE, p);
	}

}
