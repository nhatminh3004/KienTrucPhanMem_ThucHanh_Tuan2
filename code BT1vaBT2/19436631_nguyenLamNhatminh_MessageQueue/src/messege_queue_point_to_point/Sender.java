package messege_queue_point_to_point;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
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

import data.Person;
import helper.XMLConvert;

public class Sender extends JFrame {

	private JPanel contentPane;
	static JTextArea textArea;
	JTextArea textArea_1;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Sender frame = new Sender();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		try {
			//thiết lập môi trường cho JMS
			BasicConfigurator.configure();
			//thiết lập môi trường cho JJNDI
			Properties settings=new Properties();
			settings.setProperty(Context.INITIAL_CONTEXT_FACTORY,
					"org.apache.activemq.jndi.ActiveMQInitialContextFactory");
			settings.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
			//tạo context
			Context ctx=new InitialContext(settings);
			//lookup JMS connection factory
			Object obj=ctx.lookup("ConnectionFactory");
			ConnectionFactory factory=(ConnectionFactory)obj;
			//lookup destination
			Destination destination
			=(Destination) ctx.lookup("dynamicQueues/lamnhatminh");
			//tạo connection
			Connection con=factory.createConnection("admin","admin");
			//nối đến MOM
			con.start();
			//tạo session
			Session session=con.createSession(
					/*transaction*/false,
					/*ACK*/Session.CLIENT_ACKNOWLEDGE
					);
			//tạo consumer
			MessageConsumer receiver = session.createConsumer(destination);
			//blocked-method for receiving message - sync
			//receiver.receive();
			//Cho receiver lắng nghe trên queue, chừng có message thì notify - async
			System.out.println("Lâm Nhật Minh đang chờ nhận tin nhắn...");
			receiver.setMessageListener(new MessageListener() {
				@Override
				
				public void onMessage(Message msg) {
					//msg là message nhận được
					try {
						if(msg instanceof TextMessage){
							TextMessage tm=(TextMessage)msg;
							String txt=tm.getText();
							textArea.setText("Trần Thành Nam :"+txt);
							msg.acknowledge();//gửi tín hiệu ack
						}
						else if(msg instanceof ObjectMessage){
							ObjectMessage om=(ObjectMessage)msg;
							System.out.println(om);
						}
						//others message type....
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	/**
	 * Create the frame.
	 */
	public Sender() {
		setTitle("Lâm Nhật Minh");
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
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
				"Hộp thư", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(10, 11, 530, 347);
		contentPane.add(panel);
		panel.setLayout(null);

		textArea = new JTextArea();
		textArea.setBackground(Color.GREEN);
		textArea.setEditable(false);
		textArea.setBounds(10, 15, 510, 327);
		panel.add(textArea);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(269, 15, 2, 2);
		panel.add(scrollPane);

		JButton btnNewButton = new JButton("Send");
		btnNewButton.setBackground(Color.MAGENTA);
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnNewButton.setBounds(465, 369, 75, 67);
		contentPane.add(btnNewButton);

		 textArea_1 = new JTextArea();
		 textArea_1.setBackground(Color.PINK);
		textArea_1.setBounds(77, 369, 373, 67);
		contentPane.add(textArea_1);

		JLabel lblNewLabel = new JLabel("Soạn tin :");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 396, 57, 14);
		contentPane.add(lblNewLabel);
		
		//dùng để gửi cho người thứ 2 là Trần thành nam
		btnNewButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					
					//config environment for JMS
					BasicConfigurator.configure();
					//config environment for JNDI
					Properties settings=new Properties();
					settings.setProperty(Context.INITIAL_CONTEXT_FACTORY,
							"org.apache.activemq.jndi.ActiveMQInitialContextFactory");
					settings.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
					//create context
					Context ctx=new InitialContext(settings);
					//lookup JMS connection factory
					ConnectionFactory factory=
							(ConnectionFactory)ctx.lookup("ConnectionFactory");
					//lookup destination. (If not exist-->ActiveMQ create once)
					Destination destination=
							(Destination) ctx.lookup("dynamicQueues/tranthanhnam");
					//get connection using credential
					Connection con=factory.createConnection("admin","admin");
					//connect to MOM
					con.start();
					//create session
					Session session=con.createSession(
							/*transaction*/false,
							/*ACK*/Session.AUTO_ACKNOWLEDGE
							);
					//create producer
					MessageProducer producer = session.createProducer(destination);
					//create text message
					Message msg=session.createTextMessage(textArea_1.getText().toString());
					producer.send(msg);
					textArea_1.setText("");
					Person p=new Person(1001, "Trần Thành Nam", new Date());
					String xml=new XMLConvert<Person>(p).object2XML(p);
					msg=session.createTextMessage(xml);
					producer.send(msg);
					//shutdown connection
					session.close();con.close();
					System.out.println("Finished...");
					
				} catch (Exception e2) {
					System.out.println(e2);
				}
			}
		});
	}
}