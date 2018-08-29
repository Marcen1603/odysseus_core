package de.uniol.inf.is.odysseus.wrapper.nmea.sentence;

import java.util.Map;

import de.uniol.inf.is.odysseus.wrapper.nmea.data.Time;
import de.uniol.inf.is.odysseus.wrapper.nmea.util.ParseUtils;

/**
 * ZDA - Date and Time<br>
 * <br>
 * 
 * 
 * <pre>
 * {@code
 * .      1         2  3  4    5  6  7
 *        |         |  |  |    |  |  |
 * $--GLL,hhmmss.ss,dd,mm,yyyy,xx,yy*CC
 * }
 * </pre>
 * <ol>
 * <li>Time (UTC)</li>
 * <li>Day</li>
 * <li>Month</li>
 * <li>Year</li>
 * <li>Local zone hours -13..13</li>
 * <li>Local zone minutes 0..59</li>
 * <li>Checksum</li>
 * </ol>
 * 
 * @author hsurm <henrik.surm@uni-oldenburg.de>
 * 
 */
public class ZDASentence extends Sentence {
	/** Default begin char for this sentence type. */
	public static final char BEGIN_CHAR = '$';
	/** Default talker for this sentence. */
	public static final String DEFAULT_TALKER = "IN";
	/** Sentence id. */
	public static final String SENTENCE_ID = "ZDA";
	/** Default count of fields. */
	public static final int FIELD_COUNT = 6;

	private Time time;		// UTC
	private Integer day;
	private Integer month;
	private Integer year;
	private Integer localZoneHours;
	private Integer localZoneMinutes;

	/**
	 * Default constructor for writing. Empty Sentence to fill attributes and
	 * call {@link #toNMEA()}.
	 */
	public ZDASentence() {
		super(BEGIN_CHAR, DEFAULT_TALKER, SENTENCE_ID, FIELD_COUNT);
	}

	/**
	 * Default constructor for parsing.
	 * 
	 * @param nmea
	 *            Nmea String to be parsed.
	 */
	public ZDASentence(String nmea) {
		super(nmea);
	}

	/**
	 * Constructor for creating a message from a map. Reverse function of fillMap()
	 * 
	 * @param source
	 *            Map containing specific keys.
	 */	
	public ZDASentence(Map<String, Object> source)	
	{
		super(source, FIELD_COUNT);
		
		time = Time.fromMap("time", source);		
		if (source.containsKey("localZoneHours"))	localZoneHours = ((Number) source.get("localZoneHours")).intValue();
		if (source.containsKey("localZoneMinutes")) localZoneMinutes = ((Number) source.get("localZoneMinutes")).intValue();
		if (source.containsKey("year")) year = ((Number) source.get("year")).intValue();
		if (source.containsKey("month")) month = ((Number) source.get("month")).intValue();
		if (source.containsKey("day")) day = ((Number) source.get("day")).intValue();
	}		
	
	@Override
	protected void decode() {
		int index = 0;
		time = ParseUtils.parseTime(getValue(index++));
		localZoneHours = ParseUtils.parseInteger(getValue(index++));
		localZoneMinutes = ParseUtils.parseInteger(getValue(index++));
		year = ParseUtils.parseInteger(getValue(index++));
		month = ParseUtils.parseInteger(getValue(index++));
		day = ParseUtils.parseInteger(getValue(index++));
	}

	@Override
	protected void encode() {
		int index = 0;
		setValue(index++, ParseUtils.toString(time));
		setValue(index++, ParseUtils.toString(localZoneHours));
		setValue(index++, ParseUtils.toString(localZoneMinutes));		
		setValue(index++, ParseUtils.toString(year));
		setValue(index++, ParseUtils.toString(month));
		setValue(index++, ParseUtils.toString(day));
	}

	@Override
	protected void fillMap(Map<String, Object> res) {
		if (time != null) time.addToMap("time", res);
		if (day != null) res.put("day", day);
		if (month != null) res.put("month", month);
		if (year != null) res.put("year", year);
		if (localZoneHours != null) res.put("localZoneHours", localZoneHours);
		if (localZoneMinutes != null) res.put("localZoneMinutes", localZoneMinutes);
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getLocalZoneHours() {
		return localZoneHours;
	}

	public void setLocalZoneHours(Integer localZoneHours) {
		this.localZoneHours = localZoneHours;
	}

	public Integer getLocalZoneMinutes() {
		return localZoneMinutes;
	}

	public void setLocalZoneMinutes(Integer localZoneMinutes) {
		this.localZoneMinutes = localZoneMinutes;
	}
}
