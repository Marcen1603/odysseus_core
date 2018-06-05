package de.uniol.inf.is.odysseus.core.metadata;

import de.uniol.inf.is.odysseus.core.collection.Tuple;

abstract public class AbstractCombinedMetaAttribute extends AbstractMetaAttribute {
	
	private static final long serialVersionUID = -7497027906886410189L;
	
	@Override
	final public void writeValue(Tuple<?> value) {
		throw new IllegalArgumentException("Cannot call writeValue with single value on a combined value attribute");
	}

}
