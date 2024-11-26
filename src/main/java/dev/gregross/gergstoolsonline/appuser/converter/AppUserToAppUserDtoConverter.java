package dev.gregross.gergstoolsonline.appuser.converter;

import dev.gregross.gergstoolsonline.appuser.AppUser;
import dev.gregross.gergstoolsonline.appuser.AppUserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AppUserToAppUserDtoConverter implements Converter<AppUser, AppUserDto> {

	@Override
	public AppUserDto convert(AppUser source) {
		// We are not setting password in DTO.
		return new AppUserDto(source.getId(),
			source.getUsername(),
			source.isEnabled(),
			source.getRoles());
	}

}
