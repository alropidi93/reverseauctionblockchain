package com.picho.reverseauctionblockchain.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuctionCreateForm {
    private String auctionCode;
    private String auctionName;
    private String procurementType;

}
