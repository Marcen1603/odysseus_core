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

    private ProbabilisticTupleCompleteness(final boolean partialAggregateInput, final String datatype) {
        super(partialAggregateInput, datatype);
    }

    public static ProbabilisticTupleCompleteness getInstance(final boolean partialAggregateInput, final String datatype) {
        return new ProbabilisticTupleCompleteness(partialAggregateInput, datatype);
    }

    @Override
    public IPartialAggregate<ProbabilisticTuple<?>> init(final ProbabilisticTuple<?> in) {
        final IPartialAggregate<ProbabilisticTuple<?>> pa = new CompletenessPartialAggregate<ProbabilisticTuple<?>>(this.getCount((IProbabilistic) in.getMetadata()));
        return pa;
    }

    @Override
    public IPartialAggregate<ProbabilisticTuple<?>> merge(final IPartialAggregate<ProbabilisticTuple<?>> p, final ProbabilisticTuple<?> toMerge, final boolean createNew) {
        CompletenessPartialAggregate<ProbabilisticTuple<?>> pa = null;
        if (createNew) {
            pa = new CompletenessPartialAggregate<ProbabilisticTuple<?>>(((CompletenessPartialAggregate<ProbabilisticTuple<?>>) p));
        }
        else {
            pa = (CompletenessPartialAggregate<ProbabilisticTuple<?>>) p;
        }
        pa.add(this.getCount((IProbabilistic) toMerge.getMetadata()));
        return pa;
    }

    @Override
    public ProbabilisticTuple<?> evaluate(final IPartialAggregate<ProbabilisticTuple<?>> p) {
        final CompletenessPartialAggregate<ProbabilisticTuple<?>> pa = (CompletenessPartialAggregate<ProbabilisticTuple<?>>) p;

        @SuppressWarnings("rawtypes")
        final ProbabilisticTuple r = new ProbabilisticTuple(1, false);
        r.setAttribute(0, pa.getCompleteness());
        return r;
    }

    /**
     * Gets the count value for a specific tuple value.
     * 
     * @param value
     *            The tuple value
     */
    private double getCount(final IProbabilistic value) {
        return value.getExistence();
    }
}
