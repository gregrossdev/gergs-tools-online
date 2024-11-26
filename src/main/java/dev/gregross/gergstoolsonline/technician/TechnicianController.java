package dev.gregross.gergstoolsonline.technician;

import dev.gregross.gergstoolsonline.system.http.Result;
import dev.gregross.gergstoolsonline.system.http.StatusCode;
import dev.gregross.gergstoolsonline.technician.converter.TechnicianDtoToTechnicianConverter;
import dev.gregross.gergstoolsonline.technician.converter.TechnicianToTechnicianDtoConverter;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.endpoint.base-url}/technicians")
public class TechnicianController {

	private final TechnicianService technicianService;

	private final TechnicianDtoToTechnicianConverter technicianDtoToTechnicianConverter; // Convert TechnicianDto to Technician.

	private final TechnicianToTechnicianDtoConverter technicianToTechnicianDtoConverter; // Convert Technician to TechnicianDto.


	public TechnicianController(TechnicianService technicianService, TechnicianDtoToTechnicianConverter technicianDtoToTechnicianConverter, TechnicianToTechnicianDtoConverter technicianToTechnicianDtoConverter) {
		this.technicianService = technicianService;
		this.technicianDtoToTechnicianConverter = technicianDtoToTechnicianConverter;
		this.technicianToTechnicianDtoConverter = technicianToTechnicianDtoConverter;
	}

	@GetMapping
	public Result findAllTechnicians() {
		List<Technician> foundTechnicians = technicianService.findAll();

		// Convert foundTechnicians to a list of TechnicianDtos.
		List<TechnicianDto> technicianDtos = foundTechnicians.stream()
			.map(technicianToTechnicianDtoConverter::convert)
			.collect(Collectors.toList());
		return new Result(true, StatusCode.SUCCESS, "Find All Success", technicianDtos);
	}

	@GetMapping("/{technicianId}")
	public Result findTechnicianById(@PathVariable Integer technicianId) {
		Technician foundTechnician = technicianService.findById(technicianId);
		TechnicianDto technicianDto = technicianToTechnicianDtoConverter.convert(foundTechnician);
		return new Result(true, StatusCode.SUCCESS, "Find One Success", technicianDto);
	}

	@PostMapping
	public Result addTechnician(@Valid @RequestBody TechnicianDto technicianDto) {
		Technician newTechnician = technicianDtoToTechnicianConverter.convert(technicianDto);
		Technician savedTechnician = technicianService.save(newTechnician);
		TechnicianDto savedTechnicianDto = technicianToTechnicianDtoConverter.convert(savedTechnician);
		return new Result(true, StatusCode.SUCCESS, "Add Success", savedTechnicianDto);
	}

	@PutMapping("/{technicianId}")
	public Result updateTechnician(@PathVariable Integer technicianId, @Valid @RequestBody TechnicianDto technicianDto) {
		Technician update = technicianDtoToTechnicianConverter.convert(technicianDto);
		Technician updatedTechnician = technicianService.update(technicianId, update);
		TechnicianDto updatedTechnicianDto = technicianToTechnicianDtoConverter.convert(updatedTechnician);
		return new Result(true, StatusCode.SUCCESS, "Update Success", updatedTechnicianDto);
	}

	@DeleteMapping("/{technicianId}")
	public Result deleteTechnician(@PathVariable Integer technicianId) {
		technicianService.delete(technicianId);
		return new Result(true, StatusCode.SUCCESS, "Delete Success");
	}

	@PutMapping("/{technicianId}/tools/{toolId}")
	public Result assignTool(@PathVariable Integer technicianId, @PathVariable String toolId) {
		technicianService.assignTool(technicianId, toolId);
		return new Result(true, StatusCode.SUCCESS, "Tool Assignment Success");
	}

}
