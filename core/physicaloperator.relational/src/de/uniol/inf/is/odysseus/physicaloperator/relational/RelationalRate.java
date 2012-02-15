package de.uniol.inf.is.odysseus.physicaloperator.relational;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.functions.Rate;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.functions.RatePartialAggregate;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class RelationalRate extends
		Rate<RelationalTuple<?>, RelationalTuple<?>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2723042267161464535L;
	private static RelationalRate instance;

	private RelationalRate() {
		super();
	}

	public static RelationalRate getInstance() {
		if (instance == null) {
			instance = new RelationalRate();
		}
		return instance;
	}

	@Override
	public RelationalTuple<?> evaluate(IPartialAggregate<RelationalTuple<?>> p) {

		RatePartialAggregate<RelationalTuple<?>> pa = (RatePartialAggregate<RelationalTuple<?>>) p;
		RelationalTuple<IMetaAttribute> ret = new RelationalTuple<IMetaAttribute>(
				1);

		ret.setAttribute(0,
				((double) pa.getCount())
						/ pa.getEnd().minus(pa.getStart()).getMainPoint());
		return ret;
	}

}
