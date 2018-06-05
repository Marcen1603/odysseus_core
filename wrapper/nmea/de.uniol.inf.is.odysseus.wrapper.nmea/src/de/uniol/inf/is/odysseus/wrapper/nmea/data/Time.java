package de.uniol.inf.is.odysseus.wrapper.nmea.data;

import java.util.Map;

/**
 * Time container holding hours, minutes, seconds and milliseconds. The time is
 * stored in GMT (+0:00).
 * 
 * @author jboger <juergen.boger@offis.de>
 * 
 */
public class Time {
	/** Hours. */
	private int hours;
	/** Minutes. */
	private int minutes;
	/** Seconds. */
	private int seconds;
	/** Milliseconds. */
	private int milliSeconds;

	/** Constructor. */
	public Time() {
	}

	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	public int getSeconds() {
		return seconds;
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}

	public int getMilliSeconds() {
		return milliSeconds;
	}

	public void setMilliSeconds(int milliSeconds) {
		this.milliSeconds = milliSeconds;
	}

	/**
	 * Used by Odysseus to fill a key value map.
	 * 
	 * @param prefix
	 *            Prefix to be used for the key.
	 * @param map
	 *            map to fill.
	 */
	public void addToMap(String prefix, Map<String, Object> map) {
		map.put(prefix + ".hours", hours);
		map.put(prefix + ".minutes", minutes);
		map.put(prefix + ".seconds", seconds);
		map.put(prefix + ".milliSeconds", milliSeconds);
	}
	
	@SuppressWarnings("unchecked")
	public static Time fromMap(String prefix, Map<String, Object> map)
	{
		try
		{
			if (map.containsKey(prefix) && map.get(prefix) instanceof Map)
			{
				// Nested key-value
				map = (Map<String, Object>) map.get(prefix);
				prefix = "";
			}
			else
				prefix += ".";
			
			Time res = new Time();
			res.setHours(((Number)map.get(prefix + "hours")).intValue());
			res.setMinutes(((Number)map.get(prefix + "minutes")).intValue());
			res.setSeconds(((Number)map.get(prefix + "seconds")).intValue());
			res.setMilliSeconds(((Number)map.get(prefix + "milliSeconds")).intValue());
			return res;
		}
		catch (Exception e)
		{
			return null;
		}
	}
}
