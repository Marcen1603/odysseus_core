/**
 * 
 */
package de.uniol.inf.is.odysseus.probabilistic.physicaloperator.aggregationfunctions;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.Completeness;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.CompletenessPartialAggregate;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticTupleCompleteness extends Completeness<ProbabilisticTuple<?>, ProbabilisticTuple<?>> {
    /**
     * 
     */
    private static final long serialVersionUID = -1459891904889134576L;

    private ProbabilisticTupleCompleteness(boolean partialAggregateInput, String datatype) {
        super(partialAggregateInput, datatype);
    }

    public static ProbabilisticTupleCompleteness getInstance(boolean partialAggregateInput, String datatype) {
        return new ProbabilisticTupleCompleteness(partialAggregateInput, datatype);
    }

    @Override
    public IPartialAggregate<ProbabilisticTuple<?>> init(ProbabilisticTuple<?> in) {
        IPartialAggregate<ProbabilisticTuple<?>> pa = new CompletenessPartialAggregate<ProbabilisticTuple<?>>(getCount((IProbabilistic) in.getMetadata()));
        return pa;
    }

    @Override
    public IPartialAggregate<ProbabilisticTuple<?>> merge(IPartialAggregate<ProbabilisticTuple<?>> p, ProbabilisticTuple<?> toMerge, boolean createNew) {
        CompletenessPartialAggregate<ProbabilisticTuple<?>> pa = null;
        if (createNew) {
            pa = new CompletenessPartialAggregate<ProbabilisticTuple<?>>(((CompletenessPartialAggregate<ProbabilisticTuple<?>>) p));
        }
        else {
            pa = (CompletenessPartialAggregate<ProbabilisticTuple<?>>) p;
        }
        pa.add(getCount((IProbabilistic) toMerge.getMetadata()));
        return pa;
    }

    @Override
    public ProbabilisticTuple<?> evaluate(IPartialAggregate<ProbabilisticTuple<?>> p) {
        CompletenessPartialAggregate<ProbabilisticTuple<?>> pa = (CompletenessPartialAggregate<ProbabilisticTuple<?>>) p;

        @SuppressWarnings("rawtypes")
        ProbabilisticTuple r = new ProbabilisticTuple(1, false);
        r.setAttribute(0, pa.getCompleteness());
        return r;
    }

    /**
     * Gets the count value for a specific tuple value.
     * 
     * @param value
     *            The tuple value
     */
    private double getCount(IProbabilistic value) {
        return value.getExistence();
    }
}
