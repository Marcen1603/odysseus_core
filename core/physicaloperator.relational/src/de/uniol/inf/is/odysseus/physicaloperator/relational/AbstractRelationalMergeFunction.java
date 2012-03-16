/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.physicaloperator.relational;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.relational.base.Tuple;

public abstract class AbstractRelationalMergeFunction<T extends Tuple<M>, M extends IMetaAttribute> implements IDataMergeFunction<T> {

	protected int schemaSize;
	
	protected AbstractRelationalMergeFunction(int outputSchemaSize){
		this.schemaSize = outputSchemaSize;
	}
	
	protected Object[] mergeAttributes(Object[] leftAttributes, Object[] rightAttributes){
		Object[] newAttributes = new Object[this.schemaSize];
		if (leftAttributes != null) {
			System.arraycopy(leftAttributes, 0, newAttributes, 0,
					leftAttributes.length);
		}
		if (rightAttributes != null) {
			System.arraycopy(rightAttributes, 0, newAttributes, this.schemaSize
					- rightAttributes.length, rightAttributes.length);
		}
		return newAttributes;
	}
	
	
	@Override
	public abstract AbstractRelationalMergeFunction<T, M> clone();
}
