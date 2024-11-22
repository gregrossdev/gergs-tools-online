package dev.gregross.gergstoolsonline.tool.converter;

import dev.gregross.gergstoolsonline.tool.Tool;
import dev.gregross.gergstoolsonline.tool.ToolDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ToolDtoToToolConverter implements Converter<ToolDto, Tool> {

	@Override
	public Tool convert(ToolDto source) {
		Tool tool = new Tool();
		tool.setId(source.id());
		tool.setName(source.name());
		tool.setDescription(source.description());
		tool.setImageUrl(source.imageUrl());
		return tool;
	}

}
