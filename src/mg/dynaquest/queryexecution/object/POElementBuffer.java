package mg.dynaquest.queryexecution.object;

/**
 Author: $Author: grawund $
 Date: $Date: 2004/09/16 08:57:13 $ 
 Version: $Revision: 1.6 $
 Log: $Log: POElementBuffer.java,v $
 Log: Revision 1.6  2004/09/16 08:57:13  grawund
 Log: Quellcode durch Eclipse formatiert
 Log:
 Log: Revision 1.5  2004/08/03 12:34:14  grawund
 Log: Erweiterungen damit ein Buffer auch auf die Platte schreiben kann (HDMode)
 Log:
 Log: Revision 1.4  2004/07/19 13:03:44  grawund
 Log: Maximal belegte Puffergröße wird nun ermittelt und kann abgefragt werden
 Log:
 Log: Revision 1.3  2004/06/18 13:43:17  grawund
 Log: no message
 Log:
 Log: Revision 1.2  2002/01/31 16:14:09  grawund
 Log: Versionsinformationskopfzeilen eingefuegt
 Log:
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.TimeoutException;

/**
 * @author  Marco Grawunder
 */
public class POElementBuffer {
	/**
	 * @uml.property  name="maxMemBufferSize"
	 */
	int maxMemBufferSize;

	/**
	 * @uml.property  name="maxSizeReached"
	 */
	int maxSizeReached;

	LinkedList<Object> list;

	/**
	 * @uml.property  name="useHDMemory"
	 */
	boolean useHDMemory = false;

	/**
	 * @uml.property  name="hdMemoryBlockSize"
	 */
	int hdMemoryBlockSize = 10;

	/**
	 * @uml.property  name="hdMemory"
	 * @uml.associationEnd  
	 */
	FifoObjectBuffer hdMemory = null;

	ArrayList<POElementBufferEventListener> listener = new ArrayList<POElementBufferEventListener>();
	POElementBufferMaxCapacityReached bufferFullEvent = null;

    /**
	 * @uml.property  name="endElementRead"
	 */
    private boolean endElementRead = false;

	/**
	 * @return  the maxSizeReached
	 * @uml.property  name="maxSizeReached"
	 */
	public int getMaxSizeReached() {
		return maxSizeReached;
	}

	public synchronized void switchToDHMemoryMode() {
		if (!this.useHDMemory) {
			System.out.println("In den HDModus gewechselt");
			useHDMemory = true;
			hdMemory = new FifoObjectBuffer(this.getClass().toString()
					+ this.hashCode());
			notifyAll();
		}
	}

	public POElementBuffer(int maxMemBufferSize) {
		this.maxMemBufferSize = maxMemBufferSize;
		bufferFullEvent = new POElementBufferMaxCapacityReached(this, maxMemBufferSize);
		list = new LinkedList<Object>();
		clearBuffer();
	}

	public boolean setNewMaxMemBufferSize(int newMaxBufferSize) {
		this.maxMemBufferSize = newMaxBufferSize;
		if (newMaxBufferSize < list.size()) {
			bufferFullEvent = new POElementBufferMaxCapacityReached(this, maxMemBufferSize);
			return true;
		} else {
			return false;
		}
	}

	public int getMaxBufferSize() {
		return maxMemBufferSize;
	}

	public int getCurrentBufferSize() {
		return list.size();
	}

	public synchronized void clearBuffer() {
		list.clear();
		maxSizeReached = 0;
        endElementRead = false;
	}

	//  public synchronized void reInit(){
	//    list.clear();
	//  }

	public boolean isEmpty() {
		return this.list.size() == 0;
	}

	public boolean isFull() {
		if (this.useHDMemory) {
			return false;
		} else {
			boolean ret = list.size() >= maxMemBufferSize && maxMemBufferSize > 0;
			return ret;
		}
	}

	public synchronized void put(Object data) {
		// Die maximal erreichte Größe des Puffers ermitteln
		if (maxSizeReached < list.size()) {
			maxSizeReached = list.size();
		}
		// Fall Blockierung vorliegt, einmal ein Event feuern
		if (isFull()) fireBufferFullEvent();
		while (isFull()) {
			try {
				//System.out.println("Warte mit dem Eintragen von "+data);
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}// while
		//System.out.println("fertig mit Warten auf Eintragen"+data);

		// synchronized(this){
		if (this.useHDMemory) {
			try {
				this.hdMemory.put(data);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		list.addFirst(data);
		//}
		//System.out.println("Eingetragen: "+data+" aktuelle Menge
		// "+list.size());
		//System.out.println("NOTIFY");
		notifyAll();
	}

	public synchronized Object get(long timeout) throws TimeoutException {
        
        // Gar nicht erst reingehen, wenn schon alles verarbeitet
        if (endElementRead) return null;
        
		//System.out.println("Hole nächstes Objekt");
		while (isEmpty()) {
			// TODO: Das ist noch nicht richtig schön ... 
			if (this.useHDMemory) {
				while (this.hdMemory.getNoOfObjects() > 0
						&& list.size() < maxMemBufferSize) {
					try {
						list.add(this.hdMemory.get());
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {

					}
				}
			} else {
				try {
					//System.out.println("Warte auf Element ....");
                    if (timeout < 0){
                        wait();
                    }else{
                        wait(timeout);
                        // Exception werfen, falls nach dem Timeout
                        // immer noch keine Daten vorliegen
                        if (isEmpty()){
                            throw new TimeoutException();
                        }
                    }
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		Object ret = null;
		//synchronized(this){
		ret = list.removeLast();
		//}
		//System.out.println("Abgeholt: "+ret+" aktuelle Menge "+list.size());
		//System.out.println("NOTIFY");
		notifyAll();
        if (ret == null){
            endElementRead = true;
        }
		return ret;
	}

	public void addPOElementBufferEventListener(
			POElementBufferEventListener listener) {
		this.listener.add(listener);
	}

	public void removePOElementBufferEventListener(
			POElementBufferEventListener listener) {
		this.listener.remove(listener);
	}
	
	public void fireBufferFullEvent(){
		for (POElementBufferEventListener l: listener){
			l.poElementBufferEventOccured(bufferFullEvent);
		}
	}

}