package eu.planlos.anwesenheitsliste.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.planlos.anwesenheitsliste.SessionAttributes;

public class SecurityServiceTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public final void xxx() {
		fail("Not yet implemented"); // TODO
	}
	
	

	@Test
	public final void userIsAuthorizedForParticipant_returnsTrue() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void userIsAdminAndThereforeAuthorizedForParticipant_returnsTrue() {
		fail("Not yet implemented"); // TODO
	}
	
	@Test
	public final void userIsNotAuthorizedForParticipant_returnsFalse() {
		fail("Not yet implemented"); // TODO
	}
	
	
	
	
	@Test
	public final void isAdmin_returnsTrue() {
		SecurityService securityService = new SecurityService();
		HttpSession session = mock(HttpSession.class);
		when(session.getAttribute(SessionAttributes.ISADMIN)).thenReturn(true);
		
		boolean givenIsAdmin = securityService.isAdmin(session);
		
		assertTrue(givenIsAdmin);
	}
	
	@Test
	public final void isNotAdmin_returnFalse() {
		SecurityService securityService = new SecurityService();
		HttpSession session = mock(HttpSession.class);
		when(session.getAttribute(SessionAttributes.ISADMIN)).thenReturn(false);
		
		boolean givenIsAdmin = securityService.isAdmin(session);
		
		assertFalse(givenIsAdmin);	
	}

	@Test
	public final void isLoggedIn_givesName() {
		String expectedLoginName = "testMcSwagger";
		SecurityService securityService = new SecurityService();
		HttpSession session = mock(HttpSession.class);
		when(session.getAttribute(SessionAttributes.LOGINNAME)).thenReturn(expectedLoginName);
		
		String givenLoginName = securityService.getLoginName(session);
		
		assertEquals(givenLoginName, expectedLoginName);
	}

	//TODO throw not authenticated error?
	@Test
	public final void isNotLoggedIn_givesNoName() {
		String expectedLoginName = null;
		SecurityService securityService = new SecurityService();
		HttpSession session = mock(HttpSession.class);
		when(session.getAttribute(SessionAttributes.LOGINNAME)).thenReturn(expectedLoginName);
		
		String givenLoginName = securityService.getLoginName(session);
		
		assertEquals(givenLoginName, expectedLoginName);
	}
}