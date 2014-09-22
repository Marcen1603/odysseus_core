package de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.route;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.IShipRouteElement;

public class RouteState implements IShipRouteElement {

	// map constants
	private static final String ELEMENT_PREFIX = "_routeState";

	private static final String HAS_GEOMETRIC_PROBLEMS = "has_geometric_problems";
	private static final String HAS_ALARMS = "has_alarms";
	private static final String HAS_WARNINGS = "has_warnings";
	private static final String HAS_CAUTIONS = "has_cautions";

	private Boolean has_geometric_problems;
	private Boolean has_alarms;
	private Boolean has_warnings;
	private Boolean has_cautions;

	@Override
	public void fillMap(KeyValueObject<? extends IMetaAttribute> map,
			String prefix) {
		prefix += ELEMENT_PREFIX;
		String elementPrefix = prefix + "_";

		if (has_geometric_problems != null)
			map.addAttributeValue(elementPrefix + HAS_GEOMETRIC_PROBLEMS,
					has_geometric_problems);
		if (has_alarms != null)
			map.addAttributeValue(elementPrefix + HAS_ALARMS, has_alarms);
		if (has_warnings != null)
			map.addAttributeValue(elementPrefix + HAS_WARNINGS, has_warnings);
		if (has_cautions != null)
			map.addAttributeValue(elementPrefix + HAS_CAUTIONS, has_cautions);

	}

	public Boolean getHas_geometric_problems() {
		return has_geometric_problems;
	}

	public void setHas_geometric_problems(Boolean has_geometric_problems) {
		this.has_geometric_problems = has_geometric_problems;
	}

	public Boolean getHas_alarms() {
		return has_alarms;
	}

	public void setHas_alarms(Boolean has_alarms) {
		this.has_alarms = has_alarms;
	}

	public Boolean getHas_warnings() {
		return has_warnings;
	}

	public void setHas_warnings(Boolean has_warnings) {
		this.has_warnings = has_warnings;
	}

	public Boolean getHas_cautions() {
		return has_cautions;
	}

	public void setHas_cautions(Boolean has_cautions) {
		this.has_cautions = has_cautions;
	}
	
	
}
