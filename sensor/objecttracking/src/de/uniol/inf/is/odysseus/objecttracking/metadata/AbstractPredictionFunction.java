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
package de.uniol.inf.is.odysseus.objecttracking.metadata;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.metadata.MetaAttributeContainer;


public abstract class AbstractPredictionFunction<T extends MetaAttributeContainer<M>, M extends IMetaAttribute> implements IPredictionFunction<T, M>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7643329010512865920L;

	@Override
	public T predictAll(SDFSchema schema, T object, PointInTime t){
		T predictedData = this.predictData(schema, object, t);
		predictedData.setMetadata(this.predictMetadata(schema, object, t));
		
		return predictedData;
	}
	
	@Override
	public abstract AbstractPredictionFunction<T, M> clone();

}
