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
import java.util.List;

import de.uniol.inf.is.odysseus.core.ICSVToString;
import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public interface IMetaAttribute extends IClone, ICSVToString, Serializable {
	/**
	 * In this concrete implementation: What meta data types are contained
	 * @return
	 */
	Class<? extends IMetaAttribute>[] getClasses();
	/**
	 * The name of the meta data type
	 * @return
	 */
	String getName();
	/**
	 * The schema of the meta data type. If this is a combined type, the schemas
	 * are in the same order as getClasses()
	 * @return
	 */
	List<SDFSchema> getSchema();
	
	/**
	 * Returns all values of the current meta data settings by adding to
	 * the given values list
	 * @param values
	 */
	void fillValueList(List<Tuple<?>> values);
	
	/**
	 * Returns the value of the current meta data for the subtype and the position
	 * @param subtype
	 * @param index
	 * @return
	 */
	<K> K getValue(int subtype, int index);
	
	/**
	 * Clone this meta attribute
	 */
	@Override
	IMetaAttribute clone();

}
