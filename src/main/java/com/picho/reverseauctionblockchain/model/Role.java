package com.picho.reverseauctionblockchain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="role")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    public Role(String roleName){
        this.name = roleName;
    }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idRole", nullable = false)
    private Long idRole;

    @Column(name = "name", unique = true, nullable = false, length = 200)
    private String name;
}
