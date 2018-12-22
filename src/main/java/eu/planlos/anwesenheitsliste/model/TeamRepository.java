package eu.planlos.anwesenheitsliste.model;

import org.springframework.data.repository.CrudRepository;

public interface TeamRepository extends CrudRepository<Team, Long> {

	boolean existsByTeamName(String teamName);

}
