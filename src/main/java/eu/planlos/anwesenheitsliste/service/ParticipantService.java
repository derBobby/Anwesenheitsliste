package eu.planlos.anwesenheitsliste.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import eu.planlos.anwesenheitsliste.model.Participant;
import eu.planlos.anwesenheitsliste.model.ParticipantRepository;
import eu.planlos.anwesenheitsliste.model.Team;
import eu.planlos.anwesenheitsliste.model.exception.EmptyIdException;

@Service
public class ParticipantService {

	private static final Logger logger = LoggerFactory.getLogger(ParticipantService.class);
	
	@Autowired
	private ParticipantRepository participantRepo;
		
	public Participant saveParticipant(Participant participant) throws DuplicateKeyException {
		
		// Exception if new and constraints violated taken
		checkUniqueConstraintsForNewParticipant(participant);
		
		logger.debug("Teilnehmer speichern");		
		return participantRepo.save(participant);
	}

	private void checkUniqueConstraintsForNewParticipant(Participant participant) throws DuplicateKeyException {

		if(participant.getIdParticipant() == null) {
			String firstName = participant.getFirstName();
			String lastName = participant.getLastName();

			// Should cover all constraints of the Entity
			if(participantRepo.existsByFirstNameAndLastName(firstName, lastName)) {
				logger.debug("Benutzer speichern nicht möglich: Die Kombination Vor- und Nachname wird bereits verwendet");
				throw new DuplicateKeyException("Die Kombination Vor- und Nachname wird bereits verwendet");
			}
		}
	}
	
	public List<Participant> loadAllParticipants() {
		logger.debug("Lade alle Teilnehmer");
		return (List<Participant>) participantRepo.findAll();
	}
	
	public Participant loadParticipant(Long idParticipant) {
		logger.debug("Lade Teilnehmer mit id " + idParticipant);
		Optional<Participant> oParticipant = participantRepo.findById(idParticipant);	
		return oParticipant.get();
	}
	
	public List<Participant> loadParticipantsForTeam(Long idTeam) {
		logger.debug("Lade Teilnehmer für Team mit id " + idTeam);
		return participantRepo.findAllByTeamsIdTeam(idTeam);
	}
	
	/**
	 * Updates relation for participant to the team. Participants not active will be ignored.
	 * @param team containing the users to update their ralation
	 * @throws EmptyIdException
	 */
	public void updateTeamForParticipants(Team team) throws EmptyIdException {

		List<Participant> uiParticipants = team.getParticipants();
		
		// throws exception if new participant is passed
		checkParticipantListForEmptyId(uiParticipants);

		logger.debug("Füge dem übergebenen Teilnehmern das Team hinzu, wenn notwendig");
		List<Participant> dbParticipants = participantRepo.findAllByTeamsIdTeam(team.getIdTeam());
		
		for(Participant uiParticipant : uiParticipants) {
			if(uiParticipant.getIsActive() && ! dbParticipants.contains(uiParticipant)) {
				logger.debug("Füge Team zu Teilnehmer hinzu: "+ uiParticipant.getIdParticipant());
				uiParticipant.addTeam(team);
				participantRepo.save(uiParticipant);
				continue;
			}
			logger.debug("Ignoriere Teilnehmer: "+ uiParticipant.getIdParticipant());
		}

		logger.debug("Entferne dem Teilnehmer aus der Datenbank das Team, wenn notwendig");
		for(Participant dbParticipant : dbParticipants) {
			if(dbParticipant.getIsActive() && ! uiParticipants.contains(dbParticipant)) {
				logger.debug("Entferne Team von Teilnehmer: "+ dbParticipant.getIdParticipant());
				dbParticipant.removeTeam(team);
				participantRepo.save(dbParticipant);
				continue;
			}
			logger.debug("Ignoriere Teilnehmer: "+ dbParticipant.getIdParticipant());
		}
	}

	private void checkParticipantListForEmptyId(List<Participant> uiParticipants) throws EmptyIdException {
		logger.debug("Prüfe Übergebene Teilnehmer auf idParticipant=Null");
		for(Participant uiParticipant : uiParticipants) {
			if(uiParticipant.getIdParticipant() == null) {
				throw new EmptyIdException("Aktualisierung fehlgeschlagen. Daten Fehlerhaft.");
			}
		}
	}
}
