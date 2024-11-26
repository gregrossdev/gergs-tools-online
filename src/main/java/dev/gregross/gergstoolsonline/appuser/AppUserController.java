package dev.gregross.gergstoolsonline.appuser;

import dev.gregross.gergstoolsonline.system.http.Result;
import dev.gregross.gergstoolsonline.system.http.StatusCode;
import dev.gregross.gergstoolsonline.appuser.converter.AppUserDtoToAppUserConverter;
import dev.gregross.gergstoolsonline.appuser.converter.AppUserToAppUserDtoConverter;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.endpoint.base-url}/users")
public class AppUserController {
	private final AppUserService appUserService;

	private final AppUserDtoToAppUserConverter appUserDtoToAppUserConverter; // Convert userDto to user.

	private final AppUserToAppUserDtoConverter appUserToAppUserDtoConverter; // Convert user to userDto.

	public AppUserController(AppUserService appUserService, AppUserDtoToAppUserConverter appUserDtoToAppUserConverter, AppUserToAppUserDtoConverter appUserToAppUserDtoConverter) {
		this.appUserService = appUserService;
		this.appUserDtoToAppUserConverter = appUserDtoToAppUserConverter;
		this.appUserToAppUserDtoConverter = appUserToAppUserDtoConverter;
	}

	@GetMapping("/{userId}")
	public Result findUserById(@PathVariable Integer userId) {
		AppUser foundAppUser = appUserService.findById(userId);
		AppUserDto appUserDto = appUserToAppUserDtoConverter.convert(foundAppUser);
		return new Result(true, StatusCode.SUCCESS, "Find One Success", appUserDto);
	}

	@GetMapping
	public Result findAllUsers() {
		List<AppUser> foundAppUsers = appUserService.findAll();

		// Convert foundUsers to a list of UserDtos.
		List<AppUserDto> appUserDtos = foundAppUsers.stream()
			.map(appUserToAppUserDtoConverter::convert)
			.toList();

		// Note that UserDto does not contain password field.
		return new Result(true, StatusCode.SUCCESS, "Find All Success", appUserDtos);
	}

	/**
	 * We are not using UserDto, but User, since we require password.
	 *
	 * @param newAppUser
	 * @return
	 */
	@PostMapping
	public Result addUser(@Valid @RequestBody AppUser newAppUser) {
		AppUser savedAppUser = appUserService.save(newAppUser);
		AppUserDto savedAppUserDto = appUserToAppUserDtoConverter.convert(savedAppUser);
		return new Result(true, StatusCode.SUCCESS, "Add Success", savedAppUserDto);
	}

	// We are not using this to update password, need another changePassword method in this class.
	@PutMapping("/{userId}")
	public Result updateUser(@PathVariable Integer userId, @Valid @RequestBody AppUserDto appUserDto) {
		AppUser update = appUserDtoToAppUserConverter.convert(appUserDto);
		AppUser updatedAppUser = appUserService.update(userId, update);
		AppUserDto updatedAppUserDto = appUserToAppUserDtoConverter.convert(updatedAppUser);
		return new Result(true, StatusCode.SUCCESS, "Update Success", updatedAppUserDto);
	}

	@DeleteMapping("/{userId}")
	public Result deleteUser(@PathVariable Integer userId) {
		appUserService.delete(userId);
		return new Result(true, StatusCode.SUCCESS, "Delete Success");
	}

}
