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

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserRepository userRepo;
	
	private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
	private final String passwordLengthError = "Das Passwort muss zwischen " + User.passwordMinLength + " und " + User.passwordMaxLength + " Zeichen lang sein";
	
	public User save(User user) throws DuplicateKeyException, PasswordLengthException, UnknownUserSaveException {
		
		boolean isNewUser = (user.getIdUser() == null);
		
		if(isNewUser) {
			logger.debug("Benutzer hinzufügen");

			// Exception if new and credentials taken
			checkUniqueConstraintsForNewUser(user);
			// Exception if password not proper
			checkProperPassword(user);
			
			encryptPassword(user);
			
		} else {
			if(user.getPassword().equals("")) {
				logger.debug("Benutzer bearbeiten: kein Passwort angegeben, benutze altes.");
				User dbUser = loadUser(user.getIdUser());
				user.setPassword(dbUser.getPassword());
			
			} else {
				logger.debug("Benutzer bearbeiten: neues Passwort angegeben.");
				// Exception if password not proper
				checkProperPassword(user);
				
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

	private void checkProperPassword(User user) throws PasswordLengthException {
		if(! user.isPasswordLengthOK() ) {
			logger.debug("Benutzer speichern: das Passwort enspricht nicht den Anforderungen.");
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

	/**
	 * Updates relation for user to the team. Users not active will be ignored.
	 * @param team containing the users to update their ralation
	 * @throws EmptyIdException
	 */
	public void updateTeamForUsers(Team team) throws EmptyIdException {
		
		List<User> uiUsers = team.getUsers();
		
		// throws exception if new user is passed
		checkUserlistForEmptyId(uiUsers);
		
		logger.debug("Füge dem übergebenen Benutzer das Team hinzu, wenn notwendig");
		List<User> dbUsers = userRepo.findAllByTeamsIdTeam(team.getIdTeam());
		
		for(User uiUser : uiUsers) {
			if(uiUser.getIsActive() && ! dbUsers.contains(uiUser)) {
				logger.debug("Füge Team zu Benutzer hinzu: "+ uiUser.getIdUser());
				uiUser.addTeam(team);
				userRepo.save(uiUser);
				continue;
			}
			logger.debug("Ignoriere Benutzer: "+ uiUser.getIdUser());
		}
		
		logger.debug("Entferne dem Benutzer aus der Datenbank das Team, wenn notwendig");
		for(User dbUser : dbUsers) {
			if(dbUser.getIsActive() && ! uiUsers.contains(dbUser)) {
				logger.debug("Entferne Team von Benutzer: "+ dbUser.getIdUser());
				dbUser.removeTeam(team);
				userRepo.save(dbUser);
				continue;
			}
			logger.debug("Ignoriere Benutzer: "+ dbUser.getIdUser());
		}
	}

	private void checkUserlistForEmptyId(List<User> chosenUsers) throws EmptyIdException {
		logger.debug("Prüfe Übergebene Benutzer auf idUser=Null");
		for(User chosenUser : chosenUsers) {
			if(chosenUser.getIdUser() == null) {
				throw new EmptyIdException("Aktualisierung fehlgeschlagen. Daten Fehlerhaft.");
			}
		}
	}
}
