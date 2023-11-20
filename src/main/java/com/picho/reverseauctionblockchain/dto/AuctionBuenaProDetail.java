package com.picho.reverseauctionblockchain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuctionBuenaProDetail {
    String itemName;
    String itemCode;
    private Long idAuctionItem;
    Boolean buenaProRegistered;
    List<BidderInfo> bidders;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BidderInfo {
        private String bidderEntityCode;
        private String bidderEntityName;
        private Float bestBid;
        private String bestBidDatetimeReg;
        private Integer ranking;
    }

}

