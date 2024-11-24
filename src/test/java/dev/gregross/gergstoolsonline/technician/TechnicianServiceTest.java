package dev.gregross.gergstoolsonline.technician;

import dev.gregross.gergstoolsonline.system.exception.ObjectNotFoundException;
import dev.gregross.gergstoolsonline.tool.Tool;
import dev.gregross.gergstoolsonline.tool.ToolRepository;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TechnicianServiceTest {

	@Mock
	TechnicianRepository technicianRepository;

	@Mock
	ToolRepository toolRepository;

	@InjectMocks
	TechnicianService technicianService;

	List<Technician> technicians = new ArrayList<>();

	@BeforeEach
	void setUp() {
		Technician t1 = new Technician();
		t1.setId(1);
		t1.setName("Albus Dumbledore");

		Technician t2 = new Technician();
		t2.setId(2);
		t2.setName("Severus Snape");

		technicians.add(t1);
		technicians.add(t2);
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void testFindByIdSuccess() {
		// Given. Arrange inputs and targets. Define the behavior of Mock object technicianRepository.
		Technician t = new Technician();
		t.setId(1);
		t.setName("Albus Dumbledore");

		given(technicianRepository.findById(1)).willReturn(Optional.of(t)); // Define the behavior of the mock object.

		// When. Act on the target behavior. Act steps should cover the method to be tested.
		Technician returnedTechnician = technicianService.findById(1);

		// Then. Assert expected outcomes.
		assertThat(returnedTechnician.getId()).isEqualTo(t.getId());
		assertThat(returnedTechnician.getName()).isEqualTo(t.getName());
		verify(technicianRepository, times(1)).findById(1);
	}

	@Test
	void testFindByIdNotFound() {
		given(technicianRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());

		Throwable exception = catchThrowable(() -> {
			Technician returnedTechnician = technicianService.findById(1);
		});

		assertThat(exception)
			.isInstanceOf(ObjectNotFoundException.class)
			.hasMessage("Could not find technician with id: 1");

		verify(technicianRepository, times(1)).findById(Mockito.any(Integer.class));
	}

	@Test
	void testFindAllSuccess() {
		// given the repository returns a list of technicians
		given(technicianRepository.findAll()).willReturn(technicians);

		// when the service is called
		List<Technician> returnedTechnicians = technicianService.findAll();

		// then the list of technicians is returned
		assertThat(returnedTechnicians).isEqualTo(technicians);

	}

	@Test
	void testSaveSuccess() {
		// given a technician to save
		Technician newTechnician = new Technician();
		newTechnician.setName("Albus Dumbledore");

		// given the repository saves the technician
		given(technicianRepository.save(newTechnician)).willReturn(newTechnician);

		// when the service is called
		Technician returnedTechnician = technicianService.save(newTechnician);

		// then the saved technician is returned
		assertThat(returnedTechnician.getName()).isEqualTo(newTechnician.getName());

		verify(technicianRepository, times(1)).save(newTechnician);
	}

	@Test
	void testUpdateSuccess() {
		// given an existing technician
		Technician oldTechnician = new Technician();
		oldTechnician.setId(1);
		oldTechnician.setName("Albus Dumbledore");

		Technician updateTechnician = new Technician();
		updateTechnician.setName("Albus Dumbledore - updateTechnician");

		given(technicianRepository.findById(1)).willReturn(Optional.of(oldTechnician));
		given(technicianRepository.save(oldTechnician)).willReturn(oldTechnician);

		// when the service is called
		Technician updatedTechnician = technicianService.update(1, updateTechnician);

		// then the updated technician is returned
		assertThat(updatedTechnician.getId()).isEqualTo(1);
		assertThat(updatedTechnician.getName()).isEqualTo(updateTechnician.getName());
		verify(technicianRepository, times(1)).findById(1);
		verify(technicianRepository, times(1)).save(oldTechnician);
	}

	@Test
	void testUpdateNotFound() {
		// given an existing technician
		Technician updateTechnician = new Technician();
		updateTechnician.setName("Albus Dumbledore - update");

		given(technicianRepository.findById(1)).willReturn(Optional.empty());

		// when the service is called
		assertThrows(ObjectNotFoundException.class, () -> {
			technicianService.update(1, updateTechnician);
		});

		// then the updated technician is returned
		verify(technicianRepository, times(1)).findById(1);
	}

	@Test
	void testDeleteSuccess() {
		// Given
		Technician technician = new Technician();
		technician.setId(1);
		technician.setName("Albus Dumbledore");

		given(technicianRepository.findById(1)).willReturn(Optional.of(technician));
		doNothing().when(technicianRepository).deleteById(1);

		// When
		technicianService.delete(1);

		// Then
		verify(technicianRepository, times(1)).deleteById(1);
	}

	@Test
	void testDeleteNotFound() {
		// Given
		given(technicianRepository.findById(1)).willReturn(Optional.empty());

		// When
		assertThrows(ObjectNotFoundException.class, () -> {
			technicianService.delete(1);
		});

		// Then
		verify(technicianRepository, times(1)).findById(1);
	}

	@Test
	void testAssignToolSuccess() {
		// Given
		Tool a = new Tool();
		a.setId("1250808601744904192");
		a.setName("Invisibility Cloak");
		a.setDescription("An invisibility cloak is used to make the wearer invisible.");
		a.setImageUrl("ImageUrl");

		Technician w2 = new Technician();
		w2.setId(2);
		w2.setName("Harry Potter");
		w2.addTool(a);

		Technician w3 = new Technician();
		w3.setId(3);
		w3.setName("Neville Longbottom");

		given(toolRepository.findById("1250808601744904192")).willReturn(Optional.of(a));
		given(technicianRepository.findById(3)).willReturn(Optional.of(w3));

		// When
		technicianService.assignTool(3, "1250808601744904192");

		// Then
		assertThat(a.getPossessor().getId()).isEqualTo(3);
		assertThat(w3.getTools()).contains(a);
	}

	@Test
	void testAssignToolErrorWithNonExistentTechnicianId() {
		// Given
		Tool a = new Tool();
		a.setId("1250808601744904192");
		a.setName("Invisibility Cloak");
		a.setDescription("An invisibility cloak is used to make the wearer invisible.");
		a.setImageUrl("ImageUrl");

		Technician w2 = new Technician();
		w2.setId(2);
		w2.setName("Harry Potter");
		w2.addTool(a);

		given(toolRepository.findById("1250808601744904192")).willReturn(Optional.of(a));
		given(technicianRepository.findById(3)).willReturn(Optional.empty());

		// When
		Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
			technicianService.assignTool(3, "1250808601744904192");
		});

		// Then
		assertThat(thrown)
			.isInstanceOf(ObjectNotFoundException.class)
			.hasMessage("Could not find technician with id: 3");
		assertThat(a.getPossessor().getId()).isEqualTo(2);
	}

	@Test
	void testAssignToolErrorWithNonExistentToolId() {
		// Given
		given(toolRepository.findById("1250808601744904192")).willReturn(Optional.empty());

		// When
		Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
			technicianService.assignTool(3, "1250808601744904192");
		});

		// Then
		assertThat(thrown)
			.isInstanceOf(ObjectNotFoundException.class)
			.hasMessage("Could not find tool with id: 1250808601744904192");
	}




}