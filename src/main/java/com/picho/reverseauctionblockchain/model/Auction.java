package com.picho.reverseauctionblockchain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @Column(name = "procurement_type", unique = true, nullable = false, length = 200)
    private Integer procurementType;

}
