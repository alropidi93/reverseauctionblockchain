package com.picho.reverseauctionblockchain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuctionPriceBidDetail {
    private String bidderName;
    private List<AuctionPriceBidDetail.Item> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item  {
        private Integer order;
        private Long id;
        private String name;
        private Float currentBid;
        private Float lastBid;
        private Boolean isOpen;
        private Integer bidState; //ganador:1,perdedor:2, empate:3
        private Boolean isLastAuctionBid; // is this the last bid sent to the current auction
    }
}
