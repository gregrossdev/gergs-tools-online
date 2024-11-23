package dev.gregross.gergstoolsonline.technician;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TechnicianService {
	private final TechnicianRepository technicianRepository;

	public TechnicianService(TechnicianRepository technicianRepository) {
		this.technicianRepository = technicianRepository;
	}

	public Technician findById(int technicianId) {
		return technicianRepository.findById(technicianId)
			.orElseThrow(() -> new TechnicianNotFoundException(technicianId));
	}
}
