package dev.gregross.gergstoolsonline.appuser;

import dev.gregross.gergstoolsonline.system.http.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class AppUserService implements UserDetailsService {

	private final AppUserRepository appUserRepository;

	private final PasswordEncoder passwordEncoder;

	public AppUserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
		this.appUserRepository = appUserRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public AppUser findById(Integer userId) {
		return appUserRepository.findById(userId)
			.orElseThrow(() -> new ObjectNotFoundException("user", userId));
	}

	public List<AppUser> findAll() {
		return appUserRepository.findAll();
	}

	public AppUser save(AppUser newAppUser) {
		newAppUser.setPassword(passwordEncoder.encode(newAppUser.getPassword()));
		return appUserRepository.save(newAppUser);
	}

	public AppUser update(Integer userId, AppUser update) {
		AppUser currAppUser = appUserRepository.findById(userId)
			.orElseThrow(() -> new ObjectNotFoundException("user", userId));
		currAppUser.setUsername(update.getUsername());
		currAppUser.setEnabled(update.isEnabled());
		currAppUser.setRoles(update.getRoles());

		return appUserRepository.save(currAppUser);
	}

	public void delete(Integer userId) {
		appUserRepository.findById(userId)
			.orElseThrow(() -> new ObjectNotFoundException("user", userId));

		appUserRepository.deleteById(userId);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return appUserRepository.findByUsername(username) // First, we need to find this user from database.
			.map(appUser -> new MyUserPrincipal(appUser)) // If found, wrap the returned user instance in a MyUserPrincipal instance.
			.orElseThrow(() -> new UsernameNotFoundException("username " + username + " is not found.")); // Otherwise, throw an exception.
	}
}
