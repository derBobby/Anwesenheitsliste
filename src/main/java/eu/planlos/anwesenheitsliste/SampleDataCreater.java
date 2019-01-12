package eu.planlos.anwesenheitsliste;

import java.text.ParseException;
import java.util.Calendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import eu.planlos.anwesenheitsliste.model.Meeting;
import eu.planlos.anwesenheitsliste.model.MeetingRepository;
import eu.planlos.anwesenheitsliste.model.Participant;
import eu.planlos.anwesenheitsliste.model.ParticipantRepository;
import eu.planlos.anwesenheitsliste.model.Team;
import eu.planlos.anwesenheitsliste.model.TeamRepository;
import eu.planlos.anwesenheitsliste.model.User;
import eu.planlos.anwesenheitsliste.model.UserRepository;

@Component
@Profile(value = "CREATESAMPLEDATA")
public class SampleDataCreater implements ApplicationRunner {
	
	private static final Logger logger = LoggerFactory.getLogger(SampleDataCreater.class);	

    private MeetingRepository meetingsRepo;
    private ParticipantRepository participantRepo;
    private TeamRepository teamRepo;
    private UserRepository userRepo;
    
    @Autowired
    public SampleDataCreater(MeetingRepository meetingsRepo, ParticipantRepository participantRepo, TeamRepository teamRepo, UserRepository userRepository) {
        this.meetingsRepo = meetingsRepo;
        this.participantRepo = participantRepo;
        this.teamRepo = teamRepo;
        this.userRepo = userRepository;
    }
    
    @Override
    public void run(ApplicationArguments args) throws ParseException {

//    	if(args.containsOption("")) {
//    		logger.debug("Startparameter \"initdb\" gefunden -> Initialisiere Datenbank");
//    		initDB();
//    		return;
//    	}
//    	logger.debug("Startparameter \"initdb\" nicht gefunden -> Initialisiere Datenbank nicht");
    	
		logger.debug("Profil \"CREATEDB\" gefunden -> Initialisiere Datenbank");
    	initDB();
    	logger.debug("Profil \"CREATEDB\" gefunden -> Initialisiere Datenbank -> FERTIG!");
    }

	private void initDB() throws ParseException {
		
		meetingsRepo.deleteAll();
		participantRepo.deleteAll();
		teamRepo.deleteAll();
		userRepo.deleteAll();
		
		Participant p1 = participantRepo.save(new Participant("Eva", "Tester", "013371337", "a@example.com", true));
		Participant p2 = participantRepo.save(new Participant("Ava", "Tester", "013371338", "b@example.com", true));
		Participant p3 = participantRepo.save(new Participant("Iva", "Tester", "013371339", "c@example.com", true));
		Participant p4 = participantRepo.save(new Participant("Ova", "Tester", "013371339", "d@example.com", true));
			
		Team team1 = teamRepo.save(new Team("Cool Team"));
		Team team2 = teamRepo.save(new Team("Chilly Team"));
		Team team3 = teamRepo.save(new Team("Morons"));
		
//		not needed yet
//		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
//		formatter.parse("27.01.2018")
		
		Calendar calendar = Calendar.getInstance();
		
		calendar.set(2018, 12, 26);
		@SuppressWarnings("unused")
		Meeting m1 = meetingsRepo.save(new Meeting(calendar.getTime(), "Just a Test", team1));
		
		calendar.set(2018, 12, 27);
		@SuppressWarnings("unused")
		Meeting m2 = meetingsRepo.save(new Meeting(calendar.getTime(), "Just a Test", team2));
		
		calendar.set(2018, 12, 28);
		@SuppressWarnings("unused")
		Meeting m3 = meetingsRepo.save(new Meeting(calendar.getTime(), "Just a Test", team3));

		//securepw
		User u1 = userRepo.save(new User("Adam", "Sample", "asam", "a@example.com", "$2a$10$iZjfi6dsjgj.qIBBnrMjHuhlb6LkBEO.SfeP0wUmz9lNp5mhiyfIG", false, false));
		User u2 = userRepo.save(new User("Bdam", "Sample", "bsam", "b@example.com", "$2a$10$iZjfi6dsjgj.qIBBnrMjHuhlb6LkBEO.SfeP0wUmz9lNp5mhiyfIG", false, true));
		User u3 = userRepo.save(new User("Cdam", "Sample", "csam", "c@example.com", "$2a$10$iZjfi6dsjgj.qIBBnrMjHuhlb6LkBEO.SfeP0wUmz9lNp5mhiyfIG", true, false));
		User u4 = userRepo.save(new User("Ddam", "Sample", "dsam", "d@example.com", "$2a$10$iZjfi6dsjgj.qIBBnrMjHuhlb6LkBEO.SfeP0wUmz9lNp5mhiyfIG", true, false));
		User u5 = userRepo.save(new User("Bobson", "obby", "bobby", "uetz@example.com", "$2a$10$iZjfi6dsjgj.qIBBnrMjHuhlb6LkBEO.SfeP0wUmz9lNp5mhiyfIG", true, false));
		User u6 = userRepo.save(new User("Pat", "rick", "ppp", "p@example.com", "$2a$10$G7MFj4PkPu9HpSaA7dqg1Om.bfMRFSIDya66vVHIlcldCkMMa5cpm", true, true));

		u1.addTeam(team1);
		u1.addTeam(team2);
		u1.addTeam(team3);
		
		u2.addTeam(team2);
		u2.addTeam(team3);

		u3.addTeam(team3);
		
		u4.addTeam(team3);

		u5.addTeam(team1);
		u5.addTeam(team2);
		
		userRepo.save(u1);
		userRepo.save(u2);
		userRepo.save(u3);
		userRepo.save(u4);
		userRepo.save(u5);
		userRepo.save(u6);
		
		//
		
		p1.addTeam(team1);
		p1.addTeam(team2);
		p1.addTeam(team3);

		p2.addTeam(team2);
		p2.addTeam(team3);

		p3.addTeam(team3);
		
		p4.addTeam(team3);

		participantRepo.save(p1);
		participantRepo.save(p2);
		participantRepo.save(p3);
		participantRepo.save(p4);
	}
}