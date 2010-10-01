package de.uniol.inf.is.odysseus.pnapproach.id.physicalopertor.relational.join.helper;

import de.uniol.inf.is.odysseus.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.physicaloperator.ISweepArea.Order;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.IPosNeg;

public interface IPNMetadataMergeFunction<M extends IPosNeg> extends IMetadataMergeFunction<M>{

	public M createNegativeResult(M mData, Order order);
}
