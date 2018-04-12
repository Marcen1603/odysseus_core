package de.uniol.inf.is.odysseus.dsp;

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

public abstract class Const<R, W> extends AbstractAggregateFunction<R, W> {

	private static final long serialVersionUID = 7144950381259595844L;

	protected Const(boolean partialAggregateInput) {
		super("CONST", partialAggregateInput);
	}

	@Override
	protected IPartialAggregate<R> init(R in) {
		return new ConstPartialAggregate<>();
	}

	@Override
	protected IPartialAggregate<R> merge(IPartialAggregate<R> p, R toMerge, boolean createNew) {
		return new ConstPartialAggregate<>();
	}

	@Override
	public SDFDatatype getReturnType(List<SDFAttribute> inputTypes) {
		return SDFDatatype.INTEGER;
	}	
}
