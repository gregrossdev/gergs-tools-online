package dev.gregross.gergstoolsonline.technician;

import dev.gregross.gergstoolsonline.system.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/technicians")
public class TechnicianController {

	private final TechnicianService technicianService;

	public TechnicianController(TechnicianService technicianService) {
		this.technicianService = technicianService;
	}

	@GetMapping("/{technicianId}")
	public Result findTechnicianById(@PathVariable int technicianId) {
		return null;
	}

}
