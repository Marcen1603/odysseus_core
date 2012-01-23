package de.uniol.inf.is.odysseus.salsa.aggregation;

import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.salsa.model.Grid;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class MergeGrid extends AbstractAggregateFunction<RelationalTuple<?>, RelationalTuple<?>> {

    private final int attribPos;

    public MergeGrid(int[] pos) {
        super("MergeGrid");
        this.attribPos = pos[0];
    }

    @Override
    public IPartialAggregate<RelationalTuple<?>> init(final RelationalTuple<?> in) {
        final IPartialAggregate<RelationalTuple<?>> grid = new GridPartialAggregate<RelationalTuple<?>>(
                (Grid) in.getAttribute(attribPos));
        return grid;
    }

    @Override
    public IPartialAggregate<RelationalTuple<?>> merge(
            final IPartialAggregate<RelationalTuple<?>> p, final RelationalTuple<?> toMerge,
            final boolean createNew) {
        GridPartialAggregate<RelationalTuple<?>> grid = null;
        if (createNew) {
            grid = new GridPartialAggregate<RelationalTuple<?>>(
                    ((GridPartialAggregate<RelationalTuple<?>>) p).getGrid());
        }
        else {
            grid = (GridPartialAggregate<RelationalTuple<?>>) p;
        }
        grid.merge((Grid) toMerge.getAttribute(attribPos));
        return grid;
    }

    @Override
    public RelationalTuple<?> evaluate(final IPartialAggregate<RelationalTuple<?>> p) {
        final GridPartialAggregate<RelationalTuple<?>> grid = (GridPartialAggregate<RelationalTuple<?>>) p;
        grid.evaluate();
        @SuppressWarnings("rawtypes")
        final RelationalTuple<?> tuple = new RelationalTuple(1);
        tuple.setAttribute(0, grid.getGrid());
        return tuple;
    }
}
