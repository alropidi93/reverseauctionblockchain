package com.picho.reverseauctionblockchain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="auction_item_bidder")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(AuctionItemBidderId.class)
public class AuctionItemBidder {
    @Id
    @Column(name="idAuction")
    private Long idAuction;

    @Id
    @Column(name="idBidderEntity")
    private Long idBidderEntity;

    @Id
    @Column(name="idAuctionItem")
    private Long idAuctionItem;

    @Column(name = "firstBid",  nullable = false)
    private Float firstBid ;

    @Column(name = "firstBidRegisterDatetime",  nullable = false)
    private LocalDateTime firstBidRegisterDatetime ;

    @Column(name = "currentBid",  nullable = true)
    private Float currentBid ;

    @Column(name = "lastBid",  nullable = true)
    private Float lastBid ;

    @Column(name = "score",  nullable = true)
    private Float score ;

    @Column(name = "ranking",  nullable = true)
    private Integer ranking ;

    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="idAuctionItem",insertable = false, updatable = false)
    private AuctionItem auctionItem;

    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumns( {
            @JoinColumn(name="idAuction",insertable = false, updatable = false),
            @JoinColumn(name="idBidderEntity",insertable = false, updatable = false)
    } )
    private AuctionBidder auctionBidder;
}
