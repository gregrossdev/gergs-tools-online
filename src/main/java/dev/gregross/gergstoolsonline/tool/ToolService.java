package dev.gregross.gergstoolsonline.tool;

import dev.gregross.gergstoolsonline.tool.utils.IdWorker;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ToolService {

	private final ToolRepository toolRepository;
	private final IdWorker idWorker;

	public ToolService(ToolRepository toolRepository, IdWorker idWorker) {
		this.toolRepository = toolRepository;
		this.idWorker = idWorker;
	}

	public Tool findById(String toolId) {
		return toolRepository.findById(toolId)
			.orElseThrow(() -> new ToolNotFoundException(toolId));
	}

	public List<Tool> findAll() {
		return toolRepository.findAll();
	}

	public Tool save(Tool newTool) {
		newTool.setId(idWorker.nextId() + "");
		return toolRepository.save(newTool);
	}
}
