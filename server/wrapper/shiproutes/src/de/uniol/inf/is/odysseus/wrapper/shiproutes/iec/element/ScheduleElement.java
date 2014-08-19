package de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.helper.IECStringHelper;

public class ScheduleElement implements IIecElement {
	// Constants
	public static final String WAYPOINT_ID = "waypointId";
	public static final String ETD = "etd";
	public static final String ETD_WINDOW_BEFORE = "etdWindowBefore";
	public static final String ETD_WINDOW_AFTER = "etdWindowAfter";
	public static final String ETA = "eta";
	public static final String ETA_WINDOW_BEFORE = "etaWindowBefore";
	public static final String ETA_WINDOW_AFTER = "etaWindowAfter";
	public static final String STAY = "stay";
	public static final String SPEED = "speed";
	public static final String SPEED_WINDOW = "speedWindow";
	public static final String WIND_SPEED = "windSpeed";
	public static final String WIND_DIRECTION = "windDirection";
	public static final String CURRENT_SPEED = "currentSpeed";
	public static final String CURRENT_DIRECTION = "currentDirection";
	public static final String WIND_LOSS = "windLoss";
	public static final String WAVE_LOSS = "waveLoss";
	public static final String TOTAL_LOSS = "totalLoss";
	public static final String RPM = "rpm";
	public static final String PITCH = "pitch";
	public static final String FUEL = "fuel";
	public static final String REL_FUEL_SAVE = "relFuelSave";
	public static final String ABS_FUEL_SAVE = "absFuelSave";
	public static final String NOTE = "note";

	// map constants
	private static final String ELEMENT_PREFIX = "_schedElem";

	// XML Constants
	private static final String OPEN_TAG = "<scheduleElement";
	private static final String CLOSE_TAG = "</scheduleElement>";
	private static final String OPEN_EXTENSIONS_TAG = "<extensions>";
	private static final String CLOSE_EXTENSIONS_TAG = "</extensions>";

	// values
	private Integer waypointID; // mandatory
	private Date etd;
	private Double etdWindowBefore;
	private Double etdWindowAfter;
	private Date eta;
	private Double etaWindowBefore;
	private Double etaWindowAfter;
	private Double stay;
	private Double speed;
	private Double speedWindow;
	private Double windSpeed;
	private Double windDirection;
	private Double currentSpeed;
	private Double currentDirection;
	private Double windLoss;
	private Double waveLoss;
	private Double totalLoss;
	private Double rpm;
	private Integer pitch;
	private Double fuel;
	private Double relFuelSave;
	private Double absFuelSave;
	private String note;
	private List<Extension> extensions;

	@Override
	public void fillMap(KeyValueObject<? extends IMetaAttribute> map,
			String prefix) {
		prefix += ELEMENT_PREFIX;
		String elementPrefix = prefix + "_";
		
		if (waypointID != null)
			map.addAttributeValue(elementPrefix+WAYPOINT_ID, waypointID);
		if (etd != null)
			map.addAttributeValue(elementPrefix+ETD, etd);
		if (etdWindowBefore != null)
			map.addAttributeValue(elementPrefix+ETD_WINDOW_BEFORE, etdWindowBefore);
		if (etdWindowAfter != null)
			map.addAttributeValue(elementPrefix+ETD_WINDOW_AFTER, etdWindowAfter);
		if (eta != null)
			map.addAttributeValue(elementPrefix+ETA, eta);
		if (etaWindowBefore != null)
			map.addAttributeValue(elementPrefix+ETA_WINDOW_BEFORE, etaWindowBefore);
		if (etaWindowAfter != null)
			map.addAttributeValue(elementPrefix+ETA_WINDOW_AFTER, etaWindowAfter);
		if (stay != null)
			map.addAttributeValue(elementPrefix+STAY, stay);
		if (speed != null)
			map.addAttributeValue(elementPrefix+SPEED, speed);
		if (speedWindow != null)
			map.addAttributeValue(elementPrefix+SPEED_WINDOW, speedWindow);
		if (windSpeed != null)
			map.addAttributeValue(elementPrefix+WIND_SPEED, windSpeed);
		if (windDirection != null)
			map.addAttributeValue(elementPrefix+WIND_DIRECTION, windDirection);
		if (currentSpeed != null)
			map.addAttributeValue(elementPrefix+CURRENT_SPEED, currentSpeed);
		if (currentDirection != null)
			map.addAttributeValue(elementPrefix+CURRENT_DIRECTION, currentDirection);
		if (windLoss != null)
			map.addAttributeValue(elementPrefix+WIND_LOSS, windLoss);
		if (waveLoss != null)
			map.addAttributeValue(elementPrefix+WAVE_LOSS, waveLoss);
		if (totalLoss != null)
			map.addAttributeValue(elementPrefix+TOTAL_LOSS, totalLoss);
		if (rpm != null)
			map.addAttributeValue(elementPrefix+RPM, rpm);
		if (pitch != null)
			map.addAttributeValue(elementPrefix+PITCH, pitch);
		if (fuel != null)
			map.addAttributeValue(elementPrefix+FUEL, fuel);
		if (relFuelSave != null)
			map.addAttributeValue(elementPrefix+REL_FUEL_SAVE, relFuelSave);
		if (absFuelSave != null)
			map.addAttributeValue(elementPrefix+ABS_FUEL_SAVE, absFuelSave);
		if (note != null)
			map.addAttributeValue(elementPrefix+NOTE, note);

		if (extensions != null) {
			for (Extension extension : extensions) {
				extension.fillMap(map, prefix);
			}
		}

	}

	@Override
	public String toXML() {
		StringBuilder builder = new StringBuilder(OPEN_TAG);

		if (waypointID != null)
			IECStringHelper.appendIntElement(builder, WAYPOINT_ID, waypointID);
		if (etd != null)
			IECStringHelper.appendDateElement(builder, ETD, etd);
		if (etdWindowBefore != null)
			IECStringHelper.appendDoubleElement(builder, ETD_WINDOW_BEFORE,
					etdWindowBefore);
		if (etdWindowAfter != null)
			IECStringHelper.appendDoubleElement(builder, ETD_WINDOW_AFTER,
					etdWindowAfter);
		if (eta != null)
			IECStringHelper.appendDateElement(builder, ETA, eta);
		if (etaWindowBefore != null)
			IECStringHelper.appendDoubleElement(builder, ETA_WINDOW_BEFORE,
					etaWindowBefore);
		if (etaWindowAfter != null)
			IECStringHelper.appendDoubleElement(builder, ETA_WINDOW_AFTER,
					etaWindowAfter);
		if (stay != null)
			IECStringHelper.appendDoubleElement(builder, STAY, stay);
		if (speed != null)
			IECStringHelper.appendDoubleElement(builder, SPEED, speed);
		if (speedWindow != null)
			IECStringHelper.appendDoubleElement(builder, SPEED_WINDOW,
					speedWindow);
		if (windSpeed != null)
			IECStringHelper.appendDoubleElement(builder, WIND_SPEED, windSpeed);
		if (windDirection != null)
			IECStringHelper.appendDoubleElement(builder, WIND_DIRECTION,
					windDirection);
		if (currentSpeed != null)
			IECStringHelper.appendDoubleElement(builder, CURRENT_SPEED,
					currentSpeed);
		if (currentDirection != null)
			IECStringHelper.appendDoubleElement(builder, CURRENT_DIRECTION,
					currentDirection);
		if (windLoss != null)
			IECStringHelper.appendDoubleElement(builder, WIND_LOSS, windLoss);
		if (waveLoss != null)
			IECStringHelper.appendDoubleElement(builder, WAVE_LOSS, waveLoss);
		if (totalLoss != null)
			IECStringHelper.appendDoubleElement(builder, TOTAL_LOSS, totalLoss);
		if (rpm != null)
			IECStringHelper.appendDoubleElement(builder, RPM, rpm);
		if (pitch != null)
			IECStringHelper.appendIntElement(builder, PITCH, pitch);
		if (fuel != null)
			IECStringHelper.appendDoubleElement(builder, FUEL, fuel);
		if (relFuelSave != null)
			IECStringHelper.appendDoubleElement(builder, REL_FUEL_SAVE,
					relFuelSave);
		if (absFuelSave != null)
			IECStringHelper.appendDoubleElement(builder, ABS_FUEL_SAVE,
					absFuelSave);
		if (note != null)
			IECStringHelper.appendStringElement(builder, NOTE, note);

		if (!hasSubelements()) {
			builder.append("/>");
		} else {
			builder.append(">");

			if (extensions != null && !extensions.isEmpty()) {
				builder.append(OPEN_EXTENSIONS_TAG);
				for (Extension extension : extensions) {
					builder.append(extension.toXML());
				}
				builder.append(CLOSE_EXTENSIONS_TAG);
			}

			builder.append(CLOSE_TAG);
		}

		return builder.toString();
	}

	@Override
	public boolean hasSubelements() {
		if (extensions == null || extensions.isEmpty()) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isValid() {
		if (waypointID != null) {
			return true;
		}
		return false;
	}

	@Override
	public void addExtension(Extension extension) {
		if (extensions == null)
			extensions = new ArrayList<Extension>();
		extensions.add(extension);
	}

	public Integer getWaypointID() {
		return waypointID;
	}

	public void setWaypointID(Integer waypointID) {
		this.waypointID = waypointID;
	}

	public Date getEtd() {
		return etd;
	}

	public void setEtd(Date etd) {
		this.etd = etd;
	}

	public Double getEtdWindowBefore() {
		return etdWindowBefore;
	}

	public void setEtdWindowBefore(Double etdWindowBefore) {
		this.etdWindowBefore = etdWindowBefore;
	}

	public Double getEtdWindowAfter() {
		return etdWindowAfter;
	}

	public void setEtdWindowAfter(Double etdWindowAfter) {
		this.etdWindowAfter = etdWindowAfter;
	}

	public Date getEta() {
		return eta;
	}

	public void setEta(Date eta) {
		this.eta = eta;
	}

	public Double getEtaWindowBefore() {
		return etaWindowBefore;
	}

	public void setEtaWindowBefore(Double etaWindowBefore) {
		this.etaWindowBefore = etaWindowBefore;
	}

	public Double getEtaWindowAfter() {
		return etaWindowAfter;
	}

	public void setEtaWindowAfter(Double etaWindowAfter) {
		this.etaWindowAfter = etaWindowAfter;
	}

	public Double getStay() {
		return stay;
	}

	public void setStay(Double stay) {
		this.stay = stay;
	}

	public Double getSpeed() {
		return speed;
	}

	public void setSpeed(Double speed) {
		this.speed = speed;
	}

	public Double getSpeedWindow() {
		return speedWindow;
	}

	public void setSpeedWindow(Double speedWindow) {
		this.speedWindow = speedWindow;
	}

	public Double getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(Double windSpeed) {
		this.windSpeed = windSpeed;
	}

	public Double getWindDirection() {
		return windDirection;
	}

	public void setWindDirection(Double windDirection) {
		this.windDirection = windDirection;
	}

	public Double getCurrentSpeed() {
		return currentSpeed;
	}

	public void setCurrentSpeed(Double currentSpeed) {
		this.currentSpeed = currentSpeed;
	}

	public Double getCurrentDirection() {
		return currentDirection;
	}

	public void setCurrentDirection(Double currentDirection) {
		this.currentDirection = currentDirection;
	}

	public Double getWindLoss() {
		return windLoss;
	}

	public void setWindLoss(Double windLoss) {
		this.windLoss = windLoss;
	}

	public Double getWaveLoss() {
		return waveLoss;
	}

	public void setWaveLoss(Double waveLoss) {
		this.waveLoss = waveLoss;
	}

	public Double getTotalLoss() {
		return totalLoss;
	}

	public void setTotalLoss(Double totalLoss) {
		this.totalLoss = totalLoss;
	}

	public Double getRpm() {
		return rpm;
	}

	public void setRpm(Double rpm) {
		this.rpm = rpm;
	}

	public Integer getPitch() {
		return pitch;
	}

	public void setPitch(Integer pitch) {
		this.pitch = pitch;
	}

	public Double getFuel() {
		return fuel;
	}

	public void setFuel(Double fuel) {
		this.fuel = fuel;
	}

	public Double getRelFuelSave() {
		return relFuelSave;
	}

	public void setRelFuelSave(Double relFuelSave) {
		this.relFuelSave = relFuelSave;
	}

	public Double getAbsFuelSave() {
		return absFuelSave;
	}

	public void setAbsFuelSave(Double absFuelSave) {
		this.absFuelSave = absFuelSave;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public List<Extension> getExtensions() {
		return extensions;
	}

	public void setExtensions(List<Extension> extensions) {
		this.extensions = extensions;
	}
}
