package de.uniol.inf.is.odysseus.physicaloperator.base.aggregate;

import de.uniol.inf.is.odysseus.base.AggregateFunction;
import de.uniol.inf.is.odysseus.base.FESortedPair;
import de.uniol.inf.is.odysseus.metadata.base.PairMap;
import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.Evaluator;
import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.Initializer;
import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.Merger;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

public abstract class GroupingHelper<R> {
	

	abstract public int getGroupID(R elem);
	abstract public void init(); 
	abstract public R createOutputElement(Integer groupID, PairMap<SDFAttribute, AggregateFunction, R, ?> r);

	abstract public Initializer<R> getInitAggFunction(FESortedPair<SDFAttribute, AggregateFunction> p);
	abstract public Merger<R> getMergerAggFunction(FESortedPair<SDFAttribute, AggregateFunction> p);
	abstract public Evaluator<R> getEvaluatorAggFunction(FESortedPair<SDFAttribute, AggregateFunction> p);
	
}
