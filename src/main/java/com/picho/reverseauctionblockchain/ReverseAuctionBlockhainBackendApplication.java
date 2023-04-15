package com.picho.reverseauctionblockchain;

import com.picho.reverseauctionblockchain.dto.RabUserRegistrationForm;
import com.picho.reverseauctionblockchain.model.RabUser;
import com.picho.reverseauctionblockchain.model.Role;
import com.picho.reverseauctionblockchain.service.RabUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class ReverseAuctionBlockhainBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReverseAuctionBlockhainBackendApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner run(RabUserService rabUserService){
        return args -> {

			rabUserService.saveRole(new Role("ROLE_ADMIN"));
			rabUserService.saveRole(new Role("ROLE_SELLER"));
			rabUserService.saveRole(new Role("ROLE_COLLECTOR"));
			rabUserService.saveRole(new Role("ROLE_TECHNICAL"));

			rabUserService.saveUser(new RabUserRegistrationForm("alvaro.picho","1234","Alvaro Picho"));
			rabUserService.saveUser(new RabUserRegistrationForm("carolina.picho","1234","Carolina Picho"));
			rabUserService.saveUser(new RabUserRegistrationForm("pierina.picho","1234","Pierina Picho"));
			rabUserService.saveUser(new RabUserRegistrationForm("alonso.villon","1234","Alonso Villon"));
			rabUserService.saveUser(new RabUserRegistrationForm("manuel.picho","1234","Manuel Picho"));

			rabUserService.addRoleToUser("alvaro.picho","ROLE_ADMIN");
			rabUserService.addRoleToUser("alvaro.picho","ROLE_TECHNICAL");
			rabUserService.addRoleToUser("carolina.picho","ROLE_SELLER");
			rabUserService.addRoleToUser("pierina.picho","ROLE_SELLER");
			rabUserService.addRoleToUser("pierina.picho","ROLE_COLLECTOR");
			rabUserService.addRoleToUser("alonso.villon","ROLE_SELLER");
			rabUserService.addRoleToUser("manuel.picho","ROLE_TECHNICAL");
			rabUserService.addRoleToUser("manuel.picho","ROLE_COLLECTOR");
        };
    }
}
