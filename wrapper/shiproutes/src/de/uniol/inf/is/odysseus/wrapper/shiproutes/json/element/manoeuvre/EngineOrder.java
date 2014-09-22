package de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.manoeuvre;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.IShipRouteElement;

public class EngineOrder implements IShipRouteElement {
	private static final String ELEMENT_PREFIX = "_engineOrd";

	private static final String EOT1_PERC = "eot1_perc";
	private static final String EOT2_PERC = "eot2_perc";
	private static final String EOT3_PERC = "eot3_perc";
	private static final String EOT4_PERC = "eot4_perc";

	private Integer eot1_perc;
	private Integer eot2_perc;
	private Integer eot3_perc;
	private Integer eot4_perc;

	@Override
	public void fillMap(KeyValueObject<? extends IMetaAttribute> map,
			String prefix) {
		prefix += ELEMENT_PREFIX;
		String elementPrefix = prefix + "_";

		if (eot1_perc != null)
			map.addAttributeValue(elementPrefix + EOT1_PERC, eot1_perc);
		if (eot2_perc != null)
			map.addAttributeValue(elementPrefix + EOT2_PERC, eot2_perc);
		if (eot3_perc != null)
			map.addAttributeValue(elementPrefix + EOT3_PERC, eot3_perc);
		if (eot4_perc != null)
			map.addAttributeValue(elementPrefix + EOT4_PERC, eot4_perc);
	}

	public Integer getEot1_perc() {
		return eot1_perc;
	}

	public void setEot1_perc(Integer eot1_perc) {
		this.eot1_perc = eot1_perc;
	}

	public Integer getEot2_perc() {
		return eot2_perc;
	}

	public void setEot2_perc(Integer eot2_perc) {
		this.eot2_perc = eot2_perc;
	}

	public Integer getEot3_perc() {
		return eot3_perc;
	}

	public void setEot3_perc(Integer eot3_perc) {
		this.eot3_perc = eot3_perc;
	}

	public Integer getEot4_perc() {
		return eot4_perc;
	}

	public void setEot4_perc(Integer eot4_perc) {
		this.eot4_perc = eot4_perc;
	}

}
