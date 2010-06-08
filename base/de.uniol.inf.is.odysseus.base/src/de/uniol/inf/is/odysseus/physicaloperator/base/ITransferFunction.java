package de.uniol.inf.is.odysseus.physicaloperator.base;

import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;

/**
 * Diese Klasse verarbeitet den in einem Join notwendigen Heap, der die richtige Sortierung
 * der Elemente vor der Weitergabe in den Ausgabedatenstrom des Joins wiederherstellt.
 * 
 *
 * @param <T> Datentyp der Elemente, die Verarbeitet werden sollen.
 */
public interface ITransferFunction<T extends IMetaAttributeContainer<?>> extends IClone {
	
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
	public void setSourcePo(AbstractSource<T> source);

	public int size();

	public ITransferFunction<T> clone();
	
	public void newHeartbeat(PointInTime heartbeat, int port);

	public void setDebug(boolean b);
	
	
}
