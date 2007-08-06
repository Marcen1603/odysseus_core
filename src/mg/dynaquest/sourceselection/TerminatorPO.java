/*
 * Created on 07.12.2004
 *
 */
package mg.dynaquest.sourceselection;

import mg.dynaquest.queryexecution.event.POException;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.base.ReadsErrorElementsPO;
import org.apache.log4j.Logger;
import org.w3c.dom.NodeList;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * @author Marco Grawunder
 *
 */
public class TerminatorPO extends ReadsErrorElementsPO {


	/**
	 * Kompensationseigener Debuger
	 * @uml.property  name="logger"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	protected Logger logger = Logger.getLogger(this.getClass()); 
	
	/* (non-Javadoc)
     * @see mg.dynaquest.queryexecution.po.base.PlanOperator#getInternalPOName()
     */
    public String getInternalPOName() {
        return "TerminatorPO";
    }
    
    public void setInternalPOName(String name)
    {
    }

    /* (non-Javadoc)
     * @see mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator#process_open()
     */
    protected boolean process_open() throws POException {
        return true;
    }

    /* (non-Javadoc)
     * @see mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator#process_next()
     */
    protected Object process_next() throws POException {
        // Einfach alles lesen und verwerfen bis null kommt
    	Object obj = null;
    	while ((obj = this.getInputNext(this,-1)) != null)
        {
    		logger.debug(getPOName()+">  Plan ["+((AnnotatedPlan)obj).getIndex()+"]: > GIP:"+((AnnotatedPlan)obj).getGlobalInputPattern()+ "GOP:"+((AnnotatedPlan)obj).getGlobalOutputPattern());
        }
        
        return null;
    }

    /* (non-Javadoc)
     * @see mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator#process_close()
     */
    protected boolean process_close() throws POException {
        return true;
    }

    /* (non-Javadoc)
     * @see mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator#initInternalBaseValues(org.w3c.dom.NodeList)
     */
    protected void initInternalBaseValues(NodeList childNodes) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator#getInternalXMLRepresentation(java.lang.String, java.lang.String, java.lang.StringBuffer)
     */
    protected void getInternalXMLRepresentation(String baseIndent,
            String indent, StringBuffer xmlRetValue) {
        // TODO Auto-generated method stub

    }

    public static void main(String[] args) {
    }

	public SupportsCloneMe cloneMe() {
		throw new NotImplementedException();
	}


}
