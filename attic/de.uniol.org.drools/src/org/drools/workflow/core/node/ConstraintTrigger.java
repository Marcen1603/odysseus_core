package org.drools.workflow.core.node;

public class ConstraintTrigger extends Trigger implements Constrainable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7143373413459292490L;
	private String constraint;

	public String getConstraint() {
		return constraint;
	}

	public void setConstraint(String constraint) {
		this.constraint = constraint;
	}
	
}
