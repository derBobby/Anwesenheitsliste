package eu.planlos.anwesenheitsliste.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import eu.planlos.anwesenheitsliste.ApplicationRole;
import eu.planlos.anwesenheitsliste.model.TeamRepository;

@Service
public class SecurityService {

	@Autowired
	private TeamRepository teamRepo;
	
	public boolean isUserAuthorizedForTeam(long idTeam) {
		return teamRepo.existsByIdTeamAndUsersLoginName(idTeam, getLoginName());
	}
	
	public boolean isUserStaffForParticipant(long idParticipant) {
		
		return teamRepo.existsByParticipantsIdParticipantAndUsersLoginName(idParticipant, getLoginName());
	}
	
	public String getLoginName() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

	public boolean isAdmin() {
		return SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority(ApplicationRole.ROLE_ADMIN));
	}
}

