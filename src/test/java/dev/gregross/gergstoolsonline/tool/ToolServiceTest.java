package dev.gregross.gergstoolsonline.tool;

import dev.gregross.gergstoolsonline.technician.Technician;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ToolServiceTest {

	@Mock
	ToolRepository toolRepository;

	@InjectMocks
	ToolService toolService;

	List<Tool> tools = new ArrayList<>();

	@BeforeEach
	void setUp() {
		Tool a1 = new Tool();
		a1.setId("1250808601744904191");
		a1.setName("Deluminator");
		a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
		a1.setImageUrl("imageUrl");

		Tool a2 = new Tool();
		a2.setId("1250808601744904192");
		a2.setName("Invisibility Cloak");
		a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
		a2.setImageUrl("imageUrl");

		this.tools = new ArrayList<>();
		this.tools.add(a1);
		this.tools.add(a2);
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void testFindByIdSuccess() {
		Tool tool = new Tool();
		tool.setId("1250808601744904191");
		tool.setName("Deluminator");
		tool.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
		tool.setImageUrl("imageUrl");

		Technician tech = new Technician();
		tech.setId(1);
		tech.setName("Greg");

		tool.setPossessor(tech);

		// given
		given(toolRepository.findById("1250808601744904191")).willReturn(Optional.of(tool));

		// when
		Tool returnedTool = toolService.findById("1250808601744904191");

		// then
		assertThat(returnedTool.getId()).isEqualTo(tool.getId());
		assertThat(returnedTool.getName()).isEqualTo(tool.getName());
		assertThat(returnedTool.getDescription()).isEqualTo(tool.getDescription());
		assertThat(returnedTool.getImageUrl()).isEqualTo(tool.getImageUrl());
		verify(toolRepository, times(1)).findById("1250808601744904191");

	}

	@Test
	void testFindByIdNotFound() {
		// given
		given(toolRepository.findById(Mockito.any(String.class))).willReturn(Optional.empty());

		// when
		Throwable thrown = catchThrowable(() -> {
			Tool returnedTool = toolService.findById("1250808601744904191");
		});

		// then
		assertThat(thrown)
			.isInstanceOf(ToolNotFoundException.class)
			.hasMessage("Could not find tool with id: 1250808601744904191");

		verify(toolRepository, times(1)).findById("1250808601744904191");
	}

	@Test
	void testFindAllSuccess() {
		// given
		given(toolRepository.findAll()).willReturn(tools);

		// when
		List<Tool> returnedTools = toolService.findAll();

		// then
		assertThat(returnedTools.size()).isEqualTo(tools.size());
	}









}