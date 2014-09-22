package de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.manoeuvre;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.IShipRouteElement;

public class Rudder implements IShipRouteElement {
	private static final String ELEMENT_PREFIX = "_rudder";

	private static final String RUD1_CMD = "rud1_cmd";
	private static final String RUD2_CMD = "rud2_cmd";
	private static final String RUD3_CMD = "rud3_cmd";
	private static final String RUD4_CMD = "rud4_cmd";
	private static final String RUD1_ACT = "rud1_act";
	private static final String RUD2_ACT = "rud2_act";
	private static final String RUD3_ACT = "rud3_act";
	private static final String RUD4_ACT = "rud4_act";

	private Double rud1_cmd;
	private Double rud2_cmd;
	private Double rud3_cmd;
	private Double rud4_cmd;
	private Double rud1_act;
	private Double rud2_act;
	private Double rud3_act;
	private Double rud4_act;

	@Override
	public void fillMap(KeyValueObject<? extends IMetaAttribute> map,
			String prefix) {
		prefix += ELEMENT_PREFIX;
		String elementPrefix = prefix + "_";

		if (rud1_cmd != null)
			map.addAttributeValue(elementPrefix + RUD1_CMD, rud1_cmd);
		if (rud2_cmd != null)
			map.addAttributeValue(elementPrefix + RUD2_CMD, rud2_cmd);
		if (rud3_cmd != null)
			map.addAttributeValue(elementPrefix + RUD3_CMD, rud3_cmd);
		if (rud4_cmd != null)
			map.addAttributeValue(elementPrefix + RUD4_CMD, rud4_cmd);
		if (rud1_act != null)
			map.addAttributeValue(elementPrefix + RUD1_ACT, rud1_act);
		if (rud2_act != null)
			map.addAttributeValue(elementPrefix + RUD2_ACT, rud2_act);
		if (rud3_act != null)
			map.addAttributeValue(elementPrefix + RUD3_ACT, rud3_act);
		if (rud4_act != null)
			map.addAttributeValue(elementPrefix + RUD4_ACT, rud4_act);
	}

	public Double getRud1_cmd() {
		return rud1_cmd;
	}

	public void setRud1_cmd(Double rud1_cmd) {
		this.rud1_cmd = rud1_cmd;
	}

	public Double getRud2_cmd() {
		return rud2_cmd;
	}

	public void setRud2_cmd(Double rud2_cmd) {
		this.rud2_cmd = rud2_cmd;
	}

	public Double getRud3_cmd() {
		return rud3_cmd;
	}

	public void setRud3_cmd(Double rud3_cmd) {
		this.rud3_cmd = rud3_cmd;
	}

	public Double getRud4_cmd() {
		return rud4_cmd;
	}

	public void setRud4_cmd(Double rud4_cmd) {
		this.rud4_cmd = rud4_cmd;
	}

	public Double getRud1_act() {
		return rud1_act;
	}

	public void setRud1_act(Double rud1_act) {
		this.rud1_act = rud1_act;
	}

	public Double getRud2_act() {
		return rud2_act;
	}

	public void setRud2_act(Double rud2_act) {
		this.rud2_act = rud2_act;
	}

	public Double getRud3_act() {
		return rud3_act;
	}

	public void setRud3_act(Double rud3_act) {
		this.rud3_act = rud3_act;
	}

	public Double getRud4_act() {
		return rud4_act;
	}

	public void setRud4_act(Double rud4_act) {
		this.rud4_act = rud4_act;
	}

}
