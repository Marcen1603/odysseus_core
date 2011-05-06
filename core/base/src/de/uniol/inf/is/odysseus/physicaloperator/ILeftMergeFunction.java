package de.uniol.inf.is.odysseus.physicaloperator;

import de.uniol.inf.is.odysseus.IClone;

public interface ILeftMergeFunction<T extends IClone> extends IDataMergeFunction<T>{

	public T createLeftFilledUp(T left);
	
}
