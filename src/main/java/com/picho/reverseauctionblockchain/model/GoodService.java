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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idGoodService", nullable = false)
    private Long idGoodService;

    @Column(name = "code", unique = true, nullable = false, length = 20)
    private String code;

    @Column(name = "name", nullable = false, length = 400)
    private String name;

    @Column(name = "procurementType",  nullable = false)
    private Integer procurementType;

}