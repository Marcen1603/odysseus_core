package de.uniol.inf.is.odysseus.transformation.drools;

import java.util.List;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;

public class Utils {
	@SuppressWarnings("unchecked")
	public static void insertOperator(ILogicalOperator logicalOp,
			IPhysicalOperator physicalOp, List<ILogicalOperator> fathers) {
		for (ILogicalOperator op : fathers) {
			op.setPhysInputAtAOPosition(logicalOp, physicalOp);
		}
		for (int i = 0; i < logicalOp.getNumberOfInputs(); ++i) {
			((ISource) logicalOp.getPhysInputPO(i)).subscribe(
					(ISink) physicalOp, i);
		}
	}
}
