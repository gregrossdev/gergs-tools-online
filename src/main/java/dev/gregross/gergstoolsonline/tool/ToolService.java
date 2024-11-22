package dev.gregross.gergstoolsonline.tool;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ToolService {

	private final ToolRepository toolRepository;

	public ToolService(ToolRepository toolRepository) {
		this.toolRepository = toolRepository;
	}

	public Tool findById(String toolId) {
		return toolRepository.findById(toolId)
			.orElseThrow(() -> new ToolNotFoundException(toolId));
	}

	public List<Tool> findAll() {
		return toolRepository.findAll();
	}
}
