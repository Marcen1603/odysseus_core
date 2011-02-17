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
package de.uniol.inf.is.odysseus.rcp.viewer.model.graph;

import java.util.Collection;

/**
 * Stellt die Schnittstelle zur Repräsentation eines Knotens innerhalb des
 * GraphModels bereit. Ein Knoten kennt die Verbindungen seiner Nachfolger- 
 * und Vorgaengerknoten und besitzt einen Inhalt. Über einen nicht eindeutigen 
 * Namen können zusätzliche Funktionalitäten implementiert werden (z.B. für die Symbole).
 * 
 * @author Timo Michelsen
 *
 * @param <C> Typ der Oberklasse aller Operatoren, Senken und Quellen des physischen Ablaufgraphen.
 */
public interface INodeModel<C> extends IModelDataContainer<C>{
	
	public Collection<IConnectionModel<C>> getConnectionsAsEndNode();
	public Collection<IConnectionModel<C>> getConnectionsAsStartNode();
	public Collection<IConnectionModel<C>> getConnections();
	
	public String getName();
	public void setName( String name );
	
	public void addNodeModelChangeListener( INodeModelChangeListener<C> listener );
	public void removeNodeModelChangeListener( INodeModelChangeListener<C> listener );
	public void notifyNodeModelChangeListener();

}