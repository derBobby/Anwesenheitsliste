package eu.planlos.anwesenheitsliste.model;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface MeetingRepository extends CrudRepository<Meeting, Long> {

	public List<Meeting> findAllByTeamIdTeam(Long idTeam);
}
