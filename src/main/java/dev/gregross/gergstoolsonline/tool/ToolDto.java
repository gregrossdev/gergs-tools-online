package dev.gregross.gergstoolsonline.tool;

import dev.gregross.gergstoolsonline.technician.TechnicianDto;
import jakarta.validation.constraints.NotEmpty;

public record ToolDto(
	String id,
	@NotEmpty(message = "name is required.")
	String name,
	@NotEmpty(message = "description is required.")
	String description,
	@NotEmpty(message = "imageUrl is required.")
	String imageUrl,
	TechnicianDto technicianDto
) { }
