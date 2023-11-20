package com.picho.reverseauctionblockchain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuctionRegisterBuenaProForm {
    String datetimeReg;
    private String firstBidderEntityCode;
    private String secondBidderEntityCode;


}
