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

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

/**
 * This interface is used to carry a key for a prediction function
 * in tuple. A tuple will not carry the prediction function itself,
 * but key that can be used to find the corresponding prediction
 * function or their transformaed equations in the operators of
 * a query plan.
 * 
 * @author André Bolles
 *
 * @param <K> The type of the key. Usually IPredicate will be used.
 */
public interface IPredictionFunctionKey<K> extends IMetaAttribute, IClone{

	public void setPredictionFunctionKey(K key);
	public K getPredictionFunctionKey();
}
