package de.uniol.inf.is.odysseus.physicaloperator;

import de.uniol.inf.is.odysseus.IClone;
import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.PointInTime;

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

	@Override
	public IInputStreamSyncArea<T> clone();
	
	public void newHeartbeat(PointInTime heartbeat, int inPort);	
	
}
