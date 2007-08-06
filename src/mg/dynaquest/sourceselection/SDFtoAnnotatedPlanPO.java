/*
 * Created on 17.11.2004
 *
 */
package mg.dynaquest.sourceselection;

import java.util.concurrent.TimeoutException;
import mg.dynaquest.queryexecution.event.POException;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.base.UnaryPlanOperator;
import mg.dynaquest.sourcedescription.sdf.description.SDFSourceDescription;
import mg.dynaquest.sourcedescription.sdf.query.SDFQuery;
import org.w3c.dom.NodeList;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * @author  Marco Grawunder
 */
public class SDFtoAnnotatedPlanPO extends UnaryPlanOperator {

    /**
	 * @uml.property  name="currSourceDescription"
	 * @uml.associationEnd  
	 */
    SDFSourceDescription currSourceDescription = null;
    /**
	 * @uml.property  name="lastAccessPattern"
	 */
    int lastAccessPattern = -1;
    /**
	 * @uml.property  name="query"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    SDFQuery query = null;
    
	public SDFtoAnnotatedPlanPO(SDFQuery query) {
		this.query = query;
	}
    
    /* (non-Javadoc)
     * @see mg.dynaquest.queryexecution.po.base.PlanOperator#getInternalPOName()
     */
    public String getInternalPOName() {
        return "SDFtoAnnotatedPlanPO";
    }

    /* (non-Javadoc)
     * @see mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator#process_open()
     */
    protected synchronized boolean process_open() throws POException {
        currSourceDescription = null;
        lastAccessPattern = -1;
        return true;
    }

    /* (non-Javadoc)
     * @see mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator#process_next()
     */
    protected synchronized Object process_next() throws POException, TimeoutException {
       
        // Wenn keine aktuelle Quellenbeschreibung vorhanden ist, eine auslesen
        if (currSourceDescription == null){
            currSourceDescription = (SDFSourceDescription) this.getInputNext(this, -1);
            // Falls keiner geliefert wurde 
            if (currSourceDescription == null) return null;
            lastAccessPattern = -1;
        }
         
        // Schauen, ob es in der aktuellen QB noch unbearbeitete Zugriffsmuster gibt
        if (lastAccessPattern+1 < currSourceDescription.getIntDesc().getAccessPatternCount()){
            try {
            	 AnnotatedPlan aPlan = new AnnotatedPlan(currSourceDescription, ++lastAccessPattern, query);
            	 return aPlan;
            } catch (Exception e) {
            	e.printStackTrace();
            	throw new POException(e);}
        }else{
            currSourceDescription = null;
            return process_next();
        }
    }

    /* (non-Javadoc)
     * @see mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator#process_close()
     */
    protected synchronized boolean process_close() throws POException {
        currSourceDescription = null;
        lastAccessPattern = -1;
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

	public SupportsCloneMe cloneMe() {
		throw new NotImplementedException();
	}



}
