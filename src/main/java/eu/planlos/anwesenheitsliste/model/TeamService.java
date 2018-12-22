package eu.planlos.anwesenheitsliste.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
public class TeamService {

	@Autowired
	private TeamRepository teamRepo;
	
	public Team save(Team team) throws DuplicateKeyException {
		
		if(team.getIdTeam() == null) {
			String teamName = team.getTeamName();
			
			// Should cover all constraints of the Entity
			if(teamRepo.existsByTeamName(teamName)) {
				throw new DuplicateKeyException("Bitte probiere es mit einem anderen Gruppennamen");
			}
		}
				
		return teamRepo.save(team);
	}
	
	public void deleteUser(Team team) {
		teamRepo.delete(team);
	}
	
	public List<Team> findAll() {
		return (List<Team>) teamRepo.findAll();
	}
	
	public Team findById(Long idTeam) {
		return teamRepo.findById(idTeam).get();
	}
}
