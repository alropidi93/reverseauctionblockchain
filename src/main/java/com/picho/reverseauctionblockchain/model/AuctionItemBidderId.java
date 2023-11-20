package com.picho.reverseauctionblockchain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuctionItemBidderId implements Serializable {
    private Long idAuction;
    private Long idBidderEntity;
    private Long idAuctionItem;
}
