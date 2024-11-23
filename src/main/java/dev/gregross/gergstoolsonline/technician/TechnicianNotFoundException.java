package dev.gregross.gergstoolsonline.technician;

public class TechnicianNotFoundException extends RuntimeException {

		public TechnicianNotFoundException(int id) {
				super("Could not find technician with id: " + id);
		}
}
