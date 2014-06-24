package com.chalet.lskpi.comparator;

import java.util.Comparator;

import com.chalet.lskpi.model.HospitalSalesQueryObj;
import com.chalet.lskpi.model.HospitalSalesQueryParam;

public class HospitalSalesDataComparator implements Comparator<HospitalSalesQueryObj> {
    
    private HospitalSalesQueryParam queryParam;
    
    public HospitalSalesDataComparator(HospitalSalesQueryParam queryParam){
        this.queryParam = queryParam;
    }

    public int compare(HospitalSalesQueryObj o1, HospitalSalesQueryObj o2) {
        String orderParam = queryParam.getOrderParam();
        String order = queryParam.getOrder();
        String orderType = queryParam.getOrderType();
        
        switch(orderParam){
            case "pnum":
                if( "val".equalsIgnoreCase(orderType) ){
                    Integer pnum_1 = o1.getPnum();
                    Integer pnum_2 = o2.getPnum();
                    if( "asc".equalsIgnoreCase(order) ){
                        return pnum_1.compareTo(pnum_2);
                    }else{
                        return pnum_2.compareTo(pnum_1);
                    }
                }else{
                    Double pnumRatio_1 = o1.getPnumRatio();
                    Double pnumRatio_2 = o2.getPnumRatio();
                    if( "asc".equalsIgnoreCase(order) ){
                        return pnumRatio_1.compareTo(pnumRatio_2);
                    }else{
                        return pnumRatio_2.compareTo(pnumRatio_1);
                    }
                }
            case "lsnum":
                if( "val".equalsIgnoreCase(orderType) ){
                    Integer lsnum_1 = o1.getLsnum();
                    Integer lsnum_2 = o2.getLsnum();
                    if( "asc".equalsIgnoreCase(order) ){
                        return lsnum_1.compareTo(lsnum_2);
                    }else{
                        return lsnum_2.compareTo(lsnum_1);
                    }
                }else{
                    Double lsnumRatio_1 = o1.getLsnumRatio();
                    Double lsnumRatio_2 = o2.getLsnumRatio();
                    if( "asc".equalsIgnoreCase(order) ){
                        return lsnumRatio_1.compareTo(lsnumRatio_2);
                    }else{
                        return lsnumRatio_2.compareTo(lsnumRatio_1);
                    }
                }
            case "whRate":
                if( "val".equalsIgnoreCase(orderType) ){
                    Double whRate_1 = o1.getWhRate();
                    Double whRate_2 = o2.getWhRate();
                    if( "asc".equalsIgnoreCase(order) ){
                        return whRate_1.compareTo(whRate_2);
                    }else{
                        return whRate_2.compareTo(whRate_1);
                    }
                }else{
                    Double whRateRatio_1 = o1.getWhRateRatio();
                    Double whRateRatio_2 = o2.getWhRateRatio();
                    if( "asc".equalsIgnoreCase(order) ){
                        return whRateRatio_1.compareTo(whRateRatio_2);
                    }else{
                        return whRateRatio_2.compareTo(whRateRatio_1);
                    }
                }
            case "aveDose":
                if( "val".equalsIgnoreCase(orderType) ){
                    Double aveDose_1 = o1.getAveDose();
                    Double aveDose_2 = o2.getAveDose();
                    if( "asc".equalsIgnoreCase(order) ){
                        return aveDose_1.compareTo(aveDose_2);
                    }else{
                        return aveDose_2.compareTo(aveDose_1);
                    }
                }else{
                    Double aveDoseRatio_1 = o1.getAveDoseRatio();
                    Double aveDoseRatio_2 = o2.getAveDoseRatio();
                    if( "asc".equalsIgnoreCase(order) ){
                        return aveDoseRatio_1.compareTo(aveDoseRatio_2);
                    }else{
                        return aveDoseRatio_2.compareTo(aveDoseRatio_1);
                    }
                }
        }
        return 0;
    }

}
