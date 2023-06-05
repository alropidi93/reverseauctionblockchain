package com.picho.reverseauctionblockchain.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuctionCreateForm {
    private String code;
    private String name;
    private String goodServiceCode;
    private String description;
    private Float referenceValue;

}
