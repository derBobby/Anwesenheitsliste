package eu.planlos.anwesenheitsliste.controllers.user;

import static eu.planlos.anwesenheitsliste.ApplicationPath.RES_TEAMPHONELIST;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

import eu.planlos.anwesenheitsliste.model.Team;
import eu.planlos.anwesenheitsliste.service.BodyFillerService;
import eu.planlos.anwesenheitsliste.service.ParticipantService;
import eu.planlos.anwesenheitsliste.service.SecurityService;
import eu.planlos.anwesenheitsliste.service.TeamService;

@RunWith(MockitoJUnitRunner.class)
public class TeamPhoneListControllerTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@InjectMocks
	private TeamPhonelistController controller;
	
	@Mock
	private SecurityService securityService;
	
	@Mock
	private ParticipantService participantService;
	
	@Mock
	private TeamService teamService;
	
	@Mock
	private BodyFillerService bodyFillerService;
	
	@Mock
	private Model model;
	
	@Mock
	private Authentication auth;
	
	@Mock
	private HttpSession session;
	
	@Test
	public void isAuthorized_returnsTemplate() {
		Team team = new Team();
		team.setTeamName("Test Team");
		
		when(securityService.isUserAuthorizedForTeam(Mockito.any(HttpSession.class), Mockito.any(Long.class))).thenReturn(true);
		when(teamService.loadTeam(Mockito.any(Long.class))).thenReturn(team);
		
		String returnValue = controller.teamPhoneList(model, auth, session, 1L);
		
		assertEquals(RES_TEAMPHONELIST, returnValue);
	}
	
	@Test
	public void isNotAuthorized_doesNotReturnTemplate() {
		Team team = new Team();
		team.setTeamName("Test Team");
		
		when(securityService.isUserAuthorizedForTeam(Mockito.any(HttpSession.class), Mockito.any(Long.class))).thenReturn(false);
		
		String returnValue = controller.teamPhoneList(model, auth, session, 1L);
		
		assertNotEquals(RES_TEAMPHONELIST, returnValue);
	}

}
