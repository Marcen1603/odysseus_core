package de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

public class PredictionDataItem implements IShipRouteRootElement {
	// Constants
	public static final String DATA_ITEM_ID = "data_item_id";

	private String data_item_id;

	@Override
	public void fillMap(KeyValueObject<? extends IMetaAttribute> map,
			String prefix) {
		if (data_item_id != null)
			map.addAttributeValue(prefix + DATA_ITEM_ID, data_item_id);
		// if (route != null)
		// route.fillMap(map, prefix);
	}

	@Override
	public KeyValueObject<? extends IMetaAttribute> toMap() {
		KeyValueObject<? extends IMetaAttribute> map = new KeyValueObject<>();
		fillMap(map, "");
		return map;
	}
}
