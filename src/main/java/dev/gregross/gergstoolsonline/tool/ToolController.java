package dev.gregross.gergstoolsonline.tool;

import dev.gregross.gergstoolsonline.system.Result;
import dev.gregross.gergstoolsonline.system.StatusCode;
import dev.gregross.gergstoolsonline.tool.converter.ToolToToolDtoConverter;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

	@GetMapping
	public Result findAllTools() {
		List<Tool> foundTools = toolService.findAll();
		List<ToolDto> toolDtos = foundTools.stream()
			.map(toolToToolDtoConverter::convert)
			.toList();
		return new Result(true, StatusCode.SUCCESS, "Find All Success", toolDtos);
	}

	@PostMapping
	public Result addTool(@RequestBody ToolDto toolDto) {
		return null;
	}


}
