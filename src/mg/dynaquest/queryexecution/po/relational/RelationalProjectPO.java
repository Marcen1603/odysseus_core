/*
 * Created on 25.01.2005
 *
 */
package mg.dynaquest.queryexecution.po.relational;

import java.util.concurrent.TimeoutException;
import mg.dynaquest.queryexecution.event.POException;
import mg.dynaquest.queryexecution.po.algebra.ProjectPO;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.base.NAryPlanOperator;
import mg.dynaquest.queryexecution.po.base.UnaryPlanOperator;
import mg.dynaquest.queryexecution.po.relational.object.RelationalTuple;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;
import org.apache.log4j.Logger;
import org.w3c.dom.NodeList;

/**
 * @author Marco Grawunder
 *
 */
public class RelationalProjectPO extends UnaryPlanOperator {

	/**
	 * @uml.property  name="logger"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private Logger logger = Logger.getLogger(this.getClass().getName());
    /**
	 * @uml.property  name="restrictList" multiplicity="(0 -1)" dimension="1"
	 */
    private int[] restrictList;

    /**
     * @param projectPO
     */
    public RelationalProjectPO(ProjectPO projectPO) {
       super(projectPO); 
    }
    public RelationalProjectPO(RelationalProjectPO projectPO2) {
		super(projectPO2);
	}
	/* (non-Javadoc)
     * @see mg.dynaquest.queryexecution.po.base.PlanOperator#getInternalPOName()
     */
    public String getInternalPOName() {
        return "RelationalProjectPO";
    }
    
    /* (non-Javadoc)
     * @see mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator#getInternalXMLRepresentation(java.lang.String, java.lang.String, java.lang.StringBuffer)
     */
    protected void getInternalXMLRepresentation(String baseIndent, String indent, StringBuffer xmlRetValue) {
    	xmlRetValue.append(indent).append("<algebraPO>\n");
    	getAlgebraPO().getInternalXMLRepresentation(baseIndent,indent+indent,xmlRetValue);
    	xmlRetValue.append(indent).append("</algebraPO>\n");
    }
    /* (non-Javadoc)
     * @see mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator#initInternalBaseValues(org.w3c.dom.NodeList)
     */
    protected void initInternalBaseValues(NodeList childNodes) {
        // TODO Auto-generated method stub
    }
    /* (non-Javadoc)
     * @see mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator#process_close()
     */
    protected boolean process_close() throws POException {
        return true;
    }
    /* (non-Javadoc)
     * @see mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator#process_next()
     */
    protected Object process_next() throws POException, TimeoutException {
        // Eine Projektion schränkt die Eingaben auf bestimmte Attribute ein.
        RelationalTuple inputVal = (RelationalTuple) this.getInputNext(this, -1);
        RelationalTuple retVal = null;
        if (inputVal != null){
        	retVal = inputVal.restrict(restrictList);
        }
        return retVal; 
    }
    /* (non-Javadoc)
     * @see mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator#process_open()
     */
    protected boolean process_open() throws POException {
        // Anhand der eigenen Ausgaben und der Eingaben des Vorgängers ermitteln, welche
        // Attribute drin bleiben sollen
        SDFAttributeList out = this.getOutElements();
        SDFAttributeList in = ((NAryPlanOperator) this.getInputPO()).getOutElements();
        restrictList = new int[out.size()];
        int j=0;
        for (int i=0;i<in.size() && j<out.size();i++){
            if (in.get(i) == out.get(j)){
                restrictList[j] = i;
                j++;
            }
        }
        logger.info("--> restrictList Length "+restrictList.length);
        return true;
    }
    
	public SupportsCloneMe cloneMe() {
		return new RelationalProjectPO(this);
	}

}
