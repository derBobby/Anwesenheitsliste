package eu.planlos.anwesenheitsliste.model;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
	public List<User> findAllByTeamsIdTeam(Long idTeam);
	public User findByLoginName(String loginName);
}
