package mg.dynaquest.queryexecution.po.algebra;

import mg.dynaquest.queryexecution.po.base.PlanOperator;


public abstract class UnaryAlgebraPO extends AlgebraPO {

	private static final int PORTNUMBER = 0;

	public UnaryAlgebraPO(AlgebraPO po) {
		super(po);
		setNoOfInputs(1);
	}

	public UnaryAlgebraPO() {
		super();
		setNoOfInputs(1);
	}

	public void setInputPO(AlgebraPO po) {
		setInputPO(PORTNUMBER,po);
	}

	public AlgebraPO getInputPO() {
	    return getInputPO(PORTNUMBER);
	}
	
	public PlanOperator getPhysInputPO(){
		return getPhysInputPO(PORTNUMBER);
	}

}
