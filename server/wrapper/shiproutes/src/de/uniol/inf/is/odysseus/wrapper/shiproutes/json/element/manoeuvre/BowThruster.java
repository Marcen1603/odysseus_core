package de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.manoeuvre;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.IShipRouteElement;

public class BowThruster implements IShipRouteElement {
	private static final String ELEMENT_PREFIX = "_bowthr";

	private static final String BOWTHR1_CMD = "bowthr_1_cmd_perc";
	private static final String BOWTHR2_CMD = "bowthr_2_cmd_perc";
	private static final String BOWTHR3_CMD = "bowthr_3_cmd_perc";
	private static final String BOWTHR4_CMD = "bowthr_4_cmd_perc";
	private static final String BOWTHR1_ACT = "bowthr_1_act_perc";
	private static final String BOWTHR2_ACT = "bowthr_2_act_perc";
	private static final String BOWTHR3_ACT = "bowthr_3_act_perc";
	private static final String BOWTHR4_ACT = "bowthr_4_act_perc";

	private Double bowthr_1_cmd_perc;
	private Double bowthr_2_cmd_perc;
	private Double bowthr_3_cmd_perc;
	private Double bowthr_4_cmd_perc;
	private Double bowthr_1_act_perc;
	private Double bowthr_2_act_perc;
	private Double bowthr_3_act_perc;
	private Double bowthr_4_act_perc;

	@Override
	public void fillMap(KeyValueObject<? extends IMetaAttribute> map,
			String prefix) {
		prefix += ELEMENT_PREFIX;
		String elementPrefix = prefix + "_";

		if (bowthr_1_cmd_perc != null)
			map.addAttributeValue(elementPrefix + BOWTHR1_CMD,
					bowthr_1_cmd_perc);
		if (bowthr_2_cmd_perc != null)
			map.addAttributeValue(elementPrefix + BOWTHR2_CMD,
					bowthr_2_cmd_perc);
		if (bowthr_3_cmd_perc != null)
			map.addAttributeValue(elementPrefix + BOWTHR3_CMD,
					bowthr_3_cmd_perc);
		if (bowthr_4_cmd_perc != null)
			map.addAttributeValue(elementPrefix + BOWTHR4_CMD,
					bowthr_4_cmd_perc);
		if (bowthr_1_act_perc != null)
			map.addAttributeValue(elementPrefix + BOWTHR1_ACT,
					bowthr_1_act_perc);
		if (bowthr_2_act_perc != null)
			map.addAttributeValue(elementPrefix + BOWTHR2_ACT,
					bowthr_2_act_perc);
		if (bowthr_3_act_perc != null)
			map.addAttributeValue(elementPrefix + BOWTHR3_ACT,
					bowthr_3_act_perc);
		if (bowthr_4_act_perc != null)
			map.addAttributeValue(elementPrefix + BOWTHR4_ACT,
					bowthr_4_act_perc);
	}

	public Double getBowthr_1_cmd_perc() {
		return bowthr_1_cmd_perc;
	}

	public void setBowthr_1_cmd_perc(Double bowthr_1_cmd_perc) {
		this.bowthr_1_cmd_perc = bowthr_1_cmd_perc;
	}

	public Double getBowthr_2_cmd_perc() {
		return bowthr_2_cmd_perc;
	}

	public void setBowthr_2_cmd_perc(Double bowthr_2_cmd_perc) {
		this.bowthr_2_cmd_perc = bowthr_2_cmd_perc;
	}

	public Double getBowthr_3_cmd_perc() {
		return bowthr_3_cmd_perc;
	}

	public void setBowthr_3_cmd_perc(Double bowthr_3_cmd_perc) {
		this.bowthr_3_cmd_perc = bowthr_3_cmd_perc;
	}

	public Double getBowthr_4_cmd_perc() {
		return bowthr_4_cmd_perc;
	}

	public void setBowthr_4_cmd_perc(Double bowthr_4_cmd_perc) {
		this.bowthr_4_cmd_perc = bowthr_4_cmd_perc;
	}

	public Double getBowthr_1_act_perc() {
		return bowthr_1_act_perc;
	}

	public void setBowthr_1_act_perc(Double bowthr_1_act_perc) {
		this.bowthr_1_act_perc = bowthr_1_act_perc;
	}

	public Double getBowthr_2_act_perc() {
		return bowthr_2_act_perc;
	}

	public void setBowthr_2_act_perc(Double bowthr_2_act_perc) {
		this.bowthr_2_act_perc = bowthr_2_act_perc;
	}

	public Double getBowthr_3_act_perc() {
		return bowthr_3_act_perc;
	}

	public void setBowthr_3_act_perc(Double bowthr_3_act_perc) {
		this.bowthr_3_act_perc = bowthr_3_act_perc;
	}

	public Double getBowthr_4_act_perc() {
		return bowthr_4_act_perc;
	}

	public void setBowthr_4_act_perc(Double bowthr_4_act_perc) {
		this.bowthr_4_act_perc = bowthr_4_act_perc;
	}

}
