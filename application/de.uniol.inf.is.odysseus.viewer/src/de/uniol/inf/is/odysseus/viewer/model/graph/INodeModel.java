package de.uniol.inf.is.odysseus.viewer.model.graph;

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