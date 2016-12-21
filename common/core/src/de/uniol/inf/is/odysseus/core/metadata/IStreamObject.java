/**********************************************************************************
 * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.core.metadata;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.Order;

/**
 * @author Jonas Jacobi, Marco Grawunder
 */
public interface IStreamObject<M extends IMetaAttribute> extends
		IClone, Serializable, IStreamable, IProvidesMetadata<M> {

	boolean isTimeProgressMarker();
	void setTimeProgressMarker(boolean timeProgressMarker);

    /**
     *
     */
    IStreamObject<M> merge(IStreamObject<M> left, IStreamObject<M> right, IMetadataMergeFunction<M> metamerge, Order order);

    @Override
    public int hashCode();

    /**
     * Creates a new instance of the current streaming object.
     *
     * @return A new instance of the streaming object class.
     */
    IStreamable newInstance();

    /**
     * Hash code using metadata too
     * @param b
     * @return
     */
	public int hashCode(boolean b);

	int restrictedHashCode(int[] attributeNumbers);

	/**
	 * Method to compare to stream objects, in case of double values, allow tolerance;
	 * @param o
	 * @param tolerance
	 * @return
	 */
	boolean equalsTolerance(Object o, double tolerance);

	boolean equals(IStreamObject<IMetaAttribute> o, boolean compareMeta);

	String toString(boolean printMetadata);

	/**
	 * If an object provides a schema, all input is the same and access can be optmized and
	 * possible processing can be checked at compile time
	 * @return
	 */
	boolean isSchemaLess();

	/**
	 * This methods allows to mark an object with a value
	 * This value should only be used inside a single operator and will not be merged or transferred
	 * @param key
	 * @param value
	 */
	void setTransientMarker(String key, Object value);

	/**
	 * returns true, if the object contains this transient marker
	 * @param key
	 * @return
	 */
	boolean hasTransientMarker(String key);

	/**
	 * returns the value for this transient marker
	 * @param key
	 * @return
	 */
	Object getTransientMarker(String key);

}