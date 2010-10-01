package de.uniol.inf.is.odysseus.physicaloperator.aggregate;

import de.uniol.inf.is.odysseus.collection.FESortedPair;
import de.uniol.inf.is.odysseus.collection.PairMap;
import de.uniol.inf.is.odysseus.physicaloperator.AggregateFunction;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IEvaluator;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IInitializer;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IMerger;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public abstract class GroupingHelper<R> {

    abstract public int getGroupID(R elem);

    abstract public void init();

    abstract public R createOutputElement(Integer groupID,
            PairMap<SDFAttributeList, AggregateFunction, R, ?> r);

    abstract public IInitializer<R> getInitAggFunction(
            FESortedPair<SDFAttributeList, AggregateFunction> p);

    abstract public IMerger<R> getMergerAggFunction(
            FESortedPair<SDFAttributeList, AggregateFunction> p);

    abstract public IEvaluator<R> getEvaluatorAggFunction(
            FESortedPair<SDFAttributeList, AggregateFunction> p);

}
