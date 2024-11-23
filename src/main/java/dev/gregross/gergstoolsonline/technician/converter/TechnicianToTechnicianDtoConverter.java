package dev.gregross.gergstoolsonline.technician.converter;

import dev.gregross.gergstoolsonline.technician.Technician;
import dev.gregross.gergstoolsonline.technician.TechnicianDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TechnicianToTechnicianDtoConverter implements Converter<Technician, TechnicianDto> {

	@Override
	public TechnicianDto convert(Technician source) {
		return new TechnicianDto(
			source.getId(),
			source.getName(),
			source.getNumberOfTools()
		);
	}
}
