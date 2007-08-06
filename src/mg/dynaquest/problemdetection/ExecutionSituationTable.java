package mg.dynaquest.problemdetection;

import java.util.LinkedList;
import java.util.ListIterator;
import mg.dynaquest.problemdetection.ExecutionSituation.ExecutionSituation;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;

/**
 * Diese Klasse stellt einen FIFO-Austauschpuffer f�r Ausf�hrungssituationen dar.  Dar�berhinaus ist es m�glich, anhand des situationsausl�senden Operators  die zugeh�rige Ausf�hrungssituation aus der Tabelle zu entfernen. 
 * @author  Joerg Baeumer
 */
public class ExecutionSituationTable 
{
	private LinkedList<ExecutionSituation> list;
	/**
	 * @uml.property  name="maxTableSize"
	 */
	private int maxTableSize = 0;
	
	/**
	 * @uml.property  name="instance"
	 */
	private static ExecutionSituationTable instance;
			
	private ExecutionSituationTable()
	{
    	list = new LinkedList<ExecutionSituation>();
    				
	}
   
   	/**
   	 * Liefert die aktuelle Anzahl der gespeicherten Ausf�hrungssituationen.
   	 * 
   	 * @return
   	 */
	public synchronized int getSize()
	{
    	return list.size();
	}
  
  	/**
  	 * �berpr�ft, ob die Tabelle Eintr�ge enth�lt.
  	 * 
  	 * @return true - die Tabelle ist leer
  	 */
  	public synchronized boolean isEmpty(){
    	
      	return this.list.size() == 0;
  	}
    
    /**
     * L�schen aller Eintr�ge aus der Tabelle.
     *
     */
  	public synchronized void clearBuffer(){
   		list.clear();
  	}
  
  	/**
  	 * Einf�gen einer Ausf�hrungssituation in die Tabelle.
  	 * 
  	 * @param situation die hinzuzuf�gende Ausf�hrungssituation
  	 */
  	public synchronized void put(ExecutionSituation situation) {
                                        
		// Einfuegen des Events in den Buffer
		if (list.size() > maxTableSize){
   		   		
			maxTableSize = list.size();
  		
		}
      
		list.addFirst(situation);
		notifyAll();  
					
	}
   
   	/**
   	 * Entnimmt der Tabelle eine Ausf�hrungssituation 
   	 * 
   	 * @return eine Ausf�hrungssituation
   	 */
  	public synchronized ExecutionSituation get() {
    
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
   		return (ExecutionSituation) retEvent;
	}

	/**
	 * Entfernt aus der Tabelle die zu einem Operator zugeh�rige 
	 * Ausf�hrungssituation 
	 * 
	 * @param po der 'situationsausl�sende' Operator
	 */
	public synchronized void deleteOperator(SupportsCloneMe po) {
		
		ExecutionSituation sit;		
		ListIterator iter = list.listIterator(0);
				
		while (iter.hasNext()){
					
			sit = (ExecutionSituation) iter.next();
										
			if ((sit.getPlanOperator()).equals(po)) {
								
				iter.remove();
				
			}
					
		}
			
	}

	/**
	 * Liefert die maximale Gr��e der Tabelle
	 * @return  maximale Gr��e
	 * @uml.property  name="maxTableSize"
	 */
	public int getMaxTableSize(){

		return maxTableSize;
    
	}

	/**
	 * Liefert eine instanz der ExecutionSituationTable-Klasse (Singleton)
	 * @return
	 * @uml.property  name="instance"
	 */
	public static ExecutionSituationTable getInstance(){
		
		if (instance == null){
			instance = new ExecutionSituationTable() ;
		}
		return instance ;
	}
		
	public String toString()
	{
	    return  list.toString();
	}

}