package mg.dynaquest.problemdetection;

import java.util.LinkedList;
import mg.dynaquest.monitor.POMonitorEvent;
/**
 * FIFO-Austauschpuffer für POMonitor-Events
 * @author  Joerg Baeumer
 */
public class EventBuffer 
{
	private LinkedList<POMonitorEvent> list;
	/**
	 * @uml.property  name="maxbuffersize"
	 */
	private int maxbuffersize = 0;
	/**
	 * @uml.property  name="instance"
	 */
	private static EventBuffer instance;
			
	private EventBuffer()
	{
    	list = new LinkedList<POMonitorEvent>();
    				
	}
   
   	/**
   	 * Liefert die aktuelle Größe des Austauschpuffers
   	 * 
   	 * @return Anzahl der Elemente
   	 */
	public synchronized int getSize()
	{
    	return list.size();
	}
  
  	/**
  	 * Prüft, ob Austauschpuffer leer ist
  	 * 
  	 * @return true - wenn leer 
  	 */
  	public synchronized boolean isEmpty(){
    	
      	return this.list.size() == 0;
  	}
    
    /**
     * Leert den Austauschpuffer
     *
     */
  	public synchronized void clearBuffer(){
   		list.clear();
  	}
  
  	/**
  	 * Fügt dem Austauschpuffer ein POMonitorEvent hinzu
  	 * 
  	 * @param event POMonitorEvent
  	 */
  	public synchronized void put(POMonitorEvent event) {
               		   		   			      			   		
			// Einfuegen des Events in den Buffer
			if (list.size() > maxbuffersize){
   		   		
				maxbuffersize = list.size();
  		
			}
   
			list.addFirst(event);
			notifyAll();  
					
	}
   
    /**
     * Entnimmt dem Austauschpuffer ein POMonitorEvent
     * 
     * @return POMonitorEvent
     */
  	public synchronized POMonitorEvent get() {
    
    	while (list.size() == 0){
      		try {
       			wait(1000);
      		}
      		catch(InterruptedException e) {
        		e.printStackTrace();
      		}
    	}
         
   		Object retEvent = null;
   		retEvent = list.removeLast();
   		notifyAll();
   		return (POMonitorEvent) retEvent;
	}

	/**
	 * Liefert die maximal mögliche Größe des Austauschpuffers
	 * 
	 * @return maximale Größe
	 */
	public int getMaxBufferSize(){

		return maxbuffersize;
    
	}

	/**
	 * Liefert eine Instanz der Klasse EventBuffer (Singleton)
	 * @return
	 * @uml.property  name="instance"
	 */
	public static EventBuffer getInstance(){
		
		if (instance == null){
			instance = new EventBuffer() ;
		}
		return instance ;
	}
		
	public String toString()
	{
	    return  list.toString();
	}

	

}