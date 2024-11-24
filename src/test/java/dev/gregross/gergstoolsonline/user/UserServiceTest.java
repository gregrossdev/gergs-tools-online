package dev.gregross.gergstoolsonline.user;

import dev.gregross.gergstoolsonline.system.exception.ObjectNotFoundException;
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
class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	UserService userService;

	List<User> users = new ArrayList<>();

	@BeforeEach
	void setUp() {
		User u1 = new User();
		u1.setId(1);
		u1.setUsername("john");
		u1.setPassword("123456");
		u1.setEnabled(true);
		u1.setRoles("admin user");

		User u2 = new User();
		u2.setId(2);
		u2.setUsername("eric");
		u2.setPassword("654321");
		u2.setEnabled(true);
		u2.setRoles("user");

		User u3 = new User();
		u3.setId(3);
		u3.setUsername("tom");
		u3.setPassword("qwerty");
		u3.setEnabled(false);
		u3.setRoles("user");

		users.add(u1);
		users.add(u2);
		users.add(u3);
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void testFindByIdSuccess() {
		// set up
		User testUser = new User();
		testUser.setId(1);
		testUser.setUsername("john");
		testUser.setPassword("123456");
		testUser.setEnabled(true);
		testUser.setRoles("admin user");
		// given
		given(userRepository.findById(1)).willReturn(Optional.of(testUser));

		// when
		User returnedUser = userService.findById(1);

		// then
		assertThat(returnedUser.getId()).isEqualTo(testUser.getId());
		assertThat(returnedUser.getUsername()).isEqualTo(testUser.getUsername());
		assertThat(returnedUser.getPassword()).isEqualTo(testUser.getPassword());
		assertThat(returnedUser.isEnabled()).isEqualTo(testUser.isEnabled());
		assertThat(returnedUser.getRoles()).isEqualTo(testUser.getRoles());
		verify(userRepository, times(1)).findById(1);

	}

	@Test
	void testFindByIdNotFound() {
		// given
		given(userRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());

		// when
		Throwable exception = catchThrowable(() -> {
			User returnedUser = userService.findById(1);
		});

		// then
		assertThat(exception)
			.isInstanceOf(ObjectNotFoundException.class)
			.hasMessage("Could not find user with id: 1");

		verify(userRepository, times(1)).findById(Mockito.any(Integer.class));
	}

	@Test
	void testFindAllSuccess() {
		// given
		given(userRepository.findAll()).willReturn(users);

		// then
		List<User> actualUsers = userService.findAll();

		// when
		assertThat(users.size()).isEqualTo(actualUsers.size());

		verify(userRepository, times(1)).findAll();
	}

	@Test
	void testSaveSuccess() {
		// setup object
		User newUser = new User();
		newUser.setUsername("username");
		newUser.setPassword("password");
		newUser.setEnabled(true);
		newUser.setRoles("user");
		// given
		given(userRepository.save(newUser)).willReturn(newUser);

		// when
		User returnedUser = userService.save(newUser);

		// then
		assertThat(returnedUser.getUsername()).isEqualTo(newUser.getUsername());
		assertThat(returnedUser.getPassword()).isEqualTo(newUser.getPassword());
		assertThat(returnedUser.isEnabled()).isEqualTo(newUser.isEnabled());
		assertThat(returnedUser.getRoles()).isEqualTo(newUser.getRoles());

		verify(userRepository, times(1)).save(newUser);

	}

	@Test
	void testUpdateSuccess() {
		// given
		User currUser = new User();
		currUser.setId(1);
		currUser.setUsername("john");
		currUser.setPassword("123456");
		currUser.setEnabled(true);
		currUser.setRoles("admin user");

		User update = new User();
		update.setUsername("john - update");
		update.setPassword("123456");
		update.setEnabled(true);
		update.setRoles("admin user");

		given(userRepository.findById(1)).willReturn(Optional.of(currUser));
		given(userRepository.save(currUser)).willReturn(currUser);

		// when
		User updatedUser = userService.update(1, update);

		// then
		assertThat(updatedUser.getId()).isEqualTo(1);
		assertThat(updatedUser.getUsername()).isEqualTo(update.getUsername());
		verify(this.userRepository, times(1)).findById(1);
		verify(this.userRepository, times(1)).save(currUser);
		
	}

	@Test
	void testUpdateNotFound() {
		// given
		User update = new User();
		update.setUsername("john - update");
		update.setPassword("123456");
		update.setEnabled(true);
		update.setRoles("admin user");

		given(userRepository.findById(1)).willReturn(Optional.empty());

		// then
		Throwable exception = assertThrows(ObjectNotFoundException.class, () -> {
			userService.update(1, update);
		});

		// when
		assertThat(exception)
			.isInstanceOf(ObjectNotFoundException.class)
			.hasMessage("Could not find user with id: 1");

		verify(userRepository, times(1)).findById(1);
	}

	@Test
	void testDeleteSuccess() {
		// Given
		User user = new User();
		user.setId(1);
		user.setUsername("john");
		user.setPassword("123456");
		user.setEnabled(true);
		user.setRoles("admin user");

		given(this.userRepository.findById(1)).willReturn(Optional.of(user));
		doNothing().when(this.userRepository).deleteById(1);

		// When
		this.userService.delete(1);

		// Then
		verify(this.userRepository, times(1)).deleteById(1);
	}

	@Test
	void testDeleteNotFound() {
		// Given
		given(this.userRepository.findById(1)).willReturn(Optional.empty());

		// When
		Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
			this.userService.delete(1);
		});

		// Then
		assertThat(thrown)
			.isInstanceOf(ObjectNotFoundException.class)
			.hasMessage("Could not find user with id: 1");
		verify(this.userRepository, times(1)).findById(1);
	}
}