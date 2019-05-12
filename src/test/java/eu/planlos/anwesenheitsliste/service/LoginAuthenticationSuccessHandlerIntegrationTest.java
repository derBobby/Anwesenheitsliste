package eu.planlos.anwesenheitsliste.service;

import static org.junit.Assert.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;

import eu.planlos.anwesenheitsliste.security.LoginAuthenticationSuccessHandler;

@RunWith(MockitoJUnitRunner.class)
public class LoginAuthenticationSuccessHandlerIntegrationTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}
	
	@InjectMocks
	private LoginAuthenticationSuccessHandler handler;
	
	@Mock
	private HttpServletRequest request;
	
	@Mock
	private HttpServletResponse response;
	
	@Mock
	private Authentication authentication;
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
