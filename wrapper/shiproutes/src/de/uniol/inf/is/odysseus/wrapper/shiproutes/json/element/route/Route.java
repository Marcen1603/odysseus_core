package de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.route;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.IShipRouteElement;

public class Route implements IShipRouteElement {

	// map constants
	private static final String ELEMENT_PREFIX = "route";

	private static final String SIGNATURE = "signature";
	private static final String BASE_SIGNATURE = "baseSignature";
	private static final String ROUTE_ID = "route_ID";
	private static final String ROUTE_LABEL = "route_label";
	private static final String CROSSTRACKLIMIT_GLOBAL_STBD_M = "crosstracklimit_global_stbd_m";
	private static final String CROSSTRACKLIMIT_GLOBAL_PORT_M = "crosstracklimit_global_port_m";
	private static final String TARGET_ID = "target_ID";
	private static final String ETA_GLOBAL = "ETA_global";
	private static final String AUX_FLAGS = "aux_flags";
	private static final String AUX_RESERVED = "aux_reserved";
	private static final String NUMBER_OF_WP = "number_of_wp";

	private Integer signature;
	private String baseSignature;
	private Integer route_ID;
	private String route_label;
	private Integer crosstracklimit_global_stbd_m;
	private Integer crosstracklimit_global_port_m;
	private Integer target_ID;
	private Integer ETA_global;
	private Integer aux_flags;
	private Integer aux_reserved;
	private Integer number_of_wp;
	private RouteState route_state;
	private List<Waypoint> waypoints;

	@Override
	public void fillMap(KeyValueObject<? extends IMetaAttribute> map,
			String prefix) {
		prefix += ELEMENT_PREFIX;
		String elementPrefix = prefix + "_";

		if (signature != null)
			map.addAttributeValue(elementPrefix + SIGNATURE, signature);
		if (baseSignature != null)
			map.addAttributeValue(elementPrefix + BASE_SIGNATURE, baseSignature);
		if (route_ID != null)
			map.addAttributeValue(elementPrefix + ROUTE_ID, route_ID);
		if (route_label != null)
			map.addAttributeValue(elementPrefix + ROUTE_LABEL, route_label);
		if (crosstracklimit_global_stbd_m != null)
			map.addAttributeValue(
					elementPrefix + CROSSTRACKLIMIT_GLOBAL_STBD_M,
					crosstracklimit_global_stbd_m);
		if (crosstracklimit_global_port_m != null)
			map.addAttributeValue(
					elementPrefix + CROSSTRACKLIMIT_GLOBAL_PORT_M,
					crosstracklimit_global_port_m);
		if (target_ID != null)
			map.addAttributeValue(elementPrefix + TARGET_ID, target_ID);
		if (ETA_global != null)
			map.addAttributeValue(elementPrefix + ETA_GLOBAL, ETA_global);
		if (aux_flags != null)
			map.addAttributeValue(elementPrefix + AUX_FLAGS, aux_flags);
		if (aux_reserved != null)
			map.addAttributeValue(elementPrefix + AUX_RESERVED, aux_reserved);
		if (number_of_wp != null)
			map.addAttributeValue(elementPrefix + NUMBER_OF_WP, number_of_wp);

		if (route_state != null) {
			route_state.fillMap(map, prefix);
		}

		for (Waypoint waypoint : waypoints) {
			waypoint.fillMap(map, prefix);
		}
	}

	public Integer getSignature() {
		return signature;
	}

	public void setSignature(Integer signature) {
		this.signature = signature;
	}

	public String getBaseSignature() {
		return baseSignature;
	}

	public void setBaseSignature(String baseSignature) {
		this.baseSignature = baseSignature;
	}

	public Integer getRoute_ID() {
		return route_ID;
	}

	public void setRoute_ID(Integer route_ID) {
		this.route_ID = route_ID;
	}

	public String getRoute_label() {
		return route_label;
	}

	public void setRoute_label(String route_label) {
		this.route_label = route_label;
	}

	public Integer getCrosstracklimit_global_stbd_m() {
		return crosstracklimit_global_stbd_m;
	}

	public void setCrosstracklimit_global_stbd_m(
			Integer crosstracklimit_global_stbd_m) {
		this.crosstracklimit_global_stbd_m = crosstracklimit_global_stbd_m;
	}

	public Integer getCrosstracklimit_global_port_m() {
		return crosstracklimit_global_port_m;
	}

	public void setCrosstracklimit_global_port_m(
			Integer crosstracklimit_global_port_m) {
		this.crosstracklimit_global_port_m = crosstracklimit_global_port_m;
	}

	public Integer getTarget_ID() {
		return target_ID;
	}

	public void setTarget_ID(Integer target_ID) {
		this.target_ID = target_ID;
	}

	public Integer getETA_global() {
		return ETA_global;
	}

	public void setETA_global(Integer eTA_global) {
		ETA_global = eTA_global;
	}

	public Integer getAux_flags() {
		return aux_flags;
	}

	public void setAux_flags(Integer aux_flags) {
		this.aux_flags = aux_flags;
	}

	public Integer getAux_reserved() {
		return aux_reserved;
	}

	public void setAux_reserved(Integer aux_reserved) {
		this.aux_reserved = aux_reserved;
	}

	public Integer getNumber_of_wp() {
		return number_of_wp;
	}

	public void setNumber_of_wp(Integer number_of_wp) {
		this.number_of_wp = number_of_wp;
	}

	public RouteState getRoute_state() {
		return route_state;
	}

	public void setRoute_state(RouteState route_state) {
		this.route_state = route_state;
	}

	public List<Waypoint> getWaypoints() {
		return waypoints;
	}

	public void setWaypoints(List<Waypoint> waypoints) {
		this.waypoints = waypoints;
	}

	
}
