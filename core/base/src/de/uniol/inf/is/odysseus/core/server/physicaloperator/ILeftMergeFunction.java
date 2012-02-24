package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import de.uniol.inf.is.odysseus.core.IClone;

public interface ILeftMergeFunction<T extends IClone> extends IDataMergeFunction<T>{

	public T createLeftFilledUp(T left);
	
}
