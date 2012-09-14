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
package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttributeContainer;

/**
 * Diese Klasse verarbeitet den in einem Operator u.U. notwendigen Heap, der die richtige Sortierung
 * der Elemente vor der Weitergabe in den Ausgabedatenstrom des Operators wiederherstellt.
 * 
 *
 * @param <R> Datentyp der Elemente, die Verarbeitet werden sollen.
 */
public interface ITransferArea<R extends IMetaAttributeContainer<?>, W extends IMetaAttributeContainer<?>> extends IClone {
	
	/**
	 * Anhand eines neuen Elementes, welches typischerweise aktuell aus dem
	 * Eingabedatenstrom des Operators stammen sollte, werden alle Elemente in den
	 * Ausgabedatenstrom des Operarors geschrieben, fuer die das moeglich ist.
	 * 
	 * @param object Das neue Objekt aus dem Eingabedatenstrom des Operators
	 * @param port Port, auf dem das neue Objekt im Operator angekommen ist
	 */
	public void newElement(R object, int inPort);

	/**
	 * Fuegt ein neues Element in den Heap ein.
	 * @param object Objekt, das in den Heap eingefuegt werden soll.
	 */
	public void transfer(W object);

	public void done();

	public void init(AbstractSource<W> source);
	public void setSourcePo(AbstractSource<W> source);

	public int size();

	@Override
	public ITransferArea<R,W> clone();
	
	public void newHeartbeat(PointInTime heartbeat, int inPort);	
	
	public PointInTime getMinTs();
	
}
