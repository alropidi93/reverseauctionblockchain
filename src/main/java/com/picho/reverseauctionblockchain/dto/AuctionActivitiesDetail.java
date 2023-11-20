package com.picho.reverseauctionblockchain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuctionActivitiesDetail {
    String code;
    String name;
    String goodServiceType;
    List<CronogramRow> activities;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CronogramRow  {
        private Integer order;
        private String datetimeInit;
        private String datetimeEnd;
        private String activity;
        private Boolean registerFlag;
        private Integer status; // 1:en curso, 2: finalizado, 3: cancelado
    }
}
