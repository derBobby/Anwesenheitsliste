package eu.planlos.anwesenheitsliste.model;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import eu.planlos.anwesenheitsliste.model.exception.EmptyIdException;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;
	
	//TODO other classes?
	public User save(User user) throws DuplicateKeyException {
		
		if(user.getIdUser() == null) {
			String email = user.getEmail();
			String loginName = user.getLoginName();
		
			Boolean exists = userRepo.existsByLoginNameOrEmail(loginName, email);
			if(exists) {
				throw new DuplicateKeyException("Bitte probiere es mit einem anderen Loginnamen oder einer anderer E-Mailadresse");
			}
		}
		
		User newUser = userRepo.save(user);
		return newUser;
	}
	
	public void delete(User user) {
		userRepo.delete(user);
	}
	
	public List<User> findAll() {
		return (List<User>) userRepo.findAll();
	}
	
	public User findById(Long idUser) {
		return userRepo.findById(idUser).get();
	}

	public List<User> findAllByTeamsIdTeam(Long idTeam) {
		return userRepo.findAllByTeamsIdTeam(idTeam);
	}

	public User findByLoginName(String loginName) {
		return userRepo.findByLoginName(loginName);
	}

	/**
	 * Updates relation between team and users. Users not active will be ignored.
	 * @param team
	 * @param chosenUsers
	 * @throws EmptyIdException
	 */
	public void updateTeamForUsers(Team team, List<User> chosenUsers) throws EmptyIdException {
		
		for(User chosenUser : chosenUsers) {
			if(chosenUser.getIdUser() == null) {
				throw new EmptyIdException("Bitte probiere es mit einem anderen Loginnamen oder einer anderer E-Mailadresse");
			}
		}
		
		List<User> usersForTeam = userRepo.findAllByTeamsIdTeam(team.getIdTeam());
		
		for(User chosenUser : chosenUsers) {
			if(chosenUser.getIsActive() && ! usersForTeam.contains(chosenUser)) {
				chosenUser.addTeam(team);
				userRepo.save(chosenUser);
			}
		}
		
		for(User userForTeam : usersForTeam) {
			if(userForTeam.getIsActive() && ! chosenUsers.contains(userForTeam)) {
				userForTeam.removeTeam(team);
				userRepo.save(userForTeam);
			}
		}
	}
}
