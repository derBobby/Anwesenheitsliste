package eu.planlos.anwesenheitsliste.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import eu.planlos.anwesenheitsliste.model.Team;
import eu.planlos.anwesenheitsliste.model.TeamRepository;

@Service
public class TeamService {

	private static final Logger logger = LoggerFactory.getLogger(TeamService.class);
			
	@Autowired
	private TeamRepository teamRepo;
	
	public Team saveTeam(Team team) throws DuplicateKeyException {
		
		String teamName = team.getTeamName();
		
		if(team.getIdTeam() == null 
				// Should cover all constraints of the Entity
				&& teamRepo.existsByTeamName(teamName)) {
			
			logger.debug("Gruppe speichern nicht möglich: Der Gruppenname wird bereits verwendet");
			throw new DuplicateKeyException("Der Gruppenname wird bereits verwendet");
		}
		logger.debug("Speichere Gruppe");		
		return teamRepo.save(team);
	}
	
	public List<Team> loadAllTeams() {
		logger.debug("Lade alle Gruppen");
		return (List<Team>) teamRepo.findAll();
	}
	
	public Team loadTeam(Long idTeam) {
		logger.debug("Lade Gruppe mit id " + idTeam);
		return teamRepo.findById(idTeam).get();
	}

	public List<Team> loadTeamsForUser(String loginName) {
		logger.debug("Lade Gruppen für Benutzer " + loginName);
		return teamRepo.findAllByUsersLoginName(loginName);
	}
}
