package eu.planlos.anwesenheitsliste.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.env.Environment;
import org.springframework.ui.Model;

import eu.planlos.anwesenheitsliste.ApplicationProfile;

@RunWith(MockitoJUnitRunner.class)
public class BodyFillerServiceTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}
	
	@InjectMocks
	private BodyFillerService bfs;
	
	@Mock
	private Environment environment;
	
	@Mock
	private Model model;
	
	@Test
	public final void runsDevProfile_setsDevFlag() {
		String[] profileList = {ApplicationProfile.DEV_PROFILE};
		
		when(environment.getActiveProfiles()).thenReturn(profileList);
		
		bfs.fill(model, null, null, null);
		
		verify(model).addAttribute("isDevProfile", true);
	}
	
	@Test
	public final void doesntRunDevProfile_doesntSetsDevFlag() {
		String[] profileList = {ApplicationProfile.DEV_PROFILE};
		
		when(environment.getActiveProfiles()).thenReturn(profileList);
		
		bfs.fill(model, null, null, null);
		
		verify(model, times(0)).addAttribute("isDevProfile", true);
	}
	
}