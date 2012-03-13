package de.uniol.inf.is.odysseus.salsa.aggregation;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.AbstractListAggregation;


/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
abstract class AbstractGeometryAggregation<R, W> extends AbstractListAggregation<R, W> {

    /**
     * 
     */
    private static final long serialVersionUID = 7950868202294034595L;

    protected AbstractGeometryAggregation(String name) {
        super(name);
    }

    @Override
    public IPartialAggregate<R> init(R in) {
        GeometryPartialAggregate<R> aggregate = new GeometryPartialAggregate<R>();
        aggregate.addElem(in);
        return aggregate;
    }

    @Override
    public IPartialAggregate<R> merge(IPartialAggregate<R> p, R toMerge, boolean createNew) {
        GeometryPartialAggregate<R> list = (GeometryPartialAggregate<R>) p;
        if (createNew) {
            list = new GeometryPartialAggregate<R>((GeometryPartialAggregate<R>) p);
        }
        list.addElem(toMerge);
        return list;
    }

}
