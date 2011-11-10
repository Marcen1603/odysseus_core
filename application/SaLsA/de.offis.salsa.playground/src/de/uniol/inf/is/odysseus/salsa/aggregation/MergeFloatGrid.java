package de.uniol.inf.is.odysseus.salsa.aggregation;

import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class MergeFloatGrid extends
        AbstractAggregateFunction<RelationalTuple<?>, RelationalTuple<?>> {

    private final int attribPos;

    public MergeFloatGrid(int[] pos) {
        super("MergeFloatGrid");
        this.attribPos = pos[0];
    }

    @Override
    public IPartialAggregate<RelationalTuple<?>> init(final RelationalTuple<?> in) {
        final IPartialAggregate<RelationalTuple<?>> grid = new FloatGridPartialAggregate<RelationalTuple<?>>(
                (Float[][]) in.getAttribute(attribPos));
        return grid;
    }

    @Override
    public IPartialAggregate<RelationalTuple<?>> merge(
            final IPartialAggregate<RelationalTuple<?>> p, final RelationalTuple<?> toMerge,
            final boolean createNew) {
        FloatGridPartialAggregate<RelationalTuple<?>> grid = null;
        if (createNew) {
            grid = new FloatGridPartialAggregate<RelationalTuple<?>>(
                    ((FloatGridPartialAggregate<RelationalTuple<?>>) p).getGrid());
        }
        else {
            grid = (FloatGridPartialAggregate<RelationalTuple<?>>) p;
        }
        grid.merge((Float[][]) toMerge.getAttribute(attribPos));
        return grid;
    }

    @Override
    public RelationalTuple<?> evaluate(final IPartialAggregate<RelationalTuple<?>> p) {
        final FloatGridPartialAggregate<RelationalTuple<?>> grid = (FloatGridPartialAggregate<RelationalTuple<?>>) p;
        @SuppressWarnings("rawtypes")
        final RelationalTuple<?> tuple = new RelationalTuple(1);
        tuple.setAttribute(0, grid.getGrid());
        return tuple;
    }
}
