package de.uniol.inf.is.odysseus.salsa.aggregation;

import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class MergeByteGrid extends AbstractAggregateFunction<RelationalTuple<?>, RelationalTuple<?>> {

    private final int attribPos;

    public MergeByteGrid(int[] pos) {
        super("MergeGrid");
        this.attribPos = pos[0];
    }

    @Override
    public IPartialAggregate<RelationalTuple<?>> init(final RelationalTuple<?> in) {
        final IPartialAggregate<RelationalTuple<?>> grid = new ByteGridPartialAggregate<RelationalTuple<?>>(
                (Byte[][]) in.getAttribute(attribPos));
        return grid;
    }

    @Override
    public IPartialAggregate<RelationalTuple<?>> merge(
            final IPartialAggregate<RelationalTuple<?>> p, final RelationalTuple<?> toMerge,
            final boolean createNew) {
        ByteGridPartialAggregate<RelationalTuple<?>> grid = null;
        if (createNew) {
            grid = new ByteGridPartialAggregate<RelationalTuple<?>>(
                    ((ByteGridPartialAggregate<RelationalTuple<?>>) p).getGrid());
        }
        else {
            grid = (ByteGridPartialAggregate<RelationalTuple<?>>) p;
        }
        grid.merge((Byte[][]) toMerge.getAttribute(attribPos));
        return grid;
    }

    @Override
    public RelationalTuple<?> evaluate(final IPartialAggregate<RelationalTuple<?>> p) {
        final ByteGridPartialAggregate<RelationalTuple<?>> grid = (ByteGridPartialAggregate<RelationalTuple<?>>) p;
        @SuppressWarnings("rawtypes")
        final RelationalTuple<?> tuple = new RelationalTuple(1);
        tuple.setAttribute(0, grid.getGrid());
        return tuple;
    }
}
