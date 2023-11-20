package com.picho.reverseauctionblockchain.dto;

import java.util.Comparator;

public class SortBidderInfo implements Comparator<AuctionBuenaProDetail.BidderInfo> {

    public int compare(AuctionBuenaProDetail.BidderInfo a, AuctionBuenaProDetail.BidderInfo b) {

        if (a.getRanking() == 1) return -1;
        else if(b.getRanking() == 1) return 1;
        else if(a.getRanking()== 2 && b.getRanking()!=1 ) return -1;
        else if (a.getRanking() == 2 && b.getRanking() == 1) return 1;
        else if (a.getRanking()!=1 && b.getRanking() == 2) return 1;

        int res;
        if (a.getBestBid() > b.getBestBid())
            res = 1;
        else if (a.getBestBid() == b.getBestBid()){
            res = a.getBestBidDatetimeReg().compareTo(b.getBestBidDatetimeReg()) ;
        }
        else res = -1;


        return res;
    }
}
