package mg.dynaquest.queryexecution.po.base;

/**
 Author: $Author: grawund $
 Date: $Date: 2004/09/16 08:57:12 $ 
 Version: $Revision: 1.6 $
 Log: $Log: UnaryPlanOperator.java,v $
 Log: Revision 1.6  2004/09/16 08:57:12  grawund
 Log: Quellcode durch Eclipse formatiert
 Log:
 Log: Revision 1.5  2004/08/25 11:24:19  grawund
 Log: Entfernung der Methode getNoOfInputs
 Log:
 Log: Revision 1.4  2004/07/28 11:29:26  grawund
 Log: TriggerdPO an vielen Stellen durch NAryPO ersetzt
 Log:
 Log: Revision 1.3  2003/08/20 13:41:47  grawund
 Log: no message
 Log:
 Log: Revision 1.2  2002/01/31 16:14:22  grawund
 Log: Versionsinformationskopfzeilen eingefuegt
 Log:
 */

import java.util.concurrent.TimeoutException;

import mg.dynaquest.queryexecution.event.POException;
import mg.dynaquest.queryexecution.po.algebra.AlgebraPO;

/**
 * Diese Klasse ist die Oberklasse, aller Planoperatoren, die genau einen
 * Eingabestrom haben. Sie bernimmt das Setzen und Auslesen der
 * Eingabestrom-Daten
 */

public abstract class UnaryPlanOperator extends NAryPlanOperator{

	/**
     * @param operator
     */
    public UnaryPlanOperator(UnaryPlanOperator operator) {
        super(operator);
        setNoOfInputs(1);
    }
    
    public UnaryPlanOperator(){
        super();
        setNoOfInputs(1);
    }

    public UnaryPlanOperator(AlgebraPO algebraPO){
        super(algebraPO);
        setNoOfInputs(1);
    }
    
    
 

    /**
	 * Festlegen des Operators, der die Eingaben fr den aktuellen Planoperator
	 * liefert
	 */
	final public synchronized void setInputPO(PlanOperator input) {
		if (!this.isInitialized())
			this.setNoOfInputs(1);
		this.setInputPO(0, input);
	}

	/** Welcher PO liefert die Eingaben fr diese Operator */
	final public PlanOperator getInputPO() {
		return getInputPO(0);
	}

	@SuppressWarnings("unchecked")
	public <T> T getInputNext(PlanOperator caller, long timeout) throws POException, TimeoutException {
		// Benachrichtigung wird von getInputNext erledigt
		// Das muss ich noch irgendwo dokumentieren
		// hnlich zu throws muss ich sagen, welche Events
		// eine Methode wirft!!!?
		//    this.notifyPOEvent(this.readInitEvent[0]);
		T next = (T) this.getInputNext(0, caller, timeout);
		//    this.notifyPOEvent(this.readDoneEvent[0]);
		return next;
	}

}