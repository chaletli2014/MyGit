package com.chalet.lskpi.comparator;

import java.util.Comparator;

import com.chalet.lskpi.model.DailyReportData;

public class DailyReportDataInRateComparator implements Comparator<DailyReportData>{

	@Override
	public int compare(DailyReportData o1, DailyReportData o2) {
		Double o1value = o1.getInRate();
		Double o2value = o2.getInRate();
		return o2value.compareTo(o1value);
	}
}