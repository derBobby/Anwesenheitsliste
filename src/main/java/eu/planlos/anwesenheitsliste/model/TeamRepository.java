package eu.planlos.anwesenheitsliste.model;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface TeamRepository extends CrudRepository<Team, Long> {

	public boolean existsByTeamName(String teamName);
	public List<Team> findAllByUsersLoginName(String loginName);
	public boolean existsByIdTeamAndUsersLoginName(long idTeam, String loginName);
	public boolean existsByParticipantsIdParticipantAndUsersLoginName(long idParticipant, String loginName);
}
