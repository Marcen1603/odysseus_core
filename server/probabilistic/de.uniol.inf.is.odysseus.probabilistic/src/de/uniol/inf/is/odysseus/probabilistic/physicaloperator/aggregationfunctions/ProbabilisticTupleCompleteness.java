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
public class ProbabilisticTupleCompleteness extends Completeness<ProbabilisticTuple<IProbabilistic>, ProbabilisticTuple<?>> {
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
    public IPartialAggregate<ProbabilisticTuple<IProbabilistic>> init(final ProbabilisticTuple<IProbabilistic> in) {
        final IPartialAggregate<ProbabilisticTuple<IProbabilistic>> pa = new CompletenessPartialAggregate<ProbabilisticTuple<IProbabilistic>>(this.getCount(in.getMetadata()));
        return pa;
    }

    @Override
    public IPartialAggregate<ProbabilisticTuple<IProbabilistic>> merge(final IPartialAggregate<ProbabilisticTuple<IProbabilistic>> p, final ProbabilisticTuple<IProbabilistic> toMerge,
            final boolean createNew) {
        CompletenessPartialAggregate<ProbabilisticTuple<IProbabilistic>> pa = null;
        if (createNew) {
            pa = new CompletenessPartialAggregate<ProbabilisticTuple<IProbabilistic>>(((CompletenessPartialAggregate<ProbabilisticTuple<IProbabilistic>>) p));
        }
        else {
            pa = (CompletenessPartialAggregate<ProbabilisticTuple<IProbabilistic>>) p;
        }
        pa.add(this.getCount(toMerge.getMetadata()));
        return pa;
    }

    @Override
    public ProbabilisticTuple<?> evaluate(final IPartialAggregate<ProbabilisticTuple<IProbabilistic>> p) {
        final CompletenessPartialAggregate<ProbabilisticTuple<IProbabilistic>> pa = (CompletenessPartialAggregate<ProbabilisticTuple<IProbabilistic>>) p;

        final ProbabilisticTuple<IProbabilistic> r = new ProbabilisticTuple<IProbabilistic>(1, false);
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
