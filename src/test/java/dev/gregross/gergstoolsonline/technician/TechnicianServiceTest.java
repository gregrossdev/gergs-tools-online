package dev.gregross.gergstoolsonline.technician;

import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TechnicianServiceTest {

	@Mock
	TechnicianRepository technicianRepository;

	@InjectMocks
	TechnicianService technicianService;

	@BeforeEach
	void setUp() {
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
			.isInstanceOf(TechnicianNotFoundException.class)
			.hasMessage("Could not find technician with id: 1");

		verify(technicianRepository, times(1)).findById(Mockito.any(Integer.class));
	}
}