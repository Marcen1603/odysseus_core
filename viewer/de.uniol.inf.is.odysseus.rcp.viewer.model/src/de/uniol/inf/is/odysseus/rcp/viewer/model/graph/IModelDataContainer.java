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
