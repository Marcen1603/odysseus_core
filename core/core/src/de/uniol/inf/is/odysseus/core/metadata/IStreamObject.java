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
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

/**
 * @author Jonas Jacobi, Marco Grawunder
 */
public interface IStreamObject<M extends IMetaAttribute> extends
		IClone, Serializable {
	/**
	 * Gets the default meta data used processing purposes (e.g. timestamps, priority or latency)
	 * This meta data is assigned to each object in the meta data creation po 
	 * 
	 * @return
	 */
	public M getMetadata();

	/**
	 * Set the metadata object of this tuple. Typically done from the meta data creation po
	 * @param metadata
	 */
	public void setMetadata(M metadata);

	/**
	 * Returns true if the element is in order
	 * 
	 * @return
	 */
	boolean isInOrder();

	/**
	 * Set to true to state the element as in order, else out of order
	 * 
	 * @param inOrder
	 */
	void setInOrder(boolean inOrder);

	/** 
	 * Allow any object to be attached, do not use this method for
	 * often accessed meta data, (e.g. timestamps)
	 * 
	 * @param name The name (key) of the meta data, if key already exists the
	 * content will be overwritten
	 * @param content The content to be stored
	 */
	void setMetadata(String name, Object content);
	
	/**
	 * Retrieve attached meta data object with name
	 * 
	 * @param name The name (key) of the meta data
	 * @return the stored meta data object
	 */
	Object getMetadata(String name);


}