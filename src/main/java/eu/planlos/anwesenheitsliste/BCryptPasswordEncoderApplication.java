package eu.planlos.anwesenheitsliste;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptPasswordEncoderApplication {

	public static void main(String[] args) {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		System.out.println(bCryptPasswordEncoder.encode("securepw"));
		System.out.println(bCryptPasswordEncoder.encode("ptastisch"));
	}
}