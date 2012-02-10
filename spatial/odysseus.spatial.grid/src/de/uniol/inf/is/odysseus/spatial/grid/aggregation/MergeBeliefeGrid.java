package de.uniol.inf.is.odysseus.spatial.grid.aggregation;

import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.spatial.grid.model.Grid;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class MergeBeliefeGrid extends
		AbstractAggregateFunction<RelationalTuple<?>, RelationalTuple<?>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 443315119696013306L;
	private final int attribPos;

	public MergeBeliefeGrid(int[] pos) {
		super("MergeBeliefeGrid");
		this.attribPos = pos[0];
	}

	@Override
	public IPartialAggregate<RelationalTuple<?>> init(
			final RelationalTuple<?> in) {
		final IPartialAggregate<RelationalTuple<?>> grid = new GridPartialBeliefeAggregate<RelationalTuple<?>>(
				(Grid) in.getAttribute(attribPos));
		return grid;
	}

	@Override
	public IPartialAggregate<RelationalTuple<?>> merge(
			final IPartialAggregate<RelationalTuple<?>> p,
			final RelationalTuple<?> toMerge, final boolean createNew) {
		GridPartialBeliefeAggregate<RelationalTuple<?>> grid = null;
		if (createNew) {
			grid = new GridPartialBeliefeAggregate<RelationalTuple<?>>(
					((GridPartialBeliefeAggregate<RelationalTuple<?>>) p)
							.getGrid());
		} else {
			grid = (GridPartialBeliefeAggregate<RelationalTuple<?>>) p;
		}
		grid.merge((Grid) toMerge.getAttribute(attribPos));
		return grid;
	}

	@Override
	public RelationalTuple<?> evaluate(
			final IPartialAggregate<RelationalTuple<?>> p) {
		final GridPartialBeliefeAggregate<RelationalTuple<?>> grid = (GridPartialBeliefeAggregate<RelationalTuple<?>>) p;
		grid.evaluate();
		@SuppressWarnings("rawtypes")
		final RelationalTuple<?> tuple = new RelationalTuple(1);
		tuple.setAttribute(0, grid.getGrid());
		return tuple;
	}
}
