package dev.gregross.gergstoolsonline.tool;

import dev.gregross.gergstoolsonline.technician.Technician;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.io.Serializable;

@Entity
public class Tool implements Serializable {

	@Id
	private String id;
	private String name;
	private String description;
	private String imageUrl;

	@ManyToOne
	@JoinColumn(name = "possessor_id")
	private Technician possessor;

	public Tool() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Technician getPossessor() {
		return possessor;
	}

	public void setPossessor(Technician possessor) {
		this.possessor = possessor;
	}


}
