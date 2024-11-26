package dev.gregross.gergstoolsonline.system;

import dev.gregross.gergstoolsonline.appuser.AppUserService;
import dev.gregross.gergstoolsonline.technician.Technician;
import dev.gregross.gergstoolsonline.technician.TechnicianRepository;
import dev.gregross.gergstoolsonline.tool.Tool;
import dev.gregross.gergstoolsonline.tool.ToolRepository;
import dev.gregross.gergstoolsonline.appuser.AppUser;
import dev.gregross.gergstoolsonline.appuser.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DBDataInitializer implements CommandLineRunner {

	private final ToolRepository toolRepository;

	private final TechnicianRepository technicianRepository;

	private final AppUserService appUserService;

	public DBDataInitializer(ToolRepository toolRepository, TechnicianRepository technicianRepository, AppUserService appUserService) {
		this.toolRepository = toolRepository;
		this.technicianRepository = technicianRepository;
		this.appUserService = appUserService;
	}

	@Override
	public void run(String... args) throws Exception {
		Tool a1 = new Tool();
		a1.setId("1250808601744904191");
		a1.setName("Deluminator");
		a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
		a1.setImageUrl("ImageUrl");

		Tool a2 = new Tool();
		a2.setId("1250808601744904192");
		a2.setName("Invisibility Cloak");
		a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
		a2.setImageUrl("ImageUrl");

		Tool a3 = new Tool();
		a3.setId("1250808601744904193");
		a3.setName("Elder Wand");
		a3.setDescription("The Elder Wand, known throughout history as the Deathstick or the Wand of Destiny, is an extremely powerful wand made of elder wood with a core of Thestral tail hair.");
		a3.setImageUrl("ImageUrl");

		Tool a4 = new Tool();
		a4.setId("1250808601744904194");
		a4.setName("The Marauder's Map");
		a4.setDescription("A magical map of Hogwarts created by Remus Lupin, Peter Pettigrew, Sirius Black, and James Potter while they were students at Hogwarts.");
		a4.setImageUrl("ImageUrl");

		Tool a5 = new Tool();
		a5.setId("1250808601744904195");
		a5.setName("The Sword Of Gryffindor");
		a5.setDescription("A goblin-made sword adorned with large rubies on the pommel. It was once owned by Godric Gryffindor, one of the medieval founders of Hogwarts.");
		a5.setImageUrl("ImageUrl");

		Tool a6 = new Tool();
		a6.setId("1250808601744904196");
		a6.setName("Resurrection Stone");
		a6.setDescription("The Resurrection Stone allows the holder to bring back deceased loved ones, in a semi-physical form, and communicate with them.");
		a6.setImageUrl("ImageUrl");

		Technician w1 = new Technician();
		w1.setId(1);
		w1.setName("Albus Dumbledore");
		w1.addTool(a1);
		w1.addTool(a3);

		Technician w2 = new Technician();
		w2.setId(2);
		w2.setName("Harry Potter");
		w2.addTool(a2);
		w2.addTool(a4);

		Technician w3 = new Technician();
		w3.setId(3);
		w3.setName("Neville Longbottom");
		w3.addTool(a5);

		technicianRepository.save(w1);
		technicianRepository.save(w2);
		technicianRepository.save(w3);

		toolRepository.save(a6);

		// Create some users.
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

		appUserService.save(u1);
		appUserService.save(u2);
		appUserService.save(u3);
	}

}