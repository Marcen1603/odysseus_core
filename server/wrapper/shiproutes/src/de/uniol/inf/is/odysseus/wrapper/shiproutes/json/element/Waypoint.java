package de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

public class Waypoint implements IShipRouteElement {

	// map constants
	private static final String ELEMENT_PREFIX = "_waypoint";

	private static final String ID_CONST = "ID";
	private static final String LABEL = "label";
	private static final String LAT_RAD = "lat_rad";
	private static final String LON_RAD = "lon_rad";
	private static final String Z_POSITION_M = "z_position_m";
	private static final String TURNRADIUS_NM = "turnradius_nm";
	private static final String CROSSTRACKLIMIT_STBD_M = "crosstracklimit_stbd_m";
	private static final String CROSSTRACKLIMIT_PORT_M = "crosstracklimit_port_m";
	private static final String SPEED_KTS = "speed_kts";
	private static final String ETA_CONST = "ETA";
	private static final String SPEEDLIMIT_UPPER_KTS = "speedlimit_upper_kts";
	private static final String SPEEDLIMIT_LOWER_KTS = "speedlimit_lower_kts";
	private static final String DISTANCE_TO_WP_NM = "distance_to_wp_nm";
	private static final String BEARING_TO_WP_RAD = "bearing_to_wp_rad";
	private static final String AUX_SYMBOL_TYPE = "aux_symbol_type";
	private static final String AUX_COLOR_CODE = "aux_color_code";
	private static final String AUX_EXTENDED_LABEL = "aux_extended_label";
	private static final String AUX_RESTRICTED = "aux_restricted";
	private static final String AUX_FONT_TYPE = "aux_font_type";
	private static final String AUX_RESERVED = "aux_reserved";

	private Integer ID;
	private String label;
	private Double lat_rad;
	private Double lon_rad;
	private Double z_position_m;
	private Double turnradius_nm;
	private Integer crosstracklimit_stbd_m;
	private Integer crosstracklimit_port_m;
	private Double speed_kts;
	private Integer ETA;
	private Double speedlimit_upper_kts;
	private Double speedlimit_lower_kts;
	private Double distance_to_wp_nm;
	private Double bearing_to_wp_rad;
	private Integer aux_symbol_type;
	private Integer aux_color_code;
	private String aux_extended_label;
	private Integer aux_restricted;
	private Integer aux_font_type;
	private Integer aux_reserved;

	@Override
	public void fillMap(KeyValueObject<? extends IMetaAttribute> map,
			String prefix) {
		prefix += ELEMENT_PREFIX;
		String elementPrefix = prefix + "_";

		if (ID != null)
			map.addAttributeValue(elementPrefix + ID_CONST, ID);
		if (label != null)
			map.addAttributeValue(elementPrefix + LABEL, label);
		if (lat_rad != null)
			map.addAttributeValue(elementPrefix + LAT_RAD, lat_rad);
		if (lon_rad != null)
			map.addAttributeValue(elementPrefix + LON_RAD, lon_rad);
		if (z_position_m != null)
			map.addAttributeValue(elementPrefix + Z_POSITION_M, z_position_m);
		if (turnradius_nm != null)
			map.addAttributeValue(elementPrefix + TURNRADIUS_NM, turnradius_nm);
		if (crosstracklimit_stbd_m != null)
			map.addAttributeValue(elementPrefix + CROSSTRACKLIMIT_STBD_M,
					crosstracklimit_stbd_m);
		if (crosstracklimit_port_m != null)
			map.addAttributeValue(elementPrefix + CROSSTRACKLIMIT_PORT_M,
					crosstracklimit_port_m);
		if (speed_kts != null)
			map.addAttributeValue(elementPrefix + SPEED_KTS, speed_kts);
		if (ETA != null)
			map.addAttributeValue(elementPrefix + ETA_CONST, ETA);
		if (speedlimit_upper_kts != null)
			map.addAttributeValue(elementPrefix + SPEEDLIMIT_UPPER_KTS,
					speedlimit_upper_kts);
		if (speedlimit_lower_kts != null)
			map.addAttributeValue(elementPrefix + SPEEDLIMIT_LOWER_KTS,
					speedlimit_lower_kts);
		if (distance_to_wp_nm != null)
			map.addAttributeValue(elementPrefix + DISTANCE_TO_WP_NM,
					distance_to_wp_nm);
		if (bearing_to_wp_rad != null)
			map.addAttributeValue(elementPrefix + BEARING_TO_WP_RAD,
					bearing_to_wp_rad);
		if (aux_symbol_type != null)
			map.addAttributeValue(elementPrefix + AUX_SYMBOL_TYPE,
					aux_symbol_type);
		if (aux_color_code != null)
			map.addAttributeValue(elementPrefix + AUX_COLOR_CODE,
					aux_color_code);
		if (aux_extended_label != null)
			map.addAttributeValue(elementPrefix + AUX_EXTENDED_LABEL,
					aux_extended_label);
		if (aux_restricted != null)
			map.addAttributeValue(elementPrefix + AUX_RESTRICTED,
					aux_restricted);
		if (aux_font_type != null)
			map.addAttributeValue(elementPrefix + AUX_FONT_TYPE, aux_font_type);
		if (aux_reserved != null)
			map.addAttributeValue(elementPrefix + AUX_RESERVED, aux_reserved);
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public double getLat_rad() {
		return lat_rad;
	}

	public void setLat_rad(double lat_rad) {
		this.lat_rad = lat_rad;
	}

	public double getLon_rad() {
		return lon_rad;
	}

	public void setLon_rad(double lon_rad) {
		this.lon_rad = lon_rad;
	}

	public double getZ_position_m() {
		return z_position_m;
	}

	public void setZ_position_m(double z_position_m) {
		this.z_position_m = z_position_m;
	}

	public double getTurnradius_nm() {
		return turnradius_nm;
	}

	public void setTurnradius_nm(double turnradius_nm) {
		this.turnradius_nm = turnradius_nm;
	}

	public int getCrosstracklimit_stbd_m() {
		return crosstracklimit_stbd_m;
	}

	public void setCrosstracklimit_stbd_m(int crosstracklimit_stbd_m) {
		this.crosstracklimit_stbd_m = crosstracklimit_stbd_m;
	}

	public int getCrosstracklimit_port_m() {
		return crosstracklimit_port_m;
	}

	public void setCrosstracklimit_port_m(int crosstracklimit_port_m) {
		this.crosstracklimit_port_m = crosstracklimit_port_m;
	}

	public double getSpeed_kts() {
		return speed_kts;
	}

	public void setSpeed_kts(double speed_kts) {
		this.speed_kts = speed_kts;
	}

	public int getETA() {
		return ETA;
	}

	public void setETA(int eTA) {
		ETA = eTA;
	}

	public double getSpeedlimit_upper_kts() {
		return speedlimit_upper_kts;
	}

	public void setSpeedlimit_upper_kts(double speedlimit_upper_kts) {
		this.speedlimit_upper_kts = speedlimit_upper_kts;
	}

	public double getSpeedlimit_lower_kts() {
		return speedlimit_lower_kts;
	}

	public void setSpeedlimit_lower_kts(double speedlimit_lower_kts) {
		this.speedlimit_lower_kts = speedlimit_lower_kts;
	}

	public double getDistance_to_wp_nm() {
		return distance_to_wp_nm;
	}

	public void setDistance_to_wp_nm(double distance_to_wp_nm) {
		this.distance_to_wp_nm = distance_to_wp_nm;
	}

	public double getBearing_to_wp_rad() {
		return bearing_to_wp_rad;
	}

	public void setBearing_to_wp_rad(double bearing_to_wp_rad) {
		this.bearing_to_wp_rad = bearing_to_wp_rad;
	}

	public int getAux_symbol_type() {
		return aux_symbol_type;
	}

	public void setAux_symbol_type(int aux_symbol_type) {
		this.aux_symbol_type = aux_symbol_type;
	}

	public int getAux_color_code() {
		return aux_color_code;
	}

	public void setAux_color_code(int aux_color_code) {
		this.aux_color_code = aux_color_code;
	}

	public String getAux_extended_label() {
		return aux_extended_label;
	}

	public void setAux_extended_label(String aux_extended_label) {
		this.aux_extended_label = aux_extended_label;
	}

	public int getAux_restricted() {
		return aux_restricted;
	}

	public void setAux_restricted(int aux_restricted) {
		this.aux_restricted = aux_restricted;
	}

	public int getAux_font_type() {
		return aux_font_type;
	}

	public void setAux_font_type(int aux_font_type) {
		this.aux_font_type = aux_font_type;
	}

	public int getAux_reserved() {
		return aux_reserved;
	}

	public void setAux_reserved(int aux_reserved) {
		this.aux_reserved = aux_reserved;
	}

}
