package de.uniol.inf.is.odysseus.nlp.datastructure.annotations;

import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;

/**
 * This interface allows inherited objects to be transformed into a keyvalueobject, or something that can be inserted into one.
 */
public interface IKeyValueObject {
	
	/**
	 * Creates new Object, that can be used inside a {@link KeyValueObject} for output usages.
	 * @return Object that can be used inside a {@link KeyValueObject}
	 */
	public Object toObject();
}
