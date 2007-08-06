package mg.dynaquest.queryexecution.po.algebra;

import mg.dynaquest.queryexecution.po.base.PlanOperator;


public abstract class BinaryAlgebraPO extends AlgebraPO {

	public BinaryAlgebraPO(AlgebraPO po) {
		super(po);
		setNoOfInputs(2);
	}

	public BinaryAlgebraPO() {
		super();
		setNoOfInputs(2);
	}

	public void setLeftInput(AlgebraPO po) {
		setInputPO(0,po);
	}

	public void setLeftInput(PlanOperator po){
		setPhysInputPO(0, po);
	}

	
	public AlgebraPO getLeftInput() {
		return getInputPO(0);
	}
	
	
	public void setRightInput(AlgebraPO po) {
	    setInputPO(1,po);
	}
	
	public void setRightInput(PlanOperator po){
		setPhysInputPO(1, po);
	}
	
	public AlgebraPO getRightInput() {
		return getInputPO(1);
	}

}
