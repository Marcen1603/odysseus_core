package mg.dynaquest.queryexecution.po.base;

import java.util.concurrent.TimeoutException;

import mg.dynaquest.queryexecution.event.POException;


/**
 * Author: $Author: grawund $ Date: $Date: 2004/09/16 08:57:12 $ Version:
 * $Revision: 1.2 $ Log: $Log: ReadsErrorElementsPO.java,v $
 * $Revision: 1.2 $ Log: Revision 1.2  2004/09/16 08:57:12  grawund
 * $Revision: 1.2 $ Log: Quellcode durch Eclipse formatiert
 * $Revision: 1.2 $ Log: Log: Revision 1.1
 * 2003/08/20 13:45:13 grawund Log: no message Log:
 */

public abstract class ReadsErrorElementsPO extends UnaryPlanOperator {

	final public Object getInputNext(PlanOperator caller, long timeout) throws POException {
		WritesErrorElements po = (WritesErrorElements) this.getInputPO();
		//System.out.println("######=> INPUTPO:"+this.getInputPO().getPOName());
		Object next = null;
        try {
            next = po.nextErrorElement();
        } catch (TimeoutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		return next;
	}


    /* (non-Javadoc)
     * @see mg.dynaquest.queryexecution.po.base.NAryPlanOperator#open(mg.dynaquest.queryexecution.po.base.PlanOperator)
     */
    @Override
    public void open(SimplePlanOperator caller) {
        try {
            process_open();
            this.addConsumer(caller);
        } catch (POException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Da der Fehlerweg immer ein alternativer Weg ist,
        // da open nie weitergeleitet werden
    }
    
    /* (non-Javadoc)
     * @see mg.dynaquest.queryexecution.po.base.NAryPlanOperator#open(mg.dynaquest.queryexecution.po.base.PlanOperator)
     */
    @Override
    public boolean close(SimplePlanOperator caller) {
        try {
            this.removeConsumer(caller);
            return process_close();            
        } catch (POException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        // Da der Fehlerweg immer ein alternativer Weg ist,
        // da close nie weitergeleitet werden
    }   

}