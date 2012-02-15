package de.uniol.inf.is.odysseus.physicaloperator.aggregate.functions;

import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
abstract public class Nth<R, W> extends AbstractAggregateFunction<R, W> {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3840479318065114625L;
	private int n;

    protected Nth(int n) {
        super("NTH");
        this.n = n;
    }

    @Override
    public IPartialAggregate<R> init(R in) {
        IPartialAggregate<R> pa = new NthPartialAggregate<R>(in);
        return pa;
    }

    @Override
    public IPartialAggregate<R> merge(IPartialAggregate<R> p, R toMerge, boolean createNew) {
        NthPartialAggregate<R> pa = null;
        if (createNew) {
            pa = new NthPartialAggregate<R>(((NthPartialAggregate<R>) p).getElem());
        }
        else {
            pa = (NthPartialAggregate<R>) p;
        }
        if (pa.getN() != n) {
            pa.addElem(toMerge);
        }
        return pa;
    }

    protected int getN() {
        return n;
    }
}
