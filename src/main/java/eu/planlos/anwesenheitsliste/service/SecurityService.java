package eu.planlos.anwesenheitsliste.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import eu.planlos.anwesenheitsliste.model.TeamRepository;

@Service
public class SecurityService {

	@Autowired
	private TeamRepository teamRepo;
	
	public boolean isUserMemberOfTeam(long idTeam) {
		
		return teamRepo.existsByIdTeamAndUsersLoginName(idTeam, getLoginName());
	}
	
	public String getLoginName() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
}

