package de.uniol.inf.is.odysseus.physicaloperator.base;

import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;

/**
 * Diese Klasse dient dazu, dem zugehoerigen Operator die Daten aus mehreren Eingabenstroemen 
 * in einer stromuebergreifdenden sortierten Ordnung zur Verfuegung zu stellen
 * 
 *
 * @param <T> Datentyp der Elemente, die Verarbeitet werden sollen.
 */
public interface IInputStreamSyncArea<T extends IMetaAttributeContainer<?>> extends IClone {
	
	/**
	 * Fuegt ein neues Element dem Eingabepuffer hinzu und stösst ggf. die Produktion an
	 * @param object Das neue Objekt aus dem Eingabedatenstrom des Operators
	 * @param port Port, auf dem das neue Objekt im Operator angekommen ist
	 */
	public void newElement(T object, int inPort);

	public void done();

	public void init(IProcessInternal<T> sink);
	public void setSink(IProcessInternal<T> sink);

	public int size();

	public IInputStreamSyncArea<T> clone();
	
	public void newHeartbeat(PointInTime heartbeat, int inPort);	
	
}
