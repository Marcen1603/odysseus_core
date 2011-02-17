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
package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

import de.uniol.inf.is.odysseus.IClone;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;

/**
 * The IConnectionContainer contains an connection list where the rated connections between
 * objects are stored. It serves as a meta data so that in the objecttracking process
 * you could access the connections by object.getMetadata().get/setConnectionList().
 *
 * @author Volker Janz
 *
 */
public interface IConnectionContainer extends IMetaAttribute, IClone{

	public void setConnectionList(ConnectionList list);
	public ConnectionList getConnectionList();

}
