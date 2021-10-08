package com.stocksapi.appuser;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.stocksapi.registration.token.ConfirmationToken;
import com.stocksapi.registration.token.ConfirmationTokenService;

import lombok.AllArgsConstructor;



@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

	private static final int TOKEN_EXPIRATION_IN_MINUTES = 15;
	private static final String USER_NOT_FOUND_MSG = "user with email %s not found!";
	private final AppUserRepository appUserRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final ConfirmationTokenService confirmationTokenService;
	

	public AppUserService(AppUserRepository appUserRepository, BCryptPasswordEncoder bCryptPasswordEncoder, ConfirmationTokenService confirmationTokenService) {
		this.appUserRepository = appUserRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.confirmationTokenService = confirmationTokenService;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return appUserRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
	}
	
	
	public String signUpUser(AppUser appUser) {
		boolean userEmailExists = appUserRepository.findByEmail(appUser.getEmail())
			.isPresent();
		
		if(userEmailExists) {
			throw new IllegalStateException("Email already taken!");
		}
		
		String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());
		appUser.setPassword(encodedPassword);
		
		appUserRepository.save(appUser);
		
		// generate token
		String token = UUID.randomUUID().toString();
		// args (token, createdAt, expiresAt, confirmedAt, AppUser)
		ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(),
				LocalDateTime.now().plusMinutes(TOKEN_EXPIRATION_IN_MINUTES), appUser);
		
		//save to db
		confirmationTokenService.saveConfirmationToken(confirmationToken);
		
		return token;
	}
	
	public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    }
}
