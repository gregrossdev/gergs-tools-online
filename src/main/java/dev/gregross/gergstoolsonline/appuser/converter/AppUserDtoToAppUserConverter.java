package dev.gregross.gergstoolsonline.appuser.converter;

import dev.gregross.gergstoolsonline.appuser.AppUser;
import dev.gregross.gergstoolsonline.appuser.AppUserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AppUserDtoToAppUserConverter implements Converter<AppUserDto, AppUser> {

	@Override
	public AppUser convert(AppUserDto source) {
		AppUser hogwartsAppUser = new AppUser();
		hogwartsAppUser.setUsername(source.username());
		hogwartsAppUser.setEnabled(source.enabled());
		hogwartsAppUser.setRoles(source.roles());
		return hogwartsAppUser;
	}

}