package eu.planlos.anwesenheitsliste.application;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptPasswordEncoderTest {

	public static void main(String[] args) {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		System.out.println(bCryptPasswordEncoder.encode("securepw"));
		System.out.println(bCryptPasswordEncoder.encode("ptastisch"));
	}
}