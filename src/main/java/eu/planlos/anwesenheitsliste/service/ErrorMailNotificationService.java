package eu.planlos.anwesenheitsliste.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.compress.utils.CharsetNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ErrorMailNotificationService {
	
	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${anwesenheitsliste.mail.recipient}")
	private String mailRecipient;
	
	@Value("${anwesenheitsliste.mail.sender}")
	private String mailSender;
	
	@Value("${anwesenheitsliste.mail.senderrornotification}")
	private boolean sendErrorNotification;
	
	@Async
	public void sendEmailNotification(String title, String errorMessage, Exception errorException, String errorTrace) {

		if(!sendErrorNotification) {
			return;
		}
		
//		while(true) {
//			try {
//				Thread.sleep(1000);
//				System.out.println("sleeping...");
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//				break;
//			}
//		}
		
		MimeMessage mail = javaMailSender.createMimeMessage();
		MimeMessageHelper mailHelper;
		
		try {
			mailHelper = new MimeMessageHelper(mail, true, CharsetNames.UTF_8);
			mailHelper.setSubject("Fehler in Anwesenheitsliste: " + title);
			mailHelper.setFrom(mailSender);
			mailHelper.setTo(mailSender);
			
			String htmlBody =
							"<h1>Nachricht</h1><br/><br/>"
							+ "<pre>" + errorMessage + "/<pre>"
							+ "<h1>Ausnahme</h1><br/><br/>"
							+ "<pre>" + errorException + "/<pre>"
							+ "<h1>Stapel</h1><br/><br/>"
							+ "<pre>" + errorTrace + "/<pre>";

			mail.setContent(htmlBody, "text/html; charset=utf-8");
			
			javaMailSender.send(mail);
			
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}