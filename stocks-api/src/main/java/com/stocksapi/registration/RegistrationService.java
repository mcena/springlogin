package com.stocksapi.registration;

import org.springframework.stereotype.Service;

import com.stocksapi.appuser.AppUser;
import com.stocksapi.appuser.AppUserRole;
import com.stocksapi.appuser.AppUserService;

@Service
public class RegistrationService {
	
	private final EmailValidator emailValidator;
	private final AppUserService appUserService;
	
	public RegistrationService(EmailValidator emailValidator, AppUserService appUserService) {
		this.emailValidator = emailValidator;
		this.appUserService = appUserService;
	}
	
	public String register(RegistrationRequest request) {
		boolean isValidEmail = emailValidator.test(request.getEmail());
		
		if(!isValidEmail) {
			throw new IllegalStateException("email is not valid!");
		}
		return appUserService.signUpUser(new AppUser(request.getFirstName(), request.getLastName(), request.getEmail(),
				request.getPassword(), AppUserRole.USER));
	}
	
	
	
}
