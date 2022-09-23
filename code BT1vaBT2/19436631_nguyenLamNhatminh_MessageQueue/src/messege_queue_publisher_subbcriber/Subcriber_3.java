package messege_queue_publisher_subbcriber;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.apache.log4j.BasicConfigurator;

public class Subcriber_3 extends JFrame {

	private JPanel contentPane;
	static JTextArea textArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) throws Exception {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Subcriber_3 frame = new Subcriber_3();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Subcriber_3() throws JMSException, NamingException {
		setTitle("Người đăng ký 3 của Lâm Nhật Minh");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 566, 486);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setForeground(Color.CYAN);
		panel.setBackground(Color.RED);
		panel.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Hộp thư",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(10, 11, 530, 425);
		contentPane.add(panel);
		panel.setLayout(null);

		textArea = new JTextArea();
		textArea.setBackground(Color.GREEN);
		textArea.setEditable(false);
		textArea.setBounds(10, 15, 520, 404);
		panel.add(textArea);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(269, 15, 2, 2);
		panel.add(scrollPane);
		
		// thiết lập môi trường cho JMS
				BasicConfigurator.configure();
				// thiết lập môi trường cho JJNDI
				Properties settings = new Properties();
				settings.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
				settings.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
				// tạo context
				Context ctx = new InitialContext(settings);
				// lookup JMS connection factory
				Object obj = ctx.lookup("TopicConnectionFactory");
				ConnectionFactory factory = (ConnectionFactory) obj;
				// tạo connection
				Connection con = factory.createConnection("admin", "admin");
				// nối đến MOM
				con.start();
				// tạo session
				Session session = con.createSession(/* transaction */false, /* ACK */Session.CLIENT_ACKNOWLEDGE);
				// tạo consumer
				Destination destination = (Destination) ctx.lookup("dynamicTopics/lamnhatminh");
				MessageConsumer receiver = session.createConsumer(destination);
				// receiver.receive();//blocked method
				// Cho receiver lắng nghe trên queue, chừng có message thì notify
				receiver.setMessageListener(new MessageListener() {
					@Override
					// có message đến queue, phương thức này được thực thi
					public void onMessage(Message msg) {// msg là message nhận được
						try {
							if (msg instanceof TextMessage) {
								TextMessage tm = (TextMessage) msg;
								String txt = tm.getText();
								textArea.setText("Tin nhắn nhận được từ chủ phòng  : " + txt);
								
								msg.acknowledge();// gửi tín hiệu ack
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
	
	}
}