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
package de.uniol.inf.is.odysseus.intervalapproach;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetaAttributeContainer;

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
