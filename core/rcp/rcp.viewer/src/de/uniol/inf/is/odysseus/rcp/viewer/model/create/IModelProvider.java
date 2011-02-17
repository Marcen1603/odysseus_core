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
package de.uniol.inf.is.odysseus.rcp.viewer.model.create;

import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IGraphModel;

/**
 * Diese Schnittstelle kapselt die Generierung des Graphenmodells (siehe viewer.model.graph-Paket).
 * Ihre Methode get() sollte das Graphenmodell zurückgeben bzw. zur Verfügung stellen.
 * Für get() existieren keine Parameter, daher muss die Implementierung über 
 * Konstruktor und zusätzlichen Funktionen ihre benötigten Daten eingeben.
 * 
 * Es ist zu empfehlen, die Berechnung des Graphenmodells nicht innerhalb
 * der get()-Methode zu realisieren. Die Implementierung hat alle 
 * Möglichkeiten, die Berechungsalgorithmen zu einem beliebigen Zeit-
 * punkt auszuführen.
 * 
 * @author Timo Michelsen
 *
 * @param <C> Typ der Oberklasse aller Operatoren, Senken und Quellen des physischen Ablaufgraphen.
 */
public interface IModelProvider<C> {

	/**
	 * Liefert das (zuvor berechnete) Graphenmodell. Es ist zu empfehlen,
	 * in dieser Methode nur das Ergebnis der Generierung zurückzugeben.
	 * Die Berechnung sollte zuvor gesondert geregelt werden.
	 * 
	 * @return Graphenmodell
	 */
	public IGraphModel<C>  get();
	
}
