package eu.planlos.anwesenheitsliste.service;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.planlos.anwesenheitsliste.model.Meeting;
import eu.planlos.anwesenheitsliste.model.MeetingRepository;
import eu.planlos.anwesenheitsliste.model.Participant;
import eu.planlos.anwesenheitsliste.model.ParticipantRepository;

@Service
public class MeetingService {

	private static final Logger logger = LoggerFactory.getLogger(MeetingService.class);

	@Autowired
	private ParticipantRepository participantRepo;
	
	@Autowired
	private MeetingRepository meetingRepo;
		
	//No unique constraint so validation should not be necessary
	public Meeting saveMeeting(Meeting meeting) {
		logger.debug("Speichere Termine");
		return meetingRepo.save(meeting);
	}
	
	public List<Meeting> loadAllMeetings() {
		logger.debug("Lade alle Termine");
		return (List<Meeting>) meetingRepo.findAll();
	}
	
	public List<Meeting> loadMeetingsForTeam(Long idTeam) {
		logger.debug("Lade Termine für Team mit id " + idTeam);
		return meetingRepo.findAllByTeamIdTeam(idTeam);
	}
	
	public Meeting loadMeeting(Long idMeeting) {
		logger.debug("Lade Termin mit id " + idMeeting);
		return meetingRepo.findById(idMeeting).get();
	}
	
	public void correctInactiveParticipants(@Valid Meeting meeting) {
			
		List<Participant> givenParticipants = meeting.getParticipants();

		// 1.) HTML currently doesn't send disabled checkboxes so we need to correct these
		// 2.) Disabled checkboxes (=inactive participants) can be manipulated so that:
		// ----> unchecked could be checked -> remove db-inactive from POST
		// ----> checked could be unchecked -> find in POST missing and insert from db
		
		logger.debug("Entferne Teilnehmer, die eigentlich inaktiv sind");
		List<Participant> remove = participantRepo.findAllByTeamsContainingAndIsActive(meeting.getTeam(), false);
		givenParticipants.removeAll(remove);
		
		if(meeting.getIdMeeting() != null ) {
			logger.debug("Füge inaktive Teilnehmer, die bereits hinzugefügt wurden, wieder hinzu");
			List<Participant> add = participantRepo.findAllByMeetingsContainingAndIsActive(meeting, false);
			givenParticipants.addAll(add);
		}
	}
}
