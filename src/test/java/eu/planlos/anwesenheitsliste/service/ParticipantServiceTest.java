package eu.planlos.anwesenheitsliste.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DuplicateKeyException;

import eu.planlos.anwesenheitsliste.model.Participant;
import eu.planlos.anwesenheitsliste.model.ParticipantRepository;
import eu.planlos.anwesenheitsliste.model.Team;
import eu.planlos.anwesenheitsliste.model.exception.EmptyIdException;

@RunWith(MockitoJUnitRunner.class)
public class ParticipantServiceTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@InjectMocks
	private ParticipantService participantService;
	
	@Mock
	private ParticipantRepository participantRepo;
	
	/*
	 * Tests für Methode: saveParticipant
	 */
	@Test(expected = DuplicateKeyException.class)
	public final void saveNewParticipantNameTaken_throwsDuplicateKeyException() {
		Participant participant = new Participant();
		participant.setFirstName("First");
		participant.setLastName("Last");
		
		when(participantRepo.existsByFirstNameAndLastName("First", "Last")).thenReturn(true);
		
		participantService.saveParticipant(participant);
	}
	
	@Test
	public final void saveExistingParticipantNameTaken_savesParticipant() {
		Participant participant = new Participant();
		participant.setIdParticipant(1L);
		participant.setFirstName("First");
		participant.setLastName("Last");
			
		participantService.saveParticipant(participant);
		
		verify(participantRepo, times(1)).save(participant);
	}
	
	@Test
	public final void saveNewParticipantNameFree_savesParticipant() {
		Participant participant = new Participant();
		participant.setFirstName("First");
		participant.setLastName("Last");
		
		when(participantRepo.existsByFirstNameAndLastName("First", "Last")).thenReturn(false);
		
		participantService.saveParticipant(participant);
		
		verify(participantRepo, times(1)).save(participant);
	}
	
	/*
	 * Tests für Methode: updateTeamForParticipants 
	 */

	@Test(expected = EmptyIdException.class)
	public final void newParticipantWithoutIdGiven_throwsException() throws EmptyIdException {
		Team testTeam = new Team("Testteam");
		Participant newParticipant = new Participant();
		List<Participant> participantList = new ArrayList<>();
		participantList.add(newParticipant);
		testTeam.setParticipants(participantList);
		
		participantService.updateTeamForParticipants(testTeam);		
	}
	
	@Test
	public final void inactiveParticipantSubmitted_ParticipantIsIgnored() throws EmptyIdException {
		Participant uiParticipant = new Participant();
		uiParticipant.setIsActive(false);
		Team team = new Team();
		List<Participant> participantList = new ArrayList<>();
		team.setParticipants(participantList);
		
		participantService.updateTeamForParticipants(team);
		
		verify(participantRepo, times(0)).save(uiParticipant);		
	}
	
	@Test
	public final void participantAdditionallyAdded_ParticipantIsSaved() throws EmptyIdException {
		Participant uiParticipant = new Participant();
		uiParticipant.setIdParticipant(1L);
		uiParticipant.setIsActive(true);
		List<Participant> uiParticipantList = new ArrayList<>();
		uiParticipantList.add(uiParticipant);
		Team team = new Team();
		team.setParticipants(uiParticipantList);

		List<Participant> dbParticipantList = new ArrayList<>();

		when(participantRepo.findAllByTeamsIdTeam(team.getIdTeam())).thenReturn(dbParticipantList);
		
		participantService.updateTeamForParticipants(team);

		verify(participantRepo, times(1)).save(uiParticipant);
	}
	
	@Test
	public final void participantAlreadyAdded_ParticipantIsNotSave() throws EmptyIdException {
		Participant uiParticipant = new Participant();
		uiParticipant.setIdParticipant(1L);
		uiParticipant.setIsActive(true);
		List<Participant> uiParticipantList = new ArrayList<>();
		uiParticipantList.add(uiParticipant);
		Team team = new Team();
		team.setParticipants(uiParticipantList);
		
		when(participantRepo.findAllByTeamsIdTeam(team.getIdTeam())).thenReturn(uiParticipantList);
		
		participantService.updateTeamForParticipants(team);
		
		verify(participantRepo, times(0)).save(uiParticipant);
	}
	
	@Test
	public final void participantAdditionallyRemoved_ParticipantIsSaved() throws EmptyIdException {
		List<Participant> uiParticipantList = new ArrayList<>();
		Team team = new Team();
		team.setParticipants(uiParticipantList);
		
		Participant dbParticipant = new Participant();
		dbParticipant.setIsActive(true);
		List<Participant> dbParticipantList = new ArrayList<>();
		dbParticipantList.add(dbParticipant);
		
		when(participantRepo.findAllByTeamsIdTeam(team.getIdTeam())).thenReturn(dbParticipantList);
		
		participantService.updateTeamForParticipants(team);
		
		verify(participantRepo, times(1)).save(dbParticipant);
	}
}
