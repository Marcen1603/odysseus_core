package de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.manoeuvre;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.IShipRouteElement;

public class SternThruster implements IShipRouteElement {
	private static final String ELEMENT_PREFIX = "_sternthr";

	private static final String STERNTHR1_CMD = "sternthr_1_cmd_perc";
	private static final String STERNTHR2_CMD = "sternthr_2_cmd_perc";
	private static final String STERNTHR3_CMD = "sternthr_3_cmd_perc";
	private static final String STERNTHR4_CMD = "sternthr_4_cmd_perc";
	private static final String STERNTHR1_ACT = "sternthr_1_act_perc";
	private static final String STERNTHR2_ACT = "sternthr_2_act_perc";
	private static final String STERNTHR3_ACT = "sternthr_3_act_perc";
	private static final String STERNTHR4_ACT = "sternthr_4_act_perc";

	private Double sternthr_1_cmd_perc;
	private Double sternthr_2_cmd_perc;
	private Double sternthr_3_cmd_perc;
	private Double sternthr_4_cmd_perc;
	private Double sternthr_1_act_perc;
	private Double sternthr_2_act_perc;
	private Double sternthr_3_act_perc;
	private Double sternthr_4_act_perc;

	@Override
	public void fillMap(KeyValueObject<? extends IMetaAttribute> map,
			String prefix) {
		prefix += ELEMENT_PREFIX;
		String elementPrefix = prefix + "_";

		if (sternthr_1_cmd_perc != null)
			map.addAttributeValue(elementPrefix + STERNTHR1_CMD,
					sternthr_1_cmd_perc);
		if (sternthr_2_cmd_perc != null)
			map.addAttributeValue(elementPrefix + STERNTHR2_CMD,
					sternthr_2_cmd_perc);
		if (sternthr_3_cmd_perc != null)
			map.addAttributeValue(elementPrefix + STERNTHR3_CMD,
					sternthr_3_cmd_perc);
		if (sternthr_4_cmd_perc != null)
			map.addAttributeValue(elementPrefix + STERNTHR4_CMD,
					sternthr_4_cmd_perc);
		if (sternthr_1_act_perc != null)
			map.addAttributeValue(elementPrefix + STERNTHR1_ACT,
					sternthr_1_act_perc);
		if (sternthr_2_act_perc != null)
			map.addAttributeValue(elementPrefix + STERNTHR2_ACT,
					sternthr_2_act_perc);
		if (sternthr_3_act_perc != null)
			map.addAttributeValue(elementPrefix + STERNTHR3_ACT,
					sternthr_3_act_perc);
		if (sternthr_4_act_perc != null)
			map.addAttributeValue(elementPrefix + STERNTHR4_ACT,
					sternthr_4_act_perc);
	}

	public Double getSternthr_1_cmd_perc() {
		return sternthr_1_cmd_perc;
	}

	public void setSternthr_1_cmd_perc(Double sternthr_1_cmd_perc) {
		this.sternthr_1_cmd_perc = sternthr_1_cmd_perc;
	}

	public Double getSternthr_2_cmd_perc() {
		return sternthr_2_cmd_perc;
	}

	public void setSternthr_2_cmd_perc(Double sternthr_2_cmd_perc) {
		this.sternthr_2_cmd_perc = sternthr_2_cmd_perc;
	}

	public Double getSternthr_3_cmd_perc() {
		return sternthr_3_cmd_perc;
	}

	public void setSternthr_3_cmd_perc(Double sternthr_3_cmd_perc) {
		this.sternthr_3_cmd_perc = sternthr_3_cmd_perc;
	}

	public Double getSternthr_4_cmd_perc() {
		return sternthr_4_cmd_perc;
	}

	public void setSternthr_4_cmd_perc(Double sternthr_4_cmd_perc) {
		this.sternthr_4_cmd_perc = sternthr_4_cmd_perc;
	}

	public Double getSternthr_1_act_perc() {
		return sternthr_1_act_perc;
	}

	public void setSternthr_1_act_perc(Double sternthr_1_act_perc) {
		this.sternthr_1_act_perc = sternthr_1_act_perc;
	}

	public Double getSternthr_2_act_perc() {
		return sternthr_2_act_perc;
	}

	public void setSternthr_2_act_perc(Double sternthr_2_act_perc) {
		this.sternthr_2_act_perc = sternthr_2_act_perc;
	}

	public Double getSternthr_3_act_perc() {
		return sternthr_3_act_perc;
	}

	public void setSternthr_3_act_perc(Double sternthr_3_act_perc) {
		this.sternthr_3_act_perc = sternthr_3_act_perc;
	}

	public Double getSternthr_4_act_perc() {
		return sternthr_4_act_perc;
	}

	public void setSternthr_4_act_perc(Double sternthr_4_act_perc) {
		this.sternthr_4_act_perc = sternthr_4_act_perc;
	}

}
