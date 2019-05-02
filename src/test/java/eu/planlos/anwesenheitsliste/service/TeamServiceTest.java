package eu.planlos.anwesenheitsliste.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DuplicateKeyException;

import eu.planlos.anwesenheitsliste.model.Team;
import eu.planlos.anwesenheitsliste.model.TeamRepository;

@RunWith(MockitoJUnitRunner.class)
public class TeamServiceTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	private static final String TESTTEAMNAME = "Testteam";
	
	private static final Team TESTTEAMNEW = new Team(TESTTEAMNAME);
	
	@InjectMocks
	private TeamService teamService;
	
	@Mock
	private TeamRepository teamRepo;

	@Test(expected = DuplicateKeyException.class)
	public final void newTeamNameTaken_throwsDuplicateKeyException() {
		when(teamRepo.existsByTeamName(TESTTEAMNAME)).thenReturn(true);

		teamService.saveTeam(TESTTEAMNEW);
	}
	
	@Test
	public final void newTeamNameFree_savesUser() {
		when(teamRepo.existsByTeamName(TESTTEAMNAME)).thenReturn(false);

		teamService.saveTeam(TESTTEAMNEW);
		
		verify(teamRepo, times(1)).save(TESTTEAMNEW);
	}
	
	@Test
	public final void editedTeam_savesUser() {

		Team editedTeam = new Team(TESTTEAMNAME);
		editedTeam.setIdTeam(1L);
		
		teamService.saveTeam(TESTTEAMNEW);
		
		verify(teamRepo, times(1)).save(TESTTEAMNEW);
	}

}
