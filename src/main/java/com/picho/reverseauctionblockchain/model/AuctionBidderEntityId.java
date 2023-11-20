package com.picho.reverseauctionblockchain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuctionBidderEntityId implements Serializable {
    private Long idAuction;
    private Long idBidderEntity;

    /*@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuctionBidderEntityId id = (AuctionBidderEntityId) o;
        return auction.getIdAuction() == id.getAuction().getIdAuction() &&
                bidderEntity.getIdBidderEntity() == id.getBidderEntity().getIdBidderEntity();
    }

    @Override
    public int hashCode() {
        return Objects.hash(auction.getIdAuction(), bidderEntity.getIdBidderEntity());
    }*/

}
