package dev.gregross.gergstoolsonline.system.exception;

import dev.gregross.gergstoolsonline.system.Result;
import dev.gregross.gergstoolsonline.system.StatusCode;
import dev.gregross.gergstoolsonline.tool.ToolNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

	@ExceptionHandler(ToolNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	Result handleArtifactNotFoundException(ToolNotFoundException ex){
		return new Result(false, StatusCode.NOT_FOUND, ex.getMessage());
	}


}