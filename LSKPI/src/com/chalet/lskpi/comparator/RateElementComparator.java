package com.chalet.lskpi.comparator;

import java.util.Comparator;

import com.chalet.lskpi.model.RateElement;

public class RateElementComparator implements Comparator<RateElement>{

	@Override
	public int compare(RateElement o1, RateElement o2) {
		Double o1value = o1.getRateNum();
		Double o2value = o2.getRateNum();
		if( o1value.compareTo(o2value) != 0 ){
			return o2value.compareTo(o1value);
		}else{
			Integer o1type = o1.getRateType();
			Integer o2type = o2.getRateType();
			return o2type.compareTo(o1type);
		}
	}
}
