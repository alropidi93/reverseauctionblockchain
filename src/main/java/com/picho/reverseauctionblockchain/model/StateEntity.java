package com.picho.reverseauctionblockchain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Table(name="state_entity")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StateEntity {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idStateEntity", nullable = false)
    private Long idStateEntity;

    @Column(name = "name", unique = false, nullable = false, length = 200)
    private String name;

    @Column(name = "code", unique = true, nullable = false, length = 20)
    private String code;

    @Column(name = "description", unique = false, nullable = true, length = 500)
    private String description;


}