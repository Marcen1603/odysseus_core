/*
 * Created on 17.02.2005
 *
 */
package mg.dynaquest.queryexecution.po.relational;

import java.util.concurrent.TimeoutException;

import org.w3c.dom.NodeList;

import mg.dynaquest.queryexecution.event.POException;
import mg.dynaquest.queryexecution.po.algebra.SelectJoinPO;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.base.BinaryPlanOperator;

/**
 * @author Marco Grawunder
 *
 */
public class RelationalSelectJoinPO extends BinaryPlanOperator {

    /**
     * @param joinPO
     */
    public RelationalSelectJoinPO(SelectJoinPO joinPO) {
        super(joinPO);
    }

    public RelationalSelectJoinPO(RelationalSelectJoinPO joinPO2) {
		super(joinPO2);
	}

	public SupportsCloneMe cloneMe() {
		return new RelationalSelectJoinPO(this);
	}

	@Override
	public String getInternalPOName() {
		return "RelationalSelectJoin";
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
