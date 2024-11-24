package dev.gregross.gergstoolsonline.technician.converter;

import dev.gregross.gergstoolsonline.technician.Technician;
import dev.gregross.gergstoolsonline.technician.TechnicianDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TechnicianDtoToTechnicianConverter implements Converter<TechnicianDto, Technician> {
	@Override
	public Technician convert(TechnicianDto source) {
		Technician technician = new Technician();
		technician.setId(source.id());
		technician.setName(source.name());
		return technician;
	}
}
