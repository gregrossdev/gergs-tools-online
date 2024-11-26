package dev.gregross.gergstoolsonline.tool;

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
@DisplayName("Integration tests for Tool API endpoints")
@Tag("integration")
class ToolControllerIntegrationTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	String token;

	@Value("${api.endpoint.base-url}")
	String baseUrl;


	@BeforeEach
	void setUp() throws Exception {
//        ResultActions resultActions = mockMvc.perform(post(baseUrl + "/auth/login").header(HttpHeaders.AUTHORIZATION,
//                "Basic " + Base64Utils.encodeToString("john:123456".getBytes())));
		ResultActions resultActions = mockMvc.perform(post(baseUrl + "/users/login").with(httpBasic("john", "123456"))); // httpBasic() is from spring-security-test.
		MvcResult mvcResult = resultActions.andDo(print()).andReturn();
		String contentAsString = mvcResult.getResponse().getContentAsString();
		JSONObject json = new JSONObject(contentAsString);
		token = "Bearer " + json.getJSONObject("data").getString("token"); // Don't forget to add "Bearer " as prefix.
	}

	@Test
	@DisplayName("Check findAllTools (GET)")
	@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
		// Reset H2 database before calling this test case.
	void testFindAllToolsSuccess() throws Exception {
		mockMvc.perform(get(baseUrl + "/tools").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, token))
			.andExpect(jsonPath("$.flag").value(true))
			.andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
			.andExpect(jsonPath("$.message").value("Find All Success"))
			.andExpect(jsonPath("$.data", Matchers.hasSize(6)));
	}

	@Test
	@DisplayName("Check findToolById (GET)")
	void testFindToolByIdSuccess() throws Exception {
		mockMvc.perform(get(baseUrl + "/tools/1250808601744904191").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, token))
			.andExpect(jsonPath("$.flag").value(true))
			.andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
			.andExpect(jsonPath("$.message").value("Find One Success"))
			.andExpect(jsonPath("$.data.id").value("1250808601744904191"))
			.andExpect(jsonPath("$.data.name").value("Deluminator"));
	}

	@Test
	@DisplayName("Check findToolById with non-existent id (GET)")
	void testFindToolByIdNotFound() throws Exception {
		mockMvc.perform(get(baseUrl + "/tools/1250808601744904199").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, token))
			.andExpect(jsonPath("$.flag").value(false))
			.andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
			.andExpect(jsonPath("$.message").value("Could not find tool with id: 1250808601744904199"))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@DisplayName("Check addTool with valid input (POST)")
	@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
	void testAddToolSuccess() throws Exception {
		Tool a = new Tool();
		a.setName("Remembrall");
		a.setDescription("A Remembrall was a magical large marble-sized glass ball that contained smoke which turned red when its owner or user had forgotten something. It turned clear once whatever was forgotten was remembered.");
		a.setImageUrl("ImageUrl");

		String json = objectMapper.writeValueAsString(a);

		mockMvc.perform(post(baseUrl + "/tools").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, token))
			.andExpect(jsonPath("$.flag").value(true))
			.andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
			.andExpect(jsonPath("$.message").value("Add Success"))
			.andExpect(jsonPath("$.data.id").isNotEmpty())
			.andExpect(jsonPath("$.data.name").value("Remembrall"))
			.andExpect(jsonPath("$.data.description").value("A Remembrall was a magical large marble-sized glass ball that contained smoke which turned red when its owner or user had forgotten something. It turned clear once whatever was forgotten was remembered."))
			.andExpect(jsonPath("$.data.imageUrl").value("ImageUrl"));
		mockMvc.perform(get(baseUrl + "/tools").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, token))
			.andExpect(jsonPath("$.flag").value(true))
			.andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
			.andExpect(jsonPath("$.message").value("Find All Success"))
			.andExpect(jsonPath("$.data", Matchers.hasSize(7)));
	}

	@Test
	@DisplayName("Check addTool with invalid input (POST)")
	@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
	void testAddToolErrorWithInvalidInput() throws Exception {
		Tool a = new Tool();
		a.setName(""); // Name is not provided.
		a.setDescription(""); // Description is not provided.
		a.setImageUrl(""); // ImageUrl is not provided.

		String json = objectMapper.writeValueAsString(a);

		mockMvc.perform(post(baseUrl + "/tools").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, token))
			.andExpect(jsonPath("$.flag").value(false))
			.andExpect(jsonPath("$.code").value(StatusCode.INVALID_ARGUMENT))
			.andExpect(jsonPath("$.message").value("Provided arguments are invalid, see data for details."))
			.andExpect(jsonPath("$.data.name").value("name is required."))
			.andExpect(jsonPath("$.data.description").value("description is required."))
			.andExpect(jsonPath("$.data.imageUrl").value("imageUrl is required."));
		mockMvc.perform(get(baseUrl + "/tools").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, token))
			.andExpect(jsonPath("$.flag").value(true))
			.andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
			.andExpect(jsonPath("$.message").value("Find All Success"))
			.andExpect(jsonPath("$.data", Matchers.hasSize(6)));
	}

	@Test
	@DisplayName("Check updateTool with valid input (PUT)")
	void testUpdateToolSuccess() throws Exception {
		Tool a = new Tool();
		a.setId("1250808601744904192");
		a.setName("Updated tool name");
		a.setDescription("Updated description");
		a.setImageUrl("Updated imageUrl");

		String json = objectMapper.writeValueAsString(a);

		mockMvc.perform(put(baseUrl + "/tools/1250808601744904192").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, token))
			.andExpect(jsonPath("$.flag").value(true))
			.andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
			.andExpect(jsonPath("$.message").value("Update Success"))
			.andExpect(jsonPath("$.data.id").value("1250808601744904192"))
			.andExpect(jsonPath("$.data.name").value("Updated tool name"))
			.andExpect(jsonPath("$.data.description").value("Updated description"))
			.andExpect(jsonPath("$.data.imageUrl").value("Updated imageUrl"));
	}

	@Test
	@DisplayName("Check updateTool with non-existent id (PUT)")
	void testUpdateToolErrorWithNonExistentId() throws Exception {
		Tool a = new Tool();
		a.setId("1250808601744904199"); // This id does not exist in the database.
		a.setName("Updated tool name");
		a.setDescription("Updated description");
		a.setImageUrl("Updated imageUrl");

		String json = objectMapper.writeValueAsString(a);

		mockMvc.perform(put(baseUrl + "/tools/1250808601744904199").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, token))
			.andExpect(jsonPath("$.flag").value(false))
			.andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
			.andExpect(jsonPath("$.message").value("Could not find tool with id: 1250808601744904199"))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@DisplayName("Check updateTool with invalid input (PUT)")
	void testUpdateToolErrorWithInvalidInput() throws Exception {
		Tool a = new Tool();
		a.setId("1250808601744904191"); // Valid id
		a.setName(""); // Updated name is empty.
		a.setDescription(""); // Updated description is empty.
		a.setImageUrl(""); // Updated imageUrl is empty.

		String json = objectMapper.writeValueAsString(a);

		mockMvc.perform(put(baseUrl + "/tools/1250808601744904191").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, token))
			.andExpect(jsonPath("$.flag").value(false))
			.andExpect(jsonPath("$.code").value(StatusCode.INVALID_ARGUMENT))
			.andExpect(jsonPath("$.message").value("Provided arguments are invalid, see data for details."))
			.andExpect(jsonPath("$.data.name").value("name is required."))
			.andExpect(jsonPath("$.data.description").value("description is required."))
			.andExpect(jsonPath("$.data.imageUrl").value("imageUrl is required."));
		mockMvc.perform(get(baseUrl + "/tools/1250808601744904191").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, token))
			.andExpect(jsonPath("$.flag").value(true))
			.andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
			.andExpect(jsonPath("$.message").value("Find One Success"))
			.andExpect(jsonPath("$.data.id").value("1250808601744904191"))
			.andExpect(jsonPath("$.data.name").value("Deluminator"));
	}

	@Test
	@DisplayName("Check deleteTool with valid input (DELETE)")
	void testDeleteToolSuccess() throws Exception {
		mockMvc.perform(delete(baseUrl + "/tools/1250808601744904191").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, token))
			.andExpect(jsonPath("$.flag").value(true))
			.andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
			.andExpect(jsonPath("$.message").value("Delete Success"));
		mockMvc.perform(get(baseUrl + "/tools/1250808601744904191").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, token))
			.andExpect(jsonPath("$.flag").value(false))
			.andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
			.andExpect(jsonPath("$.message").value("Could not find tool with id: 1250808601744904191"))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@DisplayName("Check deleteTool with non-existent id (DELETE)")
	void testDeleteToolErrorWithNonExistentId() throws Exception {
		mockMvc.perform(delete(baseUrl + "/tools/1250808601744904199").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, token))
			.andExpect(jsonPath("$.flag").value(false))
			.andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
			.andExpect(jsonPath("$.message").value("Could not find tool with id: 1250808601744904199"))
			.andExpect(jsonPath("$.data").isEmpty());
	}

}
