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

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IPipe;

/**
 * Encapsulates the functionality to handle the cleaning process of internal states
 * of an operator in presence of punctuations getting out of date.
 * @author jan
 *
 */
public interface IPunctuationPipe<W,R> extends IPipe<W,R>{
	/**
	 * This function should be called if a punctuation is out of date and some internal
	 * cleaning up is required.
	 * @param punctuation the punctuation getting out of date
	 * @param current the current processed data stream element
	 */
	public boolean cleanInternalStates(PointInTime punctuation, IMetaAttributeContainer<?> current);
}
