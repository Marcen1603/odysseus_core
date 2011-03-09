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
package de.uniol.inf.is.odysseus.scars.util.helper;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse hat die Aufgabe, die Ankuft von Datentupeln und/oder
 * Punctuations über mehrere Ports zu synchronisieren. Im Allgemeinen wird diese
 * Hilfsklasse in einem physischen Operator in den Methoden
 * <code>processNext()</code> für Tupel und <code>processPuctuation()</code> für
 * Punctuations eingesetzt.
 * <p>
 * Dabei muss der <code>PortSync</code>-Instanz über <code>recieve()</code>
 * mitgeteilt werden, wenn ein neues Objekt an einem Port eingetroffen ist. Mit
 * <code>isReady()</code> kann dann anschließend geprüft werden, ob an allen
 * Ports Objekte eingetroffen sind. Ist dies der Fall, können diese über
 * <code>getNext()</code> abgerufen werden.
 * 
 * @author Timo Michelsen
 * 
 */
public class PortSync {

	private List<List<Object>> recievedObjects = new ArrayList<List<Object>>();

	/**
	 * Erstellt eine neue <code>PortSync</code>-Instanz mit der angegebenen
	 * Anzahl an Ports die synchronisiert werden sollen.
	 * 
	 * @param numOfPorts
	 *            Anzahl an Ports, die synchronisiert werden sollen. Muss
	 *            positiv sein.
	 */
	public PortSync(int numOfPorts) {
		if (numOfPorts <= 0)
			throw new IllegalArgumentException("numOfPorts is invalid:" + numOfPorts);

		for (int i = 0; i < numOfPorts; i++)
			recievedObjects.add(new ArrayList<Object>());
	}

	/**
	 * Mit dieser Methode kann mitgeteilt werden, dass ein Objekt an einem
	 * bestimmten Port des Operators eingetroffen ist.
	 * 
	 * @param punctuationOrTuple
	 *            Objekt, Tupel oder Punctuation, welches eingetroffen ist
	 * @param port
	 *            Port, an dem das Objekt eingetroffen ist
	 */
	public void recieve(Object punctuationOrTuple, int port) {
		recievedObjects.get(port).add(punctuationOrTuple);
	}

	/**
	 * Liefert zurück, ob an allen Ports mindestens ein Objekt (Tupel oder
	 * Punctuation) eingetroffen ist.
	 * 
	 * @return <code>true</code>, falls an allen Ports mindestens ein Objekt
	 *         eingetroffen ist, sonst <code>false</code>
	 */
	public boolean isReady() {
		for (int i = 0; i < getPortCount(); i++) {
			if (recievedObjects.get(i).isEmpty())
				return false;
		}
		return true;
	}

	/**
	 * Liefert eine Liste der Objekte zurück, die eingetroffen sind. Die Indizes
	 * der Liste sind mit dem entsprechenden Ports, an denen die Objekte eingetroffen
	 * sind identisch. D.h. an Index 0 ist das Objekt, welches an Port 0 eingetroffen ist
	 * usw. Sind noch nicht an allen Ports ein Objekt eingetroffen, so wird eine leere
	 * Liste zurückgegeben.
	 * 
	 * @return Liste der eingetroffen Objekte in der Reihenfolge der Ports
	 */
	public List<Object> getNext() {
		List<Object> list = new ArrayList<Object>();
		if (!isReady())
			return list; // leere Liste

		for (int i = 0; i < getPortCount(); i++) {
			list.add(recievedObjects.get(i).get(0));
			recievedObjects.get(i).remove(0);
		}

		return list;
	}

	/**
	 * Liefert die Anzahl der zu synchronisierenden Ports zurück.
	 * 
	 * @return Anzahl der zu synchronisierenden Ports zurück.
	 */
	public int getPortCount() {
		return recievedObjects.size();
	}
}
