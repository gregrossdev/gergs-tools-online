package dev.gregross.gergstoolsonline.technician;

import dev.gregross.gergstoolsonline.system.Result;
import dev.gregross.gergstoolsonline.system.StatusCode;
import dev.gregross.gergstoolsonline.technician.converter.TechnicianDtoToTechnicianConverter;
import dev.gregross.gergstoolsonline.technician.converter.TechnicianToTechnicianDtoConverter;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/technicians")
public class TechnicianController {

	private final TechnicianService technicianService;
	private final TechnicianToTechnicianDtoConverter technicianToTechnicianDtoConverter;
	private final TechnicianDtoToTechnicianConverter technicianDtoToTechnicianConverter;

	public TechnicianController(TechnicianService technicianService, TechnicianToTechnicianDtoConverter technicianToTechnicianDtoConverter, TechnicianDtoToTechnicianConverter technicianDtoToTechnicianConverter) {
		this.technicianService = technicianService;
		this.technicianToTechnicianDtoConverter = technicianToTechnicianDtoConverter;
		this.technicianDtoToTechnicianConverter = technicianDtoToTechnicianConverter;
	}

	@GetMapping("/{technicianId}")
	public Result findTechnicianById(@PathVariable int technicianId) {
		Technician foundTechnician = technicianService.findById(technicianId);
		TechnicianDto technicianDto = technicianToTechnicianDtoConverter.convert(foundTechnician);
		return new Result(true, StatusCode.SUCCESS, "Find One Success", technicianDto);
	}

	@GetMapping
	public Result findAllTechnicians() {
		List<Technician> foundTechnicians = technicianService.findAll();
		List<TechnicianDto> technicianDtos = foundTechnicians.stream()
			.map(technicianToTechnicianDtoConverter::convert)
			.toList();
		return new Result(true, StatusCode.SUCCESS, "Find All Success", technicianDtos);
	}

	@PostMapping
	public Result addTechnician(@Valid @RequestBody TechnicianDto technicianDto) {
		Technician newTechnician = technicianDtoToTechnicianConverter.convert(technicianDto);
		Technician savedTechnician = technicianService.save(newTechnician);
		TechnicianDto savedTechnicianDto = this.technicianToTechnicianDtoConverter.convert(savedTechnician);
		return new Result(true, StatusCode.SUCCESS, "Add Success", savedTechnicianDto);
	}

	@PutMapping("/{technicianId}")
	public Result updateTechnician(@PathVariable Integer technicianId, @Valid @RequestBody TechnicianDto technicianDto) {
		Technician update = technicianDtoToTechnicianConverter.convert(technicianDto);
		Technician updatedTechnician = technicianService.update(technicianId, update);
		TechnicianDto updatedTechnicianDto = this.technicianToTechnicianDtoConverter.convert(updatedTechnician);
		return new Result(true, StatusCode.SUCCESS, "Update Success", updatedTechnicianDto);
	}

	@DeleteMapping("/{technicianId}")
	public Result deleteTechnician(@PathVariable Integer technicianId) {
		technicianService.delete(technicianId);
		return new Result(true, StatusCode.SUCCESS, "Delete Success");
	}

}
