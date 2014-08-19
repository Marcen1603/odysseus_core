package de.uniol.inf.is.odysseus.wrapper.shiproutes.json.element;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

public interface IShipRouteRootElement extends IShipRouteElement{

	KeyValueObject<? extends IMetaAttribute> toMap();
}
