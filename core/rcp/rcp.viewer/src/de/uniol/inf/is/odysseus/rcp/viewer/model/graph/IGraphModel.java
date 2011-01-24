package de.uniol.inf.is.odysseus.rcp.viewer.model.graph;

import java.util.Collection;

/**
 * Schnittstelle für das GraphModel. Sie besitzt Kenntnisse über alle Knoten
 * und Verbindungen innerhalb des GraphModel. Gewissermaßen repräsentiert diese
 * Schnittstelle den Graphen als Ganzheit dar. Es ist zu empfehlen, nur über diese
 * Schnittstelle und dessen Methoden den Graphen zu manipulieren, anstatt 
 * Knoten und Verbindungen gesondert zu verwalten. Damit ist es leichter, Verfahren
 * zu implementieren, die sich auf den ganzen Graphen beziehen. Über Listener kann beobachtet
 * werden, ob sich der Graph verändert hat.
 * 
 * @author Timo Michelsen
 *
 * @param <C> Typ der Oberklasse aller Operatoren, Senken und Quellen des physischen Ablaufgraphen.
 */
public interface IGraphModel<C> {
	
	/**
	 * Gibt eine Auflistung aller Knoten des GraphModels zurück.
	 * 
	 * @return Aufistung aller Knoten des GraphModels.
	 */
	public Collection<INodeModel<C>> getNodes();
	
	/**
	 * Gibt eine Auflistung aller Verbindungen des GraphModels zurück.
	 * 
	 * @return Auflistung aller Verbindungen des GraphModels
	 */
	public Collection<IConnectionModel<C>> getConnections();
	
	/**
	 * Fügt einen Knoten zu dem Graphen hinzu. Ist dieser Knoten bereits vorhanden,
	 * so geschieht nichts.
	 * 
	 * @param node Der neue Knoten, welcher hinzugefügt werden soll.
	 */
	public void addNode( INodeModel<C> node );
	
	/**
	 * Entfernt einen Knoten aus dem Graphen. Verbindungen, welche mit diesem Knoten
	 * vorhanden waren, werden ebenfalls entfernt. Ist der zu entfernende Knoten
	 * nicht im GraphModel vorhanden, so wird nichts entfernt.
	 * 
	 * @param node Der zu entfernende Knoten
	 */
	public void removeNode( INodeModel<C> node );
	
	/**
	 * Fügt eine Verbindung in den GraphModel ein. Das setzt voraus, dass die 
	 * beteiliten Anfangs- und Entknoten breits im GraphModel vorhanden sind.
	 * 
	 * @param connection Die neue Verbindung, welche hinzugefügt werden soll.
	 */
	public void addConnection( IConnectionModel<C> connection );
	
	/**
	 * Entfernt eine Verbindung aus dem GraphModel. Die beteiligten Knoten 
	 * werden nicht entfernt. Ist die Verbindung nicht im GraphModel vorhanden, so
	 * geschieht nichts.
	 * 
	 * @param connection Verbindung, welche aus dem GraphModel entfernt werden soll.
	 */
	public void removeConnection( IConnectionModel<C> connection );

	/**
	 * Fügt einen IGraphModelChangeListener hinzu. Es können mehrere Listener
	 * an einem GraphModel angemeldet werden. Diese werden dann je nach Implementierung
	 * benachrichtigt, wenn sich an dem GraphModel was ändert. 
	 * 
	 * @param listener Der neue IGraphModelChangeListener.
	 */
	public void addGraphModelChangeListener( IGraphModelChangeListener<C> listener );
	
	/**
	 * Entfernt einen zuvor angemeldeten IGraphModelChangeListener. 
	 * 
	 * @param listener IGraphModelChangeListener, welcher entfernt werden soll.
	 */
	public void removeGraphModelChangeListener( IGraphModelChangeListener<C> listener );

	/**
	 * Alle angemeldeten IGraphModelChangeListener werden darüber
	 * benachrichtigt, dass sich der Graph geändert hat.
	 */
	public void notifyGraphModelChangeListener();

	/**
	 * Setzt einen Namen/Bezeichnung des Graphen fest. Dieser muss nicht zu
	 * anderen Namen von Graphen verschieden sein. 
	 * 
	 * @param nameOfGraph Name des Graphen
	 */
	public void setName( String nameOfGraph );
	
	/**
	 * Liefert den Namen des Graphen zur�ck.
	 * 
	 * @return Name des Graphen
	 */
	public String getName();
}