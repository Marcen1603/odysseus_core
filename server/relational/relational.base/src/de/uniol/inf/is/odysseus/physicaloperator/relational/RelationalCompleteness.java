/**
 * 
 */
package de.uniol.inf.is.odysseus.physicaloperator.relational;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.Completeness;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.CompletenessPartialAggregate;

/**
 * Computes the completeness of a relational stream that is defined as the ratio
 * between non-null attribute values to the number of tuple in an aggregate
 * window.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class RelationalCompleteness extends Completeness<Tuple<?>, Tuple<?>> {

    /**
     * 
     */
    private static final long serialVersionUID = 9092772748058684135L;
    private int pos;

    private RelationalCompleteness(int pos, boolean partialAggregateInput, String datatype) {
        super(partialAggregateInput, datatype);
        this.pos = pos;
    }

    public static RelationalCompleteness getInstance(int pos, boolean partialAggregateInput, String datatype) {
        return new RelationalCompleteness(pos, partialAggregateInput, datatype);
    }

    @Override
    public IPartialAggregate<Tuple<?>> init(Tuple<?> in) {
        IPartialAggregate<Tuple<?>> pa = new CompletenessPartialAggregate<Tuple<?>>(getCount(in.getAttribute(getPos())));
        return pa;
    }

    @Override
    public IPartialAggregate<Tuple<?>> merge(IPartialAggregate<Tuple<?>> p, Tuple<?> toMerge, boolean createNew) {
        CompletenessPartialAggregate<Tuple<?>> pa = null;
        if (createNew) {
            pa = new CompletenessPartialAggregate<Tuple<?>>(((CompletenessPartialAggregate<Tuple<?>>) p));
        }
        else {
            pa = (CompletenessPartialAggregate<Tuple<?>>) p;
        }
        pa.add(getCount(toMerge.getAttribute(getPos())));
        return pa;
    }

    @Override
    public Tuple<?> evaluate(IPartialAggregate<Tuple<?>> p) {
        CompletenessPartialAggregate<Tuple<?>> pa = (CompletenessPartialAggregate<Tuple<?>>) p;

        @SuppressWarnings("rawtypes")
        Tuple r = new Tuple(1, false);
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
    private double getCount(Object value) {
        double count = 0.0;
        if (value != null) {
            count = 1.0;
        }
        return count;
    }
}
