package de.uniol.inf.is.odysseus.scars.operator.jdvesink.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class JDVESinkAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -8687211058868598662L;

	private int port;

	public JDVESinkAO() {

	}

	public JDVESinkAO(JDVESinkAO op) {
		this.port = op.port;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return this.getInputSchema();
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new JDVESinkAO(this);
	}

}
