package de.uniol.inf.is.odysseus.salsa.aggregation;

import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class MergeDoubleGrid extends AbstractAggregateFunction<RelationalTuple<?>, RelationalTuple<?>> {

    private final int attribPos;

    public MergeDoubleGrid(int[] pos) {
        super("MergeGrid");
        this.attribPos = pos[0];
    }

    @Override
    public IPartialAggregate<RelationalTuple<?>> init(final RelationalTuple<?> in) {
        final IPartialAggregate<RelationalTuple<?>> grid = new DoubleGridPartialAggregate<RelationalTuple<?>>(
                (Double[][]) in.getAttribute(attribPos));
        return grid;
    }

    @Override
    public IPartialAggregate<RelationalTuple<?>> merge(
            final IPartialAggregate<RelationalTuple<?>> p, final RelationalTuple<?> toMerge,
            final boolean createNew) {
        DoubleGridPartialAggregate<RelationalTuple<?>> grid = null;
        if (createNew) {
            grid = new DoubleGridPartialAggregate<RelationalTuple<?>>(
                    ((DoubleGridPartialAggregate<RelationalTuple<?>>) p).getGrid());
        }
        else {
            grid = (DoubleGridPartialAggregate<RelationalTuple<?>>) p;
        }
        grid.merge((Double[][]) toMerge.getAttribute(attribPos));
        return grid;
    }

    @Override
    public RelationalTuple<?> evaluate(final IPartialAggregate<RelationalTuple<?>> p) {
        final DoubleGridPartialAggregate<RelationalTuple<?>> grid = (DoubleGridPartialAggregate<RelationalTuple<?>>) p;
        @SuppressWarnings("rawtypes")
        final RelationalTuple<?> tuple = new RelationalTuple(1);
        tuple.setAttribute(0, grid.getGrid());
        return tuple;
    }
}
