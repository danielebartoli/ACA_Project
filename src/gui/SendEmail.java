package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SendEmail extends JFrame {

	private JPanel contentPane;
	private JTextField txtStudentNo;
	private JTextField txtStudentEmail;
	protected String BodyHtml;



	/**
	 * Create the frame.
	 */
	public SendEmail(String strBody) {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setBounds(100, 100, 296, 166);
		
		
		BodyHtml=strBody;
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		this.setVisible(true);
		
		JLabel label = new JLabel("Student N.:");
		label.setBounds(10, 45, 63, 14);
		contentPane.add(label);
		
		JLabel label_1 = new JLabel("Email:");
		label_1.setBounds(10, 14, 63, 14);
		contentPane.add(label_1);
		
		JButton button = new JButton("Cancel Send");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false); //you can't see me!
				dispose(); //Destroy the JFrame object
			}
		});
		button.setBounds(12, 90, 106, 23);
		contentPane.add(button);
		
		JButton button_1 = new JButton("Send answer");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(BodyHtml!=""||BodyHtml!=null)
				{
				StringBuilder sb=new StringBuilder();
		 		
		 			
		 			sb.append("<!DOCTYPE html><html><head><title>Answer Pipeline</title></head><body>");
		 			sb.append("<table style=\"width:300px;\">");
		 			sb.append("<tr><td>Student No.</td><td>"+txtStudentNo.getText()+"</td></tr>");
		 			sb.append("<tr><td>Email</td><td>"+txtStudentEmail.getText()+"</td></tr>");
		 			sb.append("</table>");
		 			
		 			sb.append(BodyHtml);
		 			
		 			sb.append("</body></html> ");
		 			
		 			
		 		
		 		
		 		// TODO here at the next should be change to dynamic email to receiver 
		 		MailSender ssender=new MailSender();
		 		ssender.sendEmail("hesamacatest@gmail.com","Answer ACA",sb.toString());
		 	
				}
			}
		});
		button_1.setBounds(128, 90, 131, 23);
		contentPane.add(button_1);
		
		txtStudentNo = new JTextField();
		txtStudentNo.setColumns(10);
		txtStudentNo.setBounds(72, 42, 205, 20);
		contentPane.add(txtStudentNo);
		
		txtStudentEmail = new JTextField();
		txtStudentEmail.setColumns(10);
		txtStudentEmail.setBounds(72, 11, 205, 20);
		contentPane.add(txtStudentEmail);
	}
}
