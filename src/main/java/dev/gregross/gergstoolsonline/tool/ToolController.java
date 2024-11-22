package dev.gregross.gergstoolsonline.tool;

import dev.gregross.gergstoolsonline.system.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tools")
public class ToolController {


	@GetMapping("/{toolId}")
	public Result findArtifactById(@PathVariable String toolId) {
		return null;
	}

	
}
