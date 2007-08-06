package mg.dynaquest.queryexecution.po.base;

/**
 Author: $Author: grawund $
 Date: $Date: 2004/09/16 08:57:12 $ 
 Version: $Revision: 1.5 $
 Log: $Log: BinaryPlanOperator.java,v $
 Log: Revision 1.5  2004/09/16 08:57:12  grawund
 Log: Quellcode durch Eclipse formatiert
 Log:
 Log: Revision 1.4  2004/08/25 11:24:19  grawund
 Log: Entfernung der Methode getNoOfInputs
 Log:
 Log: Revision 1.3  2004/07/28 11:29:26  grawund
 Log: TriggerdPO an vielen Stellen durch NAryPO ersetzt
 Log:
 Log: Revision 1.2  2002/01/31 16:14:13  grawund
 Log: Versionsinformationskopfzeilen eingefuegt
 Log:
 */

import java.util.concurrent.TimeoutException;

import mg.dynaquest.queryexecution.event.POException;
import mg.dynaquest.queryexecution.po.algebra.AlgebraPO;

/**
 * Diese Klasse ist die Oberklasse, aller Planoperatoren, die genau zwei
 * Eingabestroeme haben. Sie übernimmt das Setzen und Auslesen der
 * Eingabestrom-Daten und nimmt eine Übersetzung links nach 0 und rechts nach 1
 * vor --> einfacher zu handhaben
 */

public abstract class BinaryPlanOperator extends NAryPlanOperator {

	/**
     * @param operator
     */
    public BinaryPlanOperator(BinaryPlanOperator operator) {
        super(operator);
        setNoOfInputs(2);
    }

    public BinaryPlanOperator(){
        super();
        setNoOfInputs(2);
    }

    public BinaryPlanOperator(AlgebraPO algebraPO){
        super(algebraPO);
        setNoOfInputs(2);
    }

    
    final public synchronized void setLeftInput(PlanOperator input) {
		// Falls die Oberklasse noch nicht initialisert ist, tue dies nun
		if (!this.isInitialized())
			this.setNoOfInputs(2);
		this.setInputPO(0, input);
	}

	final public PlanOperator getLeftInput() {
		return this.getInputPO(0);
	}

	final public synchronized void setRightInput(PlanOperator input) {
		// Falls die Oberklasse noch nicht initialisert ist, tue dies nun
		if (!this.isInitialized())
			this.setNoOfInputs(2);
		this.setInputPO(1, input);
	}

	final public PlanOperator getRightInput() {
		return this.getInputPO(1);
	}

	final public Object getLeftNext(PlanOperator caller, long timeout) throws POException, TimeoutException {
		return this.getInputNext(0, caller, timeout);
	}

	final public Object getRightNext(PlanOperator caller, long timeout) throws POException, TimeoutException {
		return this.getInputNext(1, caller, timeout);
	}

}