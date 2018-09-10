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

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;

public interface IMetaAttribute extends IClone, Serializable {

	IMetaAttribute createInstance();
	
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
	List<SDFMetaSchema> getSchema();
	
	/**
	 * Returns all values of the current meta data settings by adding to
	 * the given values list
	 * @param values
	 */
	void retrieveValues(List<Tuple<?>> values);
	
	/**
	 * Write all meta data with new values
	 * @param values A list of tuples that contain meta data. The list must contain a set of tuples where
	 * each tuple corresponds to one single meta data (e.g. TimeInterval) and must have the corresponding schema. 
	 * The list must contain the meta data values in the same order as getSchema() returns 
	 */
	void writeValues(List<Tuple<?>> values);
	
	/**
	 * For all meta data with a single Tuple (for simple meta types)
	 * @param value
	 */
	void writeValue(Tuple<?> value);
	
	/**
	 * Returns the value of the current meta data for the subtype and the position
	 * @param subtype which subtype position (ignored in base types)
	 * @param index the position of the value
	 * @return
	 */
	<K> K getValue(int subtype, int index);
	
//	/**
//	 * Allows to set a single value
//	 * @param subtype which subtype position (ignored in base types)
//	 * @param index which position the value should be set
//	 * @param value which value should be set
//	 */
//	<K> void setValue(int subtype, int index, K value);
	
	/**
	 * Clone this meta attribute
	 */
	@Override
	IMetaAttribute clone();
	
	List<IInlineMetadataMergeFunction<? extends IMetaAttribute>> getInlineMergeFunctions();

}
