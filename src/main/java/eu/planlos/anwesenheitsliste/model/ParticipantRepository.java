package eu.planlos.anwesenheitsliste.model;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface ParticipantRepository extends CrudRepository<Participant, Long> {
	public List<Participant> findAllByTeamsIdTeam(Long idTeam);

	public Boolean existsByFirstNameAndLastName(String firstName, String lastName);
}
