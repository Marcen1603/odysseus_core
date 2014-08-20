package de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.manoeuvre;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.IShipRouteElement;

public class Rpm implements IShipRouteElement {
	private static final String ELEMENT_PREFIX = "_rpm";

	private static final String RPM1_CMD = "rpm1_cmd_rpm";
	private static final String RPM2_CMD = "rpm2_cmd_rpm";
	private static final String RPM3_CMD = "rpm3_cmd_rpm";
	private static final String RPM4_CMD = "rpm4_cmd_rpm";
	private static final String RPM1_ACT = "rpm1_act_rpm";
	private static final String RPM2_ACT = "rpm2_act_rpm";
	private static final String RPM3_ACT = "rpm3_act_rpm";
	private static final String RPM4_ACT = "rpm4_act_rpm";

	private Double rpm1_cmd_rpm;
	private Double rpm2_cmd_rpm;
	private Double rpm3_cmd_rpm;
	private Double rpm4_cmd_rpm;
	private Double rpm1_act_rpm;
	private Double rpm2_act_rpm;
	private Double rpm3_act_rpm;
	private Double rpm4_act_rpm;

	@Override
	public void fillMap(KeyValueObject<? extends IMetaAttribute> map,
			String prefix) {
		prefix += ELEMENT_PREFIX;
		String elementPrefix = prefix + "_";

		if (rpm1_cmd_rpm != null)
			map.addAttributeValue(elementPrefix + RPM1_CMD, rpm1_cmd_rpm);
		if (rpm2_cmd_rpm != null)
			map.addAttributeValue(elementPrefix + RPM2_CMD, rpm2_cmd_rpm);
		if (rpm3_cmd_rpm != null)
			map.addAttributeValue(elementPrefix + RPM3_CMD, rpm3_cmd_rpm);
		if (rpm4_cmd_rpm != null)
			map.addAttributeValue(elementPrefix + RPM4_CMD, rpm4_cmd_rpm);
		if (rpm1_act_rpm != null)
			map.addAttributeValue(elementPrefix + RPM1_ACT, rpm1_act_rpm);
		if (rpm2_act_rpm != null)
			map.addAttributeValue(elementPrefix + RPM2_ACT, rpm2_act_rpm);
		if (rpm3_act_rpm != null)
			map.addAttributeValue(elementPrefix + RPM3_ACT, rpm3_act_rpm);
		if (rpm4_act_rpm != null)
			map.addAttributeValue(elementPrefix + RPM4_ACT, rpm4_act_rpm);
	}

	public Double getRpm1_cmd_rpm() {
		return rpm1_cmd_rpm;
	}

	public void setRpm1_cmd_rpm(Double rpm1_cmd_rpm) {
		this.rpm1_cmd_rpm = rpm1_cmd_rpm;
	}

	public Double getRpm2_cmd_rpm() {
		return rpm2_cmd_rpm;
	}

	public void setRpm2_cmd_rpm(Double rpm2_cmd_rpm) {
		this.rpm2_cmd_rpm = rpm2_cmd_rpm;
	}

	public Double getRpm3_cmd_rpm() {
		return rpm3_cmd_rpm;
	}

	public void setRpm3_cmd_rpm(Double rpm3_cmd_rpm) {
		this.rpm3_cmd_rpm = rpm3_cmd_rpm;
	}

	public Double getRpm4_cmd_rpm() {
		return rpm4_cmd_rpm;
	}

	public void setRpm4_cmd_rpm(Double rpm4_cmd_rpm) {
		this.rpm4_cmd_rpm = rpm4_cmd_rpm;
	}

	public Double getRpm1_act_rpm() {
		return rpm1_act_rpm;
	}

	public void setRpm1_act_rpm(Double rpm1_act_rpm) {
		this.rpm1_act_rpm = rpm1_act_rpm;
	}

	public Double getRpm2_act_rpm() {
		return rpm2_act_rpm;
	}

	public void setRpm2_act_rpm(Double rpm2_act_rpm) {
		this.rpm2_act_rpm = rpm2_act_rpm;
	}

	public Double getRpm3_act_rpm() {
		return rpm3_act_rpm;
	}

	public void setRpm3_act_rpm(Double rpm3_act_rpm) {
		this.rpm3_act_rpm = rpm3_act_rpm;
	}

	public Double getRpm4_act_rpm() {
		return rpm4_act_rpm;
	}

	public void setRpm4_act_rpm(Double rpm4_act_rpm) {
		this.rpm4_act_rpm = rpm4_act_rpm;
	}

}
