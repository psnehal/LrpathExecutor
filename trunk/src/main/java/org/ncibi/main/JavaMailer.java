package org.ncibi.main;

import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;



public class JavaMailer
{
	
	public void sendMail(String sender, String recipient, String subject, String emailMessage)
	{
		ResourceBundle url = ResourceBundle.getBundle("org.ncibi.resource.bundle.url");
		Properties props = new Properties();
		props.put(url.getString("mailSmtpHostKey"), url.getString("mailSmtpHostValue"));
		props.put(url.getString("mailSmtpPortkey"), url.getString("mailSmtpPortValue"));

		Session session = Session.getDefaultInstance(props);

		try
		{
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(sender));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
			message.setSubject(subject);
			message.setContent(emailMessage, "text/html");
			System.out.println(url.getString("mailSmtpHostKey"));
			System.out.println(url.getString("mailSmtpHostValue"));
			Transport.send(message);
		}
		catch (MessagingException e)
		{
			throw new RuntimeException(e);
		}
	}
}