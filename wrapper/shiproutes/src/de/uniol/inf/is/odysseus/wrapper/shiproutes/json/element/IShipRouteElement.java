package de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

public interface IShipRouteElement {
	
	public void fillMap(KeyValueObject<? extends IMetaAttribute> map, String prefix);
}
