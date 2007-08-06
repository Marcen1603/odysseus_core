/*
 * Created on 17.02.2005
 *
 */
package mg.dynaquest.queryexecution.po.relational;

import java.util.concurrent.TimeoutException;

import org.w3c.dom.NodeList;

import mg.dynaquest.queryexecution.event.POException;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.algebra.UnionPO;
import mg.dynaquest.queryexecution.po.base.BinaryPlanOperator;

/**
 * @author Marco Grawunder
 *
 */
public class RelationalUnionPO extends BinaryPlanOperator {


    /**
     * @param unionPO
     */
    public RelationalUnionPO(UnionPO unionPO) {
        super(unionPO);
    }

    /**
     * 
     */
    public RelationalUnionPO() {
        super();
        // TODO Auto-generated constructor stub
    }

    public RelationalUnionPO(RelationalUnionPO unionPO2) {
		super(unionPO2);
	}

	public static void main(String[] args) {
    }

	public SupportsCloneMe cloneMe() {
		return new RelationalUnionPO(this);
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
}
