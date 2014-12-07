package com.chalet.lskpi.comparator;

import java.util.Comparator;

import com.chalet.lskpi.model.DailyReportData;

public class DailyReportDataWhRateComparator implements Comparator<DailyReportData>{
	
	@Override
	public int compare(DailyReportData o1, DailyReportData o2) {
		Double o1value = o1.getWhRate();
		Double o2value = o2.getWhRate();
		return o2value.compareTo(o1value);
	}
}
