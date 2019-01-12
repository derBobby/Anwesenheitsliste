package eu.planlos.anwesenheitsliste.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import eu.planlos.anwesenheitsliste.model.Team;
import eu.planlos.anwesenheitsliste.model.User;
import eu.planlos.anwesenheitsliste.model.UserRepository;
import eu.planlos.anwesenheitsliste.model.exception.EmptyIdException;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;
	
	//TODO other classes?
	public User save(User user) throws DuplicateKeyException {
		
		if(user.getIdUser() == null) {
			String firstName = user.getFirstName();
			String lastName = user.getLastName();
			String email = user.getEmail();
			String loginName = user.getLoginName();
		
			// Should cover all constraints of the Entity
			if(userRepo.existsByLoginNameOrEmail(loginName, email)
					|| userRepo.existsByFirstNameAndLastName(firstName, lastName)
					) {
				throw new DuplicateKeyException("Ein Benutzer mit dem Namen \"" + firstName + " " + lastName + "\""
						+ "oder dem Benutzernamen \"" + loginName + "\""
								+ "oder der E-Mailadresse \"" + email + "\" existiert bereits");

			}
		}
		
		return userRepo.save(user);
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
	public void updateTeamForUsers(Team team) throws EmptyIdException {
		
		List<User> chosenUsers = team.getUsers();
		
		// Error if false user is passed
		for(User chosenUser : chosenUsers) {
			if(chosenUser.getIdUser() == null) {
				throw new EmptyIdException("Aktualisierung fehlgeschlagen. Daten Fehlerhaft.");
			}
		}
		
		// For all active users who were added but are not yet linked to the team
		List<User> usersForTeam = userRepo.findAllByTeamsIdTeam(team.getIdTeam());
		for(User chosenUser : chosenUsers) {
			if(chosenUser.getIsActive() && ! usersForTeam.contains(chosenUser)) {
				chosenUser.addTeam(team);
				userRepo.save(chosenUser);
			}
		}
		
		// For all active users in DB check if are chosen but should not be
		for(User userForTeam : usersForTeam) {
			if(userForTeam.getIsActive() && ! chosenUsers.contains(userForTeam)) {
				userForTeam.removeTeam(team);
				userRepo.save(userForTeam);
			}
		}
	}
}
