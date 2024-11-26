package dev.gregross.gergstoolsonline.auth;

import dev.gregross.gergstoolsonline.appuser.AppUser;
import dev.gregross.gergstoolsonline.appuser.AppUserDto;
import dev.gregross.gergstoolsonline.appuser.MyUserPrincipal;
import dev.gregross.gergstoolsonline.appuser.converter.AppUserToAppUserDtoConverter;
import dev.gregross.gergstoolsonline.system.security.JwtProvider;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

	private final JwtProvider jwtProvider;

	private final AppUserToAppUserDtoConverter appUserToAppUserDtoConverter;


	public AuthService(JwtProvider jwtProvider, AppUserToAppUserDtoConverter appUserToAppUserDtoConverter) {
		this.jwtProvider = jwtProvider;
		this.appUserToAppUserDtoConverter = appUserToAppUserDtoConverter;
	}

	public Map<String, Object> createLoginInfo(Authentication authentication) {
		// Create user info.
		MyUserPrincipal principal = (MyUserPrincipal)authentication.getPrincipal();
		AppUser appUser = principal.getAppUser();
		AppUserDto appUserDto = appUserToAppUserDtoConverter.convert(appUser);
		// Create a JWT.
		String token = jwtProvider.createToken(authentication);

		Map<String, Object> loginResultMap = new HashMap<>();

		loginResultMap.put("userInfo", appUserDto);
		loginResultMap.put("token", token);

		return loginResultMap;
	}

}