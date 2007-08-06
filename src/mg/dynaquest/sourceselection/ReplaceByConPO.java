package mg.dynaquest.sourceselection;

import java.util.HashMap;
import mg.dynaquest.queryexecution.event.POException;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.base.UnaryPlanOperator;
import org.w3c.dom.NodeList;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;


public class ReplaceByConPO extends UnaryPlanOperator {

	/**
	 * @uml.property  name="poName"
	 */
	private String poName = "Unnamed CompensatePO";  // Wird beim initialisieren überschrieben

	public ReplaceByConPO(HashMap constReplacements) 
	{
		 super();
	}
	

	
    /**
     * @param conPO
     */
    public ReplaceByConPO(ReplaceByConPO conPO) {
        super(conPO);
    }

    /* (non-Javadoc)
     * @see mg.dynaquest.queryexecution.po.base.PlanOperator#getInternalPOName()
     */
    public String getInternalPOName() {
        // TODO Auto-generated method stub
    	 return this.poName;
    }

    /* (non-Javadoc)
     * @see mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator#process_open()
     */
    protected boolean process_open() throws POException {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator#process_next()
     */
    protected Object process_next() throws POException {
        // TODO Auto-generated method stub
       
    	
    	return null;
    }

    /* (non-Javadoc)
     * @see mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator#process_close()
     */
    protected boolean process_close() throws POException {
        // TODO Auto-generated method stub
        return false;
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
