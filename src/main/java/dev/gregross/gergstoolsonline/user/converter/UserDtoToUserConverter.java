package dev.gregross.gergstoolsonline.user.converter;

import dev.gregross.gergstoolsonline.user.User;
import dev.gregross.gergstoolsonline.user.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserDtoToUserConverter implements Converter<UserDto, User> {

	@Override
	public User convert(UserDto source) {
		User hogwartsUser = new User();
		hogwartsUser.setUsername(source.username());
		hogwartsUser.setEnabled(source.enabled());
		hogwartsUser.setRoles(source.roles());
		return hogwartsUser;
	}

}