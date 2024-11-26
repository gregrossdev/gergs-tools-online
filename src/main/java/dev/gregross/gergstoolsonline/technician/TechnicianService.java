package dev.gregross.gergstoolsonline.technician;

import dev.gregross.gergstoolsonline.tool.Tool;
import dev.gregross.gergstoolsonline.tool.ToolRepository;
import jakarta.transaction.Transactional;
import dev.gregross.gergstoolsonline.system.http.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class TechnicianService {
	private final TechnicianRepository technicianRepository;
	private final ToolRepository toolRepository;

	public TechnicianService(TechnicianRepository technicianRepository,
													 ToolRepository toolRepository) {
		this.technicianRepository = technicianRepository;
		this.toolRepository = toolRepository;
	}

	public Technician findById(int technicianId) {
		return technicianRepository.findById(technicianId)
			.orElseThrow(() -> new ObjectNotFoundException("technician", technicianId));
	}

	public List<Technician> findAll() {
		return technicianRepository.findAll();
	}

	public Technician save(Technician technician) {
		return technicianRepository.save(technician);
	}

	// We are not updating a technician's tools through this method, we only update their name.
	public Technician update(Integer technicianId, Technician update) {
		return technicianRepository.findById(technicianId)
			.map(oldTechnician -> {
				oldTechnician.setName(update.getName());
				return technicianRepository.save(oldTechnician);
			})
			.orElseThrow(() -> new ObjectNotFoundException("technician", technicianId));
	}

	public void delete(Integer technicianId) {
		Technician technicianToBeDeleted = technicianRepository.findById(technicianId)
			.orElseThrow(() -> new ObjectNotFoundException("technician", technicianId));

		// Before deletion, we will unassign this technician's owned tools.
		technicianToBeDeleted.removeAllTools();
		technicianRepository.deleteById(technicianId);
	}


	public void assignTool(Integer technicianId, String toolId){
		// Find this tool by Id from DB.
		Tool toolToBeAssigned = toolRepository.findById(toolId)
			.orElseThrow(() -> new ObjectNotFoundException("tool", toolId));

		// Find this technician by Id from DB.
		Technician technician = technicianRepository.findById(technicianId)
			.orElseThrow(() -> new ObjectNotFoundException("technician", technicianId));

		// Tool assignment
		// We need to see if the tool is already owned by some technician.
		if (toolToBeAssigned.getPossessor() != null) {
			toolToBeAssigned.getPossessor().removeTool(toolToBeAssigned);
		}
		technician.addTool(toolToBeAssigned);
	}
}
