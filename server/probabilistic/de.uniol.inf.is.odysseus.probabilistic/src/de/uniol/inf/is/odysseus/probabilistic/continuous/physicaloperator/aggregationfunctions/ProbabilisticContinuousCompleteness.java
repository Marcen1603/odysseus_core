/**
 * 
 */
package de.uniol.inf.is.odysseus.probabilistic.continuous.physicaloperator.aggregationfunctions;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.Completeness;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.CompletenessPartialAggregate;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.ExtendedMixtureMultivariateRealDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.continuous.datatype.ProbabilisticContinuousDouble;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticContinuousCompleteness extends Completeness<ProbabilisticTuple<?>, ProbabilisticTuple<?>> {
    /**
     * 
     */
    private static final long serialVersionUID = -5184003084875276744L;
    private int pos;

    private ProbabilisticContinuousCompleteness(int pos, boolean partialAggregateInput, String datatype) {
        super(partialAggregateInput, datatype);
        this.pos = pos;
    }

    public static ProbabilisticContinuousCompleteness getInstance(int pos, boolean partialAggregateInput, String datatype) {
        return new ProbabilisticContinuousCompleteness(pos, partialAggregateInput, datatype);
    }

    @Override
    public IPartialAggregate<ProbabilisticTuple<?>> init(ProbabilisticTuple<?> in) {
        ProbabilisticContinuousDouble distribution = (ProbabilisticContinuousDouble) in.getAttribute(getPos());

        IPartialAggregate<ProbabilisticTuple<?>> pa = new CompletenessPartialAggregate<ProbabilisticTuple<?>>(getCount(in.getDistribution(distribution.getDistribution())));
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
        ProbabilisticContinuousDouble distribution = (ProbabilisticContinuousDouble) toMerge.getAttribute(getPos());

        pa.add(getCount(toMerge.getDistribution(distribution.getDistribution())));
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
     * @return the pos
     */
    private int getPos() {
        return this.pos;
    }

    /**
     * Gets the count value for a specific attribute value.
     * 
     * @param value
     *            The atribute value
     */
    private double getCount(ExtendedMixtureMultivariateRealDistribution value) {
        return 1.0 / value.getScale();
    }
}
