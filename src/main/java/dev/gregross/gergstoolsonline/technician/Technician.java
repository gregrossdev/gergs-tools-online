package dev.gregross.gergstoolsonline.technician;

import dev.gregross.gergstoolsonline.tool.Tool;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Technician implements Serializable {

  @Id
  private Integer id;
  private String name;

  @OneToMany(mappedBy = "possessor", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private List<Tool> tools = new ArrayList<>();

  public Technician() {
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setTools(List<Tool> tools) {
    this.tools = tools;
  }

  public List<Tool> getTools() {
    return tools;
  }

  public Integer getNumberOfTools() {
    return tools.size();
  }

  public void addTool(Tool tool) {
    tool.setPossessor(this);
    tools.add(tool);
  }

  public void removeAllTools() {
    tools.forEach(tool -> tool.setPossessor(null));
    tools = new ArrayList<>();
  }

  public void removeTool(Tool toolToBeAssigned) {
    // Remove tool owner.
    toolToBeAssigned.setPossessor(null);
    tools.remove(toolToBeAssigned);
  }
}