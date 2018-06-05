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
 * Stellt im GraphModel eine Verbindung zwischen zwei Knoten dar. Sie besitzt genau
 * einen Anfangs- und einen Endknoten. Über spezielle Listener kann beobachtet werden,
 * wann die Verbindung verändert wurde (je nach Implementierung).
 * 
 * @author Timo Michelsen
 *
 * @param <C> Typ der Oberklasse aller Operatoren, Senken und Quellen des physischen Ablaufgraphen.
 */
public interface IConnectionModel<C> {

	/**
	 * Liefert den Anfangsknoten zurück.
	 * 
	 * @return Anfangsknoten der Verbindung.
	 */
	public INodeModel<C> getStartNode();
	
	/**
	 * Liefert den Endknoten der Verbindung zurück.
	 * 
	 * @return Endknoten der Verbindung.
	 */
	public INodeModel<C> getEndNode();
	
	/**
	 * Fügt einen IConnectionModelChangeListener hinzu. Es können mehrere Listener
	 * an einer Verbindung angemeldet werden. Diese werden dann je nach Implementierung
	 * benachrichtigt, wenn sich an der Verbindung was ändert. 
	 * 
	 * @param listener Der neue IConnectionModelChangeListener.
	 */
	public void addConnectionModelChangeListener( IConnectionModelChangeListener<C> listener );
	
	/**
	 * Entfernt einen zuvor angemeldeten IConnectionModelChangeListener. 
	 * 
	 * @param listener IConnectionModelChangeListener, welcher entfernt werden soll.
	 */
	public void removeConnectionModelChangeListener( IConnectionModelChangeListener<C> listener );
	
	/**
	 * Alle angemeldeten IConnectionModelChangeListener werden darüber
	 * benachrichtigt, dass sich die Verbindung geändert hat.
	 */
	public void notifyConnectionModelChangeListener( );
}
