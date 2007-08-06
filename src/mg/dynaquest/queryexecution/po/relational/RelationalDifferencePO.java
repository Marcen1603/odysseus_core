/*
 * Created on 19.01.2005
 *
 */
package mg.dynaquest.queryexecution.po.relational;

import java.util.Collection;
import java.util.concurrent.TimeoutException;

import org.w3c.dom.NodeList;

import mg.dynaquest.queryexecution.event.POException;
import mg.dynaquest.queryexecution.po.algebra.DifferencePO;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.base.BinaryPlanOperator;
import mg.dynaquest.queryexecution.po.relational.object.RelationalTuple;

/**
 * @author Marco Grawunder
 *
 */
public class RelationalDifferencePO extends BinaryPlanOperator implements
        IsSetOperator {

    public RelationalDifferencePO(RelationalDifferencePO po){
        super(po);
    }
    
    public RelationalDifferencePO(){super();}
    
    public RelationalDifferencePO(DifferencePO differencePO){
    	super(differencePO);
    }
    
    /* (non-Javadoc)
     * @see mg.dynaquest.queryexecution.po.relational.IsSetOperator#getAllReadElements()
     */
    public Collection<RelationalTuple> getAllReadElements() {
        // TODO Auto-generated method stub
        return null;
    }

	public SupportsCloneMe cloneMe() {
		return new RelationalDifferencePO(this);
	}

	@Override
	protected boolean process_close() throws POException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected Object process_next() throws POException, TimeoutException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean process_open() throws POException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getInternalPOName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void getInternalXMLRepresentation(String baseIndent, String indent, StringBuffer xmlRetValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initInternalBaseValues(NodeList childNodes) {
		// TODO Auto-generated method stub
		
	}

}
