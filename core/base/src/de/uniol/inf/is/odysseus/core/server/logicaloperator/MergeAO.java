package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

/**
 * Merge different input streams to one common stream
 * 
 * @author Marco Grawunder
 *
 */

@LogicalOperator(name = "MERGE", minInputPorts = 1, maxInputPorts = Integer.MAX_VALUE)
public class MergeAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 4206936945113954674L;

	public MergeAO(){
		super();
	}
	
	public MergeAO(MergeAO mergeAO) {
		super(mergeAO);
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new MergeAO(this);
	}

}
