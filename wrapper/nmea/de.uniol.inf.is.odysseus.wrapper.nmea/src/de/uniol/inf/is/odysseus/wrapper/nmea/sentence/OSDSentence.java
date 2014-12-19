package de.uniol.inf.is.odysseus.wrapper.nmea.sentence;

import java.util.Map;

import de.uniol.inf.is.odysseus.wrapper.nmea.data.Reference;
import de.uniol.inf.is.odysseus.wrapper.nmea.data.SignalIntegrity;
import de.uniol.inf.is.odysseus.wrapper.nmea.data.Status;
import de.uniol.inf.is.odysseus.wrapper.nmea.data.Unit;
import de.uniol.inf.is.odysseus.wrapper.nmea.util.ParseUtils;

/**
 * OSD - Own Ship Data<br>
 * <br>
 * 
 * <pre>
 * {@code
 *.      1   2 3   4 5   6 7   8   9 10
 *       |   | |   | |   | |   |   | |
 *$--OSD,x.x,A,x.x,a,x.x,a,x.x,x.x,a*hh
 * }
 * </pre>
 * <ol>
 * <li>Heading, degrees true</li>
 * <li>Status, A = Data Valid </li>
 * <li>Vessel Course, degrees True </li>
 * <li>Course Reference </li>
 * <li>Vessel Speed </li>
 * <li>Speed Reference </li>
 * <li>Vessel Set, degrees True  </li>
 * <li>Vessel drift (speed)  </li>
 * <li>Speed Units  </li>
 * <li>Checksum </li>
 * </ol>
 * 
 * @author jbmzh <jan.meyer.zu.holte@uni-oldenburg.de>
 * 
 */
public class OSDSentence extends Sentence {
	/** Default begin char for this sentence type. */
	public static final char BEGIN_CHAR = '$';
	/** Default talker for this sentence. */
	public static final String DEFAULT_TALKER = "GP";
	/** Sentence id. */
	public static final String SENTENCE_ID = "OSD";
	/** Default count of fields. */
	public static final int FIELD_COUNT = 10;
	
	/** Heading  */
	private Double heading;
	/** Status, A = Data Valid */
	private Status status;
	/** Vessel Course */
	private Double course;
	/** Course Reference  */
	private Reference courseReference;
	/** Vessel Speed */
	private Double vesselSpeed;
	/** Speed Reference */
	private Reference vesselSpeedReference;
	/** Vessel Set */
	private Double vesselSet;
	/** Vessel drift (speed)  */
	private Double vesselDrift;
	/** Speed Units  */
	private Unit speedKnotsUnits;
	/** Checksum */
	private SignalIntegrity signalIntegrity;
	
	/**
	 * Default constructor for writing. Empty Sentence to fill attributes and
	 * call {@link #toNMEA()}.
	 */
	public OSDSentence() {
		super(BEGIN_CHAR, DEFAULT_TALKER, SENTENCE_ID, FIELD_COUNT);
	}
	
	/**
	 * Default constructor for parsing.
	 * @param nmea
	 * Nmea String to be parsed.
	 */
	public OSDSentence(String nmea) {
		super(nmea);
	}

	/**
	 * Constructor for creating a message from a map. Reverse function of fillMap()
	 * 
	 * @param source
	 *            Map containing specific keys.
	 */		
	public OSDSentence(Map<String, Object> source)	
	{
		super(source, FIELD_COUNT);
		
		if (source.containsKey("heading")) heading = (double) source.get("heading");		
		if (source.containsKey("course")) course = (double) source.get("course");
		if (source.containsKey("vesselSpeed")) vesselSpeed = (double) source.get("vesselSpeed");
		if (source.containsKey("vesselSet")) vesselSet = (double) source.get("vesselSet");
		if (source.containsKey("vesselDrift")) vesselDrift = (double) source.get("vesselDrift");		
		if (source.containsKey("status")) status = ParseUtils.parseStatus((String) source.get("status"));
		if (source.containsKey("courseReference")) courseReference = ParseUtils.parseReference((String) source.get("courseReference"));		
		if (source.containsKey("vesselSpeedReference"))	vesselSpeedReference = ParseUtils.parseReference((String) source.get("vesselSpeedReference"));		
		if (source.containsKey("speedKnotsUnits")) speedKnotsUnits = ParseUtils.parseUnit((String) source.get("speedKnotsUnits"));
		if (source.containsKey("signalIntegrity")) signalIntegrity = ParseUtils.parseSignalIntegrity((String) source.get("signalIntegrity"));
	}				
	
	@Override
	protected void decode() {
		int index = 0;
		setHeading(ParseUtils.parseDouble(getValue(index++)));
		setStatus(ParseUtils.parseStatus(getValue(index++)));
		setCourse(ParseUtils.parseDouble(getValue(index++)));
		setCourseReference(ParseUtils.parseReference(getValue(index++)));
		setVesselSpeed(ParseUtils.parseDouble(getValue(index++)));
		setVesselSpeedReference(ParseUtils.parseReference(getValue(index++)));
		setVesselSet(ParseUtils.parseDouble(getValue(index++)));
		setVesselDrift(ParseUtils.parseDouble(getValue(index++)));
		setSpeedKnotsUnits(ParseUtils.parseUnit(getValue(index++)));
		signalIntegrity = ParseUtils.parseSignalIntegrity(getValue(index++));
	}

	@Override
	protected void encode() {
		int index = 0;
		setValue(index++, ParseUtils.toString(getHeading()));
		setValue(index++, ParseUtils.toString(getStatus()));
		setValue(index++, ParseUtils.toString(getCourse()));
		setValue(index++, ParseUtils.toString(getCourseReference()));
		setValue(index++, ParseUtils.toString(getVesselSpeed()));
		setValue(index++, ParseUtils.toString(getVesselSpeedReference()));
		setValue(index++, ParseUtils.toString(getVesselSet()));
		setValue(index++, ParseUtils.toString(getVesselDrift()));
		setValue(index++, ParseUtils.toString(getSpeedKnotsUnits()));
		setValue(index++, ParseUtils.toString(signalIntegrity));
	}

	@Override
	protected void fillMap(Map<String, Object> res) {
		if (getHeading() != null) res.put("heading", getHeading());
		if (getStatus() != null) res.put("status", getStatus());
		if (getCourse() != null) res.put("course", getCourse());
		if (getCourseReference() != null) res.put("courseReference", getCourseReference());
		if (getVesselSpeed() != null) res.put("vesselSpeed", getVesselSpeed());
		if (getVesselSpeedReference() != null) res.put("vesselSpeedReference", getVesselSpeedReference());
		if (getVesselSet() != null) res.put("vesselSet", getVesselSet());
		if (getVesselDrift() != null) res.put("vesselDrift", getVesselDrift());
		if (getSpeedKnotsUnits() != null) res.put("speedKnotsUnits", getSpeedKnotsUnits());
		if (signalIntegrity != SignalIntegrity.NULL) res.put("signalIntegrity", signalIntegrity.name());
	}

	public Double getHeading() {
		return heading;
	}

	public void setHeading(Double heading) {
		this.heading = heading;
	}

	public Double getCourse() {
		return course;
	}

	public void setCourse(Double course) {
		this.course = course;
	}

	public Reference getCourseReference() {
		return courseReference;
	}

	public void setCourseReference(Reference courseReference) {
		this.courseReference = courseReference;
	}

	public Double getVesselSpeed() {
		return vesselSpeed;
	}

	public void setVesselSpeed(Double vesselSpeed) {
		this.vesselSpeed = vesselSpeed;
	}

	public Double getVesselSet() {
		return vesselSet;
	}

	public void setVesselSet(Double vesselSet) {
		this.vesselSet = vesselSet;
	}

	public Double getVesselDrift() {
		return vesselDrift;
	}

	public void setVesselDrift(Double vesselDrift) {
		this.vesselDrift = vesselDrift;
	}

	public Unit getSpeedKnotsUnits() {
		return speedKnotsUnits;
	}

	public void setSpeedKnotsUnits(Unit speedKnotsUnits) {
		this.speedKnotsUnits = speedKnotsUnits;
	}

	public SignalIntegrity getSignalIntegrity() {
		return signalIntegrity;
	}

	public void setSignalIntegrity(SignalIntegrity signalIntegrity) {
		this.signalIntegrity = signalIntegrity;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Reference getVesselSpeedReference() {
		return vesselSpeedReference;
	}

	public void setVesselSpeedReference(Reference vesselSpeedReference) {
		this.vesselSpeedReference = vesselSpeedReference;
	}

}
