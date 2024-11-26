package dev.gregross.gergstoolsonline.appuser;

import jakarta.validation.constraints.NotEmpty;


public record AppUserDto(
	Integer id,
	@NotEmpty(message = "username is required.")
	String username,
	boolean enabled,
	@NotEmpty(message = "roles are required.")
	String roles) {
}