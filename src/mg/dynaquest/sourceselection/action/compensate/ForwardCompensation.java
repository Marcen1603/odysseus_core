/*
 * Created on 15.12.2005
 *
 */
package mg.dynaquest.sourceselection.action.compensate;

import java.util.ArrayList;
import mg.dynaquest.sourcedescription.sdf.query.SDFQuery;
import mg.dynaquest.sourceselection.AnnotatedPlan;
import org.w3c.dom.NodeList;

/**
 * @author  Marco Grawunder
 */
public class ForwardCompensation extends AbstractCompensateAction {
	public ForwardCompensation(){}
	/**
	 * Initialisiert die CompensateAction mit der Anfrage.
	 * @param query Nutzeranfrage
	 */
	public ForwardCompensation(SDFQuery query)
	{
    	this.setQuery(query);
	}
	
    public void setQuery(SDFQuery query)
    {
    	setErrorPlans(new ArrayList <AnnotatedPlan>());
    }

    public ArrayList<AnnotatedPlan> compensate(
            ArrayList<AnnotatedPlan> toCompensate) {        
        return toCompensate;
    }

    public void getInternalXMLRepresentation(String baseIndent, String indent,
            StringBuffer xmlRetValue) {
        // TODO Auto-generated method stub

    }

    public void initInternalBaseValues(NodeList children) {
        // TODO Auto-generated method stub

    }
    
	/**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
