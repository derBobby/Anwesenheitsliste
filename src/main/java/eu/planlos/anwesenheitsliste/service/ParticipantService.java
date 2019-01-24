package eu.planlos.anwesenheitsliste.service;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import eu.planlos.anwesenheitsliste.model.Participant;
import eu.planlos.anwesenheitsliste.model.ParticipantRepository;
import eu.planlos.anwesenheitsliste.model.Team;
import eu.planlos.anwesenheitsliste.model.Meeting;
import eu.planlos.anwesenheitsliste.model.exception.EmptyIdException;

@Service
public class ParticipantService {

	@Autowired
	private ParticipantRepository participantRepo;
		
	public Participant save(Participant participant) throws DuplicateKeyException {
		
		if(participant.getIdParticipant() == null) {
			String firstName = participant.getFirstName();
			String lastName = participant.getLastName();

			// Should cover all constraints of the Entity
			if(participantRepo.existsByFirstNameAndLastName(firstName, lastName)) {
				throw new DuplicateKeyException("Ein Teilnehmer mit dem Namen \"" + firstName + " " + lastName + "\" existiert bereits");
			}
		}
		
		return participantRepo.save(participant);
	}
	
	public void deleteUser(Participant participant) {
		participantRepo.delete(participant);
	}
	
	public List<Participant> findAll() {
		return (List<Participant>) participantRepo.findAll();
	}
	
	public Participant findById(Long idParticipant) {
		return participantRepo.findById(idParticipant).get();
	}
	
	public List<Participant> findAllByTeamsIdTeam(Long idTeam) {
		return participantRepo.findAllByTeamsIdTeam(idTeam);
	}
	
	public void updateTeamForParticipants(Team team) throws EmptyIdException {

		List<Participant> chosenParticipants = team.getParticipants();
		
		for(Participant chosenParticipant : chosenParticipants) {
			if(chosenParticipant.getIdParticipant() == null) {
				throw new EmptyIdException("Aktualisierung fehlgeschlagen. Daten Fehlerhaft.");
			}
		}
		
		List<Participant> participantsForTeam = participantRepo.findAllByTeamsIdTeam(team.getIdTeam());
		
		for(Participant chosenParticipant : chosenParticipants) {
			if(chosenParticipant.getIsActive() && ! participantsForTeam.contains(chosenParticipant)) {
				chosenParticipant.addTeam(team);
				participantRepo.save(chosenParticipant);
			}
		}
		
		for(Participant participantForTeam : participantsForTeam) {
			if(participantForTeam.getIsActive() && ! chosenParticipants.contains(participantForTeam)) {
				participantForTeam.removeTeam(team);
				participantRepo.save(participantForTeam);
			}
		}
	}
	
	public List<Participant> getInactiveParticipantsForMeeting(@Valid Meeting meeting) {
		
		if(meeting.getIdMeeting() == null) {
			return new ArrayList<Participant>();
		}
		
		List<Participant> inactiveParticipants = participantRepo.findAllByMeetingsInAndIsActive(meeting, false);
		
		return inactiveParticipants;
	}
}