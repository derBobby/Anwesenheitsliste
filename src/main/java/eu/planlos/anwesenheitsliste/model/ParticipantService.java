package eu.planlos.anwesenheitsliste.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParticipantService {

	@Autowired
	private ParticipantRepository participantRepo;
	
	public Participant save(Participant participant) {
		return participantRepo.save(participant);
	}
	
	public void deleteUser(Participant participant) {
		participantRepo.delete(participant);
	}
	
	public List<Participant> findAll() {
		return (List<Participant>) participantRepo.findAll();
	}
	
	public Participant findById(Long idParticipant) {
		return participantRepo.findById(idParticipant).get();
	}
}
