package com.picho.reverseauctionblockchain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="rab_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RabUser {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idUser", nullable = false)
    private Long idUser;

    @Column(name = "username", unique = true, nullable = false, length = 200)
    private String username;

    @Column(name="password", nullable = false, length = 500)
    private String password;

    @Column(name="fullname", nullable = false, length = 200)
    private String fullname;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles = new ArrayList<>();
}
