package de.uniol.inf.is.odysseus.physicaloperator.base.aggregate;

import de.uniol.inf.is.odysseus.base.AggregateFunction;
import de.uniol.inf.is.odysseus.base.FESortedPair;
import de.uniol.inf.is.odysseus.metadata.base.PairMap;
import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.IEvaluator;
import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.IInitializer;
import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.IMerger;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

public abstract class GroupingHelper<R> {
	

	abstract public int getGroupID(R elem);
	abstract public void init(); 
	abstract public R createOutputElement(Integer groupID, PairMap<SDFAttribute, AggregateFunction, R, ?> r);

	abstract public IInitializer<R> getInitAggFunction(FESortedPair<SDFAttribute, AggregateFunction> p);
	abstract public IMerger<R> getMergerAggFunction(FESortedPair<SDFAttribute, AggregateFunction> p);
	abstract public IEvaluator<R> getEvaluatorAggFunction(FESortedPair<SDFAttribute, AggregateFunction> p);
	
}
