package co.edu.unbosque.entrecolbackend.configuration;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.edu.unbosque.entrecolbackend.model.Admin;
import co.edu.unbosque.entrecolbackend.repository.AdminRepository;
import co.edu.unbosque.entrecolbackend.util.AESUtil;



@Configuration
public class LoadDataBase {
	private static final Logger log = LoggerFactory.getLogger(LoadDataBase.class);

	@Bean
	CommandLineRunner initDatabase(AdminRepository adminRepo) {
		
		return args -> {
			Optional<Admin> found = adminRepo.findByUsuario(AESUtil.encrypt("admin"));
			if (found.isPresent()) {
				log.info("Admin already exists,  skipping admin creating  ...");
			} else {
				adminRepo.save(new Admin(AESUtil.encrypt("admin"), AESUtil.encrypt("pendertuga13"),AESUtil.encrypt("sebastiancarroz@gmail.com")));
				log.info("Preloading admin user");
			}
		};
	}

}

