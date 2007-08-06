package mg.dynaquest.problemdetection;

import mg.dynaquest.problemdetection.ExecutionSituation.ExecutionSituation;
import mg.dynaquest.problemdetection.solutionobjects.SolutionObject;


/**
 * Der RecoveryHandler entnimmt der ExecutionSituationTable nacheinander Ausführungssituationen und führt sie einer behandlung zu.
 * @author  Joerg Baeumer
 */
public class RecoveryHandler implements Runnable 
{
  	
	/**
	 * @uml.property  name="instance"
	 */
	private static RecoveryHandler instance = null;
	/**
	 * @uml.property  name="execTable"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private ExecutionSituationTable execTable = null;
	/**
	 * @uml.property  name="stop"
	 */
	private boolean stop = false;   	
	/**
	 * @uml.property  name="t"
	 */
	private Thread t = null;
		  
	  
	private RecoveryHandler()
	{
	   
	    t = new Thread (this, "RecoveryHandler");
	  	execTable = ExecutionSituationTable.getInstance(); 
	   
	}
	 
	/**
	 * Starten des RecoveryHandlers.
	 *
	 */ 	
	public void start()
	{
	  t.start();
	    
	}
	 
	/**
	 * Stoppen des RecoveryHandlers.
	 *
	 */     
	public void stop()
	{
		stop = true;
		//t = null;
	}
	
	/**
	 * Der ExecutionSituationTable werden nacheinander Ausführungssituationen entnommen
	 * und ihrer Behandlung zugeführt.
	 * 
	 */          
	public void run()
	{
	    // Auslesen aus dem ExecutionSituationTable 
	   
	   ExecutionSituation execSituation = null;
	              
	   while (!execTable.isEmpty() || !stop )
	   	{     
	      execSituation = execTable.get();
	     
	      if (Constants.debug) 
	      	System.out.println("Recovery: Ausführungssituation entnommen! "  );
	          
	      // Behandlung der Ausfuehrungssituation
	      
	      // Vor Behandlung der Ausführungssituation bei blockierenden Situationen überprüfen,
	      // ob die Blockierung noch aktuell ist --> PreSituationTable
	           
	      if ((!FilterComp.isBlockedEvent(execSituation.getEvent())) || (PreSituationTable.operatorBlockedAtTime(execSituation.getPlanOperator(), System.currentTimeMillis()))) {
	          
			// Hier bisher nur sequentielle Abarbeitung der Ausführungssituationen
			// denkbar ist aber auch eine parallele Abarbeitung der Lösungen
			// verschiedener Ausführungssituationen / Solutions als Threads
			// (sie Dipl.-Arbeit S.83)
	                              
	        SolutionObject solution = execSituation.getBestSolution();
		    
		    if (Constants.debug)  
				System.out.println("Lösung: " + solution + "\n");
			  
			// Ausführen der Lösung
			//solution.executeSolution();
			  
			// Hier oder später kann eine Validierung stattfinden;
			// evtl. nächstbeste Lösung versuchen
						
			// solution = execSituation.getAlternativeSolution(2);
			// solution.executeSolution();
	      
			// bei Erfolg Fall als gelöst markieren
			// execSituation.getProblemCase().setStatus(Case.SOLVEDCASE);
			
			// ... und Lösung zuweisen
			// execSituation.getProblemCase().addRelation("has solution", execSituation.getSolutionEntity(i));
	      
	      }
		   
	               
	 	 }
	 	 
	 	t = null; 
	  
	}	
	 
	/**
	 * Liefert ein Instanz der RecoveryHandler-Klasse (Singleton)
	 * @return
	 * @uml.property  name="instance"
	 */          
	public static RecoveryHandler getInstance(){
		if (instance == null){
	      instance = new RecoveryHandler();
	    }
	    return instance;  
	}
}