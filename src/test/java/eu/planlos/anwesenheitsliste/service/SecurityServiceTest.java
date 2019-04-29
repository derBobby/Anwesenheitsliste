package eu.planlos.anwesenheitsliste.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import eu.planlos.anwesenheitsliste.SessionAttributes;
import eu.planlos.anwesenheitsliste.model.TeamRepository;

@RunWith(MockitoJUnitRunner.class)
public class SecurityServiceTest {

	@InjectMocks
	private SecurityService securityService;
	
	@Mock
	private TeamRepository teamRepo;
	
	@Mock
	private HttpSession session;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}
	
	//TODO something is wrong...
	@Test
	public final void userIsAuthorizedForParticipant_returnsTrue() {
		when(session.getAttribute(SessionAttributes.ISADMIN)).thenReturn(false);
		when(session.getAttribute(SessionAttributes.LOGINNAME)).thenReturn("My Loginname");
		when(teamRepo.existsByParticipantsIdParticipantAndUsersLoginName(1, "My Loginname")).thenReturn(true);
		
		boolean givenIsAuthorized = securityService.isUserAuthorizedForParticipant(session, 1);
		
		assertTrue(givenIsAuthorized);
	}	
	
	@Test
	public final void userIsNotAuthorizedForParticipant_returnsFalse() {
		when(session.getAttribute(SessionAttributes.ISADMIN)).thenReturn(false);
		when(session.getAttribute(SessionAttributes.LOGINNAME)).thenReturn("My Loginname");
		when(teamRepo.existsByParticipantsIdParticipantAndUsersLoginName(1, "My Loginname")).thenReturn(false);
		
		boolean givenIsAuthorized = securityService.isUserAuthorizedForParticipant(session, 1);
		
		assertFalse(givenIsAuthorized);
	}
	
	@Test
	public final void isAdmin_returnsTrue() {
		when(session.getAttribute(SessionAttributes.ISADMIN)).thenReturn(true);
		
		boolean givenIsAdmin = securityService.isAdmin(session);
		
		assertTrue(givenIsAdmin);
	}
	
	@Test
	public final void isNotAdmin_returnFalse() {
		when(session.getAttribute(SessionAttributes.ISADMIN)).thenReturn(false);
		
		boolean givenIsAdmin = securityService.isAdmin(session);
		
		assertFalse(givenIsAdmin);	
	}

	@Test
	public final void isLoggedIn_givesName() {
		String expectedLoginName = "testMcSwagger";
		when(session.getAttribute(SessionAttributes.LOGINNAME)).thenReturn(expectedLoginName);
		
		String givenLoginName = securityService.getLoginName(session);
		
		assertEquals(givenLoginName, expectedLoginName);
	}

	@Test
	public final void isNotLoggedIn_givesNoName() {
		String expectedLoginName = null;
		when(session.getAttribute(SessionAttributes.LOGINNAME)).thenReturn(expectedLoginName);
		
		String givenLoginName = securityService.getLoginName(session);
		
		assertEquals(givenLoginName, expectedLoginName);
	}
}