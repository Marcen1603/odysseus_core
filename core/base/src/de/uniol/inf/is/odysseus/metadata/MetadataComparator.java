package de.uniol.inf.is.odysseus.metadata;

import java.util.Comparator;

import de.uniol.inf.is.odysseus.IClone;

/**
 * @author Jonas Jacobi
 */
public class MetadataComparator<T extends IClone> implements Comparator<IMetaAttributeContainer<? extends T>> {

	@Override
	@SuppressWarnings("unchecked")
	public int compare(IMetaAttributeContainer<? extends T> o1, IMetaAttributeContainer<? extends T> o2) {
		return ((Comparable<T>)o1.getMetadata()).compareTo(o2.getMetadata());
	}
      
}
