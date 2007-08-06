
package mg.dynaquest.problemdetection;

import java.io.IOException;
import jcreek.representation.LocalKnowledgeModel;
import mg.dynaquest.problemdetection.ExecutionSituation.ExecutionSituation;
import mg.dynaquest.queryexecution.event.POEventType;



/**
 * Test-Klasse
 * @author  Joerg Baeumer
 */
public class TestEnv {

	private static FilterComp filterComp;
	
	
	private static RecoveryHandler recoveryHandler;
	private static GenerateExecutionSituation generateComp;
	private static LocalKnowledgeModel km; 

	
	public static void main(String[] args) {
				
		generateComp = GenerateExecutionSituation.getInstance();
		recoveryHandler = RecoveryHandler.getInstance();
		filterComp = FilterComp.getInstance();
		
		connectCreek();
		
		// Zuweisen der Events zu den zugehörigen Ausführungssituationen
		generateComp.addEventToSituation(POEventType.ReadTimeout, ExecutionSituation.BLOCKEDREAD);
		generateComp.addEventToSituation(POEventType.WriteTimeout, ExecutionSituation.BLOCKEDWRITE);
		generateComp.addEventToSituation(POEventType.OutOfMemoryException, ExecutionSituation.MEMORYOVERFLOW);
		generateComp.addEventToSituation(POEventType.Exeception, ExecutionSituation.EXPLICITSITUATION);

		// Start der GenerateExecutionSituation-Komponente und des RecoveryHandlers
		generateComp.start();
		recoveryHandler.start();
		
		// Filter initialisieren
		// addBlockedEventId: Events, die eine Blockierung signalisieren und
		//					  deren Operatoren somit in die PreSituationTable
		//					  eingefuegt werden sollen			
		filterComp.addBlockedEventId(POEventType.ReadTimeout, FilterComp.BLOCK);
		filterComp.addBlockedEventId(POEventType.WriteTimeout, FilterComp.BLOCK);
		filterComp.addBlockedEventId(POEventType.OutOfMemoryException, FilterComp.BLOCK );

		// addFilterEventId: die Events, die einer weiteren Behandlung unterzogen werden sollen,
		//					 d.h. die eine Ausführungssituation signalisieren
		filterComp.addFilterEventId(POEventType.ReadTimeout);
		filterComp.addFilterEventId(POEventType.WriteTimeout);
		filterComp.addFilterEventId(POEventType.OutOfMemoryException);

		// addBlockedEventId: Events, die die Aufhebung einer Blockierung signalisieren,
		//					  welche in der PreSituationTable vermerkt werden soll
		filterComp.addBlockedEventId(POEventType.NextDone, FilterComp.UNBLOCK);
		
		test();

		generateComp.stop();
		recoveryHandler.stop();

	}
	
	
	
	
	/**
	 * 
	 * Verbindung zu Creek herstellen
	 *
	 */
	public static void connectCreek() {
	
	// Erstellen eines neuen Creek-KnowledgeModells aus Datei	
	try {
			km = new LocalKnowledgeModel("e:/dissertation/DynaQuest/km/DynaQuestCreek_new3.km");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	/**
	 * Liefert das Creek-Wissensmodell
	 * 
	 * @return Creek-Wissensmodell
	 */
	public static LocalKnowledgeModel getKnowledgeModel(){
		
		return km;
		
	}
	/**
	 * Test-Methode, erstellen von Events usw.
	 *
	 */
	
	public static void test() {

	    
//		PreSituationTable preTable = PreSituationTable.getInstance();

		// ein Paar Operatoren erstellen
//		SortMemPO sortPO = new SortMemPO();
//		SortMemPO sortPO2 = new SortMemPO();
		
		// Erst mal alles auskommentiert ... HashJoinPO macht so keinen Sinn!
		
		//HashJoinPO hashPO3 = new HashJoinPO(new RelationalTupleCorrelator(2));
		//HashJoinPO hashPO4 = new HashJoinPO(new RelationalTupleCorrelator(2));
		//HashJoinPO hashPO5 = new HashJoinPO(new RelationalTupleCorrelator(2));
		
//		// Inputs zuweisen			
//		sortPO.setInputPO(hashPO3);
//		//sortPO2.setInputPO(hashPO4);
//		
//		// Puffergroessen initialisieren
//		
//		hashPO3.setMaxElementsBufferSize(26);
//		
//		// Monitore erstellen		
//		POMonitor pomonitor = new POMonitor("TestPO", sortPO);
//		pomonitor.setReadTimeOutMilliSec(60);
//		POMonitor pomonitor2 = new POMonitor("TestPO2", sortPO2);
//		pomonitor2.setReadTimeOutMilliSec(90);
//		POMonitor pomonitor3 = new POMonitor("TestPO3", hashPO3);
//		pomonitor3.setWriteTimeOutMilliSec(56);
//		
//		// Test-Events feuern
//		filterComp.poMonitorEventOccured(new POMonitorEvent(pomonitor, new ReadTimeoutEvent(sortPO, 0), System.currentTimeMillis()));
//
//		filterComp.poMonitorEventOccured(new POMonitorEvent(pomonitor2, new WriteTimeoutEvent(sortPO2, hashPO5 ), System.currentTimeMillis()));
//
//		filterComp.poMonitorEventOccured(new POMonitorEvent(pomonitor, new NextDoneEvent(sortPO), System.currentTimeMillis()));
//
//		filterComp.poMonitorEventOccured(new POMonitorEvent(pomonitor3, new WriteTimeoutEvent(hashPO3, hashPO5), System.currentTimeMillis()));		
//			
	}
	
}
