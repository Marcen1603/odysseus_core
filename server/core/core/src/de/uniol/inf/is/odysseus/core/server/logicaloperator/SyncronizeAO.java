package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(minInputPorts = 2, maxInputPorts = Integer.MAX_VALUE, name = "SYNCHRONIZE", doc = "Synchronizes different input streams", url = "http://wiki.odysseus.informatik.uni-oldenburg.de/display/ODYSSEUS/Synchronize+operator", category = {
        LogicalOperatorCategory.BASE, LogicalOperatorCategory.SET })
public class SyncronizeAO extends UnionAO {

	private static final long serialVersionUID = -6499201712284116683L;

	public SyncronizeAO(UnionAO unionPO) {
		super(unionPO);
	}

	public SyncronizeAO() {
		useInputPortAsOutputPort = true;
	}

}
