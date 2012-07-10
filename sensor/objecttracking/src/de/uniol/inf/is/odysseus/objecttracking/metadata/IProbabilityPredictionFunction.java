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
package de.uniol.inf.is.odysseus.objecttracking.metadata;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.MetaAttributeContainer;

/**
 * This interface will be implemented by
 * probability prediction functions, since
 * they have to use a noise matrix for calculating
 * the new covariance matrix.
 * 
 * @author Andre Bolles
 *
 */
public interface IProbabilityPredictionFunction <T extends MetaAttributeContainer<M>, M extends IMetaAttribute> extends IPredictionFunction<T, M>{

	public void setNoiseMatrix(double[][] noiseMatrix);
	
	/**
	 * The restrict list contains the positions of the measurement
	 * attributes in a schema. Since the first measurement attribute
	 * has not necessarily to be the first attribute in the schema,
	 * these positions must be stored explicitly.
	 * 
	 * @param restrictList contains the positions of the measurement attributes in a schema
	 */
	public void setRestrictList(int[] restrictList);
}
