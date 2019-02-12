package eu.planlos.anwesenheitsliste.service;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.planlos.anwesenheitsliste.SessionAttributes;
import eu.planlos.anwesenheitsliste.model.TeamRepository;

@Service
public class SecurityService {

	private static final Logger logger = LoggerFactory.getLogger(SecurityService.class);
	
	@Autowired
	private TeamRepository teamRepo;
	
	/**
	 * Return true if user is an admin or if he is authorized for the given team.
	 * @param session
	 * @param idTeam
	 * @return
	 */
	public boolean isUserAuthorizedForTeam(HttpSession session, long idTeam) {
		logger.debug("Prüfe ob Benutzer " + getLoginName(session) + " Zugriff hat auf Team mit id " + idTeam);
		return isAdmin(session) || teamRepo.existsByIdTeamAndUsersLoginName(idTeam, getLoginName(session));
	}
	
	public boolean isUserAuthorizedForParticipant(HttpSession session, long idParticipant) {
		logger.debug("Prüfe ob Benutzer " + getLoginName(session) + " Zugriff hat auf Teilnehmer mit id " + idParticipant);
		return isAdmin(session) || teamRepo.existsByParticipantsIdParticipantAndUsersLoginName(idParticipant, getLoginName(session));
	}
	
	public boolean isAdmin(HttpSession session) {
		return (boolean) session.getAttribute(SessionAttributes.ISADMIN);
	}

	public String getLoginName(HttpSession session) {
		return (String) session.getAttribute(SessionAttributes.LOGINNAME);
	}
}

