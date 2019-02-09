package eu.planlos.anwesenheitsliste.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.planlos.anwesenheitsliste.model.TeamRepository;

@Service
public class SecurityService {

	@Autowired
	private TeamRepository teamRepo;
	
	public boolean isUserAuthorizedForTeam(long idTeam, String loginName) {
		return teamRepo.existsByIdTeamAndUsersLoginName(idTeam, loginName);
	}
	
	public boolean isUserStaffForParticipant(long idParticipant, String loginName) {
		
		return teamRepo.existsByParticipantsIdParticipantAndUsersLoginName(idParticipant, loginName);
	}
}

