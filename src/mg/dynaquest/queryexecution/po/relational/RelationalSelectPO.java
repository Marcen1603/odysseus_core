/*
 * Created on 25.01.2005
 *
 */
package mg.dynaquest.queryexecution.po.relational;

import java.util.HashMap;
import java.util.concurrent.TimeoutException;
import mg.dynaquest.queryexecution.event.POException;
import mg.dynaquest.queryexecution.po.algebra.SelectPO;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.base.NAryPlanOperator;
import mg.dynaquest.queryexecution.po.base.UnaryPlanOperator;
import mg.dynaquest.queryexecution.po.relational.object.RelationalTuple;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;
import mg.dynaquest.sourcedescription.sdf.schema.SDFConstant;
import mg.dynaquest.sourcedescription.sdf.schema.SDFNumberConstant;
import mg.dynaquest.sourcedescription.sdf.schema.SDFStringConstant;
import mg.dynaquest.sourcedescription.sdf.vocabulary.SDFDatatypes;
import org.w3c.dom.NodeList;

/**
 * @author  Marco Grawunder
 */
public class RelationalSelectPO extends UnaryPlanOperator {

	/** Braucht nur einmal angelegt werden und wird immer mit passenden Werten gefüllt*/
	transient HashMap<SDFAttribute, SDFConstant> attribMap = new HashMap<SDFAttribute, SDFConstant>();
		
    public RelationalSelectPO(SelectPO selectPO){
        super(selectPO);
    }
    
    public RelationalSelectPO(RelationalSelectPO selectPO2) {
		super(selectPO2);
	}

	/* (non-Javadoc)
     * @see mg.dynaquest.queryexecution.po.base.PlanOperator#getInternalPOName()
     */
    public String getInternalPOName() {
        return "RelationalSelectPO";
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
    protected Object process_next() throws POException, TimeoutException {
        // Vom Vorgänger solange Objekte anfordern, bis eines die Selektions-
        // bedingung erfüllt
        RelationalTuple retVal = (RelationalTuple) this.getInputNext(this, -1);
        
        retVal = doSelection(retVal);
        return retVal;
    }

	private RelationalTuple doSelection(RelationalTuple retVal) throws POException, TimeoutException {
		while (retVal != null){
            // Aus dem RelationalTuple ein Element für das SDFPredicate machen
        	        
            for (int i=0;i<getInAttribs().getAttributeCount();i++){
                if (getInAttribs().getAttribute(i).getDatatype().equals(SDFDatatypes.String)){
                    attribMap.put(getInAttribs().getAttribute(i), new SDFStringConstant("",retVal.getAttribute(i)));
                }
                if (getInAttribs().getAttribute(i).getDatatype().equals(SDFDatatypes.Number)){
                    attribMap.put(getInAttribs().getAttribute(i), new SDFNumberConstant("",retVal.getAttribute(i)));
                }
            }
            
            // Wenn das Tupel das Prädikat erfüllt, die Schleife verlassen
            if (this.getPredicate().evaluate(attribMap)){
                break;
            }   
            retVal = (RelationalTuple) this.getInputNext(this, -1);
        }
		return retVal;
	}
    
	private SDFAttributeList getInAttribs() {
		return ((NAryPlanOperator) this.getInputPO()).getOutElements();
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
    	xmlRetValue.append(indent).append("<algebraPO>\n");
    	getAlgebraPO().getInternalXMLRepresentation(baseIndent,indent+indent,xmlRetValue);
    	xmlRetValue.append(indent).append("</algebraPO>\n");
    }

	public SupportsCloneMe cloneMe() {
		return new RelationalSelectPO(this);
	}
}
