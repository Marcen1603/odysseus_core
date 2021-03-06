package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.InputOrderRequirement;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

/**
 * Merge different input streams to one common stream
 * 
 * @author Marco Grawunder
 *
 */

@LogicalOperator(name = "MERGE", minInputPorts = 1, maxInputPorts = Integer.MAX_VALUE, doc = "Merge different input streams into one stream with \"first comes first served\" semantics.", url = "http://wiki.odysseus.informatik.uni-oldenburg.de/display/ODYSSEUS/Merge+operator", category = { LogicalOperatorCategory.BASE })
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
	
	@Override
		public InputOrderRequirement getInputOrderRequirement(int inputPort) {
			return InputOrderRequirement.NONE;
		}

	
	
}
