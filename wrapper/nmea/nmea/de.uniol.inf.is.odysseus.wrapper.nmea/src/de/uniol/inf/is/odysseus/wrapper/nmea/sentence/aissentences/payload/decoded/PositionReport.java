
/**
 * 
 */
package de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded;

import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.AISMessageType;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.MMSI;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.ManeuverIndicator;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.NavigationStatus;


/**
 *  
 *
 */

public abstract class PositionReport extends DecodedAISPayload {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5139598546090052205L;
	
	private final NavigationStatus navigationStatus;
	private final Integer rateOfTurn;
	private final Double speedOverGround;
	private final Boolean positionAccurate;
	private final Double latitude;
	private final Double longitude;
	private final Double courseOverGround;
	private final Integer trueHeading;
	private final Integer second;
	private final ManeuverIndicator maneuverIndicator;
	private final Boolean raimFlag;
	
	protected PositionReport(String originalNmea, AISMessageType messageType,
			Integer repeatIndicator, MMSI sourceMmsi,
			NavigationStatus navigationStatus, Integer rateOfTurn,
			Double speedOverGround, Boolean positionAccurate, Double latitude,
			Double longitude, Double courseOverGround, Integer trueHeading,
			Integer second, ManeuverIndicator maneuverIndicator,
			Boolean raimFlag) {
		super(originalNmea, messageType, repeatIndicator, sourceMmsi);
		this.navigationStatus = navigationStatus;
		this.rateOfTurn = rateOfTurn;
		this.speedOverGround = speedOverGround;
		this.positionAccurate = positionAccurate;
		this.latitude = latitude;
		this.longitude = longitude;
		this.courseOverGround = courseOverGround;
		this.trueHeading = trueHeading;
		this.second = second;
		this.maneuverIndicator = maneuverIndicator;
		this.raimFlag = raimFlag;
	}
		
	public final NavigationStatus getNavigationStatus() {
		return navigationStatus;
	}

	public final Integer getRateOfTurn() {
		return rateOfTurn;
	}

	public final Double getSpeedOverGround() {
		return speedOverGround;
	}

	public final Boolean getPositionAccurate() {
		return positionAccurate;
	}

	public final Double getLatitude() {
		return latitude;
	}

	public final Double getLongitude() {
		return longitude;
	}

	public final Double getCourseOverGround() {
		return courseOverGround;
	}

	public final Integer getTrueHeading() {
		return trueHeading;
	}

	public final Integer getSecond() {
		return second;
	}

	public final ManeuverIndicator getManeuverIndicator() {
		return maneuverIndicator;
	}

	public final Boolean getRaimFlag() {
		return raimFlag;
	}

	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PositionReport [navigationStatus=")
				.append(navigationStatus).append(", rateOfTurn=")
				.append(rateOfTurn).append(", speedOverGround=")
				.append(speedOverGround).append(", positionAccurate=")
				.append(positionAccurate).append(", latitude=")
				.append(latitude).append(", longitude=").append(longitude)
				.append(", courseOverGround=").append(courseOverGround)
				.append(", trueHeading=").append(trueHeading)
				.append(", second=").append(second)
				.append(", maneuverIndicator=").append(maneuverIndicator)
				.append(", raimFlag=").append(raimFlag)
				.append(", getMessageType()=").append(getMessageType())
				.append(", getRepeatIndicator()=").append(getRepeatIndicator())
				.append(", getSourceMmsi()=").append(getSourceMmsi())
				.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((courseOverGround == null) ? 0 : courseOverGround.hashCode());
		result = prime * result
				+ ((latitude == null) ? 0 : latitude.hashCode());
		result = prime * result
				+ ((longitude == null) ? 0 : longitude.hashCode());
		result = prime
				* result
				+ ((maneuverIndicator == null) ? 0 : maneuverIndicator
						.hashCode());
		result = prime
				* result
				+ ((navigationStatus == null) ? 0 : navigationStatus.hashCode());
		result = prime
				* result
				+ ((positionAccurate == null) ? 0 : positionAccurate.hashCode());
		result = prime * result
				+ ((raimFlag == null) ? 0 : raimFlag.hashCode());
		result = prime * result
				+ ((rateOfTurn == null) ? 0 : rateOfTurn.hashCode());
		result = prime * result + ((second == null) ? 0 : second.hashCode());
		result = prime * result
				+ ((speedOverGround == null) ? 0 : speedOverGround.hashCode());
		result = prime * result
				+ ((trueHeading == null) ? 0 : trueHeading.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof PositionReport))
			return false;
		PositionReport other = (PositionReport) obj;
		if (courseOverGround == null) {
			if (other.courseOverGround != null)
				return false;
		} else if (!courseOverGround.equals(other.courseOverGround))
			return false;
		if (latitude == null) {
			if (other.latitude != null)
				return false;
		} else if (!latitude.equals(other.latitude))
			return false;
		if (longitude == null) {
			if (other.longitude != null)
				return false;
		} else if (!longitude.equals(other.longitude))
			return false;
		if (maneuverIndicator != other.maneuverIndicator)
			return false;
		if (navigationStatus != other.navigationStatus)
			return false;
		if (positionAccurate == null) {
			if (other.positionAccurate != null)
				return false;
		} else if (!positionAccurate.equals(other.positionAccurate))
			return false;
		if (raimFlag == null) {
			if (other.raimFlag != null)
				return false;
		} else if (!raimFlag.equals(other.raimFlag))
			return false;
		if (rateOfTurn == null) {
			if (other.rateOfTurn != null)
				return false;
		} else if (!rateOfTurn.equals(other.rateOfTurn))
			return false;
		if (second == null) {
			if (other.second != null)
				return false;
		} else if (!second.equals(other.second))
			return false;
		if (speedOverGround == null) {
			if (other.speedOverGround != null)
				return false;
		} else if (!speedOverGround.equals(other.speedOverGround))
			return false;
		if (trueHeading == null) {
			if (other.trueHeading != null)
				return false;
		} else if (!trueHeading.equals(other.trueHeading))
			return false;
		return true;
	}
}
