package eu.planlos.anwesenheitsliste.service;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger logger = LoggerFactory.getLogger(ParticipantService.class);
	
	@Autowired
	private ParticipantRepository participantRepo;
		
	public Participant saveParticipant(Participant participant) throws DuplicateKeyException {
		
		if(participant.getIdParticipant() == null) {
			String firstName = participant.getFirstName();
			String lastName = participant.getLastName();

			// Should cover all constraints of the Entity
			if(participantRepo.existsByFirstNameAndLastName(firstName, lastName)) {
				logger.debug("Benutzer speichern nicht möglich: Die Kombination Vor- und Nachname wird bereits verwendet");
				throw new DuplicateKeyException("Die Kombination Vor- und Nachname wird bereits verwendet");
			}
		}
		logger.debug("Speichere Teilnehmer");		
		return participantRepo.save(participant);
	}
	
	public List<Participant> loadAllParticipants() {
		logger.debug("Lade alle Teilnehmer");
		return (List<Participant>) participantRepo.findAll();
	}
	
	//TODO throws doesnotexist?
	public Participant loadParticipant(Long idParticipant) {
		logger.debug("Lade Teilnehmer mit id " + idParticipant);
		return participantRepo.findById(idParticipant).get();
	}
	
	public List<Participant> loadParticipantsForTeam(Long idTeam) {
		logger.debug("Lade Teilnehmer für Team mit id " + idTeam);
		return participantRepo.findAllByTeamsIdTeam(idTeam);
	}
	
	//TODO errors here probably
	//TODO logging
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

	//TODO errors here probably
	//TODO logging
	public void correctParticipantsInMeeting(@Valid Meeting meeting) {
	
		List<Participant> givenParticipants = meeting.getParticipants();
		
		// 1.) HTML currently doesn't send disabled checkboxes so we need to correct these
		// 2.) Disabled checkboxes (=inactive participants) can be manipulated so that:
		// ----> unchecked could be checked -> remove db-inactive from POST
		// ----> checked could be unchecked -> find in POST missing and insert from db
		
		List<Participant> remove = participantRepo.findAllByTeamsContainingAndIsActive(meeting.getTeam(), false);
		givenParticipants.removeAll(remove);
		
		List<Participant> add = participantRepo.findAllByMeetingsContainingAndIsActive(meeting, false);
		givenParticipants.addAll(add);
	}
}
