package com.picho.reverseauctionblockchain.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RabUserRegistrationForm {
    private String username;
    private String password;
    private String fullname;

}
