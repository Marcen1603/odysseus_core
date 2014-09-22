package de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.manoeuvre;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.IShipRouteElement;

public class Pitch implements IShipRouteElement {
	private static final String ELEMENT_PREFIX = "_pitch";

	private static final String PITCH1_CMD = "pitch1_cmd_perc";
	private static final String PITCH2_CMD = "pitch2_cmd_perc";
	private static final String PITCH3_CMD = "pitch3_cmd_perc";
	private static final String PITCH4_CMD = "pitch4_cmd_perc";
	private static final String PITCH1_ACT = "pitch1_act_perc";
	private static final String PITCH2_ACT = "pitch2_act_perc";
	private static final String PITCH3_ACT = "pitch3_act_perc";
	private static final String PITCH4_ACT = "pitch4_act_perc";

	private Double pitch1_cmd_perc;
	private Double pitch2_cmd_perc;
	private Double pitch3_cmd_perc;
	private Double pitch4_cmd_perc;
	private Double pitch1_act_perc;
	private Double pitch2_act_perc;
	private Double pitch3_act_perc;
	private Double pitch4_act_perc;

	@Override
	public void fillMap(KeyValueObject<? extends IMetaAttribute> map,
			String prefix) {
		prefix += ELEMENT_PREFIX;
		String elementPrefix = prefix + "_";

		if (pitch1_cmd_perc != null)
			map.addAttributeValue(elementPrefix + PITCH1_CMD, pitch1_cmd_perc);
		if (pitch2_cmd_perc != null)
			map.addAttributeValue(elementPrefix + PITCH2_CMD, pitch2_cmd_perc);
		if (pitch3_cmd_perc != null)
			map.addAttributeValue(elementPrefix + PITCH3_CMD, pitch3_cmd_perc);
		if (pitch4_cmd_perc != null)
			map.addAttributeValue(elementPrefix + PITCH4_CMD, pitch4_cmd_perc);
		if (pitch1_act_perc != null)
			map.addAttributeValue(elementPrefix + PITCH1_ACT, pitch1_act_perc);
		if (pitch2_act_perc != null)
			map.addAttributeValue(elementPrefix + PITCH2_ACT, pitch2_act_perc);
		if (pitch3_act_perc != null)
			map.addAttributeValue(elementPrefix + PITCH3_ACT, pitch3_act_perc);
		if (pitch4_act_perc != null)
			map.addAttributeValue(elementPrefix + PITCH4_ACT, pitch4_act_perc);
	}

	public Double getPitch1_cmd_perc() {
		return pitch1_cmd_perc;
	}

	public void setPitch1_cmd_perc(Double pitch1_cmd_perc) {
		this.pitch1_cmd_perc = pitch1_cmd_perc;
	}

	public Double getPitch2_cmd_perc() {
		return pitch2_cmd_perc;
	}

	public void setPitch2_cmd_perc(Double pitch2_cmd_perc) {
		this.pitch2_cmd_perc = pitch2_cmd_perc;
	}

	public Double getPitch3_cmd_perc() {
		return pitch3_cmd_perc;
	}

	public void setPitch3_cmd_perc(Double pitch3_cmd_perc) {
		this.pitch3_cmd_perc = pitch3_cmd_perc;
	}

	public Double getPitch4_cmd_perc() {
		return pitch4_cmd_perc;
	}

	public void setPitch4_cmd_perc(Double pitch4_cmd_perc) {
		this.pitch4_cmd_perc = pitch4_cmd_perc;
	}

	public Double getPitch1_act_perc() {
		return pitch1_act_perc;
	}

	public void setPitch1_act_perc(Double pitch1_act_perc) {
		this.pitch1_act_perc = pitch1_act_perc;
	}

	public Double getPitch2_act_perc() {
		return pitch2_act_perc;
	}

	public void setPitch2_act_perc(Double pitch2_act_perc) {
		this.pitch2_act_perc = pitch2_act_perc;
	}

	public Double getPitch3_act_perc() {
		return pitch3_act_perc;
	}

	public void setPitch3_act_perc(Double pitch3_act_perc) {
		this.pitch3_act_perc = pitch3_act_perc;
	}

	public Double getPitch4_act_perc() {
		return pitch4_act_perc;
	}

	public void setPitch4_act_perc(Double pitch4_act_perc) {
		this.pitch4_act_perc = pitch4_act_perc;
	}

}
