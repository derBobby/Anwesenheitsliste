package eu.planlos.anwesenheitsliste.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

public class UserRepositoryIntegrationTest {

	@RunWith(SpringRunner.class)
	@DataJpaTest
	public class EmployeeRepositoryIntegrationTest {
	 
	    @Autowired
	    private TestEntityManager entityManager;
	 
	    @Autowired
	    private UserRepository userRepository;
	 
	    @Test
	    public void whenFindByName_thenReturnUser() {
	    	
	    	BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
	    	
	        // given
	    	User flastname = new User("Firstname", "Lastname", "flastname", "flastname@example.com", bCryptPasswordEncoder.encode("securepw"), true, false);       
	    			
	    	entityManager.persist(flastname);
	        entityManager.flush();
	     
	        // when
	        User found = userRepository.findByLoginName(flastname.getLoginName());
	     
	        // then
	        assertThat(found.getLoginName())
	        	.isEqualTo(flastname.getLoginName());
	        
	        assertThat(found.getPassword())
	        	.isEqualTo(flastname.getPassword());
	        	
	    }	 
	}
}
