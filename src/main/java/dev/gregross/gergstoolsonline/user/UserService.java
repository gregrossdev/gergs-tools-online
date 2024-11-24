package dev.gregross.gergstoolsonline.user;

import dev.gregross.gergstoolsonline.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserService {
	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User findById(Integer userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new ObjectNotFoundException("user", userId));
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public User save(User newUser) {
		return userRepository.save(newUser);
	}

	public User update(Integer userId, User update) {
		User currUser = userRepository.findById(userId)
			.orElseThrow(() -> new ObjectNotFoundException("user", userId));
		currUser.setUsername(update.getUsername());
		currUser.setEnabled(update.isEnabled());
		currUser.setRoles(update.getRoles());

		return userRepository.save(currUser);
	}

	public void delete(Integer userId) {
		userRepository.findById(userId)
			.orElseThrow(() -> new ObjectNotFoundException("user", userId));

		userRepository.deleteById(userId);
	}
}
