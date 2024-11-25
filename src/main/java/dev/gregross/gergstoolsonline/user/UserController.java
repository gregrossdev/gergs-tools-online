package dev.gregross.gergstoolsonline.user;

import dev.gregross.gergstoolsonline.system.http.Result;
import dev.gregross.gergstoolsonline.system.http.StatusCode;
import dev.gregross.gergstoolsonline.user.converter.UserDtoToUserConverter;
import dev.gregross.gergstoolsonline.user.converter.UserToUserDtoConverter;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.endpoint.base-url}/users")
public class UserController {
	private final UserService userService;

	private final UserDtoToUserConverter userDtoToUserConverter; // Convert userDto to user.

	private final UserToUserDtoConverter userToUserDtoConverter; // Convert user to userDto.

	public UserController(UserService userService, UserDtoToUserConverter userDtoToUserConverter, UserToUserDtoConverter userToUserDtoConverter) {
		this.userService = userService;
		this.userDtoToUserConverter = userDtoToUserConverter;
		this.userToUserDtoConverter = userToUserDtoConverter;
	}

	@GetMapping("/{userId}")
	public Result findUserById(@PathVariable Integer userId) {
		User foundUser = this.userService.findById(userId);
		UserDto userDto = this.userToUserDtoConverter.convert(foundUser);
		return new Result(true, StatusCode.SUCCESS, "Find One Success", userDto);
	}

	@GetMapping
	public Result findAllUsers() {
		List<User> foundUsers = this.userService.findAll();

		// Convert foundUsers to a list of UserDtos.
		List<UserDto> userDtos = foundUsers.stream()
			.map(userToUserDtoConverter::convert)
			.toList();

		// Note that UserDto does not contain password field.
		return new Result(true, StatusCode.SUCCESS, "Find All Success", userDtos);
	}

	/**
	 * We are not using UserDto, but User, since we require password.
	 *
	 * @param newUser
	 * @return
	 */
	@PostMapping
	public Result addUser(@Valid @RequestBody User newUser) {
		User savedUser = userService.save(newUser);
		UserDto savedUserDto = userToUserDtoConverter.convert(savedUser);
		return new Result(true, StatusCode.SUCCESS, "Add Success", savedUserDto);
	}

	// We are not using this to update password, need another changePassword method in this class.
	@PutMapping("/{userId}")
	public Result updateUser(@PathVariable Integer userId, @Valid @RequestBody UserDto userDto) {
		User update = userDtoToUserConverter.convert(userDto);
		User updatedUser = userService.update(userId, update);
		UserDto updatedUserDto = userToUserDtoConverter.convert(updatedUser);
		return new Result(true, StatusCode.SUCCESS, "Update Success", updatedUserDto);
	}

	@DeleteMapping("/{userId}")
	public Result deleteUser(@PathVariable Integer userId) {
		userService.delete(userId);
		return new Result(true, StatusCode.SUCCESS, "Delete Success");
	}

}
