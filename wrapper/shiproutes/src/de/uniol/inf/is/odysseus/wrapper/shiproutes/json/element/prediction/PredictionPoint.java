package de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.prediction;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.IShipRouteElement;

public class PredictionPoint implements IShipRouteElement {
	private static final String ELEMENT_PREFIX = "_predPoint";

	private static final String ID_CONST = "ID";
	private static final String LAT_RAD = "lat_rad";
	private static final String LON_RAD = "lon_rad";
	private static final String HEADING_RAD = "heading_rad";
	private static final String COURSE_OVER_GROUND_RAD = "course_over_ground_rad";
	private static final String RATE_OF_TURN = "rate_of_turn";

	private Integer ID;
	private Double lat_rad;
	private Double lon_rad;
	private Double heading_rad;
	private Double course_over_ground_rad;
	private Double rate_of_turn;

	@Override
	public void fillMap(KeyValueObject<? extends IMetaAttribute> map,
			String prefix) {
		prefix += ELEMENT_PREFIX;
		String elementPrefix = prefix + "_";

		if (ID != null)
			map.addAttributeValue(elementPrefix + ID_CONST, ID);
		if (lat_rad != null)
			map.addAttributeValue(elementPrefix + LAT_RAD, lat_rad);
		if (lon_rad != null)
			map.addAttributeValue(elementPrefix + LON_RAD, lon_rad);
		if (heading_rad != null)
			map.addAttributeValue(elementPrefix + HEADING_RAD, heading_rad);
		if (course_over_ground_rad != null)
			map.addAttributeValue(elementPrefix + COURSE_OVER_GROUND_RAD,
					course_over_ground_rad);
		if (rate_of_turn != null)
			map.addAttributeValue(elementPrefix + RATE_OF_TURN, rate_of_turn);
	}

	public Integer getID() {
		return ID;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	public Double getLat_rad() {
		return lat_rad;
	}

	public void setLat_rad(Double lat_rad) {
		this.lat_rad = lat_rad;
	}

	public Double getLon_rad() {
		return lon_rad;
	}

	public void setLon_rad(Double lon_rad) {
		this.lon_rad = lon_rad;
	}

	public Double getHeading_rad() {
		return heading_rad;
	}

	public void setHeading_rad(Double heading_rad) {
		this.heading_rad = heading_rad;
	}

	public Double getCourse_over_ground_rad() {
		return course_over_ground_rad;
	}

	public void setCourse_over_ground_rad(Double course_over_ground_rad) {
		this.course_over_ground_rad = course_over_ground_rad;
	}

	public Double getRate_of_turn() {
		return rate_of_turn;
	}

	public void setRate_of_turn(Double rate_of_turn) {
		this.rate_of_turn = rate_of_turn;
	}

}
