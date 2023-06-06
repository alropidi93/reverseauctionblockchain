package com.picho.reverseauctionblockchain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.swing.plaf.nimbus.State;

@Entity
@Table(name="auction")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Auction {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idAuction", nullable = false)
    private Long idAuction;

    @Column(name = "code", unique = true, nullable = false, length = 20)
    private String code;

    @Column(name = "name", unique = true, nullable = false, length = 200)
    private String name;

    @Column(name = "description", nullable = true, length = 500)
    private String description;

    @Column(name = "state",  nullable = false)
    private Integer state = 1;

    @Column(name = "referenceValue",  nullable = true)
    private Float referenceValue ;


    @ManyToOne(optional = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="idStateEntity")
    private StateEntity stateEntity;




}
