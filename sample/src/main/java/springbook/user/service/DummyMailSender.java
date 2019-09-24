package springbook.user.service;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class DummyMailSender implements MailSender{
	public void send(SimpleMailMessage mailMessage)throws MailException{
		System.out.println("Subject: "+mailMessage.getSubject());
		System.out.println("Text: "+mailMessage.getText());
	}
	public void send(SimpleMailMessage[] mailMessage)throws MailException{}
}
