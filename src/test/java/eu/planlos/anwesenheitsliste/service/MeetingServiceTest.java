package eu.planlos.anwesenheitsliste.service;

import static org.junit.Assert.assertTrue;
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

import eu.planlos.anwesenheitsliste.model.Meeting;
import eu.planlos.anwesenheitsliste.model.Participant;
import eu.planlos.anwesenheitsliste.model.ParticipantRepository;

@RunWith(MockitoJUnitRunner.class)
public class MeetingServiceTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}
	
	@InjectMocks
	private MeetingService meetingService;
	
	@Mock
	private ParticipantRepository participantRepo;

	@Test
	public final void newMeetingGiven_doNotAddInactive() {
		Meeting newMeeting = new Meeting();
		
		meetingService.correctInactiveParticipants(newMeeting);
	
		verify(participantRepo, times(0)).findAllByMeetingsContainingAndIsActive(newMeeting, false);
	}

	@Test
	public final void inactiveParticipantGiven_isRemoved() {
		Participant activeParticipant = new Participant();
		activeParticipant.setIdParticipant(1L);
		activeParticipant.setIsActive(true);
		
		Participant inactiveParticipant = new Participant();
		inactiveParticipant.setIdParticipant(2L);
		inactiveParticipant.setIsActive(false);	
		
		List<Participant> uiParticipantList = new ArrayList<>();
		uiParticipantList.add(activeParticipant);
		uiParticipantList.add(inactiveParticipant);
		
		Meeting newMeeting = new Meeting();
		newMeeting.setIdMeeting(1L);
		newMeeting.addParticipants(uiParticipantList);
		
		List<Participant> dbParticipantList = new ArrayList<>();
		dbParticipantList.add(inactiveParticipant);
		
		when(participantRepo.findAllByTeamsContainingAndIsActive(newMeeting.getTeam(), false)).thenReturn(dbParticipantList);
		
		meetingService.correctInactiveParticipants(newMeeting);
	

		//TODO C.D.
		debugPringList(newMeeting.getParticipants());
		debugPringList(uiParticipantList);

		assertTrue(!newMeeting.getParticipants().contains(inactiveParticipant));
	}

	private void debugPringList(List<Participant> pList) {
		System.out.println("############## returned:");
		for(Participant p : pList) {
			System.out.println(p.getIdParticipant());
		}
		System.out.println("############## ");
	}
	
	@Test
	public final void inactiveParticipantMissing_isAdded() {
		Participant activeParticipant = new Participant();
		activeParticipant.setIdParticipant(1L);
		activeParticipant.setIsActive(true);
		
		List<Participant> uiParticipantList = new ArrayList<>();
		uiParticipantList.add(activeParticipant);
		
		Meeting newMeeting = new Meeting();
		newMeeting.setIdMeeting(1L);
		newMeeting.addParticipants(uiParticipantList);
		
		Participant inactiveParticipant = new Participant();
		inactiveParticipant.setIdParticipant(2L);
		inactiveParticipant.setIsActive(false);	

		List<Participant> dbParticipantList = new ArrayList<>();
		dbParticipantList.add(inactiveParticipant);
		
		when(participantRepo.findAllByMeetingsContainingAndIsActive(newMeeting, false)).thenReturn(dbParticipantList);
		
		meetingService.correctInactiveParticipants(newMeeting);
	
		assertTrue(newMeeting.getParticipants().contains(inactiveParticipant));
	}
}
