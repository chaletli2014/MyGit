package com.chalet.lskpi.comparator;

import java.util.Comparator;

import com.chalet.lskpi.model.DailyReportData;

public class DailyReportDataAverageComparator implements Comparator<DailyReportData>{
	
	@Override
	public int compare(DailyReportData o1, DailyReportData o2) {
		Double o1value = o1.getAverageDose();
		Double o2value = o2.getAverageDose();
		return o2value.compareTo(o1value);
	}
}
