package de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

public class RouteDataItem implements IShipRouteRootElement {
	// Constants
	public static final String DATA_ITEM_ID = "data_item_id";

	private String data_item_id;
	private Route route;

	@Override
	public void fillMap(KeyValueObject<? extends IMetaAttribute> map,
			String prefix) {
		if (data_item_id != null)
			map.addAttributeValue(prefix + DATA_ITEM_ID, data_item_id);
		if (route != null)
			route.fillMap(map, prefix);
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

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

}
