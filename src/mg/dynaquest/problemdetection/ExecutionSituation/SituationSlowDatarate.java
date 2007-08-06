package mg.dynaquest.problemdetection.ExecutionSituation;

import jcreek.representation.Case;
import mg.dynaquest.monitor.POMonitorEvent;
import mg.dynaquest.queryexecution.event.WriteTimeoutEvent;
import mg.dynaquest.queryexecution.po.base.PlanOperator;
import mg.dynaquest.queryexecution.po.relational.SortMemPO;

/**
 * Ausführungssituation die entsteht, wenn die tatsächliche Datenrate geringer
 * als die erwartete Datenrate ist.
 * 
 * @author Joerg Baeumer
 *
 */
@SuppressWarnings("unused")
public class SituationSlowDatarate extends RateSituation {

	
	/**
	 * @uml.property  name="consumerType"
	 */
	private String consumerType;
	/**
	 * @uml.property  name="expectedDatarate"
	 */
	private Integer expectedDatarate;
	/**
	 * @uml.property  name="actualDatarate"
	 */
	private Integer actualDatarate;
	

	/**
	 * Konstruktor
	 * 
	 * @param event situationsauslösendes Ereignis
	 */
	public SituationSlowDatarate(POMonitorEvent event) {
		super(event);
		situationType = ExecutionSituation.SLOWDATARATE;
	}

	/* (non-Javadoc)
	 * @see mg.dynaquest.problemdetection.ExecutionSituation.ExecutionSituation#generateCase()
	 */
	public Case generateCase() {
		
		Case creekCase;
		WriteTimeoutEvent writeTimeoutEvent = null;
		PlanOperator consumerPO;
		
		// Ermitteln des Konsumenten
		writeTimeoutEvent = (WriteTimeoutEvent) monitorEvent.getEvent();
		//*******************************
		//consumerPO = (PlanOperator) po.getRegisteredConsumers().get(writeTimeoutEvent.getPort());
		
		// TODO SlowDeliveryEvent port einfuegen
		consumerPO = new SortMemPO();
		//*******************************
		// TODO Datenraten ermitteln
		
		return null;
	}

	/* (non-Javadoc)
	 * @see mg.dynaquest.problemdetection.ExecutionSituation.ExecutionSituation#getInternalName()
	 */
	public String getInternalName() {
		
		return "SituationSlowDatarate";
	}
	
	
	
	

}
