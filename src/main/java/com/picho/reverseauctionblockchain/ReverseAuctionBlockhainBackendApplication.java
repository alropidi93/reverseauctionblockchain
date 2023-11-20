package com.picho.reverseauctionblockchain;


import com.picho.reverseauctionblockchain.model.GoodService;
import com.picho.reverseauctionblockchain.model.Role;
import com.picho.reverseauctionblockchain.service.GoodServiceService;
import com.picho.reverseauctionblockchain.service.RabUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


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
	CommandLineRunner run(GoodServiceService goodServiceService, RabUserService rabUserService ){
        return args -> {

			rabUserService.saveRole(new Role("ROLE_ENTITY"));
			rabUserService.saveRole(new Role("ROLE_BIDDER"));

			goodServiceService.saveGoodService(new GoodService("GOOD-001","Cemento A",1000f,"kg",1));
			goodServiceService.saveGoodService(new GoodService("GOOD-002","Cemento Mezcla Especial",1200f,"kg",1));
			goodServiceService.saveGoodService(new GoodService("SERV-001","Seguro de accidentes de transito",3000f,"mensual",2));


        };
    }
}
