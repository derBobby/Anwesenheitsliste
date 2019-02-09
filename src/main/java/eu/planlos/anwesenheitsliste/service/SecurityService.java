package eu.planlos.anwesenheitsliste.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.planlos.anwesenheitsliste.model.TeamRepository;

@Service
public class SecurityService {

	private static final Logger logger = LoggerFactory.getLogger(SecurityService.class);
	
	@Autowired
	private TeamRepository teamRepo;
	
	public boolean isUserAuthorizedForTeam(long idTeam, String loginName) {
		logger.debug("Prüfe ob Benutzer " + loginName + " Zugriff hat auf Team mit id " + idTeam);
		return teamRepo.existsByIdTeamAndUsersLoginName(idTeam, loginName);
	}
	
	public boolean isUserStaffForParticipant(long idParticipant, String loginName) {
		logger.debug("Prüfe ob Benutzer " + loginName + " Zugriff hat auf Teilnehmer mit id " + idParticipant);
		return teamRepo.existsByParticipantsIdParticipantAndUsersLoginName(idParticipant, loginName);
	}
}

