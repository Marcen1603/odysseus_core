package mg.dynaquest.problemdetection;

import mg.dynaquest.problemdetection.ExecutionSituation.ExecutionSituation;
import mg.dynaquest.problemdetection.solutionobjects.SolutionObject;


/**
 * Der RecoveryHandler entnimmt der ExecutionSituationTable nacheinander Ausf�hrungssituationen und f�hrt sie einer behandlung zu.
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
	 * Der ExecutionSituationTable werden nacheinander Ausf�hrungssituationen entnommen
	 * und ihrer Behandlung zugef�hrt.
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
	      	System.out.println("Recovery: Ausf�hrungssituation entnommen! "  );
	          
	      // Behandlung der Ausfuehrungssituation
	      
	      // Vor Behandlung der Ausf�hrungssituation bei blockierenden Situationen �berpr�fen,
	      // ob die Blockierung noch aktuell ist --> PreSituationTable
	           
	      if ((!FilterComp.isBlockedEvent(execSituation.getEvent())) || (PreSituationTable.operatorBlockedAtTime(execSituation.getPlanOperator(), System.currentTimeMillis()))) {
	          
			// Hier bisher nur sequentielle Abarbeitung der Ausf�hrungssituationen
			// denkbar ist aber auch eine parallele Abarbeitung der L�sungen
			// verschiedener Ausf�hrungssituationen / Solutions als Threads
			// (sie Dipl.-Arbeit S.83)
	                              
	        SolutionObject solution = execSituation.getBestSolution();
		    
		    if (Constants.debug)  
				System.out.println("L�sung: " + solution + "\n");
			  
			// Ausf�hren der L�sung
			//solution.executeSolution();
			  
			// Hier oder sp�ter kann eine Validierung stattfinden;
			// evtl. n�chstbeste L�sung versuchen
						
			// solution = execSituation.getAlternativeSolution(2);
			// solution.executeSolution();
	      
			// bei Erfolg Fall als gel�st markieren
			// execSituation.getProblemCase().setStatus(Case.SOLVEDCASE);
			
			// ... und L�sung zuweisen
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