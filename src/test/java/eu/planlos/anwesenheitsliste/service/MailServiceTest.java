package eu.planlos.anwesenheitsliste.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import javax.mail.MessagingException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class MailServiceTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@InjectMocks
	private MailService mailService;
	
	@Mock
	private JavaMailSender javaMailSender;
	
	@Test
	public final void sendMailToggleIsFalse_doesntSendMail() {
		ReflectionTestUtils.setField(mailService, "sendErrorNotification", false);
		
		mailService.sendErrorNotification("Test Titel", "Test Nachricht", new Exception(), "Test Trace");
		
		verify(javaMailSender, times(0)).createMimeMessage();
	}
	
	@Test
	public final void sendMailToggleIsTrue_sendsMail() throws MessagingException {
		//TODO DOLLC
	}
	
}
