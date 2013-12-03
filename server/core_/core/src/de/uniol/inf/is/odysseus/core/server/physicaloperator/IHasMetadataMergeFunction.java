package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;

public interface IHasMetadataMergeFunction<K> {

	IMetadataMergeFunction<K> getMetadataMerge();

}
