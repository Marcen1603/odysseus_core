package de.uniol.inf.is.odysseus.salsa.aggregation;

import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.functions.ElementPartialAggregate;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
abstract public class Last<R, W> extends AbstractAggregateFunction<R, W> {
    protected Last() {
        super("LAST");
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
        pa.setElem(toMerge);
        return pa;
    }
}
