package de.uniol.inf.is.odysseus.wrapper.nmea.sentence;

import java.util.Map;

import de.uniol.inf.is.odysseus.wrapper.nmea.data.Acquisition;
import de.uniol.inf.is.odysseus.wrapper.nmea.data.Reference;
import de.uniol.inf.is.odysseus.wrapper.nmea.data.TargetNumber;
import de.uniol.inf.is.odysseus.wrapper.nmea.data.TargetReference;
import de.uniol.inf.is.odysseus.wrapper.nmea.data.TargetStatus;
import de.uniol.inf.is.odysseus.wrapper.nmea.data.Time;
import de.uniol.inf.is.odysseus.wrapper.nmea.data.Unit;
import de.uniol.inf.is.odysseus.wrapper.nmea.util.ParseUtils;

/**
 * TTM - Tracked Target Message<br>
 * <br>
 * 
 * <pre>
 * {@code
 * .      1  2   3   4 5   6   7 8   9  10 11  12 1314       15 16
 *        |  |   |   | |   |   | |   |   | |    | | |         | |
 * $--TTM,xx,x.x,x.x,a,x.x,x.x,a,x.x,x.x,a,c--c,a,a,hhmmss.ss,a*hh
 * }
 * </pre>
 * <ol>
 * <li>Target Number</li>
 * <li>Target Distance</li>
 * <li>Bearing from own ship</li>
 * <li>Bearing Units</li>
 * <li>Target speed</li>
 * <li>Target Course</li>
 * <li>Course Units</li>
 * <li>Distance of closest-point-of-approach</li>
 * <li>Time until closest-point-of-approach "-" means increasing</li>
 * <li>Distance units (K = Kilometers, S = Statute miles)</li>
 * <li>Target name</li>
 * <li>Target Status (T = Target, L = Lost target, Q = Target being acquired)</li>
 * <li>Reference Target (R = Reference target, blank = designated fixed target)</li>
 * <li>Time (UTC)</li>
 * <li>Type acquisition (M = manual, A = automatic)</li>
 * <li>Checksum</li>
 * </ol>
 * 
 * @author jboger <juergen.boger@offis.de>
 * 
 */
public class TTMSentence extends Sentence {
	/** Default begin char for this sentence type. */
	public static final char BEGIN_CHAR = '$';
	/** Default talker for this sentence. */
	public static final String DEFAULT_TALKER = "RA";
	/** Sentence id. */
	public static final String SENTENCE_ID = "TTM";
	/** Default count of fields. */
	public static final int FIELD_COUNT = 15;
	
	/** Target Number */
	private TargetNumber targetNumber;
	/** Target Distance */
	private Double targetDistance;
	/** Bearing from own ship */
	private Double bearing;
	/** Bearing Units */
	private Reference bearingUnit;
	/** Target speed */
	private Double targetSpeed;
	/** Target Course */
	private Double targetCourse;
	/** Course Units */
	private Reference courseUnit;
	/** Distance of closest-point-of-approach */
	private Double closestPointOfApproach;
	/** Time until closest-point-of-approach "-" means increasing */
	private Double timeUntilClosestPoint;
	/** Distance units (K = Kilometers, S = Statute miles) */
	private Unit distanceUnit;
	/** Target name */
	private String targetLabel;
	/** Target Status (T = Target, L = Lost target, Q = Target being acquired) */
	private TargetStatus targetStatus;
	/** Reference Target (R = Reference target, blank = designated fixed target) */
	private TargetReference referenceTarget;
	/** Time (UTC) */
	private Time time;
	/** Type acquisition (M = manual, A = automatic) */
	private Acquisition typeAcquisition;
	
	/**
	 * Default constructor for writing. Empty Sentence to fill attributes and
	 * call {@link #toNMEA()}.
	 */
	public TTMSentence() {
		super(BEGIN_CHAR, DEFAULT_TALKER, SENTENCE_ID, FIELD_COUNT);
	}
	
	/**
	 * Default constructor for parsing.
	 * @param nmea
	 * Nmea String to be parsed.
	 */
	public TTMSentence(String nmea) {
		super(nmea);
	}

	/**
	 * Constructor for creating a message from a map. Reverse function of fillMap()
	 * 
	 * @param source
	 *            Map containing specific keys.
	 */		
	public TTMSentence(Map<String, Object> source)	
	{
		super(source, FIELD_COUNT);
		
		if (source.containsKey("targetNumber")) targetNumber = ParseUtils.parseTargetNumber((String) source.get("targetNumber"));
		if (source.containsKey("targetDistance")) targetDistance = (double) source.get("targetDistance");
		if (source.containsKey("bearing")) bearing = (double) source.get("bearing");
		if (source.containsKey("bearingUnit")) bearingUnit = ParseUtils.parseReference((String) source.get("bearingUnit"));
		if (source.containsKey("targetSpeed")) targetSpeed = (double) source.get("targetSpeed");
		if (source.containsKey("targetCourse")) targetCourse = (double) source.get("targetCourse");
		if (source.containsKey("courseUnit")) courseUnit = ParseUtils.parseReference((String) source.get("courseUnit"));
		if (source.containsKey("closestPointOfApproach")) closestPointOfApproach = (double) source.get("closestPointOfApproach");
		if (source.containsKey("timeUntilClosestPoint")) timeUntilClosestPoint = (double) source.get("timeUntilClosestPoint");
		if (source.containsKey("distanceUnit")) distanceUnit = ParseUtils.parseUnit((String) source.get("distanceUnit"));
		if (source.containsKey("targetLabel")) targetLabel = (String) source.get("targetLabel");
		if (source.containsKey("targetStatus")) targetStatus = ParseUtils.parseTargetStatus((String) source.get("targetStatus"));
		if (source.containsKey("referenceTarget")) referenceTarget = ParseUtils.parseTargetReference((String) source.get("referenceTarget"));
		time = Time.fromMap("time", source);
		if (source.containsKey("typeAcquisition")) typeAcquisition = ParseUtils.parseAcquisition((String) source.get("typeAcquisition"));
	}				
	
	@Override
	protected void decode() {
		int index = 0;
		targetNumber = ParseUtils.parseTargetNumber(getValue(index++));
		targetDistance = ParseUtils.parseDouble(getValue(index++));
		bearing = ParseUtils.parseDouble(getValue(index++));
		bearingUnit = ParseUtils.parseReference(getValue(index++));
		targetSpeed = ParseUtils.parseDouble(getValue(index++));
		targetCourse = ParseUtils.parseDouble(getValue(index++));
		courseUnit = ParseUtils.parseReference(getValue(index++));
		closestPointOfApproach = ParseUtils.parseDouble(getValue(index++));
		timeUntilClosestPoint = ParseUtils.parseDouble(getValue(index++));
		distanceUnit = ParseUtils.parseUnit(getValue(index++));
		targetLabel = getValue(index++);
		targetStatus = ParseUtils.parseTargetStatus(getValue(index++));
		referenceTarget = ParseUtils.parseTargetReference(getValue(index++));
		time = ParseUtils.parseTime(getValue(index++));
		typeAcquisition = ParseUtils.parseAcquisition(getValue(index++));
	}

	@Override
	protected void encode() {
		int index = 0;
		setValue(index++, ParseUtils.toString(targetNumber));
		setValue(index++, ParseUtils.toString(targetDistance));
		setValue(index++, ParseUtils.toString(bearing));
		setValue(index++, ParseUtils.toString(bearingUnit));
		setValue(index++, ParseUtils.toString(targetSpeed));
		setValue(index++, ParseUtils.toString(targetCourse));
		setValue(index++, ParseUtils.toString(courseUnit));
		setValue(index++, ParseUtils.toString(closestPointOfApproach));
		setValue(index++, ParseUtils.toString(timeUntilClosestPoint));
		setValue(index++, ParseUtils.toString(distanceUnit));
		setValue(index++, targetLabel);
		setValue(index++, ParseUtils.toString(targetStatus));
		setValue(index++, ParseUtils.toString(referenceTarget));
		setValue(index++, ParseUtils.toString(time));
		setValue(index++, ParseUtils.toString(typeAcquisition));
	}

	@Override
	protected void fillMap(Map<String, Object> res) {
		if (targetNumber != null) res.put("targetNumber", targetNumber.getNumber());
		if (targetDistance != null) res.put("targetDistance", targetDistance);
		if (bearing != null) res.put("bearing", bearing);
		if (bearingUnit != Reference.NULL) res.put("bearingUnit", bearingUnit.name());
		if (targetSpeed != null) res.put("targetSpeed", targetSpeed);
		if (targetCourse != null) res.put("targetCourse", targetCourse);
		if (courseUnit != Reference.NULL) res.put("courseUnit", courseUnit.name());
		if (closestPointOfApproach != null) res.put("closestPointOfApproach", closestPointOfApproach);
		if (timeUntilClosestPoint != null) res.put("timeUntilClosestPoint", timeUntilClosestPoint);
		if (distanceUnit != Unit.NULL) res.put("distanceUnit", distanceUnit.name());
		if (targetLabel != null) res.put("targetLabel", targetLabel);
		if (targetStatus != TargetStatus.NULL) res.put("targetStatus", targetStatus.name());
		if (referenceTarget != null) res.put("referenceTarget", referenceTarget);
		if (time != null) time.addToMap("time", res);
		if (typeAcquisition != Acquisition.NULL) res.put("typeAcquisition", typeAcquisition.name());
	}

	public TargetNumber getTargetNumber() {
		return targetNumber;
	}

	public void setTargetNumber(TargetNumber targetNumber) {
		this.targetNumber = targetNumber;
	}

	public Double getTargetDistance() {
		return targetDistance;
	}

	public void setTargetDistance(Double targetDistance) {
		this.targetDistance = targetDistance;
	}

	public Double getBearing() {
		return bearing;
	}

	public void setBearing(Double bearing) {
		this.bearing = bearing;
	}

	public Reference getBearingUnit() {
		return bearingUnit;
	}

	public void setBearingUnit(Reference bearingUnit) {
		this.bearingUnit = bearingUnit;
	}

	public Double getTargetSpeed() {
		return targetSpeed;
	}

	public void setTargetSpeed(Double targetSpeed) {
		this.targetSpeed = targetSpeed;
	}

	public Double getTargetCourse() {
		return targetCourse;
	}

	public void setTargetCourse(Double targetCourse) {
		this.targetCourse = targetCourse;
	}

	public Reference getCourseUnit() {
		return courseUnit;
	}

	public void setCourseUnit(Reference courseUnit) {
		this.courseUnit = courseUnit;
	}

	public Double getClosestPointOfApproach() {
		return closestPointOfApproach;
	}

	public void setClosestPointOfApproach(Double closestPointOfApproach) {
		this.closestPointOfApproach = closestPointOfApproach;
	}

	public Double getTimeUntilClosestPoint() {
		return timeUntilClosestPoint;
	}

	public void setTimeUntilClosestPoint(Double timeUntilClosestPoint) {
		this.timeUntilClosestPoint = timeUntilClosestPoint;
	}

	public Unit getDistanceUnit() {
		return distanceUnit;
	}

	public void setDistanceUnit(Unit distanceUnit) {
		this.distanceUnit = distanceUnit;
	}

	public String getTargetLabel() {
		return targetLabel;
	}

	public void setTargetLabel(String targetLabel) {
		this.targetLabel = targetLabel;
	}

	public TargetStatus getTargetStatus() {
		return targetStatus;
	}

	public void setTargetStatus(TargetStatus targetStatus) {
		this.targetStatus = targetStatus;
	}

	public TargetReference getReferenceTarget() {
		return referenceTarget;
	}

	public void setReferenceTarget(TargetReference referenceTarget) {
		this.referenceTarget = referenceTarget;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public Acquisition getTypeAcquisition() {
		return typeAcquisition;
	}

	public void setTypeAcquisition(Acquisition typeAcquisition) {
		this.typeAcquisition = typeAcquisition;
	}
}
