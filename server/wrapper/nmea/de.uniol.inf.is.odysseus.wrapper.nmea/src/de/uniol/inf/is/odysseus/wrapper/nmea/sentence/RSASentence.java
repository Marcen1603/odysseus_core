package de.uniol.inf.is.odysseus.wrapper.nmea.sentence;

import java.util.Map;

import de.uniol.inf.is.odysseus.wrapper.nmea.data.Status;
import de.uniol.inf.is.odysseus.wrapper.nmea.util.ParseUtils;

/**
 * RSA - Rudder Sensor Angle<br>
 * <br>
 * 
 * <pre>
 * {@code
 * .      1   2 3   4 5
 *        |   | |   | |
 * $--RSA,x.x,A,x.x,A*hh
 * }
 * </pre>
 * <ol>
 * <li>Starboard (or single) rudder sensor, "-" means Turn To Port</li>
 * <li>Status, A means data is valid</li>
 * <li>Port rudder sensor</li>
 * <li>Status, A means data is valid</li>
 * <li>Checksum</li>
 * </ol>
 * 
 * @author aoppermann <alexander.oppermann@offis.de>
 *
 */

public class RSASentence extends Sentence{
	/** Default begin char for this sentence type. */
	public static final char BEGIN_CHAR = '$';
	/** Default talker for this sentence. */
	public static final String DEFAULT_TALKER = "IN";
	/** Sentence id. */
	public static final String SENTENCE_ID = "RSA";
	/** Default count of fields. */
	public static final int FIELD_COUNT = 4;
	
	/** Starboard (or single) rudder sensor, "-" means Turn To Port. */
	private Double starboard;
	/** Status, A means data is valid. */
	private Status sbStatus;
	/** Port rudder sensor. */
	private Double portboard;
	/** Status, A means data is valid. */
	private Status pbStatus;
	
	
	/**
	 * Default constructor for writing. Empty Sentence to fill attributes and
	 * call {@link #toNMEA()}.
	 */
	public RSASentence() {
		super(BEGIN_CHAR, DEFAULT_TALKER, SENTENCE_ID, FIELD_COUNT);
	}

	/**
	 * Default constructor for parsing.
	 * 
	 * @param nmea
	 *            Nmea String to be parsed.
	 */
	public RSASentence(String nmea) {
		super(nmea);
	}
	
	@Override
	protected void decode() {
		int index = 0;
		starboard = ParseUtils.parseDouble(getValue(index++));
		sbStatus = ParseUtils.parseStatus(getValue(index++));
		portboard = ParseUtils.parseDouble(getValue(index++));
		pbStatus = ParseUtils.parseStatus(getValue(index++));
	}

	@Override
	protected void encode() {
		int index = 0;
		setValue(index++, ParseUtils.toString(starboard));
		setValue(index++, ParseUtils.toString(sbStatus));
		setValue(index++, ParseUtils.toString(portboard));
		setValue(index++, ParseUtils.toString(pbStatus));
	}

	@Override
	protected void fillMap(Map<String, Object> res) {
		if (starboard != null) res.put("starboard", starboard);
		if (sbStatus != null) res.put("sbStatus", sbStatus);
		if (portboard != null) res.put("portboard", portboard);
		if (pbStatus != null) res.put("pbStatus", pbStatus);
	}

	public Double getStarboard(){
		return starboard;
	}
	
	public void setStarboard(Double starboard){
		this.starboard = starboard;
	}
	
	public Status getStarboardStatus(){
		return sbStatus;
	}
	
	public void setStarboardStatus(Status sbStatus){
		this.sbStatus = sbStatus;
	}
	
	public Double getPortboard(){
		return portboard;
	}
	
	public void setPortboard(Double portboard){
		this.portboard = portboard;
	}
	
	public Status getPortboardStatus(){
		return pbStatus;
	}
	
	public void setPortboardStatus(Status pbStatus){
		this.pbStatus = pbStatus;
	}
}
