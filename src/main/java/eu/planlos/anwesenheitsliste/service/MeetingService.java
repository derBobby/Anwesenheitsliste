package eu.planlos.anwesenheitsliste.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.planlos.anwesenheitsliste.model.Meeting;
import eu.planlos.anwesenheitsliste.model.MeetingRepository;

@Service
public class MeetingService {

	private static final Logger logger = LoggerFactory.getLogger(MeetingService.class);

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
}
