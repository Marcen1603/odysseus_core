package de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.prediction;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element.IShipRouteRootElement;

public class PredictionDataItem implements IShipRouteRootElement {
	// Constants
	public static final String DATA_ITEM_ID = "data_item_id";

	private String data_item_id;
	private PredictionPlan mplan;

	@Override
	public void fillMap(KeyValueObject<? extends IMetaAttribute> map,
			String prefix) {
		if (getData_item_id() != null)
			map.addAttributeValue(prefix + DATA_ITEM_ID, getData_item_id());
		if (mplan != null)
			mplan.fillMap(map, prefix);
	}

	@Override
	public KeyValueObject<? extends IMetaAttribute> toMap() {
		KeyValueObject<? extends IMetaAttribute> map = new KeyValueObject<>();
		fillMap(map, "");
		return map;
	}

	public String getData_item_id() {
		return data_item_id;
	}

	public void setData_item_id(String data_item_id) {
		this.data_item_id = data_item_id;
	}

	public PredictionPlan getMplan() {
		return mplan;
	}

	public void setMplan(PredictionPlan mplan) {
		this.mplan = mplan;
	}
}
