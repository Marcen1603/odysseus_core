package de.uniol.inf.is.odysseus.logicaloperator.relational;

import java.util.Arrays;

import de.uniol.inf.is.odysseus.IClone;
import de.uniol.inf.is.odysseus.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;

public class FixedSetAccessAO<T extends IMetaAttributeContainer<? extends IClone>> extends AccessAO {

	private static final long serialVersionUID = -4026927772571867684L;
	private final T[] tuples;
	
	public FixedSetAccessAO(SDFSource sdfSource, T... tuples) {
		super(sdfSource);
		this.tuples = tuples;
	}
	
	public T[] getTuples() {
		return tuples;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(tuples);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("unchecked")
		FixedSetAccessAO other = (FixedSetAccessAO) obj;
		if (!Arrays.equals(tuples, other.tuples))
			return false;
		return true;
	}
}
