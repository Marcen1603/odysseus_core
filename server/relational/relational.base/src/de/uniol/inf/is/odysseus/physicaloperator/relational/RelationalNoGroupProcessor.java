package de.uniol.inf.is.odysseus.physicaloperator.relational;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.NoGroupProcessor;

public class RelationalNoGroupProcessor<T extends IMetaAttribute> extends NoGroupProcessor<Tuple<T>, Tuple<T>> {
	
	private static final long serialVersionUID = -4691145764024320101L;

	@SuppressWarnings("rawtypes")
	static final RelationalNoGroupProcessor instance = new RelationalNoGroupProcessor();
	
	@SuppressWarnings("rawtypes")
	public static RelationalNoGroupProcessor getInstance() {
		return instance;
	}
	
	@Override
	public Tuple<T> getGroupingPart(Tuple<T> elem) {
		return elem.restrict(null, true);
	}

}
