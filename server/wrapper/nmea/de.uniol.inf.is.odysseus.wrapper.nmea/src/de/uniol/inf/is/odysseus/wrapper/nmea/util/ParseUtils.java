package de.uniol.inf.is.odysseus.wrapper.nmea.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import de.uniol.inf.is.odysseus.wrapper.nmea.data.GpsQuality;
import de.uniol.inf.is.odysseus.wrapper.nmea.data.Hemisphere;
import de.uniol.inf.is.odysseus.wrapper.nmea.data.SignalIntegrity;
import de.uniol.inf.is.odysseus.wrapper.nmea.data.Status;
import de.uniol.inf.is.odysseus.wrapper.nmea.data.Unit;

public class ParseUtils {
	public static final DateFormat UTC_TIME_FORMATTER = new SimpleDateFormat("HHmmss");
	public static final DateFormat UTC_TIME_FORMATTER_MS = new SimpleDateFormat("HHmmss.SSS");
	public static final DateFormat UTC_DATE_FORMATTER = new SimpleDateFormat("ddMMyy");
	static {
		UTC_TIME_FORMATTER.setTimeZone(TimeZone.getTimeZone("GMT"));
		UTC_TIME_FORMATTER_MS.setTimeZone(TimeZone.getTimeZone("GMT"));
		UTC_DATE_FORMATTER.setTimeZone(TimeZone.getTimeZone("GMT"));
	}
	
	public static Double parseCoordinate(String value) {
		try {
			String[] gr = value.split("\\.");
			int d = Integer.parseInt(gr[0].substring(0, gr[0].length() - 2));
			int d1 = Integer.parseInt(gr[0].substring(gr[0].length() - 2, gr[0].length()));
			double d2 = Float.parseFloat("0." + gr[1]);
			Double res = new Double(d + ((d1 + d2) / 60.0));
			return res;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String toString(Double value, int coordLength) {
		double val = value;
		int i1 = (int) val;
		double f1 = (val - i1) * 60;
		int i2 = (int) f1;
		int i3 = (int) Math.round((f1 - i2) * 10000);
		String pattern = "%0" + coordLength + "d%02d.%04d";
		return String.format(pattern, i1, i2, i3);
	}
	
	public static Double parseDouble(String value) {
		try {
			return Double.parseDouble(value);
		} catch (Exception ex) {
			return null;
		}
	}
	
	public static Integer parseInteger(String value) {
		try {
			return Integer.parseInt(value);
		} catch (Exception ex) {
			return null;
		}
	}
	
	public static Hemisphere parseHemisphere(String value) {
		try {
			return Hemisphere.parse(value);
		} catch (Exception e) {
			return Hemisphere.NULL;
		}
	}
	
	public static Unit parseUnit(String value) {
		try {
			return Unit.parse(value);
		} catch (Exception e) {
			return Unit.NULL;
		}
	}
	
	public static GpsQuality parseGpsQuality(String value) {
		try {
			return GpsQuality.parse(value);
		} catch (Exception e) {
			return GpsQuality.INVALID;
		}
	}
	
	public static SignalIntegrity parseSignalIntegrity(String value) {
		try {
			return SignalIntegrity.parse(value);
		} catch (Exception e) {
			return SignalIntegrity.NULL;
		}
	}
	
	public static Status parseStatus(String value) {
		try {
			return Status.parse(value);
		} catch (Exception e) {
			return Status.NULL;
		}
	}
	
	public static Date parseUTCTime(String value) {
		try {
			if (value.contains(".")) {
				return UTC_TIME_FORMATTER_MS.parse(value);
			} else {
				return UTC_TIME_FORMATTER.parse(value);
			}
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Date parseUTCDate(String value) {
		try {
			return UTC_DATE_FORMATTER.parse(value);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String toString(Integer value) {
		if (value == null) {
			return "";
		} else {
			return String.valueOf(value);
		}
	}
	
	public static String toString(Double value) {
		if (value == null) {
			return "";
		} else {
			return String.valueOf(value);
		}
	}
	
	public static String toString(Hemisphere value) {
		return value.getShortName();
	}
	
	public static String toString(Unit value) {
		return value.getShortName();
	}
	
	public static String toString(SignalIntegrity value) {
		return value.getShortName();
	}
	
	public static String toString(GpsQuality value) {
		return String.valueOf(value.getValue());
	}
	
	public static String toString(Status value) {
		return value.getShortName();
	}
	
	public static String getDate(Date value) {
		if (value == null) {
			return "";
		} else {
			return UTC_DATE_FORMATTER.format(value);
		}
	}
	
	public static String getTime(Date value) {
		if (value == null) {
			return "";
		} else {
			if (value.getTime() % 1000 == 0) {
				return UTC_TIME_FORMATTER.format(value);
			} else {
				return UTC_TIME_FORMATTER_MS.format(value);
			}
		}
	}
}
