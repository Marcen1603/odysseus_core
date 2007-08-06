package mg.dynaquest.gui.composer;

/**
 * Author: $Author: grawund $ Date: $Date: 2004/09/16 08:57:12 $ Version:
 * $Revision: 1.4 $ Log: $Log: JPOConnectionModel.java,v $
 * $Revision: 1.4 $ Log: Revision 1.4  2004/09/16 08:57:12  grawund
 * $Revision: 1.4 $ Log: Quellcode durch Eclipse formatiert
 * $Revision: 1.4 $ Log: Log: Revision 1.3
 * 2004/09/16 08:53:53 grawund Log: *** empty log message *** Log: Log: Revision
 * 1.2 2002/01/31 16:14:57 grawund Log: Versionsinformationskopfzeilen
 * eingefuegt Log:
 */

import javax.swing.event.*;

/**
 * @author  Marco Grawunder
 */
public class JPOConnectionModel {

	/**
	 * @uml.property  name="changeEvent"
	 * @uml.associationEnd  
	 */
	protected transient ChangeEvent changeEvent = null;

	/**
	 * @uml.property  name="listenerList"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	protected EventListenerList listenerList = new EventListenerList();

	// Ist es hier richtig, Plan-Operatoren zu verwenden?
	/**
	 * @uml.property  name="source"
	 * @uml.associationEnd  
	 */
	private JPlanOperator source = null;

	/**
	 * @uml.property  name="destination"
	 * @uml.associationEnd  
	 */
	private JPlanOperator destination = null;

	/**
	 * @uml.property  name="destinationPortNo"
	 */
	private int destinationPortNo = 0;

	/**
	 * @return  the source
	 * @uml.property  name="source"
	 */
	public JPlanOperator getSource() {
		return source;
	}

	/**
	 * @param source  the source to set
	 * @uml.property  name="source"
	 */
	public void setSource(JPlanOperator source) {
		this.source = source;
		fireChange();
	}

	/**
	 * @return  the destination
	 * @uml.property  name="destination"
	 */
	public JPlanOperator getDestination() {
		return destination;
	}

	/**
	 * @return  the destinationPortNo
	 * @uml.property  name="destinationPortNo"
	 */
	public int getDestinationPortNo() {
		return destinationPortNo;
	}

	/**
	 * @param destination
	 * @param destinationPortNo
	 * @uml.property  name="destination"
	 */
	public void setDestination(JPlanOperator destination, int destinationPortNo) {
		this.destination = destination;
		this.destinationPortNo = destinationPortNo;
		fireChange();
	}

	public void addChangeListener(ChangeListener l) {
		listenerList.add(ChangeListener.class, l);
	}

	public void removeChangeListener(ChangeListener l) {
		listenerList.remove(ChangeListener.class, l);
	}

	protected void fireChange() {
		Object[] listeners = listenerList.getListenerList();
		if (changeEvent == null) {
			changeEvent = new ChangeEvent(this);
		}
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ChangeListener.class) {
				((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
			}
		}
	}
}