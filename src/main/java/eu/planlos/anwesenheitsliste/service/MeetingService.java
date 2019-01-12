package eu.planlos.anwesenheitsliste.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.planlos.anwesenheitsliste.model.Meeting;
import eu.planlos.anwesenheitsliste.model.MeetingRepository;
import eu.planlos.anwesenheitsliste.model.Participant;
import eu.planlos.anwesenheitsliste.model.Team;

@Service
public class MeetingService {

	@Autowired
	private MeetingRepository meetingRepo;
	
	@Autowired
	private TeamService teamService;
	
	//No unique constraint so validation should not be necessary
	public Meeting save(Meeting meeting) {
		return meetingRepo.save(meeting);
	}
	
	public void deleteUser(Meeting meeting) {
		meetingRepo.delete(meeting);
	}
	
	public List<Meeting> findAll() {
		return (List<Meeting>) meetingRepo.findAll();
	}
	
	public List<Meeting> findAllByTeam(Long idTeam) {
		return (List<Meeting>) meetingRepo.findAllByTeamIdTeam(idTeam);
	}
	
	public Meeting findById(Long idMeeting) {
		return meetingRepo.findById(idMeeting).get();
	}
	
	public List<Participant> getOnlyEditableParticipantsForMeeting(Meeting meeting) {

		Long idTeam = meeting.getTeam().getIdTeam();
		
		List<Participant> participants = meeting.getParticipants();
		
		// Load Meeting from DB
		Team team = teamService.findById(idTeam);
		
		// Get all possible P's for Meeting via Team from DB
		List<Participant> dbParticipants = team.getParticipants();
		
		// Discard all active P's
		//TODO dbParticipants.removeIf(filter)
		List<Participant> inactiveParticipants = new ArrayList<>();
		for(Participant dbParticipant : dbParticipants) {
			if(dbParticipant.getIsActive()==false) {
				inactiveParticipants.add(dbParticipant);
			}
		}
		
		// Remove remaining inactive P's from DB to remove all inactive from given P's
		participants.removeAll(inactiveParticipants);
			
		return participants;
	}
}
