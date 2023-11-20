package com.picho.reverseauctionblockchain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="auction_bidder")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(AuctionBidderEntityId.class)
public class AuctionBidder {

    @Id
    @Column(name="idAuction")
    private Long idAuction;

    @Id
    @Column(name="idBidderEntity")
    private Long idBidderEntity;

    @Column(name = "registerDateTime", nullable = true)
    private LocalDateTime registerDateTime;

    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="idAuction",insertable = false, updatable = false)
    private Auction auction;

    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="idBidderEntity",insertable = false, updatable = false)
    private BidderEntity bidderEntity;
}
