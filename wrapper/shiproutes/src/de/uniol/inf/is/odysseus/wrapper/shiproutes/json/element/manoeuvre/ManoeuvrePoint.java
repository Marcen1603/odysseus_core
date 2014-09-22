package de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.manoeuvre;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.IShipRouteElement;

public class ManoeuvrePoint implements IShipRouteElement {
	private static final String ELEMENT_PREFIX = "_mPoint";

	private static final String ID_CONST = "ID";
	private static final String LABEL = "label";
	private static final String LAT_RAD = "lat_rad";
	private static final String LON_RAD = "lon_rad";
	private static final String SEGEMENTLEN_SEC = "segmentlen_sec";
	private static final String SOG_LONG_KTS = "sog_long_kts";
	private static final String SOG_TRANS_KTS = "sog_trans_kts";
	private static final String STW_LONG_KTS = "stw_long_kts";
	private static final String STW_TRANS_KTS = "stw_trans_kts";
	private static final String HEADING_RAD = "heading_rad";
	private static final String COURSE_OVER_GROUND_RAD = "course_over_ground_rad";
	private static final String RATE_OF_TURN = "rate_of_turn";
	private static final String WIND_DIR_RAD = "wind_dir_rad";
	private static final String WIND_SPEED_KTS = "wind_speed_kts";
	private static final String CURRENT_DIR_RAD = "current_dir_rad";
	private static final String CURRENT_SPEED_KTS = "current_speed_kts";
	private static final String DEPTH_M = "depth_m";
	private static final String SYNC_EOT_BOOL = "sync_eot_bool";
	private static final String SYNC_RUDDER_BOOL = "sync_rudder_bool";
	private static final String SYNC_BOWTHRUSTER_BOOL = "sync_bowthruster_bool";
	private static final String SYNC_STERNTHRUSTER_BOOL = "sync_sternthruster_bool";

	private Integer ID;
	private String label;
	private Double lat_rad;
	private Double lon_rad;
	private Integer segmentlen_sec;
	private Double sog_long_kts;
	private Double sog_trans_kts;
	private Double stw_long_kts;
	private Double stw_trans_kts;
	private Double heading_rad;
	private Double course_over_ground_rad;
	private Double rate_of_turn;
	private Double wind_dir_rad;
	private Double wind_speed_kts;
	private Double current_dir_rad;
	private Double current_speed_kts;
	private Double depth_m;
	private Integer sync_eot_bool;
	private Integer sync_rudder_bool;
	private Integer sync_bowthruster_bool;
	private Integer sync_sternthruster_bool;

	private EngineOrder engine_order;
	private Rpm rpm_command;
	private Rpm rpm_actual;
	private Pitch pitch_command;
	private Pitch pitch_actual;
	private Rudder rudder_command;
	private Rudder rudder_actual;
	private SternThruster stern_thruster_command;
	private SternThruster stern_thruster_actual;
	private BowThruster bow_thruster_command;
	private BowThruster bow_thruster_actual;
	private BowRudder bow_rudder_command;
	private BowRudder bow_rudder_actual;

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
		if (segmentlen_sec != null)
			map.addAttributeValue(elementPrefix + SEGEMENTLEN_SEC,
					segmentlen_sec);
		if (sog_long_kts != null)
			map.addAttributeValue(elementPrefix + SOG_LONG_KTS, sog_long_kts);
		if (sog_trans_kts != null)
			map.addAttributeValue(elementPrefix + SOG_TRANS_KTS, sog_trans_kts);
		if (stw_long_kts != null)
			map.addAttributeValue(elementPrefix + STW_LONG_KTS, stw_long_kts);
		if (stw_trans_kts != null)
			map.addAttributeValue(elementPrefix + STW_TRANS_KTS, stw_trans_kts);
		if (heading_rad != null)
			map.addAttributeValue(elementPrefix + HEADING_RAD, heading_rad);
		if (course_over_ground_rad != null)
			map.addAttributeValue(elementPrefix + COURSE_OVER_GROUND_RAD,
					course_over_ground_rad);
		if (rate_of_turn != null)
			map.addAttributeValue(elementPrefix + RATE_OF_TURN, rate_of_turn);
		if (wind_dir_rad != null)
			map.addAttributeValue(elementPrefix + WIND_DIR_RAD, wind_dir_rad);
		if (wind_speed_kts != null)
			map.addAttributeValue(elementPrefix + WIND_SPEED_KTS,
					wind_speed_kts);
		if (current_dir_rad != null)
			map.addAttributeValue(elementPrefix + CURRENT_DIR_RAD,
					current_dir_rad);
		if (current_speed_kts != null)
			map.addAttributeValue(elementPrefix + CURRENT_SPEED_KTS,
					current_speed_kts);
		if (depth_m != null)
			map.addAttributeValue(elementPrefix + DEPTH_M, depth_m);
		if (getSync_eot_bool() != null)
			map.addAttributeValue(elementPrefix + SYNC_EOT_BOOL, getSync_eot_bool());
		if (getSync_rudder_bool() != null)
			map.addAttributeValue(elementPrefix + SYNC_RUDDER_BOOL,
					getSync_rudder_bool());
		if (getSync_bowthruster_bool() != null)
			map.addAttributeValue(elementPrefix + SYNC_BOWTHRUSTER_BOOL,
					getSync_bowthruster_bool());
		if (getSync_sternthruster_bool() != null)
			map.addAttributeValue(elementPrefix + SYNC_STERNTHRUSTER_BOOL,
					getSync_sternthruster_bool());

		if (engine_order != null)
			engine_order.fillMap(map, prefix);
		if (rpm_command != null)
			rpm_command.fillMap(map, prefix);
		if (rpm_actual != null)
			rpm_actual.fillMap(map, prefix);
		if (pitch_command != null)
			pitch_command.fillMap(map, prefix);
		if (pitch_actual != null)
			pitch_actual.fillMap(map, prefix);
		if (rudder_command != null)
			rudder_command.fillMap(map, prefix);
		if (rudder_actual != null)
			rudder_actual.fillMap(map, prefix);
		if (stern_thruster_command != null)
			stern_thruster_command.fillMap(map, prefix);
		if (stern_thruster_actual != null)
			stern_thruster_actual.fillMap(map, prefix);
		if (bow_thruster_command != null)
			bow_thruster_command.fillMap(map, prefix);
		if (bow_thruster_actual != null)
			bow_thruster_actual.fillMap(map, prefix);
		if (bow_rudder_command != null)
			bow_rudder_command.fillMap(map, prefix);
		if (bow_rudder_actual != null)
			bow_rudder_actual.fillMap(map, prefix);
	}

	public Integer getID() {
		return ID;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
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

	public Integer getSegmentlen_sec() {
		return segmentlen_sec;
	}

	public void setSegmentlen_sec(Integer segmentlen_sec) {
		this.segmentlen_sec = segmentlen_sec;
	}

	public Double getSog_long_kts() {
		return sog_long_kts;
	}

	public void setSog_long_kts(Double sog_long_kts) {
		this.sog_long_kts = sog_long_kts;
	}

	public Double getSog_trans_kts() {
		return sog_trans_kts;
	}

	public void setSog_trans_kts(Double sog_trans_kts) {
		this.sog_trans_kts = sog_trans_kts;
	}

	public Double getStw_long_kts() {
		return stw_long_kts;
	}

	public void setStw_long_kts(Double stw_long_kts) {
		this.stw_long_kts = stw_long_kts;
	}

	public Double getStw_trans_kts() {
		return stw_trans_kts;
	}

	public void setStw_trans_kts(Double stw_trans_kts) {
		this.stw_trans_kts = stw_trans_kts;
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

	public Double getWind_dir_rad() {
		return wind_dir_rad;
	}

	public void setWind_dir_rad(Double wind_dir_rad) {
		this.wind_dir_rad = wind_dir_rad;
	}

	public Double getWind_speed_kts() {
		return wind_speed_kts;
	}

	public void setWind_speed_kts(Double wind_speed_kts) {
		this.wind_speed_kts = wind_speed_kts;
	}

	public Double getCurrent_dir_rad() {
		return current_dir_rad;
	}

	public void setCurrent_dir_rad(Double current_dir_rad) {
		this.current_dir_rad = current_dir_rad;
	}

	public Double getCurrent_speed_kts() {
		return current_speed_kts;
	}

	public void setCurrent_speed_kts(Double current_speed_kts) {
		this.current_speed_kts = current_speed_kts;
	}

	public Double getDepth_m() {
		return depth_m;
	}

	public void setDepth_m(Double depth_m) {
		this.depth_m = depth_m;
	}

	public EngineOrder getEngine_order() {
		return engine_order;
	}

	public void setEngine_order(EngineOrder engine_order) {
		this.engine_order = engine_order;
	}

	public Rpm getRpm_command() {
		return rpm_command;
	}

	public void setRpm_command(Rpm rpm_command) {
		this.rpm_command = rpm_command;
	}

	public Rpm getRpm_actual() {
		return rpm_actual;
	}

	public void setRpm_actual(Rpm rpm_actual) {
		this.rpm_actual = rpm_actual;
	}

	public Pitch getPitch_command() {
		return pitch_command;
	}

	public void setPitch_command(Pitch pitch_command) {
		this.pitch_command = pitch_command;
	}

	public Pitch getPitch_actual() {
		return pitch_actual;
	}

	public void setPitch_actual(Pitch pitch_actual) {
		this.pitch_actual = pitch_actual;
	}

	public Rudder getRudder_command() {
		return rudder_command;
	}

	public void setRudder_command(Rudder rudder_command) {
		this.rudder_command = rudder_command;
	}

	public Rudder getRudder_actual() {
		return rudder_actual;
	}

	public void setRudder_actual(Rudder rudder_actual) {
		this.rudder_actual = rudder_actual;
	}

	public SternThruster getStern_thruster_command() {
		return stern_thruster_command;
	}

	public void setStern_thruster_command(SternThruster stern_thruster_command) {
		this.stern_thruster_command = stern_thruster_command;
	}

	public SternThruster getStern_thruster_actual() {
		return stern_thruster_actual;
	}

	public void setStern_thruster_actual(SternThruster stern_thruster_actual) {
		this.stern_thruster_actual = stern_thruster_actual;
	}

	public BowThruster getBow_thruster_command() {
		return bow_thruster_command;
	}

	public void setBow_thruster_command(BowThruster bow_thruster_command) {
		this.bow_thruster_command = bow_thruster_command;
	}

	public BowThruster getBow_thruster_actual() {
		return bow_thruster_actual;
	}

	public void setBow_thruster_actual(BowThruster bow_thruster_actual) {
		this.bow_thruster_actual = bow_thruster_actual;
	}

	public BowRudder getBow_rudder_command() {
		return bow_rudder_command;
	}

	public void setBow_rudder_command(BowRudder bow_rudder_command) {
		this.bow_rudder_command = bow_rudder_command;
	}

	public BowRudder getBow_rudder_actual() {
		return bow_rudder_actual;
	}

	public void setBow_rudder_actual(BowRudder bow_rudder_actual) {
		this.bow_rudder_actual = bow_rudder_actual;
	}

	public Integer getSync_eot_bool() {
		return sync_eot_bool;
	}

	public void setSync_eot_bool(Integer sync_eot_bool) {
		this.sync_eot_bool = sync_eot_bool;
	}

	public Integer getSync_rudder_bool() {
		return sync_rudder_bool;
	}

	public void setSync_rudder_bool(Integer sync_rudder_bool) {
		this.sync_rudder_bool = sync_rudder_bool;
	}

	public Integer getSync_bowthruster_bool() {
		return sync_bowthruster_bool;
	}

	public void setSync_bowthruster_bool(Integer sync_bowthruster_bool) {
		this.sync_bowthruster_bool = sync_bowthruster_bool;
	}

	public Integer getSync_sternthruster_bool() {
		return sync_sternthruster_bool;
	}

	public void setSync_sternthruster_bool(Integer sync_sternthruster_bool) {
		this.sync_sternthruster_bool = sync_sternthruster_bool;
	}

}
