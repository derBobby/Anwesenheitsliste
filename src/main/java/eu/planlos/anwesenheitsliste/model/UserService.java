package eu.planlos.anwesenheitsliste.model;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;
	
	public User save(User user) {
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
}
