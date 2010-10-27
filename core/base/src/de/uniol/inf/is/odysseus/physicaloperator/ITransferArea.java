package de.uniol.inf.is.odysseus.physicaloperator;

import de.uniol.inf.is.odysseus.IClone;
import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.PointInTime;

/**
 * Diese Klasse verarbeitet den in einem Operator u.U. notwendigen Heap, der die richtige Sortierung
 * der Elemente vor der Weitergabe in den Ausgabedatenstrom des Operators wiederherstellt.
 * 
 *
 * @param <R> Datentyp der Elemente, die Verarbeitet werden sollen.
 */
public interface ITransferArea<R extends IMetaAttributeContainer<?>, W extends IMetaAttributeContainer<?>> extends IClone {
	
	/**
	 * Anhand eines neuen Elementes, welches typischerweise aktuell aus dem
	 * Eingabedatenstrom des Operators stammen sollte, werden alle Elemente in den
	 * Ausgabedatenstrom des Operarors geschrieben, fuer die das moeglich ist.
	 * 
	 * @param object Das neue Objekt aus dem Eingabedatenstrom des Operators
	 * @param port Port, auf dem das neue Objekt im Operator angekommen ist
	 */
	public void newElement(R object, int inPort);

	/**
	 * Fuegt ein neues Element in den Heap ein.
	 * @param object Objekt, das in den Heap eingefuegt werden soll.
	 */
	public void transfer(W object);

	public void done();

	public void init(AbstractSource<W> source);
	public void setSourcePo(AbstractSource<W> source);

	public int size();

	@Override
	public ITransferArea<R,W> clone();
	
	public void newHeartbeat(PointInTime heartbeat, int inPort);	
	
}
