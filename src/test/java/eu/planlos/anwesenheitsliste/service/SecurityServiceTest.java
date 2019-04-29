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
	
	String testLoginName = "My Loginname";
	
	//TODO Tests missing for isAdmin==true
	
	/*
	 * Tests for isUserAuthorizedForTeam()
	 */
	
	 @Test
	public final void userIsNotAuthorizedForTeam_returnsTrueTeamAuthorization() {
		when(session.getAttribute(SessionAttributes.ISADMIN)).thenReturn(true);
		when(session.getAttribute(SessionAttributes.LOGINNAME)).thenReturn(testLoginName);
		 
		boolean givenIsAuthorized = securityService.isUserAuthorizedForTeam(session, 1);
		 
		assertTrue(givenIsAuthorized);
	 }
	 
	 @Test
	public final void userIsAdmin_returnsTrue() {
		when(session.getAttribute(SessionAttributes.ISADMIN)).thenReturn(false);
		when(session.getAttribute(SessionAttributes.LOGINNAME)).thenReturn(testLoginName);
		when(teamRepo.existsByIdTeamAndUsersLoginName(1, testLoginName)).thenReturn(false);
		 
		boolean givenIsAuthorized = securityService.isUserAuthorizedForTeam(session, 1);
		 
		assertFalse(givenIsAuthorized);
	 }
	 
	@Test
	public final void userIsAuthorizedForTeam_returnsTrue() {
		when(session.getAttribute(SessionAttributes.ISADMIN)).thenReturn(false);
		when(session.getAttribute(SessionAttributes.LOGINNAME)).thenReturn(testLoginName);
		when(teamRepo.existsByIdTeamAndUsersLoginName(1, testLoginName)).thenReturn(true);
		 
		boolean givenIsAuthorized = securityService.isUserAuthorizedForTeam(session, 1);
			 
		assertTrue(givenIsAuthorized);		 
	 }
	 
	@Test
	public final void sessionIsNull_returnsFalseTeamAuthorization() {
		 HttpSession session = null;
		 
		 boolean givenIsAuthorized = securityService.isUserAuthorizedForTeam(session, 1);
		 
		 assertFalse(givenIsAuthorized);
	 }
	
	/*
	 * Tests for isUserAuthorizedForParticipant()
	 */
	
	@Test
	public final void userIsAdmin_returnsTrueParticipantAuthorization() {
		when(session.getAttribute(SessionAttributes.LOGINNAME)).thenReturn(testLoginName);
		when(session.getAttribute(SessionAttributes.ISADMIN)).thenReturn(true);
		
		boolean givenIsAuthorized = securityService.isUserAuthorizedForParticipant(session, 1);

		assertTrue(givenIsAuthorized);
	}
	
	@Test
	public final void userIsAuthorizedForParticipant_returnsTrue() {
		when(session.getAttribute(SessionAttributes.ISADMIN)).thenReturn(false);
		when(session.getAttribute(SessionAttributes.LOGINNAME)).thenReturn(testLoginName);
		when(teamRepo.existsByParticipantsIdParticipantAndUsersLoginName(1, testLoginName)).thenReturn(true);
		
		boolean givenIsAuthorized = securityService.isUserAuthorizedForParticipant(session, 1);
		
		assertTrue(givenIsAuthorized);
	}	

	@Test
	public final void userIsNotAuthorizedForParticipant_returnsFalse() {
		when(session.getAttribute(SessionAttributes.ISADMIN)).thenReturn(false);
		when(session.getAttribute(SessionAttributes.LOGINNAME)).thenReturn(testLoginName);
		when(teamRepo.existsByParticipantsIdParticipantAndUsersLoginName(1, testLoginName)).thenReturn(false);
		
		boolean givenIsAuthorized = securityService.isUserAuthorizedForParticipant(session, 1);
		
		assertFalse(givenIsAuthorized);
	}
	
	@Test
	public final void sessionIsNull_returnsFalseParticipantAuthorization() {
		HttpSession session = null;
		
		boolean givenIsAuthorized = securityService.isUserAuthorizedForParticipant(session, 1);
		
		assertFalse(givenIsAuthorized);
	}

	
	/*
	 * Tests for isAdmin()
	 */
	
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
	public final void sessionIsNull_returnsFalseForIsAdmin() {
		HttpSession session = null;
		
		boolean givenIsAdmin = securityService.isAdmin(session);
		
		assertFalse(givenIsAdmin);
	}
	
	/*
	 * Tests for getLoginName()
	 */
	
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
	
	@Test
	public final void sessionIsNull_givesNoName() {
		String expectedLoginName = null;
		
		String givenLoginName = securityService.getLoginName(null);
		
		assertEquals(expectedLoginName, givenLoginName);
	}
}