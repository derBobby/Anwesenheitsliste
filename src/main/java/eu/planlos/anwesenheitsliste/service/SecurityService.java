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
		String loginName = getLoginName(session);
		if(loginName == null) {
			return false;
		}
		logger.debug("Prüfe ob Benutzer " + loginName + " Zugriff hat auf Team mit id " + idTeam);
		return isAdmin(session) || teamRepo.existsByIdTeamAndUsersLoginName(idTeam, loginName);
	}
	
	public boolean isUserAuthorizedForParticipant(HttpSession session, long idParticipant) {
		String loginName = getLoginName(session);
		if(loginName == null) {
			logger.debug("Benutzer nicht eingeloggt. Kein Zugriff auf Teilnehmer mit id " + idParticipant);
			return false;
		}
		logger.debug("Prüfe ob Benutzer " + loginName + " Zugriff hat auf Teilnehmer mit id " + idParticipant);
		return isAdmin(session) || teamRepo.existsByParticipantsIdParticipantAndUsersLoginName(idParticipant, loginName);
	}
	
	public boolean isAdmin(HttpSession session) {
		if(session == null) {
			return false;
		}
		return (boolean) session.getAttribute(SessionAttributes.ISADMIN);
	}

	public String getLoginName(HttpSession session) {
		if(session == null) {
			return null;
		}
		return (String) session.getAttribute(SessionAttributes.LOGINNAME);
	}
}

