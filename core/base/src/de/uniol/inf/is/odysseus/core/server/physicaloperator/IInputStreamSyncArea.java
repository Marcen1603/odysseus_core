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
package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetaAttributeContainer;

/**
 * Diese Klasse dient dazu, dem zugehoerigen Operator die Daten aus mehreren Eingabenstroemen 
 * in einer stromuebergreifdenden sortierten Ordnung zur Verfuegung zu stellen
 * 
 *
 * @param <T> Datentyp der Elemente, die Verarbeitet werden sollen.
 */
public interface IInputStreamSyncArea<T extends IMetaAttributeContainer<?>> extends IClone {
	
	/**
	 * Fuegt ein neues Element dem Eingabepuffer hinzu und stösst ggf. die Produktion an
	 * @param object Das neue Objekt aus dem Eingabedatenstrom des Operators
	 * @param port Port, auf dem das neue Objekt im Operator angekommen ist
	 */
	public void newElement(T object, int inPort);

	public void done();

	public void init(IProcessInternal<T> sink);
	public void setSink(IProcessInternal<T> sink);

	public int size();

	@Override
	public IInputStreamSyncArea<T> clone();
	
	public void newHeartbeat(PointInTime heartbeat, int inPort);	
	
}
