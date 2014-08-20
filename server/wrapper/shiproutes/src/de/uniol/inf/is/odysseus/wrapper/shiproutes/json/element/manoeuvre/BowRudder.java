package de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.manoeuvre;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.IShipRouteElement;

public class BowRudder implements IShipRouteElement {
	private static final String ELEMENT_PREFIX = "_bowrud";

	private static final String BOWRUD1_CMD = "bowrud1_cmd";
	private static final String BOWRUD2_CMD = "bowrud2_cmd";
	private static final String BOWRUD3_CMD = "bowrud3_cmd";
	private static final String BOWRUD4_CMD = "bowrud4_cmd";
	private static final String BOWRUD1_ACT = "bowrud1_act";
	private static final String BOWRUD2_ACT = "bowrud2_act";
	private static final String BOWRUD3_ACT = "bowrud3_act";
	private static final String BOWRUD4_ACT = "bowrud4_act";

	private Double bowrud1_cmd;
	private Double bowrud2_cmd;
	private Double bowrud3_cmd;
	private Double bowrud4_cmd;
	private Double bowrud1_act;
	private Double bowrud2_act;
	private Double bowrud3_act;
	private Double bowrud4_act;

	@Override
	public void fillMap(KeyValueObject<? extends IMetaAttribute> map,
			String prefix) {
		prefix += ELEMENT_PREFIX;
		String elementPrefix = prefix + "_";

		if (bowrud1_cmd != null)
			map.addAttributeValue(elementPrefix + BOWRUD1_CMD, bowrud1_cmd);
		if (bowrud2_cmd != null)
			map.addAttributeValue(elementPrefix + BOWRUD2_CMD, bowrud2_cmd);
		if (bowrud3_cmd != null)
			map.addAttributeValue(elementPrefix + BOWRUD3_CMD, bowrud3_cmd);
		if (bowrud4_cmd != null)
			map.addAttributeValue(elementPrefix + BOWRUD4_CMD, bowrud4_cmd);
		if (bowrud1_act != null)
			map.addAttributeValue(elementPrefix + BOWRUD1_ACT, bowrud1_act);
		if (bowrud2_act != null)
			map.addAttributeValue(elementPrefix + BOWRUD2_ACT, bowrud2_act);
		if (bowrud3_act != null)
			map.addAttributeValue(elementPrefix + BOWRUD3_ACT, bowrud3_act);
		if (bowrud4_act != null)
			map.addAttributeValue(elementPrefix + BOWRUD4_ACT, bowrud4_act);
	}

	public Double getBowrud1_cmd() {
		return bowrud1_cmd;
	}

	public void setBowrud1_cmd(Double bowrud1_cmd) {
		this.bowrud1_cmd = bowrud1_cmd;
	}

	public Double getBowrud2_cmd() {
		return bowrud2_cmd;
	}

	public void setBowrud2_cmd(Double bowrud2_cmd) {
		this.bowrud2_cmd = bowrud2_cmd;
	}

	public Double getBowrud3_cmd() {
		return bowrud3_cmd;
	}

	public void setBowrud3_cmd(Double bowrud3_cmd) {
		this.bowrud3_cmd = bowrud3_cmd;
	}

	public Double getBowrud4_cmd() {
		return bowrud4_cmd;
	}

	public void setBowrud4_cmd(Double bowrud4_cmd) {
		this.bowrud4_cmd = bowrud4_cmd;
	}

	public Double getBowrud1_act() {
		return bowrud1_act;
	}

	public void setBowrud1_act(Double bowrud1_act) {
		this.bowrud1_act = bowrud1_act;
	}

	public Double getBowrud2_act() {
		return bowrud2_act;
	}

	public void setBowrud2_act(Double bowrud2_act) {
		this.bowrud2_act = bowrud2_act;
	}

	public Double getBowrud3_act() {
		return bowrud3_act;
	}

	public void setBowrud3_act(Double bowrud3_act) {
		this.bowrud3_act = bowrud3_act;
	}

	public Double getBowrud4_act() {
		return bowrud4_act;
	}

	public void setBowrud4_act(Double bowrud4_act) {
		this.bowrud4_act = bowrud4_act;
	}

}
