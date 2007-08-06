/*
 * Created on 17.02.2005
 *
 */
package mg.dynaquest.queryexecution.po.relational;

import java.util.concurrent.TimeoutException;

import org.w3c.dom.NodeList;

import mg.dynaquest.queryexecution.event.POException;
import mg.dynaquest.queryexecution.po.algebra.JoinPO;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;

/**
 * @author Marco Grawunder
 *
 */
public class NestedLoopJoinPO extends RelationalPhysicalJoinPO {


    /**
     * 
     */
    public NestedLoopJoinPO() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @param joinPO
     */
    public NestedLoopJoinPO(JoinPO joinPO) {
        super(joinPO);
        // TODO Auto-generated constructor stub
    }

    public NestedLoopJoinPO(NestedLoopJoinPO joinPO) {
		super(joinPO);
	}

	public static void main(String[] args) {
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

	public SupportsCloneMe cloneMe() {
		return new NestedLoopJoinPO(this);
	}

	@Override
	public String getInternalPOName() {
		return "NestedLoopJoinPO";
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
