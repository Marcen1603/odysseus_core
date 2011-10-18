package de.uniol.inf.is.odysseus.salsa.aggregation;

import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class MergeBooleanGrid extends AbstractAggregateFunction<RelationalTuple<?>, RelationalTuple<?>> {

    private final int attribPos;

    public MergeBooleanGrid(int[] pos) {
        super("MergeBooleanGrid");
        this.attribPos = pos[0];
    }

    @Override
    public IPartialAggregate<RelationalTuple<?>> init(final RelationalTuple<?> in) {
        final IPartialAggregate<RelationalTuple<?>> grid = new BooleanGridPartialAggregate<RelationalTuple<?>>(
                (Boolean[][]) in.getAttribute(attribPos));
        return grid;
    }

    @Override
    public IPartialAggregate<RelationalTuple<?>> merge(
            final IPartialAggregate<RelationalTuple<?>> p, final RelationalTuple<?> toMerge,
            final boolean createNew) {
        BooleanGridPartialAggregate<RelationalTuple<?>> grid = null;
        if (createNew) {
            grid = new BooleanGridPartialAggregate<RelationalTuple<?>>(
                    ((BooleanGridPartialAggregate<RelationalTuple<?>>) p).getGrid());
        }
        else {
            grid = (BooleanGridPartialAggregate<RelationalTuple<?>>) p;
        }
        grid.merge((Boolean[][]) toMerge.getAttribute(attribPos));
        return grid;
    }

    @Override
    public RelationalTuple<?> evaluate(final IPartialAggregate<RelationalTuple<?>> p) {
        final BooleanGridPartialAggregate<RelationalTuple<?>> grid = (BooleanGridPartialAggregate<RelationalTuple<?>>) p;
        @SuppressWarnings("rawtypes")
        final RelationalTuple<?> tuple = new RelationalTuple(1);
        tuple.setAttribute(0, grid.getGrid());
        return tuple;
    }
}
