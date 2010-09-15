package de.uniol.inf.is.odysseus.util;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.LogicalSubscription;

public class GraphHelper {

	/**
	 * Find all connected Operators from this root
	 * @param root
	 * @return
	 */
	static public ArrayList<IPhysicalOperator> getChildren(IPhysicalOperator root) {
		IGraphNodeVisitor<IPhysicalOperator, ArrayList<IPhysicalOperator>> visitor = new CollectOperatorsVisitor<IPhysicalOperator>();
		AbstractGraphWalker<ArrayList<IPhysicalOperator>, ILogicalOperator, ?> walker = new AbstractGraphWalker<ArrayList<IPhysicalOperator>, ILogicalOperator, LogicalSubscription>();
		walker.prefixWalkPhysical(root, visitor);
		return visitor.getResult();
	}
	
}
