package eu.planlos.anwesenheitsliste.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MeetingService {

	@Autowired
	private MeetingRepository meetingRepo;
	
	public Meeting save(Meeting meeting) {
		return meetingRepo.save(meeting);
	}
	
	public void deleteUser(Meeting meeting) {
		meetingRepo.delete(meeting);
	}
	
	public List<Meeting> findAll() {
		return (List<Meeting>) meetingRepo.findAll();
	}
	
	public Meeting findById(Long idMeeting) {
		return meetingRepo.findById(idMeeting).get();
	}
}
