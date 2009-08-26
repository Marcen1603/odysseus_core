package de.uniol.inf.is.odysseus.physicaloperator.base;

import de.uniol.inf.is.odysseus.metadata.base.IMetaAttribute;

/**
 * Diese Klasse verarbeitet den in einem Join notwendigen Heap, der die richtige Sortierung
 * der Elemente vor der Weitergabe in den Ausgabedatenstrom des Joins wiederherstellt.
 * 
 *
 * @param <T> Datentyp der Elemente, die Verarbeitet werden sollen.
 */
public interface ITransferFunction<T extends IMetaAttribute<?>> {
	
	/**
	 * Anhand eines neuen Elementes, welches typischerweise aktuell aus dem
	 * Eingabedatenstrom des Joins stammen sollte, werden alle Elemente in den
	 * Ausgabedatenstrom des Joins geschrieben, fuer die das moeglich ist.
	 * 
	 * @param object Das neue Objekt aus dem Eingabedatenstrom des Joins
	 * @param port Port, auf dem das neue Objekt im Join angekommen ist
	 */
	public void newElement(T object, int port);

	/**
	 * Fuegt ein neues Element in den Heap ein.
	 * @param object Objekt, das in den Heap eingefuegt werden soll.
	 */
	public void transfer(T object);

	public void done();

	public void init(AbstractSource<T> source);

	public int size();
	
}
