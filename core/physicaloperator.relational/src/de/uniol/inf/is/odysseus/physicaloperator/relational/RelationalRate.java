package de.uniol.inf.is.odysseus.physicaloperator.relational;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.Rate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.RatePartialAggregate;
import de.uniol.inf.is.odysseus.relational.base.Tuple;

public class RelationalRate extends
		Rate<Tuple<?>, Tuple<?>> {

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
	public Tuple<?> evaluate(IPartialAggregate<Tuple<?>> p) {

		RatePartialAggregate<Tuple<?>> pa = (RatePartialAggregate<Tuple<?>>) p;
		Tuple<IMetaAttribute> ret = new Tuple<IMetaAttribute>(
				1);

		ret.setAttribute(0,
				((double) pa.getCount())
						/ pa.getEnd().minus(pa.getStart()).getMainPoint());
		return ret;
	}

}
