package com.picho.reverseauctionblockchain.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="auction_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuctionItem {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idAuctionItem", nullable = false)
    private Long idAuctionItem;

    @Column(name = "name", unique = true, nullable = false, length = 300)
    private String name;

    @Column(name = "bestValue",  nullable = true)
    private Float bestValue ;

    @Column(name = "quantity",  nullable = false)
    private Float quantity ;


    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="idAuction")
    private Auction auction;

    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="idGoodService")
    private GoodService goodServiceType;

}
