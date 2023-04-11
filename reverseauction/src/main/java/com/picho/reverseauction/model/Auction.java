package com.picho.reverseauction.model;

import javax.persistence.*;

import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="auction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Auction {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "auction_id")
    private long auctionId;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name="organism_username", nullable = false, unique = true, length = 50)
    private String organismUsername;

    @Column(name="current_phase", nullable = false)
    private int currentPhase;

    @Column(name="reg_first_bid_init_datetime", nullable = false)
    private LocalDateTime regFirstBidInitDatetime;

    @Column(name="reg_first_bid_end_datetime", nullable = false)
    private LocalDateTime regFirstBidEndDatetime;

    @Column(name="bid_improvement_init_datetime", nullable = false)
    private LocalDateTime bidImprovementInitDatetime;

    @Column(name="bid_improvement_end_datetime", nullable = false)
    private LocalDateTime bidImprovementEndDatetime;

    @Column(name="state", nullable = false)
    private int state;

}
