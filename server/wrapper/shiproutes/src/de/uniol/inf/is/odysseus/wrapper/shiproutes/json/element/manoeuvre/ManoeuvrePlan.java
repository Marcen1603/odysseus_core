package de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.manoeuvre;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.IShipRouteElement;

public class ManoeuvrePlan implements IShipRouteElement {

	// map constants
	private static final String ELEMENT_PREFIX = "manPlan";

	private static final String MPLAN_ID = "mplan_ID";
	private static final String MPLAN_LABEL = "mplan_label";
	private static final String NUMBER_OF_MP = "number_of_mp";

	private Integer mplan_ID;
	private String mplan_label;
	private Integer number_of_mp;
	private List<ManoeuvrePoint> mpoints;

	@Override
	public void fillMap(KeyValueObject<? extends IMetaAttribute> map,
			String prefix) {
		prefix += ELEMENT_PREFIX;
		String elementPrefix = prefix + "_";

		if (mplan_ID != null)
			map.addAttributeValue(elementPrefix + MPLAN_ID, mplan_ID);
		if (mplan_label != null)
			map.addAttributeValue(elementPrefix + MPLAN_LABEL, mplan_label);
		if (number_of_mp != null)
			map.addAttributeValue(elementPrefix + NUMBER_OF_MP, number_of_mp);
		
		if (mpoints != null){
			for (ManoeuvrePoint manoeuvrePoint : mpoints) {
				manoeuvrePoint.fillMap(map, prefix);
			}
		}
	}

	public Integer getMplan_ID() {
		return mplan_ID;
	}

	public void setMplan_ID(Integer mplan_ID) {
		this.mplan_ID = mplan_ID;
	}

	public String getMplan_label() {
		return mplan_label;
	}

	public void setMplan_label(String mplan_label) {
		this.mplan_label = mplan_label;
	}

	public Integer getNumber_of_mp() {
		return number_of_mp;
	}

	public void setNumber_of_mp(Integer number_of_mp) {
		this.number_of_mp = number_of_mp;
	}

	public List<ManoeuvrePoint> getMpoints() {
		return mpoints;
	}

	public void setMpoints(List<ManoeuvrePoint> mpoints) {
		this.mpoints = mpoints;
	}
}
