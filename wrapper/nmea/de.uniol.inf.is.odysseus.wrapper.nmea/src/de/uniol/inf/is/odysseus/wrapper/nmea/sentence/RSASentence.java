package de.uniol.inf.is.odysseus.wrapper.nmea.sentence;

import java.util.Map;

import de.uniol.inf.is.odysseus.wrapper.nmea.data.SignalIntegrity;
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
	public static final int FIELD_COUNT = 5;
	
	/** Starboard (or single) rudder sensor, "-" means Turn To Port. */
	private Double starboardside;
	/** Status, A means data is valid. */
	private Status sbStatus;
	/** Port rudder sensor. */
	private Double portside;
	/** Status, A means data is valid. */
	private Status pbStatus;
	/** Checksum */
	private SignalIntegrity signalIntegrity;
	
	
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
	
	/**
	 * Constructor for creating a message from a map. Reverse function of fillMap()
	 * 
	 * @param source
	 *            Map containing specific keys.
	 */		
	public RSASentence(Map<String, Object> source)	
	{
		super(source, FIELD_COUNT);
		
		if (source.containsKey("starboardside")) starboardside = (double) source.get("starboardside");
		if (source.containsKey("sbStatus")) sbStatus = ParseUtils.parseStatus((String) source.get("sbStatus"));
		if (source.containsKey("portside")) portside = (double) source.get("portside");
		if (source.containsKey("pbStatus")) pbStatus = ParseUtils.parseStatus((String) source.get("pbStatus"));
		if (source.containsKey("signalIntegrity")) signalIntegrity = ParseUtils.parseSignalIntegrity((String) source.get("signalIntegrity"));	
	}				
	
	@Override
	protected void decode() {
		int index = 0;
		starboardside = ParseUtils.parseDouble(getValue(index++));
		sbStatus = ParseUtils.parseStatus(getValue(index++));
		portside = ParseUtils.parseDouble(getValue(index++));
		pbStatus = ParseUtils.parseStatus(getValue(index++));
		signalIntegrity = ParseUtils.parseSignalIntegrity(getValue(index++));
	}

	@Override
	protected void encode() {
		int index = 0;
		setValue(index++, ParseUtils.toString(starboardside));
		setValue(index++, ParseUtils.toString(sbStatus));
		setValue(index++, ParseUtils.toString(portside));
		setValue(index++, ParseUtils.toString(pbStatus));
		setValue(index++, ParseUtils.toString(signalIntegrity));
	}

	@Override
	protected void fillMap(Map<String, Object> res) {
		if (starboardside != null) res.put("starboardside", starboardside);
		if (sbStatus != Status.NULL) res.put("sbStatus", sbStatus.name());
		if (portside != null) res.put("portside", portside);
		if (pbStatus != Status.NULL) res.put("pbStatus", pbStatus.name());
		if (signalIntegrity != SignalIntegrity.NULL) res.put("signalIntegrity", signalIntegrity.name());
	}

	public Double getStarboard(){
		return starboardside;
	}
	
	public void setStarboard(Double starboard){
		this.starboardside = starboard;
	}
	
	public Status getStarboardStatus(){
		return sbStatus;
	}
	
	public void setStarboardStatus(Status sbStatus){
		this.sbStatus = sbStatus;
	}
	
	public Double getPortboard(){
		return portside;
	}
	
	public void setPortboard(Double portboard){
		this.portside = portboard;
	}
	
	public Status getPortboardStatus(){
		return pbStatus;
	}
	
	public void setPortboardStatus(Status pbStatus){
		this.pbStatus = pbStatus;
	}

	public SignalIntegrity getSignalIntegrity() {
		return signalIntegrity;
	}

	public void setSignalIntegrity(SignalIntegrity signalIntegrity) {
		this.signalIntegrity = signalIntegrity;
	}
}
