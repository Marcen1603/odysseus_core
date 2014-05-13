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
package de.uniol.inf.is.odysseus.core.physicaloperator;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IStreamable;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

/**
 * Diese Klasse verarbeitet den in einem Operator u.U. notwendigen Heap, der die richtige Sortierung
 * der Elemente vor der Weitergabe in den Ausgabedatenstrom des Operators wiederherstellt.
 * 
 *
 * @param <R> Datentyp der Elemente, die Verarbeitet werden sollen.
 */
public interface ITransferArea<R extends IStreamObject<?>, W extends IStreamObject<?>> extends IClone {
	
	/**
	 * Anhand eines neuen Elementes, welches typischerweise aktuell aus dem
	 * Eingabedatenstrom des Operators stammen sollte, werden alle Elemente in den
	 * Ausgabedatenstrom des Operarors geschrieben, fuer die das moeglich ist.
	 * 
	 * @param object Das neue Objekt aus dem Eingabedatenstrom des Operators
	 * @param port Port, auf dem das neue Objekt im Operator angekommen ist
	 */
	void newElement(IStreamable object, int inPort);

	/**
	 * Fuegt ein neues Element in den Heap ein.
	 * @param object Objekt, das in den Heap eingefuegt werden soll.
	 */
	void transfer(W object);
	void transfer(W object, int toPort);
	/**
	 * To avoid packing time stamps into a punctuation, this
	 * method can be used. It will not create a new punctuation!
	 * @param heartbeat
	 * @param inPort
	 */
	void newHeartbeat(PointInTime heartbeat, int inPort);

	PointInTime getMinTs(int inPort);
	
	/** Wenn eine Punctuation kommt, muss diese auch korrekt verwaltet werden
	 * 
	 * @param punctuation
	 */
	public void sendPunctuation(IPunctuation punctuation);		
	void sendPunctuation(IPunctuation punctuation, int toPort);

	public void done(int port);

	public void init(ITransfer<W> sendTo,  int inputPortCount);
	
	public void addNewInput(int port);
	public void removeInput(int port);

	
	public int size();

	@Override
	public ITransferArea<R,W> clone();

	/**
	 * Order
	 */
	public void setInOrder(boolean isInOrder);
	public boolean isInOrder();
	
	/**
	 * Debug
	 */
	public void dump();
	
		
}
