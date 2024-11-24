package dev.gregross.gergstoolsonline.user.converter;

import dev.gregross.gergstoolsonline.user.User;
import dev.gregross.gergstoolsonline.user.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDtoConverter implements Converter<User, UserDto> {

	@Override
	public UserDto convert(User source) {
		// We are not setting password in DTO.
		return new UserDto(source.getId(),
			source.getUsername(),
			source.isEnabled(),
			source.getRoles());
	}

}
