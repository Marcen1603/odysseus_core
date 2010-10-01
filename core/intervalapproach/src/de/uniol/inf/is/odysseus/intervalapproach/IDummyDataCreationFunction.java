package de.uniol.inf.is.odysseus.intervalapproach;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;

/**
 * IDummyDataCreationFunction is used to create data with special properties (e.g. set a priority
 * to zero or change a time interval to a predefinend size) based uppon an existing data stream item.
 * @author jan steinke
 *
 * @param <K>
 * @param <T>
 */
public interface IDummyDataCreationFunction<K extends IMetaAttribute,T extends IMetaAttributeContainer<K>> {
		public T createMetadata(T source);
		public boolean hasMetadata(T source);
		public IDummyDataCreationFunction<K, T> clone() ;
}
