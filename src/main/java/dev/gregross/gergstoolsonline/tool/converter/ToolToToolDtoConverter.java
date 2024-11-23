package dev.gregross.gergstoolsonline.tool.converter;


import dev.gregross.gergstoolsonline.technician.converter.TechnicianToTechnicianDtoConverter;
import dev.gregross.gergstoolsonline.tool.Tool;
import dev.gregross.gergstoolsonline.tool.ToolDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ToolToToolDtoConverter implements Converter<Tool, ToolDto> {

	private final TechnicianToTechnicianDtoConverter technicianToTechnicianDtoConverter;

	public ToolToToolDtoConverter(TechnicianToTechnicianDtoConverter technicianToTechnicianDtoConverter) {
		this.technicianToTechnicianDtoConverter = technicianToTechnicianDtoConverter;
	}

	@Override
	public ToolDto convert(Tool source) {

		return new ToolDto(source.getId(), source.getName(), source.getDescription(),  source.getImageUrl(), source.getPossessor() != null ? technicianToTechnicianDtoConverter.convert(source.getPossessor()) : null);
	}
}
