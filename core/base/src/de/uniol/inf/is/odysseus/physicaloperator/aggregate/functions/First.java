package de.uniol.inf.is.odysseus.physicaloperator.aggregate.functions;

import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
abstract public class First<R, W> extends AbstractAggregateFunction<R, W> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5606626236472048799L;

	protected First() {
        super("FIRST");
    }

    @Override
    public IPartialAggregate<R> init(R in) {
        IPartialAggregate<R> pa = new ElementPartialAggregate<R>(in);
        return pa;
    }

    @Override
    public IPartialAggregate<R> merge(IPartialAggregate<R> p, R toMerge, boolean createNew) {
        ElementPartialAggregate<R> pa = null;
        if (createNew) {
            pa = new ElementPartialAggregate<R>(((ElementPartialAggregate<R>) p).getElem());
        }
        else {
            pa = (ElementPartialAggregate<R>) p;
        }
        return pa;
    }
}
