package dev.gregross.gergstoolsonline.tool;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.gregross.gergstoolsonline.system.http.StatusCode;
import dev.gregross.gergstoolsonline.system.http.exception.ObjectNotFoundException;

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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // Turn off Spring Security
class ToolControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	ToolService toolService;

	@Autowired
	ObjectMapper objectMapper;

	List<Tool> tools;

	@Value("${api.endpoint.base-url}") // Spring will go to application.yml to find the value and inject into this field.
	String baseUrl;


	@BeforeEach
	void setUp() {
		tools = new ArrayList<>();

		Tool a1 = new Tool();
		a1.setId("1250808601744904191");
		a1.setName("Deluminator");
		a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
		a1.setImageUrl("ImageUrl");
		tools.add(a1);

		Tool a2 = new Tool();
		a2.setId("1250808601744904192");
		a2.setName("Invisibility Cloak");
		a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
		a2.setImageUrl("ImageUrl");
		tools.add(a2);

		Tool a3 = new Tool();
		a3.setId("1250808601744904193");
		a3.setName("Elder Wand");
		a3.setDescription("The Elder Wand, known throughout history as the Deathstick or the Wand of Destiny, is an extremely powerful wand made of elder wood with a core of Thestral tail hair.");
		a3.setImageUrl("ImageUrl");
		tools.add(a3);

		Tool a4 = new Tool();
		a4.setId("1250808601744904194");
		a4.setName("The Marauder's Map");
		a4.setDescription("A magical map of Hogwarts created by Remus Lupin, Peter Pettigrew, Sirius Black, and James Potter while they were students at Hogwarts.");
		a4.setImageUrl("ImageUrl");
		tools.add(a4);

		Tool a5 = new Tool();
		a5.setId("1250808601744904195");
		a5.setName("The Sword Of Gryffindor");
		a5.setDescription("A goblin-made sword adorned with large rubies on the pommel. It was once owned by Godric Gryffindor, one of the medieval founders of Hogwarts.");
		a5.setImageUrl("ImageUrl");
		tools.add(a5);

		Tool a6 = new Tool();
		a6.setId("1250808601744904196");
		a6.setName("Resurrection Stone");
		a6.setDescription("The Resurrection Stone allows the holder to bring back deceased loved ones, in a semi-physical form, and communicate with them.");
		a6.setImageUrl("ImageUrl");
		tools.add(a6);
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void testFindToolByIdSuccess() throws Exception {
		// Given
		given(toolService.findById("1250808601744904191")).willReturn(tools.get(0));

		// When and then
		mockMvc.perform(get(baseUrl + "/tools/1250808601744904191").accept(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.flag").value(true))
			.andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
			.andExpect(jsonPath("$.message").value("Find One Success"))
			.andExpect(jsonPath("$.data.id").value("1250808601744904191"))
			.andExpect(jsonPath("$.data.name").value("Deluminator"));
	}

	@Test
	void testFindToolByIdNotFound() throws Exception {
		// Given
		given(toolService.findById("1250808601744904191")).willThrow(new ObjectNotFoundException("tool", "1250808601744904191"));

		// When and then
		mockMvc.perform(get(baseUrl + "/tools/1250808601744904191").accept(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.flag").value(false))
			.andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
			.andExpect(jsonPath("$.message").value("Could not find tool with id: 1250808601744904191"))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	void testFindAllToolsSuccess() throws Exception {
		// Given
		given(toolService.findAll()).willReturn(tools);

		// When and then
		mockMvc.perform(get(baseUrl + "/tools").accept(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.flag").value(true))
			.andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
			.andExpect(jsonPath("$.message").value("Find All Success"))
			.andExpect(jsonPath("$.data", Matchers.hasSize(tools.size())))
			.andExpect(jsonPath("$.data[0].id").value("1250808601744904191"))
			.andExpect(jsonPath("$.data[0].name").value("Deluminator"))
			.andExpect(jsonPath("$.data[1].id").value("1250808601744904192"))
			.andExpect(jsonPath("$.data[1].name").value("Invisibility Cloak"));
	}

	@Test
	void testAddToolSuccess() throws Exception {
		// Given
		ToolDto toolDto = new ToolDto(null,
			"Remembrall",
			"A Remembrall was a magical large marble-sized glass ball that contained smoke which turned red when its owner or user had forgotten something. It turned clear once whatever was forgotten was remembered.",
			"ImageUrl",
			null);
		String json = objectMapper.writeValueAsString(toolDto);

		Tool savedTool = new Tool();
		savedTool.setId("1250808601744904197");
		savedTool.setName("Remembrall");
		savedTool.setDescription("A Remembrall was a magical large marble-sized glass ball that contained smoke which turned red when its owner or user had forgotten something. It turned clear once whatever was forgotten was remembered.");
		savedTool.setImageUrl("ImageUrl");

		given(toolService.save(Mockito.any(Tool.class))).willReturn(savedTool);

		// When and then
		mockMvc.perform(post(baseUrl + "/tools").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.flag").value(true))
			.andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
			.andExpect(jsonPath("$.message").value("Add Success"))
			.andExpect(jsonPath("$.data.id").isNotEmpty())
			.andExpect(jsonPath("$.data.name").value(savedTool.getName()))
			.andExpect(jsonPath("$.data.description").value(savedTool.getDescription()))
			.andExpect(jsonPath("$.data.imageUrl").value(savedTool.getImageUrl()));
	}

	@Test
	void testUpdateToolSuccess() throws Exception {
		// Given
		ToolDto toolDto = new ToolDto("1250808601744904192",
			"Invisibility Cloak",
			"A new description.",
			"ImageUrl",
			null);
		String json = objectMapper.writeValueAsString(toolDto);

		Tool updatedTool = new Tool();
		updatedTool.setId("1250808601744904192");
		updatedTool.setName("Invisibility Cloak");
		updatedTool.setDescription("A new description.");
		updatedTool.setImageUrl("ImageUrl");

		given(toolService.update(eq("1250808601744904192"), Mockito.any(Tool.class))).willReturn(updatedTool);

		// When and then
		mockMvc.perform(put(baseUrl + "/tools/1250808601744904192").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.flag").value(true))
			.andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
			.andExpect(jsonPath("$.message").value("Update Success"))
			.andExpect(jsonPath("$.data.id").value("1250808601744904192"))
			.andExpect(jsonPath("$.data.name").value(updatedTool.getName()))
			.andExpect(jsonPath("$.data.description").value(updatedTool.getDescription()))
			.andExpect(jsonPath("$.data.imageUrl").value(updatedTool.getImageUrl()));
	}

	@Test
	void testUpdateToolErrorWithNonExistentId() throws Exception {
		// Given
		ToolDto toolDto = new ToolDto("1250808601744904192",
			"Invisibility Cloak",
			"A new description.",
			"ImageUrl",
			null);
		String json = objectMapper.writeValueAsString(toolDto);

		given(toolService.update(eq("1250808601744904192"), Mockito.any(Tool.class))).willThrow(new ObjectNotFoundException("tool", "1250808601744904192"));

		// When and then
		mockMvc.perform(put(baseUrl + "/tools/1250808601744904192").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.flag").value(false))
			.andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
			.andExpect(jsonPath("$.message").value("Could not find tool with id: 1250808601744904192"))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	void testDeleteToolSuccess() throws Exception {
		// Given
		doNothing().when(toolService).delete("1250808601744904191");

		// When and then
		mockMvc.perform(delete(baseUrl + "/tools/1250808601744904191").accept(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.flag").value(true))
			.andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
			.andExpect(jsonPath("$.message").value("Delete Success"))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	void testDeleteToolErrorWithNonExistentId() throws Exception {
		// Given
		doThrow(new ObjectNotFoundException("tool", "1250808601744904191")).when(toolService).delete("1250808601744904191");

		// When and then
		mockMvc.perform(delete(baseUrl + "/tools/1250808601744904191").accept(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.flag").value(false))
			.andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
			.andExpect(jsonPath("$.message").value("Could not find tool with id: 1250808601744904191"))
			.andExpect(jsonPath("$.data").isEmpty());
	}

}