package com.picho.reverseauctionblockchain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuctionFistBidDetail {
    private String code;
    private String name;
    private List<AuctionFistBidDetail.Item> items;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item  {
        private Integer order;
        private Long id;
        private String name;
        private Float firstBid;
        private String datetimeReg;


    }
}
