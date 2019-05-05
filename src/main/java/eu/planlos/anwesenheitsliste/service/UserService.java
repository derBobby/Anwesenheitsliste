package eu.planlos.anwesenheitsliste.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import eu.planlos.anwesenheitsliste.model.Team;
import eu.planlos.anwesenheitsliste.model.User;
import eu.planlos.anwesenheitsliste.model.UserRepository;
import eu.planlos.anwesenheitsliste.model.exception.EmptyIdException;
import eu.planlos.anwesenheitsliste.model.exception.PasswordLengthException;
import eu.planlos.anwesenheitsliste.model.exception.UnknownUserSaveException;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;
	
	private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
	
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	
	private final String passwordLengthError = "Das Passwort muss zwischen " + User.passwordMinLength + " und " + User.passwordMaxLength + " Zeichen lang sein";
	
	
	public User save(User user) throws DuplicateKeyException, PasswordLengthException, UnknownUserSaveException {
		
		boolean isNewUser = (user.getIdUser() == null);
		
		if(isNewUser) {
			logger.debug("Benutzer hinzufügen");

			// Exception if credentials taken
			checkUniqueConstraintsForNewUser(user); //TODO throw in method necessary?
			// Exception if not proper
			checkProperPasswordForNewUser(user);
			
			encryptPassword(user);
			
		} else {
			if(user.getPassword().equals("")) {
				logger.debug("Benutzer bearbeiten: kein Passwort angegeben, benutze altes.");
				User dbUser = loadUser(user.getIdUser());
				user.setPassword(dbUser.getPassword());
			
			} else {
				logger.debug("Benutzer bearbeiten: neues Passwort angegeben.");
				// Exception if not proper
				checkProperPasswordForUserEdit(user);
				
				encryptPassword(user);
			}
		}

		logger.debug("Benutzer speichern.");
		return userRepo.save(user);
	}

	private void encryptPassword(User user) {
		logger.debug("Verschlüssle Passwort");
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
	}

	private void checkProperPasswordForUserEdit(User user) throws PasswordLengthException {
		if(! user.isPasswordLengthOK() ) {
			logger.debug("Benutzer bearbeiten: das Passwort enspricht nicht den Anforderungen.");
			throw new PasswordLengthException(passwordLengthError);
		}
	}

	private void checkProperPasswordForNewUser(User user) throws PasswordLengthException {
		if(! user.isPasswordLengthOK() ) {
			logger.debug("Benutzer anlegen: das Passwort enspricht nicht den Anforderungen.");
			throw new PasswordLengthException(passwordLengthError);
		}
	}

	private void checkUniqueConstraintsForNewUser(User user) {
		
		String firstName = user.getFirstName();
		String lastName = user.getLastName();
		String email = user.getEmail();
		String loginName = user.getLoginName();
	
		// Should cover all constraints of the Entity
		if(userRepo.existsByLoginNameOrEmail(loginName, email)
				|| userRepo.existsByFirstNameAndLastName(firstName, lastName)
				) {
			
			logger.debug("Benutzer anlegen: Constraints verletzt");
			throw new DuplicateKeyException("Ein Benutzer mit dem Namen \"" + firstName + " " + lastName + "\""
					+ "oder dem Benutzernamen \"" + loginName + "\""
							+ "oder der E-Mailadresse \"" + email + "\" existiert bereits");
		}
	}
	
	public List<User> loadAllUsers() {
		logger.debug("Lade alle Benutzer");
		return (List<User>) userRepo.findAll();
	}
	
	public User loadUser(Long idUser) {
		logger.debug("Lade Benutzer mit id " + idUser);
		return userRepo.findById(idUser).get();
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//TODO probably not working
	//TODO LOGGING
	/**
	 * Updates relation between team and users. Users not active will be ignored.
	 * @param team
	 * @param chosenUsers
	 * @throws EmptyIdException
	 */
	public void updateTeamForUsers(Team team) throws EmptyIdException {
		
		List<User> uiUsers = team.getUsers();
		
		checkUserlistForEmptyId(uiUsers);
		
		// For all active users who were added but are not yet linked to the team
		List<User> dbUsers = userRepo.findAllByTeamsIdTeam(team.getIdTeam());
		for(User uiUser : uiUsers) {
			if(uiUser.getIsActive() && ! dbUsers.contains(uiUser)) {
				uiUser.addTeam(team);
				userRepo.save(uiUser);
			}
		}
		
		// For all active users in DB check if are chosen but should not be
		for(User dbUser : dbUsers) {
			if(dbUser.getIsActive() && ! uiUsers.contains(dbUser)) {
				dbUser.removeTeam(team);
				userRepo.save(dbUser);
			}
		}
	}

	private void checkUserlistForEmptyId(List<User> chosenUsers) throws EmptyIdException {
		// Error if false user is passed
		for(User chosenUser : chosenUsers) {
			if(chosenUser.getIdUser() == null) {
				throw new EmptyIdException("Aktualisierung fehlgeschlagen. Daten Fehlerhaft.");
			}
		}
	}
}
