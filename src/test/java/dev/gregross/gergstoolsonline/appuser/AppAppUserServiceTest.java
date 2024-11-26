package dev.gregross.gergstoolsonline.appuser;

import dev.gregross.gergstoolsonline.system.http.exception.ObjectNotFoundException;
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
class AppAppUserServiceTest {

	@Mock
	private AppUserRepository appUserRepository;

	@InjectMocks
	AppUserService appUserService;

	List<AppUser> appUsers = new ArrayList<>();

	@BeforeEach
	void setUp() {
		AppUser u1 = new AppUser();
		u1.setId(1);
		u1.setUsername("john");
		u1.setPassword("123456");
		u1.setEnabled(true);
		u1.setRoles("admin user");

		AppUser u2 = new AppUser();
		u2.setId(2);
		u2.setUsername("eric");
		u2.setPassword("654321");
		u2.setEnabled(true);
		u2.setRoles("user");

		AppUser u3 = new AppUser();
		u3.setId(3);
		u3.setUsername("tom");
		u3.setPassword("qwerty");
		u3.setEnabled(false);
		u3.setRoles("user");

		appUsers.add(u1);
		appUsers.add(u2);
		appUsers.add(u3);
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void testFindByIdSuccess() {
		// set up
		AppUser testAppUser = new AppUser();
		testAppUser.setId(1);
		testAppUser.setUsername("john");
		testAppUser.setPassword("123456");
		testAppUser.setEnabled(true);
		testAppUser.setRoles("admin user");
		// given
		given(appUserRepository.findById(1)).willReturn(Optional.of(testAppUser));

		// when
		AppUser returnedAppUser = appUserService.findById(1);

		// then
		assertThat(returnedAppUser.getId()).isEqualTo(testAppUser.getId());
		assertThat(returnedAppUser.getUsername()).isEqualTo(testAppUser.getUsername());
		assertThat(returnedAppUser.getPassword()).isEqualTo(testAppUser.getPassword());
		assertThat(returnedAppUser.isEnabled()).isEqualTo(testAppUser.isEnabled());
		assertThat(returnedAppUser.getRoles()).isEqualTo(testAppUser.getRoles());
		verify(appUserRepository, times(1)).findById(1);

	}

//	@Test
//	void testFindByIdNotFound() {
//		// given
//		given(appUserRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());
//
//		// when
//		Throwable exception = catchThrowable(() -> {
//			AppUser returnedAppUser = appUserService.findById(1);
//		});
//
//		// then
//		assertThat(exception)
//			.isInstanceOf(ObjectNotFoundException.class)
//			.hasMessage("Could not find user with id: 1");
//
//		verify(appUserRepository, times(1)).findById(Mockito.any(Integer.class));
//	}
//
//	@Test
//	void testFindAllSuccess() {
//		// given
//		given(appUserRepository.findAll()).willReturn(appUsers);
//
//		// then
//		List<AppUser> actualAppUsers = appUserService.findAll();
//
//		// when
//		assertThat(appUsers.size()).isEqualTo(actualAppUsers.size());
//
//		verify(appUserRepository, times(1)).findAll();
//	}
//
//	@Test
//	void testSaveSuccess() {
//		// setup object
//		AppUser newAppUser = new AppUser();
//		newAppUser.setUsername("username");
//		newAppUser.setPassword("password");
//		newAppUser.setEnabled(true);
//		newAppUser.setRoles("user");
//		// given
//		given(appUserRepository.save(newAppUser)).willReturn(newAppUser);
//
//		// when
//		AppUser returnedAppUser = appUserService.save(newAppUser);
//
//		// then
//		assertThat(returnedAppUser.getUsername()).isEqualTo(newAppUser.getUsername());
//		assertThat(returnedAppUser.getPassword()).isEqualTo(newAppUser.getPassword());
//		assertThat(returnedAppUser.isEnabled()).isEqualTo(newAppUser.isEnabled());
//		assertThat(returnedAppUser.getRoles()).isEqualTo(newAppUser.getRoles());
//
//		verify(appUserRepository, times(1)).save(newAppUser);
//
//	}
//
//	@Test
//	void testUpdateSuccess() {
//		// given
//		AppUser currAppUser = new AppUser();
//		currAppUser.setId(1);
//		currAppUser.setUsername("john");
//		currAppUser.setPassword("123456");
//		currAppUser.setEnabled(true);
//		currAppUser.setRoles("admin user");
//
//		AppUser update = new AppUser();
//		update.setUsername("john - update");
//		update.setPassword("123456");
//		update.setEnabled(true);
//		update.setRoles("admin user");
//
//		given(appUserRepository.findById(1)).willReturn(Optional.of(currAppUser));
//		given(appUserRepository.save(currAppUser)).willReturn(currAppUser);
//
//		// when
//		AppUser updatedAppUser = appUserService.update(1, update);
//
//		// then
//		assertThat(updatedAppUser.getId()).isEqualTo(1);
//		assertThat(updatedAppUser.getUsername()).isEqualTo(update.getUsername());
//		verify(this.appUserRepository, times(1)).findById(1);
//		verify(this.appUserRepository, times(1)).save(currAppUser);
//
//	}
//
//	@Test
//	void testUpdateNotFound() {
//		// given
//		AppUser update = new AppUser();
//		update.setUsername("john - update");
//		update.setPassword("123456");
//		update.setEnabled(true);
//		update.setRoles("admin user");
//
//		given(appUserRepository.findById(1)).willReturn(Optional.empty());
//
//		// then
//		Throwable exception = assertThrows(ObjectNotFoundException.class, () -> {
//			appUserService.update(1, update);
//		});
//
//		// when
//		assertThat(exception)
//			.isInstanceOf(ObjectNotFoundException.class)
//			.hasMessage("Could not find user with id: 1");
//
//		verify(appUserRepository, times(1)).findById(1);
//	}
//
//	@Test
//	void testDeleteSuccess() {
//		// Given
//		AppUser appUser = new AppUser();
//		appUser.setId(1);
//		appUser.setUsername("john");
//		appUser.setPassword("123456");
//		appUser.setEnabled(true);
//		appUser.setRoles("admin user");
//
//		given(this.appUserRepository.findById(1)).willReturn(Optional.of(appUser));
//		doNothing().when(this.appUserRepository).deleteById(1);
//
//		// When
//		this.appUserService.delete(1);
//
//		// Then
//		verify(this.appUserRepository, times(1)).deleteById(1);
//	}
//
//	@Test
//	void testDeleteNotFound() {
//		// Given
//		given(this.appUserRepository.findById(1)).willReturn(Optional.empty());
//
//		// When
//		Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
//			this.appUserService.delete(1);
//		});
//
//		// Then
//		assertThat(thrown)
//			.isInstanceOf(ObjectNotFoundException.class)
//			.hasMessage("Could not find user with id: 1");
//		verify(this.appUserRepository, times(1)).findById(1);
//	}
}