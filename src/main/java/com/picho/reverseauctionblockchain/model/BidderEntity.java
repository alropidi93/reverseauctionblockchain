package com.picho.reverseauctionblockchain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="bidder_entity")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BidderEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idBidderEntity", nullable = false)
    private Long idBidderEntity;

    @Column(name = "name", unique = false, nullable = false, length = 200)
    private String name;

    @Column(name = "code", unique = true, nullable = false, length = 20)
    private String code;

    @Column(name = "ruc", unique = false, nullable = true, length = 20)
    private String ruc;


}
