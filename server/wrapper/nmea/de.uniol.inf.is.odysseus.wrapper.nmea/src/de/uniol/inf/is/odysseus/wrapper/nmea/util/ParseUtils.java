package de.uniol.inf.is.odysseus.wrapper.nmea.util;

import de.uniol.inf.is.odysseus.wrapper.nmea.data.Date;
import de.uniol.inf.is.odysseus.wrapper.nmea.data.GpsQuality;
import de.uniol.inf.is.odysseus.wrapper.nmea.data.Hemisphere;
import de.uniol.inf.is.odysseus.wrapper.nmea.data.SignalIntegrity;
import de.uniol.inf.is.odysseus.wrapper.nmea.data.Status;
import de.uniol.inf.is.odysseus.wrapper.nmea.data.Time;
import de.uniol.inf.is.odysseus.wrapper.nmea.data.Unit;

/**
 * The parse utils provides some methods to parse enum values or other type of
 * values from a String.
 * 
 * @author jboger <juergen.boger@offis.de>
 * 
 */
public class ParseUtils {
	/**
	 * Parses a given String to a Date.
	 * 
	 * @param value
	 *            String.
	 * @return The date.
	 */
	public static Date parseDate(String value) {
		Date res = new Date();
		try {
			res.setYear(Integer.parseInt(value.substring(0, 2)));
			res.setMonth(Integer.parseInt(value.substring(2, 4)));
			res.setDay(Integer.parseInt(value.substring(4, 6)));
		} catch (Exception e) {
			return null;
		}
		return res;
	}
	
	/**
	 * Converts the given date to a String.
	 * 
	 * @param value
	 *            Date value.
	 * @return String representation in nmea.
	 */
	public static String toString(Date value) {
		return String.format("%02d%02d%02d", value.getYear(), value.getMonth(), value.getDay());
	}
	
	/**
	 * Parses a given String to a Time.
	 * 
	 * @param value
	 *            String.
	 * @return The time.
	 */
	public static Time parseTime(String value) {
		Time res = new Time();
		try {
			if (value.contains(".")) {
				String[] s = value.split("\\.");
				res.setHours(Integer.parseInt(s[0].substring(0, 2)));
				res.setMinutes(Integer.parseInt(s[0].substring(2, 4)));
				res.setSeconds(Integer.parseInt(s[0].substring(4, 6)));
				res.setMilliSeconds(Integer.parseInt(s[1]));
			} else {
				res.setHours(Integer.parseInt(value.substring(0, 2)));
				res.setMinutes(Integer.parseInt(value.substring(2, 4)));
				res.setSeconds(Integer.parseInt(value.substring(4, 6)));
			}
		} catch (Exception e) {
			return null;
		}
		return res;
	}
	
	/**
	 * Converts the given time to a String.
	 * 
	 * @param value
	 *            Time value.
	 * @return String representation in nmea.
	 */
	public static String toString(Time value) {
		if (value.getMilliSeconds() > 0) { 
			return String.format("%02d%02d%02d.%03d", value.getHours(), value.getMinutes(), value.getSeconds(), value.getMilliSeconds());
		} else {
			return String.format("%02d%02d%02d", value.getHours(), value.getMinutes(), value.getSeconds());
		}
	}
	
	/**
	 * Parses a given String value and calculates the coordinate in decimal
	 * representation.
	 * 
	 * @param value
	 *            String from nmea.
	 * @return Double value of the coordinate.
	 */
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
	
	/**
	 * Converts a given double value to a nmea String representation.
	 * 
	 * @param value
	 *            The decimal value.
	 * @param coordLength
	 *            Use 2 for latitude and 3 for longitude.
	 * @return String representation in nmea.
	 */
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
}
