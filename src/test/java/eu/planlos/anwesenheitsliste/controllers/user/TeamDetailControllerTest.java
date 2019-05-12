package eu.planlos.anwesenheitsliste.controllers.user;

import static eu.planlos.anwesenheitsliste.ApplicationPath.RES_TEAM;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

import eu.planlos.anwesenheitsliste.model.Team;
import eu.planlos.anwesenheitsliste.service.BodyFillerService;
import eu.planlos.anwesenheitsliste.service.ParticipantService;
import eu.planlos.anwesenheitsliste.service.SecurityService;
import eu.planlos.anwesenheitsliste.service.TeamService;
import eu.planlos.anwesenheitsliste.service.UserService;

@RunWith(MockitoJUnitRunner.class)
public class TeamDetailControllerTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@InjectMocks
	private TeamDetailController teamDetailController;
	
	@Mock
	private SecurityService securityService;
	
	@Mock
	private TeamService teamService;
	
	@Mock
	private UserService userService;
	
	@Mock
	private ParticipantService participantService;
	
	@Mock
	private BodyFillerService bf;
	
	@Mock
	private Model model;
	
	@Mock
	private Authentication auth;
	
	@Mock
	private HttpSession session;
	
	@Test
	public void showTeamDetailIsNotAuthorized_doesntReturnTemplate() {
		long idTeam = 1L;
		when(securityService.getLoginName(session)).thenReturn("Loginname");
		when(securityService.isUserAuthorizedForTeam(session, idTeam)).thenReturn(false);
		
		String returnValue = teamDetailController.edit(model, auth, session, idTeam);
		
		assertNotEquals(RES_TEAM, returnValue);
	}
	
	@Test
	public void showTeamDetailIsAuthorized_ReturnsTemplate() {
		long idTeam = 1L;
		Team team = new Team();
		team.setIdTeam(idTeam);
		
		when(teamService.loadTeam(idTeam)).thenReturn(team);
		when(securityService.getLoginName(session)).thenReturn("Loginname");
		when(securityService.isUserAuthorizedForTeam(session, 1L)).thenReturn(true);
		
		String returnValue = teamDetailController.edit(model, auth, session, 1L);
		
		assertEquals(RES_TEAM, returnValue);
	}
	
	@Test
	public void submitTeamDetailIsAuthorized_XXXX() {
		fail();
	}
	
	@Test
	public void submitTeamDetailIsNotAuthorized_XXXX() {
		fail();
	}
}