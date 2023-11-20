package com.picho.reverseauctionblockchain.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuctionCreateForm {
    private String code;
    private String name;
    private List<Item> goodServices;
    private String description;
    private Float referenceValue;
    private String datetimeInitFirstBid;
    private String datetimeEndFirstBid;
    private String datetimeInitPriceBid;
    private String datetimeEndPriceBid;
    private String datetimeInitBuenaPro;
    private String datetimeEndBuenaPro;
    private String datetimeReg;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item  {
        private String code;
        private Float quantity;
        private Float subtotal;


    }
}
