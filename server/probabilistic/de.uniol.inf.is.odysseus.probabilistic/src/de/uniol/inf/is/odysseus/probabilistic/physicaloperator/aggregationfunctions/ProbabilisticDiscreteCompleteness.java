/**
 * 
 */
package de.uniol.inf.is.odysseus.probabilistic.physicaloperator.aggregationfunctions;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.Completeness;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.CompletenessPartialAggregate;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.discrete.datatype.AbstractProbabilisticValue;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticDiscreteCompleteness extends Completeness<ProbabilisticTuple<?>, ProbabilisticTuple<?>> {
    /**
     * 
     */
    private static final long serialVersionUID = 5979362406371490105L;
    private int pos;

    private ProbabilisticDiscreteCompleteness(int pos, boolean partialAggregateInput, String datatype) {
        super(partialAggregateInput, datatype);
        this.pos = pos;
    }

    public static ProbabilisticDiscreteCompleteness getInstance(int pos, boolean partialAggregateInput, String datatype) {
        return new ProbabilisticDiscreteCompleteness(pos, partialAggregateInput, datatype);
    }

    @Override
    public IPartialAggregate<ProbabilisticTuple<?>> init(ProbabilisticTuple<?> in) {
        IPartialAggregate<ProbabilisticTuple<?>> pa = new CompletenessPartialAggregate<ProbabilisticTuple<?>>(getCount((AbstractProbabilisticValue<?>) in.getAttribute(getPos())));
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
        pa.add(getCount((AbstractProbabilisticValue<?>) toMerge.getAttribute(getPos())));
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
     *            The attribute value
     */
    private double getCount(AbstractProbabilisticValue<?> value) {
        double sum = 0.0;
        for (Object p : value.getValues().values()) {
            sum += ((Number) p).doubleValue();
        }
        return sum;
    }
}
