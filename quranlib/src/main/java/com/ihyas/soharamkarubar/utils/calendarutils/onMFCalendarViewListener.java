package com.ihyas.soharamkarubar.utils.calendarutils;


/**
 * @since Jan 8 2014
 * @author Mustafa Ferhan Akman
 * */
public interface onMFCalendarViewListener {

	/**
	 * @param month first month is: 1 (January) and last month is: 12 (Dec)

	 * @param monthStr provides local month name like January (English) or Ocak (Turkish)
	 * */
	void onDisplayedMonthChanged(int month, int year, String monthStr);
	
}
