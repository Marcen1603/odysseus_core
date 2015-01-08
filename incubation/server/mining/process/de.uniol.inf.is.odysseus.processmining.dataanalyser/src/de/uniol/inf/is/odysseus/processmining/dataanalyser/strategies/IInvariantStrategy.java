package de.uniol.inf.is.odysseus.processmining.dataanalyser.strategies;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.processmining.dataanalyser.models.IInvariantResult;

public interface IInvariantStrategy<T extends IMetaAttribute> {
	public IInvariantResult calculateStrategy(Tuple<T> mostRecent, Tuple<T> current);
}
