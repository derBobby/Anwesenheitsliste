package eu.planlos.anwesenheitsliste.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamService {

	@Autowired
	private TeamRepository teamRepo;
	
	public Team save(Team team) {
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
