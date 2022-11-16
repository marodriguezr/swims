package swimsEJB.utilities;

import java.util.Date;
import java.util.List;

public class DateUtilities {
	public static Date findMaxDateOfList(List<Date> dates) {
		if (dates.isEmpty())
			return null;
		Date maxDate = dates.get(0);

		for (Date date : dates) {
			if (date.compareTo(maxDate) > 0) {
				maxDate = date;
			}
		}

		return maxDate;
	}

	public static Date findMinDateOfList(List<Date> dates) {
		if (dates.isEmpty())
			return null;
		Date minDate = dates.get(0);

		for (Date date : dates) {
			if (date.compareTo(minDate) < 0) {
				minDate = date;
			}
		}

		return minDate;
	}
}
