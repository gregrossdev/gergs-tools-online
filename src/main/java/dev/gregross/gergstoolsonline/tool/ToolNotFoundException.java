package dev.gregross.gergstoolsonline.tool;

public class ToolNotFoundException extends RuntimeException {

		public ToolNotFoundException(String id) {
				super("Could not find tool with id: " + id);
		}

}
