package dev.gregross.gergstoolsonline.technician;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.gregross.gergstoolsonline.system.http.StatusCode;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Integration tests for Technician API endpoints")
@Tag("integration")
class TechnicianControllerIntegrationTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	String token;

	@Value("${api.endpoint.base-url}")
	String baseUrl;


	@BeforeEach
	void setUp() throws Exception {
		ResultActions resultActions = mockMvc.perform(post(baseUrl + "/users/login").with(httpBasic("john", "123456"))); // httpBasic() is from spring-security-test.
		MvcResult mvcResult = resultActions.andDo(print()).andReturn();
		String contentAsString = mvcResult.getResponse().getContentAsString();
		JSONObject json = new JSONObject(contentAsString);
		token = "Bearer " + json.getJSONObject("data").getString("token");
	}

	@Test
	@DisplayName("Check findAllTechnicians (GET)")
	@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD) // Reset H2 database before calling this test case.
	void testFindAllTechniciansSuccess() throws Exception {
		mockMvc.perform(get(baseUrl + "/technicians").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, token))
			.andExpect(jsonPath("$.flag").value(true))
			.andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
			.andExpect(jsonPath("$.message").value("Find All Success"))
			.andExpect(jsonPath("$.data", Matchers.hasSize(3)));
	}

	@Test
	@DisplayName("Check findTechnicianById (GET)")
	void testFindTechnicianByIdSuccess() throws Exception {
		mockMvc.perform(get(baseUrl + "/technicians/1").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, token))
			.andExpect(jsonPath("$.flag").value(true))
			.andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
			.andExpect(jsonPath("$.message").value("Find One Success"))
			.andExpect(jsonPath("$.data.id").value(1))
			.andExpect(jsonPath("$.data.name").value("Albus Dumbledore"));
	}

	@Test
	@DisplayName("Check findTechnicianById with non-existent id (GET)")
	void testFindTechnicianByIdNotFound() throws Exception {
		mockMvc.perform(get(baseUrl + "/technicians/5").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, token))
			.andExpect(jsonPath("$.flag").value(false))
			.andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
			.andExpect(jsonPath("$.message").value("Could not find technician with id: 5"))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@DisplayName("Check addTechnician with valid input (POST)")
	@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
	void testAddTechnicianSuccess() throws Exception {
		Technician a = new Technician();
		a.setId(4);
		a.setName("Hermione Granger");

		String json = objectMapper.writeValueAsString(a);

		mockMvc.perform(post(baseUrl + "/technicians").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, token))
			.andExpect(jsonPath("$.flag").value(true))
			.andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
			.andExpect(jsonPath("$.message").value("Add Success"))
			.andExpect(jsonPath("$.data.id").isNotEmpty())
			.andExpect(jsonPath("$.data.name").value("Hermione Granger"));

		mockMvc.perform(get(baseUrl + "/technicians").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, token))
			.andExpect(jsonPath("$.flag").value(true))
			.andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
			.andExpect(jsonPath("$.message").value("Find All Success"))
			.andExpect(jsonPath("$.data", Matchers.hasSize(4)));
	}

	@Test
	@DisplayName("Check addTechnician with invalid input (POST)")
	@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
	void testAddTechnicianErrorWithInvalidInput() throws Exception {
		Technician a = new Technician();
		a.setId(4);
		a.setName(""); // Name is not provided.

		String json = objectMapper.writeValueAsString(a);

		mockMvc.perform(post(baseUrl + "/technicians").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, token))
			.andExpect(jsonPath("$.flag").value(false))
			.andExpect(jsonPath("$.code").value(StatusCode.INVALID_ARGUMENT))
			.andExpect(jsonPath("$.message").value("Provided arguments are invalid, see data for details."))
			.andExpect(jsonPath("$.data.name").value("name is required."));
		mockMvc.perform(get(baseUrl + "/technicians").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, token))
			.andExpect(jsonPath("$.flag").value(true))
			.andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
			.andExpect(jsonPath("$.message").value("Find All Success"))
			.andExpect(jsonPath("$.data", Matchers.hasSize(3)));
	}

	@Test
	@DisplayName("Check updateTechnician with valid input (PUT)")
	void testUpdateTechnicianSuccess() throws Exception {
		Technician a = new Technician();
		a.setId(1);
		a.setName("Updated technician name");

		String json = objectMapper.writeValueAsString(a);

		mockMvc.perform(put(baseUrl + "/technicians/1").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, token))
			.andExpect(jsonPath("$.flag").value(true))
			.andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
			.andExpect(jsonPath("$.message").value("Update Success"))
			.andExpect(jsonPath("$.data.id").value(1))
			.andExpect(jsonPath("$.data.name").value("Updated technician name"));
	}

	@Test
	@DisplayName("Check updateTechnician with non-existent id (PUT)")
	void testUpdateTechnicianErrorWithNonExistentId() throws Exception {
		Technician a = new Technician();
		a.setId(5); // This id does not exist in the database.
		a.setName("Updated technician name");

		String json = objectMapper.writeValueAsString(a);

		mockMvc.perform(put(baseUrl + "/technicians/5").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, token))
			.andExpect(jsonPath("$.flag").value(false))
			.andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
			.andExpect(jsonPath("$.message").value("Could not find technician with id: 5"))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@DisplayName("Check updateTechnician with invalid input (PUT)")
	void testUpdateTechnicianErrorWithInvalidInput() throws Exception {
		Technician a = new Technician();
		a.setId(1); // Valid id
		a.setName(""); // Updated name is empty.

		String json = objectMapper.writeValueAsString(a);

		mockMvc.perform(put(baseUrl + "/technicians/1").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, token))
			.andExpect(jsonPath("$.flag").value(false))
			.andExpect(jsonPath("$.code").value(StatusCode.INVALID_ARGUMENT))
			.andExpect(jsonPath("$.message").value("Provided arguments are invalid, see data for details."))
			.andExpect(jsonPath("$.data.name").value("name is required."));
		mockMvc.perform(get(baseUrl + "/technicians/1").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, token))
			.andExpect(jsonPath("$.flag").value(true))
			.andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
			.andExpect(jsonPath("$.message").value("Find One Success"))
			.andExpect(jsonPath("$.data.id").value(1))
			.andExpect(jsonPath("$.data.name").value("Albus Dumbledore"));
	}

	@Test
	@DisplayName("Check deleteTechnician with valid input (DELETE)")
	void testDeleteTechnicianSuccess() throws Exception {
		mockMvc.perform(delete(baseUrl + "/technicians/3").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, token))
			.andExpect(jsonPath("$.flag").value(true))
			.andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
			.andExpect(jsonPath("$.message").value("Delete Success"));
		mockMvc.perform(get(baseUrl + "/technicians/3").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, token))
			.andExpect(jsonPath("$.flag").value(false))
			.andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
			.andExpect(jsonPath("$.message").value("Could not find technician with id: 3"))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@DisplayName("Check deleteTechnician with non-existent id (DELETE)")
	void testDeleteTechnicianErrorWithNonExistentId() throws Exception {
		mockMvc.perform(delete(baseUrl + "/technicians/5").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, token))
			.andExpect(jsonPath("$.flag").value(false))
			.andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
			.andExpect(jsonPath("$.message").value("Could not find technician with id: 5"))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@DisplayName("Check assignArtifact with valid ids (PUT)")
	void testAssignArtifactSuccess() throws Exception {
		mockMvc.perform(put(baseUrl + "/technicians/2/tools/1250808601744904191").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, token))
			.andExpect(jsonPath("$.flag").value(true))
			.andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
			.andExpect(jsonPath("$.message").value("Artifact Assignment Success"))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@DisplayName("Check assignArtifact with non-existent technician id (PUT)")
	void testAssignArtifactErrorWithNonExistentTechnicianId() throws Exception {
		mockMvc.perform(put(baseUrl + "/technicians/5/tools/1250808601744904191").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, token))
			.andExpect(jsonPath("$.flag").value(false))
			.andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
			.andExpect(jsonPath("$.message").value("Could not find technician with id: 5"))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@DisplayName("Check assignArtifact with non-existent tool id (PUT)")
	void testAssignArtifactErrorWithNonExistentArtifactId() throws Exception {
		mockMvc.perform(put(baseUrl + "/technicians/2/tools/1250808601744904199").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, token))
			.andExpect(jsonPath("$.flag").value(false))
			.andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
			.andExpect(jsonPath("$.message").value("Could not find tool with id: 1250808601744904199"))
			.andExpect(jsonPath("$.data").isEmpty());
	}

}