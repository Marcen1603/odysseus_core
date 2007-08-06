package mg.dynaquest.problemdetection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.base.PlanOperator;

/**
 * Die PreSituationTable verwaltet die blockierten Operatoren. F�r jeden blockierten Operator wird Eintrittzeitpunkt sowie gegebenfalls die Austrittszeitpunkt einer Blockierung gespeichert. So ist es m�glich, f�r einen bestimmten Zeitpunkt zu ermitteln, ob ein Operator zu diesem Zeitpunkt  blockiert war. 
 * @author  Joerg Baeumer
 */
public class PreSituationTable 
{
 				
	/**
	 * @uml.property  name="instance"
	 */
	private static PreSituationTable instance = null;;
	
	// HashMap mit
	// Key -> PlanOperator
	// Object -> TimeIntervals, eine Liste mit Zeitintervallen, welche die 
	//			 Entritts- und Austrittszeitpunkte aus einer Blockierung markieren	
	/**
	 * @uml.property  name="operatorBlockedList"
	 * @uml.associationEnd  qualifier="key:java.lang.Object mg.dynaquest.problemdetection.TimeIntervals"
	 */
	private static Map<PlanOperator,TimeIntervals> operatorBlockedList =  null;
	 
			 
	private PreSituationTable()
	{
	  
		operatorBlockedList =  new HashMap<PlanOperator,TimeIntervals>();
	    	  	
	}
	
	/**
	 * Hinzuf�gen eines Operators; ist der Operator schon in der Tabelle
	 * enthalten, so wird lediglich ein neuer Eintritts-Zeitpunkt f�r den Operator 
	 * eingef�gt
	 * 
	 * @param po situationsausl�sender Operator
	 * @param time Zeitpunkt der Entstehung des Events
	 */		
	public static void setOperatorBlocked(PlanOperator po, long time){
	                
	    if (!operatorBlockedList.containsKey(po)){
	        
			//Operator hinzufuegen
			operatorBlockedList.put(po, new TimeIntervals());
	        	        	
	    }	
	    
	    // Hinzufuegen des Entry-Zeitpunktes
		((TimeIntervals) operatorBlockedList.get(po)).blockOperator(time);	
			     
	  }
	
	/**
	 * Ein Operator wird als nicht mehr blockiert markiert; hierzu wird der 
	 * Zeitpunkt als Leave-Zeitpunkt gespeichert, zu dem die Blockierung aufgehoben wurde 
	 * 
	 * @param po situationsausl�sender Operator
	 * @param time Zeitpunkt der Aufhebung
	 */
	public static void setOperatorUnblocked(SupportsCloneMe po, long time) {
	  	  	
	 	if (operatorBlockedList.containsKey(po)) {
	  	  	
	  	  	// Hinzufuegen des Leave-Zeitpunktes	
			((TimeIntervals) operatorBlockedList.get(po)).unblockOperator(time);
	  		
	  	}
	    	
	}
	
	/**
	 * �berpr�ft, ob ein Operator zu einem bestimmten Zeitpunkt blockiert war. 
	 * 
	 * @param po der zu �berpr�fende Operator
	 * @param time der zu �berpr�fende Zeitpunkt
	 * @return true - Operator war zu dem Zeitpunkt blockiert
	 */
	public static boolean operatorBlockedAtTime(PlanOperator po, long time){
	 		
		if (operatorBlockedList.containsKey(po)) {
			
			return ((TimeIntervals) operatorBlockedList.get(po)).operatorBlockedAtTime(time);
			
		}
		
		return false;
	  	
	}
	
	/**
	 * �berpr�ft, ob ein Operator in Tabelle enthalten ist. Hinweis: diese Methode
	 * pr�ft nicht, ob ein Operator blockiert ist, hierzu ist die Methode
	 * operatorBlockedAtTime (PlanOperator op, long time) zu verwenden. 
	 * 
	 * @param Op der zu �berpr�fende Operator
	 * @return	true - Tabelle enth�lt dne Operator
	 */  
	public static boolean containsOperator(SupportsCloneMe Op)
	{
	  // �berpr�ft, ob ein Operator in BlockedTable
	  return operatorBlockedList.containsKey(Op);
	  
	}
	
	/**
	 * Entfernt einen Operator aus der Tabelle.
	 * 
	 * @param Op
	 */ 
	public static void deleteOperator(SupportsCloneMe Op)
	{
		// L�scht einen Operator aus der Liste
	    operatorBlockedList.remove(Op);
	}
	
	/**
	 * Liefert eine Instanz der Klasse PreSituationTable (Singleton)
	 * @return  Instanz der Klasse
	 * @uml.property  name="instance"
	 */
	 public static PreSituationTable getInstance()  {
		
		if (instance == null){
			instance = new PreSituationTable() ;
		} 
		
		return instance ;
	}
	
	public String toString() {
	  	
		String s = "";
		SupportsCloneMe op;
	
		Iterator it = operatorBlockedList.keySet().iterator();
	
		while (it.hasNext()) {
		
			op = (SupportsCloneMe) it.next();
			s = s + op + " interval " + ((TimeIntervals)operatorBlockedList.get(op)).toString() + "\n" ; 
		
		}
	 	
		return "PreSituationTable: \n" + s;
	  	
	  	
  	}
  
}