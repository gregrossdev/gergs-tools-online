package dev.gregross.gergstoolsonline.tool;

import dev.gregross.gergstoolsonline.system.Result;
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

	public Tool update(String toolId, Tool updatedTool) {
		return toolRepository.findById(toolId)
			.map(currentTool -> {
				currentTool.setName(updatedTool.getName());
				currentTool.setDescription(updatedTool.getDescription());
				currentTool.setImageUrl(updatedTool.getImageUrl());
				return toolRepository.save(currentTool);
			})
			.orElseThrow(() -> new ToolNotFoundException(toolId));
	}


	public void delete(String toolId) {
		toolRepository.findById(toolId)
			.orElseThrow(() -> new ToolNotFoundException(toolId));
		toolRepository.deleteById(toolId);
	}
}
