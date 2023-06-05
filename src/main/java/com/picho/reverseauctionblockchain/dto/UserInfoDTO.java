package com.picho.reverseauctionblockchain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO {
    private String fullname;
    private String username;
    private String role;
    private EntityInfo entityInfo;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static abstract class EntityInfo {
        private String name;
        private String code;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StateEntityInfo extends  EntityInfo{
        private String description;

        public String getDescription() {
            return description;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BidderEntityInfo extends  EntityInfo{
        private  String ruc;


    }

}


