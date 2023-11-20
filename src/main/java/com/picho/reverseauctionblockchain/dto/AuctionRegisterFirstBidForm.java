package com.picho.reverseauctionblockchain.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuctionRegisterFirstBidForm {
    String firstBidRegisterDate;
    Float firstBid;

}
