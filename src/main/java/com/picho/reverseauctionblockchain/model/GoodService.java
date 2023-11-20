package com.picho.reverseauctionblockchain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="good_service")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodService {

    public GoodService(String code, String name, Float unitPrice,String dimension, Integer procurementType){

        this.code = code;
        this.name = name;
        this.unitPrice = unitPrice;
        this.dimension = dimension;
        this.procurementType = procurementType;
    }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idGoodService", nullable = false)
    private Long idGoodService;

    @Column(name = "code", unique = true, nullable = false, length = 20)
    private String code;

    @Column(name = "name", nullable = false, length = 400)
    private String name;

    @Column(name = "unitPrice", nullable = false)
    private Float unitPrice;

    @Column(name = "dimension", nullable = true, length = 20)
    private String dimension;

    @Column(name = "procurementType",  nullable = false)
    private Integer procurementType;

}