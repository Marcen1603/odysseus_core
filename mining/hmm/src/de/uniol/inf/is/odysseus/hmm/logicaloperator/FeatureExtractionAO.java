package de.uniol.inf.is.odysseus.hmm.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(name="FEATUREEXTRACTION", minInputPorts=1, maxInputPorts=1)
public class FeatureExtractionAO extends UnaryLogicalOp implements ILogicalOperator {

	/**
	 * Auto-generated serial by eclipse
	 */
	private static final long serialVersionUID = -3986431121912825299L;


	// The default constructor is required as instances of logical operators are created by newInstance()
	public FeatureExtractionAO() {
		super();
	}

	//constructor called, if clone() was used
	public FeatureExtractionAO(FeatureExtractionAO feAO) {
		super(feAO);
	}
		
		
	@Override
	public AbstractLogicalOperator clone() {
		// TODO Auto-generated method stub
		return new FeatureExtractionAO(this);
	}

}
