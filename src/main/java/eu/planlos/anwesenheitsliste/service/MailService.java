package eu.planlos.anwesenheitsliste.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.apache.commons.compress.utils.CharsetNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailService {
	
	private static final Logger logger = LoggerFactory.getLogger(MailService.class);

	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${anwesenheitsliste.mail.recipient}")
	private String mailRecipient;
	
	@Value("${anwesenheitsliste.mail.sender}")
	private String mailSender;
	
	@Value("${anwesenheitsliste.mail.senderrornotification}")
	private boolean sendErrorNotification;
	
	@Async
	public void sendErrorNotification(String title, String errorMessage, Exception errorException, String errorTrace) {

		if(!sendErrorNotification) {
			logger.debug("Not sending email with error notification.");
			return;
		}
		
		logger.debug("Sending email with error notification.");
		
		MimeMessageHelper mailHelper;
		MimeMessage mail = javaMailSender.createMimeMessage();
		
		try {
			
			mailHelper = new MimeMessageHelper(mail, true, CharsetNames.UTF_8);
			mailHelper.setSubject("Fehler in Anwesenheitsliste: " + title);
			mailHelper.setFrom(mailSender);
			mailHelper.setTo(mailRecipient);
			
			String htmlBody =
							"<h1>Nachricht</h1><br/><br/>"
							+ "<pre>" + errorMessage + "/<pre>"
							+ "<h1>Ausnahme</h1><br/><br/>"
							+ "<pre>" + errorException + "/<pre>"
							+ "<h1>Stapel</h1><br/><br/>"
							+ "<pre>" + errorTrace + "/<pre>";

			mail.setContent(htmlBody, "text/html; charset=utf-8");
			
			javaMailSender.send(mail);
			logger.debug("Email with error notification has been sent.");
			
		} catch (MessagingException e) {
			logger.error("Email with error notification could not been sent: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
