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
package de.uniol.inf.is.odysseus.rcp.viewer.model.graph;


/**
 * Stellt den Inhalt eines Knotens dar. Sie biet die Methode an, 
 * womit auf den eigentlichen Inhalt eines Knotens zugegriffen werden
 * kann.
 * 
 * @author Timo Michelsen
 *
 * @param <C> Typ der Oberklasse aller Operatoren, Senken und Quellen des physischen Ablaufgraphen.
 */
public interface IModelDataContainer<C> {

	/**
	 * Liefert den Inhalt.
	 * 
	 * @return Inhalt
	 */
	public C getContent();
	
}
