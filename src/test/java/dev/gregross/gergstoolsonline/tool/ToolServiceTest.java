package dev.gregross.gergstoolsonline.tool;

import dev.gregross.gergstoolsonline.technician.Technician;
import dev.gregross.gergstoolsonline.tool.utils.IdWorker;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ToolServiceTest {

	@Mock
	ToolRepository toolRepository;

	@Mock
	IdWorker idWorker;

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


	@Test
	void testSaveSuccess() {
		Tool newTool = new Tool();
		newTool.setName("12v Milwaukee");
		newTool.setDescription("A 12v Milwaukee drill");
		newTool.setImageUrl("imageUrl");

		// given
		given(idWorker.nextId()).willReturn(123456L);
		given(toolRepository.save(newTool)).willReturn(newTool);

		// when
		Tool savedTool = toolService.save(newTool);

		// then
		assertThat(savedTool.getId()).isEqualTo("123456");
		assertThat(savedTool.getName()).isEqualTo(newTool.getName());
		assertThat(savedTool.getDescription()).isEqualTo(newTool.getDescription());
		assertThat(savedTool.getImageUrl()).isEqualTo(newTool.getImageUrl());
		verify(toolRepository, times(1)).save(newTool);
	}

	@Test
	void testUpdateSuccess() {
		// given
		Tool oldTool = new Tool();
		oldTool.setId("1250808601744904192");
		oldTool.setName("Invisibility Cloak");
		oldTool.setDescription("An invisibility cloak is used to make the wearer invisible.");
		oldTool.setImageUrl("ImageUrl");

		Tool update = new Tool();
		update.setId("1250808601744904192");
		update.setName("Invisibility Cloak");
		update.setDescription("A new description.");
		update.setImageUrl("ImageUrl");

		given(toolRepository.findById("1250808601744904192")).willReturn(Optional.of(oldTool));
		given(toolRepository.save(oldTool)).willReturn(oldTool);

		// when
		Tool updatedTool = toolService.update("1250808601744904192", update);

		// then
		assertThat(updatedTool.getId()).isEqualTo(update.getId());
		assertThat(updatedTool.getDescription()).isEqualTo(update.getDescription());
		verify(toolRepository, times(1)).findById("1250808601744904192");
		verify(toolRepository, times(1)).save(oldTool);

	}

	@Test
	void testUpdateNotFound() {
		// given
		Tool update = new Tool();
		update.setName("Invisibility Cloak");
		update.setDescription("A new description.");
		update.setImageUrl("ImageUrl");

		given(toolRepository.findById("1250808601744904192")).willReturn(Optional.empty());

		// when
		assertThrows(ToolNotFoundException.class, () -> {
			toolService.update("1250808601744904192", update);
		});

		// then
		verify(toolRepository, times(1)).findById("1250808601744904192");
	}

	@Test
	void testDeleteSuccess(){
		// Given
		Tool tool = new Tool();
		tool.setId("1250808601744904192");
		tool.setName("Invisibility Cloak");
		tool.setDescription("An invisibility cloak is used to make the wearer invisible.");
		tool.setImageUrl("ImageUrl");

		given(toolRepository.findById("1250808601744904192")).willReturn(Optional.of(tool));
		doNothing().when(toolRepository).deleteById("1250808601744904192");

		// When
		toolService.delete("1250808601744904192");

		// Then
		verify(toolRepository, times(1)).deleteById("1250808601744904192");
	}

	@Test
	void testDeleteNotFound(){
		// Given
		given(toolRepository.findById("1250808601744904192")).willReturn(Optional.empty());

		// When
		assertThrows(ToolNotFoundException.class, () -> {
			toolService.delete("1250808601744904192");
		});

		// Then
		verify(toolRepository, times(1)).findById("1250808601744904192");
	}

}