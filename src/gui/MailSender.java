package gui;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
/**
 * 
 * @author Hesam
 *
 */
public class MailSender {

	public boolean sendEmail(String to,String subject,String body )
	{
		 // Recipient's email ID needs to be mentioned.

	      // Sender's email ID needs to be mentioned
	      String from = "hesamacatest@gmail.com";//change accordingly
	      final String username = "hesamacatest@gmail.com";//change accordingly
	      final String password = "a123456&";//change accordingly

	      // Assuming you are sending email through relay.jangosmtp.net
//	      String host = "smtp.gmail.com";
//
	      Properties props = new Properties();
//	      props.put("mail.smtp.auth", "true");
//	      props.put("mail.smtp.starttls.enable", "true");
//	      props.put("mail.smtp.host", host);
//	      props.put("mail.smtp.port", "587");

	      props.put("mail.smtp.host", "smtp.gmail.com"); 
	             props.put("mail.smtp.socketFactory.port", "465"); 
	             props.put("mail.smtp.socketFactory.class", 
	                     "javax.net.ssl.SSLSocketFactory"); 
	             props.put("mail.smtp.auth", "true"); 

	             props.put("mail.smtp.port", "465"); 

	      // Get the Session object.
	      Session session = Session.getInstance(props,
	      new javax.mail.Authenticator() {
	         protected PasswordAuthentication getPasswordAuthentication() {
	            return new PasswordAuthentication(username, password);
	         }
	      });

	      try {
	         // Create a default MimeMessage object.
	         Message message = new MimeMessage(session);

	         // Set From: header field of the header.
	         message.setFrom(new InternetAddress(from));

	         // Set To: header field of the header.
	         message.setRecipients(Message.RecipientType.TO,
	         InternetAddress.parse(to));

	         // Set Subject: header field
	         message.setSubject(subject);

	         // Now set the actual message
	         message.setContent(body, "text/html; charset=utf-8");

	         // Send message
	         Transport.send(message);

	         System.out.println("Sent message successfully....");

	      } catch (MessagingException e) {
	            throw new RuntimeException(e);
	      }
	      
		return false;
	}
	
}
