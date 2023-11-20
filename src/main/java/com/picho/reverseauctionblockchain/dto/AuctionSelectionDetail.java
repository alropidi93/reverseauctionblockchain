package com.picho.reverseauctionblockchain.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuctionSelectionDetail {
    String code;
    String name;
    String entityName;
    String goodServiceType;
    String datetimeInitConv;
    String datetimeEndConv;
    String datetimeInitFirstBid;
    String datetimeEndFirstBid;
    String datetimeInitPriceBid;
    String datetimeEndPriceBid;
    String datetimeInitBuenaPro;
    String datetimeEndBuenaPro;
    Integer currentPhase;
    Float referenceValue;
    String description;

}
