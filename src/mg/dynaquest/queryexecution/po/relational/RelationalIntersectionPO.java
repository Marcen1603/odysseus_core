package mg.dynaquest.queryexecution.po.relational;

import java.util.Collection;
import java.util.concurrent.TimeoutException;

import org.w3c.dom.NodeList;

import mg.dynaquest.queryexecution.event.POException;
import mg.dynaquest.queryexecution.po.algebra.IntersectionPO;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.base.BinaryPlanOperator;
import mg.dynaquest.queryexecution.po.relational.object.RelationalTuple;

public class RelationalIntersectionPO extends BinaryPlanOperator implements
		IsSetOperator {
	
	public RelationalIntersectionPO(RelationalIntersectionPO intersectionPO) {
		super(intersectionPO);	
	}
	
	public RelationalIntersectionPO(IntersectionPO intersectionPO) {
		super(intersectionPO);	
	}
	
	
	@Override
	public String getInternalPOName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void getInternalXMLRepresentation(String baseIndent,
			String indent, StringBuffer xmlRetValue) {
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

	public Collection<RelationalTuple> getAllReadElements() {
		// TODO Auto-generated method stub
		return null;
	}

	public SupportsCloneMe cloneMe(){
		return new RelationalIntersectionPO(this);
	}

}
