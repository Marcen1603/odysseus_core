package de.uniol.inf.is.odysseus.viewer.model.graph;

/**
 * Listener für das GraphModel. Die einzige Methode
 * graphModelChanged() wird aufgerufen, wenn sich der GraphModel, woran sich
 * dieser Listener angemeldet hat, geändert hat.
 * 
 * @author Timo Michelsen
 *
 * @param <C> Typ der Oberklasse aller Operatoren, Senken und Quellen des physischen Ablaufgraphen.
 */
public interface IGraphModelChangeListener<C> {

	public void graphModelChanged( IGraphModel<C> sender );
}
