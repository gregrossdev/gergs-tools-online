package dev.gregross.gergstoolsonline.tool;

import dev.gregross.gergstoolsonline.technician.TechnicianDto;

public record ToolDto(
	String id,
	String name,
	String description,
	String imageUrl,
	TechnicianDto technicianDto
) { }
