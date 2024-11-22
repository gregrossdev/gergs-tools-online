package dev.gregross.gergstoolsonline.tool;

import dev.gregross.gergstoolsonline.system.Result;
import dev.gregross.gergstoolsonline.system.StatusCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tools")
public class ToolController {

	private final ToolService toolService;

	public ToolController(ToolService toolService) {
		this.toolService = toolService;
	}

	@GetMapping("/{toolId}")
	public Result findToolById(@PathVariable String toolId) {
		Tool foundTool = toolService.findById(toolId);
		return new Result(true, StatusCode.SUCCESS, "Find One Success", foundTool);
	}


}
