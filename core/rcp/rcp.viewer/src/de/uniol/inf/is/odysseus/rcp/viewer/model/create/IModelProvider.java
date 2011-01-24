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
