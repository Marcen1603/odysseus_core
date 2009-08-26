package de.uniol.inf.is.odysseus.logicaloperator.relational;

import java.util.Arrays;

import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.logicaloperator.base.AccessAO;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;

public class FixedSetAccessAO<T extends IMetaAttribute<? extends IClone>> extends AccessAO {

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
		FixedSetAccessAO other = (FixedSetAccessAO) obj;
		if (!Arrays.equals(tuples, other.tuples))
			return false;
		return true;
	}
}
