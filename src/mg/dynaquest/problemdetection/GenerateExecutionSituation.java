package mg.dynaquest.problemdetection;

import java.util.HashMap;
import mg.dynaquest.monitor.POMonitorEvent;
import mg.dynaquest.problemdetection.ExecutionSituation.ExecutionSituation;
import mg.dynaquest.queryexecution.event.POEventType;
import mg.dynaquest.queryexecution.po.base.PlanOperator;

/**
 * Die GenerateExecutionSituation-Komponente entnimmt dem EventBuffer die Ereignisse und generiert aus ihnen Ausf�hrungssituationen. Diese werden dann der ExecutionSituationTbale hinzugef�gt.
 * @author  Joerg Baeumer
 */
public class GenerateExecutionSituation implements Runnable 
{
  
	/**
	 * @uml.property  name="instance"
	 */
	private static GenerateExecutionSituation instance = null;
     
	/**
	 * @uml.property  name="buffer"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private EventBuffer buffer;
 	/**
	 * @uml.property  name="t"
	 */
 	private Thread t = null;
	/**
	 * @uml.property  name="mappingEventToSituation"
	 * @uml.associationEnd  qualifier="getPOEventType:mg.dynaquest.queryexecution.event.POEventType java.lang.Integer"
	 */
	private HashMap<POEventType,Integer> mappingEventToSituation = null; 
	
	
  
  
  private GenerateExecutionSituation()
  {
     
    t = new Thread (this, "GenerateExecutionSituation");
    // Zuweisung von Event -> Ausf�hrungssituation
    mappingEventToSituation = new HashMap<POEventType,Integer>();
    buffer = EventBuffer.getInstance();
   
  }
 
 
  /**
   * Starten der GenerateExecutionSituation-Komponente. 
   *
   */
  public void start()
  {
    t.start();
  }
 
  /**
   * Stoppt die GenerateExecutionSituation-Komponente.
   *
   */   
  public void stop()
  {
    t = null;
  }
  
  /**
   * Innerhalb dieser Methode werden st�ndig Ereignisse aus dem EventBuffer entnommen
   * und entsprechende Ausf�hrungssituationen generiert. Diese werden
   * der ExecutionSituationTable hinzugef�gt (Implementierung von Runnable).
   */       
  public void run()
  {
     
   ExecutionSituation exSituation = null;
   POMonitorEvent event = null;
    
   // Auslesen aus dem Buffer      
   while (!buffer.isEmpty() || (t != null) )
   	{     
      event = buffer.get();
      
      if (Constants.debug) 
      	System.out.println("Generate: Event entnommen " + event);
           
      // Generierung einer Ausfuehrungssituation
      
      if (mappingEventToSituation.containsKey(event.getEvent().getPOEventType())) {
      	
      	
      	// Bei 'blockierender' Ausf�hrungssituation �berpr�fen, ob 'situationsausl�sender'
      	// Operator noch blockiert ist, oder ob Blockierung schon aufgehoben (PreSituationTable)     	
      	
		if ((!FilterComp.isBlockedEvent(event)) || (PreSituationTable.operatorBlockedAtTime((PlanOperator) event.getEvent().getSource(), System.currentTimeMillis()))) {
      	                                                           
      	
      		//Generierung der Ausfuehrungssituation	    
      		exSituation = ExecutionSituation.createSituation(event, (Integer) mappingEventToSituation.get(event.getEvent().getPOEventType()));
      	
			if (Constants.debug) 
				System.out.println("Generate: Ausf�hrungssituation erstellt: " + exSituation.getInternalName());
      	
      		// Einfuegen der Ausfuehrungssituation in 'ExecutionSituationTable'
      		ExecutionSituationTable.getInstance().put(exSituation);
      	}	
      }
               
 	 }
 	  
  }	
  
  /**
   * Ordnet einem Event die zu generierende Ausf�hrungssituation zu.  
   * 
   * @param eventID ID des Ereignisses
   * @param situationID ID der Ausf�hrungssituation
   */
  public void addEventToSituation(POEventType eventID, Integer situationID){
  	
  	mappingEventToSituation.put(eventID, situationID);
  	
  	
  }
 
 /**
 * Liefert eine Instanz der GenerateExecutionSituation-Klasse (Singleton)
 * @return  Instanz
 * @uml.property  name="instance"
 */         
  public static GenerateExecutionSituation getInstance(){
    if (instance == null){
      instance = new GenerateExecutionSituation();
    }
    return instance;  
  }
}