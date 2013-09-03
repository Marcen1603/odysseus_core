package de.uniol.inf.is.odysseus.core.server.store;

import java.util.Map.Entry;


abstract public class AbstractStore<IDType extends Comparable<?>, STORETYPE> implements
		IStore<IDType, STORETYPE> {

	@Override
	public void dumpTo(StringBuffer buffer) {
		for (Entry<?, ?> e : entrySet()) {
			buffer.append(e.getKey()).append("-->").append(e.getValue());
		}
	}

}
