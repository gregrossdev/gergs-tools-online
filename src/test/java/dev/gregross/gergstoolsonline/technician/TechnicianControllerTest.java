package dev.gregross.gergstoolsonline.technician;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.gregross.gergstoolsonline.system.http.StatusCode;
import dev.gregross.gergstoolsonline.system.http.exception.ObjectNotFoundException;
import dev.gregross.gergstoolsonline.tool.Tool;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class TechnicianControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	TechnicianService technicianService;

	List<Technician> technicians;

	@Value("${api.endpoint.base-url}")
	String baseUrl;


	@BeforeEach
	void setUp() throws Exception {
		Tool a1 = new Tool();
		a1.setId("1250808601744904191");
		a1.setName("Deluminator");
		a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
		a1.setImageUrl("ImageUrl");

		Tool a2 = new Tool();
		a2.setId("1250808601744904192");
		a2.setName("Invisibility Cloak");
		a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
		a2.setImageUrl("ImageUrl");

		Tool a3 = new Tool();
		a3.setId("1250808601744904193");
		a3.setName("Elder Wand");
		a3.setDescription("The Elder Wand, known throughout history as the Deathstick or the Wand of Destiny, is an extremely powerful wand made of elder wood with a core of Thestral tail hair.");
		a3.setImageUrl("ImageUrl");

		Tool a4 = new Tool();
		a4.setId("1250808601744904194");
		a4.setName("The Marauder's Map");
		a4.setDescription("A magical map of Hogwarts created by Remus Lupin, Peter Pettigrew, Sirius Black, and James Potter while they were students at Hogwarts.");
		a4.setImageUrl("ImageUrl");

		Tool a5 = new Tool();
		a5.setId("1250808601744904195");
		a5.setName("The Sword Of Gryffindor");
		a5.setDescription("A goblin-made sword adorned with large rubies on the pommel. It was once owned by Godric Gryffindor, one of the medieval founders of Hogwarts.");
		a5.setImageUrl("ImageUrl");

		Tool a6 = new Tool();
		a6.setId("1250808601744904196");
		a6.setName("Resurrection Stone");
		a6.setDescription("The Resurrection Stone allows the holder to bring back deceased loved ones, in a semi-physical form, and communicate with them.");
		a6.setImageUrl("ImageUrl");

		technicians = new ArrayList<>();

		Technician w1 = new Technician();
		w1.setId(1);
		w1.setName("Albus Dumbledore");
		w1.addTool(a1);
		w1.addTool(a3);
		technicians.add(w1);

		Technician w2 = new Technician();
		w2.setId(2);
		w2.setName("Harry Potter");
		w2.addTool(a2);
		w2.addTool(a4);
		technicians.add(w2);

		Technician w3 = new Technician();
		w3.setId(3);
		w3.setName("Neville Longbottom");
		w3.addTool(a5);
		technicians.add(w3);
	}

	@Test
	void testFindAllTechniciansSuccess() throws Exception {
		// Given. Arrange inputs and targets. Define the behavior of Mock object technicianService.
		given(technicianService.findAll()).willReturn(technicians);

		// When and then
		mockMvc.perform(get(baseUrl + "/technicians").accept(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.flag").value(true))
			.andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
			.andExpect(jsonPath("$.message").value("Find All Success"))
			.andExpect(jsonPath("$.data", Matchers.hasSize(technicians.size())))
			.andExpect(jsonPath("$.data[0].id").value(1))
			.andExpect(jsonPath("$.data[0].name").value("Albus Dumbledore"))
			.andExpect(jsonPath("$.data[1].id").value(2))
			.andExpect(jsonPath("$.data[1].name").value("Harry Potter"));
	}

	@Test
	void testFindTechnicianByIdSuccess() throws Exception {
		// Given. Arrange inputs and targets. Define the behavior of Mock object technicianService.
		given(technicianService.findById(1)).willReturn(technicians.get(0));

		// When and then
		mockMvc.perform(get(baseUrl + "/technicians/1").accept(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.flag").value(true))
			.andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
			.andExpect(jsonPath("$.message").value("Find One Success"))
			.andExpect(jsonPath("$.data.id").value(1))
			.andExpect(jsonPath("$.data.name").value("Albus Dumbledore"));
	}

	@Test
	void testFindTechnicianByIdNotFound() throws Exception {
		// Given. Arrange inputs and targets. Define the behavior of Mock object technicianService.
		given(technicianService.findById(5)).willThrow(new ObjectNotFoundException("technician", 5));

		// When and then
		mockMvc.perform(get(baseUrl + "/technicians/5").accept(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.flag").value(false))
			.andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
			.andExpect(jsonPath("$.message").value("Could not find technician with id: 5"))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	void testAddTechnicianSuccess() throws Exception {
		TechnicianDto technicianDto = new TechnicianDto(null, "Hermione Granger", 0);

		String json = objectMapper.writeValueAsString(technicianDto);

		Technician savedTechnician = new Technician();
		savedTechnician.setId(4);
		savedTechnician.setName("Hermione Granger");

		// Given. Arrange inputs and targets. Define the behavior of Mock object technicianService.
		given(technicianService.save(Mockito.any(Technician.class))).willReturn(savedTechnician);

		// When and then
		mockMvc.perform(post(baseUrl + "/technicians").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.flag").value(true))
			.andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
			.andExpect(jsonPath("$.message").value("Add Success"))
			.andExpect(jsonPath("$.data.id").isNotEmpty())
			.andExpect(jsonPath("$.data.name").value("Hermione Granger"));
	}

	@Test
	void testUpdateTechnicianSuccess() throws Exception {
		TechnicianDto technicianDto = new TechnicianDto(null, "Updated technician name", 0);

		Technician updatedTechnician = new Technician();
		updatedTechnician.setId(1);
		updatedTechnician.setName("Updated technician name");

		String json = objectMapper.writeValueAsString(updatedTechnician);

		// Given. Arrange inputs and targets. Define the behavior of Mock object technicianService.
		given(technicianService.update(eq(1), Mockito.any(Technician.class))).willReturn(updatedTechnician);

		// When and then
		mockMvc.perform(put(baseUrl + "/technicians/1").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.flag").value(true))
			.andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
			.andExpect(jsonPath("$.message").value("Update Success"))
			.andExpect(jsonPath("$.data.id").value(1))
			.andExpect(jsonPath("$.data.name").value("Updated technician name"));
	}

	@Test
	void testUpdateTechnicianErrorWithNonExistentId() throws Exception {
		// Given. Arrange inputs and targets. Define the behavior of Mock object technicianService.
		given(technicianService.update(eq(5), Mockito.any(Technician.class))).willThrow(new ObjectNotFoundException("technician", 5));

		TechnicianDto technicianDto = new TechnicianDto(5, // This id does not exist in the database.
			"Updated technician name",
			0);

		String json = objectMapper.writeValueAsString(technicianDto);

		// When and then
		mockMvc.perform(put(baseUrl + "/technicians/5").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.flag").value(false))
			.andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
			.andExpect(jsonPath("$.message").value("Could not find technician with id: 5"))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	void testDeleteTechnicianSuccess() throws Exception {
		// Given. Arrange inputs and targets. Define the behavior of Mock object technicianService.
		doNothing().when(technicianService).delete(3);

		// When and then
		mockMvc.perform(delete(baseUrl + "/technicians/3").accept(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.flag").value(true))
			.andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
			.andExpect(jsonPath("$.message").value("Delete Success"))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	void testDeleteTechnicianErrorWithNonExistentId() throws Exception {
		// Given. Arrange inputs and targets. Define the behavior of Mock object technicianService.
		doThrow(new ObjectNotFoundException("technician", 5)).when(technicianService).delete(5);

		// When and then
		mockMvc.perform(delete(baseUrl + "/technicians/5").accept(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.flag").value(false))
			.andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
			.andExpect(jsonPath("$.message").value("Could not find technician with id: 5"))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	void testAssignToolSuccess() throws Exception {
		// Given
		doNothing().when(technicianService).assignTool(2, "1250808601744904191");

		// When and then
		mockMvc.perform(put(baseUrl + "/technicians/2/tools/1250808601744904191").accept(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.flag").value(true))
			.andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
			.andExpect(jsonPath("$.message").value("Tool Assignment Success"))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	void testAssignToolErrorWithNonExistentTechnicianId() throws Exception {
		// Given
		doThrow(new ObjectNotFoundException("technician", 5)).when(technicianService).assignTool(5, "1250808601744904191");

		// When and then
		mockMvc.perform(put(baseUrl + "/technicians/5/tools/1250808601744904191").accept(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.flag").value(false))
			.andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
			.andExpect(jsonPath("$.message").value("Could not find technician with id: 5"))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	void testAssignToolErrorWithNonExistentToolId() throws Exception {
		// Given
		doThrow(new ObjectNotFoundException("tool", "1250808601744904199")).when(technicianService).assignTool(2, "1250808601744904199");

		// When and then
		mockMvc.perform(put(baseUrl + "/technicians/2/tools/1250808601744904199").accept(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.flag").value(false))
			.andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
			.andExpect(jsonPath("$.message").value("Could not find tool with id: 1250808601744904199"))
			.andExpect(jsonPath("$.data").isEmpty());
	}

}