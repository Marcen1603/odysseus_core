package de.uniol.inf.is.odysseus.rcp.viewer.model.graph;

/**
 * Listener für die Verbindungen im GraphModel. Die einzige Methode
 * connectionModelChanged() wird aufgerufen, wenn die Verbindung, woran sich
 * dieser Listener angemeldet hat, sich geändert hat.
 * 
 * @author Timo Michelsen
 *
 * @param <C> Typ der Oberklasse aller Operatoren, Senken und Quellen des physischen Ablaufgraphen.
 */
public interface IConnectionModelChangeListener<C> {

	public void connectionModelChanged( IConnectionModel<C> sender );
}
