package com.picho.reverseauctionblockchain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BidderEntityCreateForm {
    private String code;
    private String name;
    private String ruc;


}