package de.uniol.inf.is.odysseus.salsa.aggregation;

import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.salsa.model.Grid;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class MergePlausabilityGrid extends AbstractAggregateFunction<RelationalTuple<?>, RelationalTuple<?>> {

    private final int attribPos;

    public MergePlausabilityGrid(int[] pos) {
        super("MergePlausabilityGrid");
        this.attribPos = pos[0];
    }

    @Override
    public IPartialAggregate<RelationalTuple<?>> init(final RelationalTuple<?> in) {
        final IPartialAggregate<RelationalTuple<?>> grid = new GridPartialPlausabilityAggregate<RelationalTuple<?>>(
                (Grid) in.getAttribute(attribPos));
        return grid;
    }

    @Override
    public IPartialAggregate<RelationalTuple<?>> merge(
            final IPartialAggregate<RelationalTuple<?>> p, final RelationalTuple<?> toMerge,
            final boolean createNew) {
        GridPartialPlausabilityAggregate<RelationalTuple<?>> grid = null;
        if (createNew) {
            grid = new GridPartialPlausabilityAggregate<RelationalTuple<?>>(
                    ((GridPartialPlausabilityAggregate<RelationalTuple<?>>) p).getGrid());
        }
        else {
            grid = (GridPartialPlausabilityAggregate<RelationalTuple<?>>) p;
        }
        grid.merge((Grid) toMerge.getAttribute(attribPos));
        return grid;
    }

    @Override
    public RelationalTuple<?> evaluate(final IPartialAggregate<RelationalTuple<?>> p) {
        final GridPartialPlausabilityAggregate<RelationalTuple<?>> grid = (GridPartialPlausabilityAggregate<RelationalTuple<?>>) p;
        grid.evaluate();
        @SuppressWarnings("rawtypes")
        final RelationalTuple<?> tuple = new RelationalTuple(1);
        tuple.setAttribute(0, grid.getGrid());
        return tuple;
    }
}
