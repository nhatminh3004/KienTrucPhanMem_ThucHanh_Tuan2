package nhatminh1.springboot_activeMQ.receiver;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import nhatminh1.springboot_activeMQ.data.Person;
@Component
public class MessageReceiver {
	private static final String MESSAGE_QUEUE = "lamnhatminh";
	@JmsListener(destination = MESSAGE_QUEUE)
	public void receiveMessage(Person p) {
		System.out.println("Nhận được thông tin :" +p);
	}
}
