package dev.gregross.gergstoolsonline.tool;

import dev.gregross.gergstoolsonline.system.Result;
import dev.gregross.gergstoolsonline.system.StatusCode;
import dev.gregross.gergstoolsonline.tool.converter.ToolToToolDtoConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tools")
public class ToolController {

	private final ToolService toolService;
	private final ToolToToolDtoConverter toolToToolDtoConverter;

	public ToolController(ToolService toolService, ToolToToolDtoConverter toolToToolDtoConverter) {
		this.toolService = toolService;
		this.toolToToolDtoConverter = toolToToolDtoConverter;
	}

	@GetMapping("/{toolId}")
	public Result findToolById(@PathVariable String toolId) {
		Tool foundTool = toolService.findById(toolId);
		ToolDto toolDto = toolToToolDtoConverter.convert(foundTool);
		return new Result(true, StatusCode.SUCCESS, "Find One Success", toolDto);
	}


}
