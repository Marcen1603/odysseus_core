package de.uniol.inf.is.odysseus.metadata.base;

import java.util.Comparator;

import de.uniol.inf.is.odysseus.base.IClone;

/**
 * @author Jonas Jacobi
 */
public class MetadataComparator<T extends IClone> implements Comparator<IMetaAttribute<? extends T>> {

	@SuppressWarnings("unchecked")
	public int compare(IMetaAttribute<? extends T> o1, IMetaAttribute<? extends T> o2) {
		return ((Comparable<T>)o1.getMetadata()).compareTo(o2.getMetadata());
	}
      
}
