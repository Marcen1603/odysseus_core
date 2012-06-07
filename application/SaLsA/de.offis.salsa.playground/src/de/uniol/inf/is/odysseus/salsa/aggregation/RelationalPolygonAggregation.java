package de.uniol.inf.is.odysseus.salsa.aggregation;

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class RelationalPolygonAggregation
        extends
        AbstractGeometryAggregation<Tuple<? extends IMetaAttribute>, Tuple<? extends IMetaAttribute>> {

    /**
     * 
     */
    private static final long serialVersionUID = -6131919686476873342L;
    private int[] restrictList;

    public RelationalPolygonAggregation(int[] restrictList) {
        super("PMERGE");
        this.restrictList = restrictList;
    }

    @Override
    public Tuple<?> evaluate(IPartialAggregate<Tuple<? extends IMetaAttribute>> p) {
        ((GeometryPartialAggregate<Tuple<? extends IMetaAttribute>>) p).compress();
        List<Tuple<?>> elems = ((GeometryPartialAggregate<Tuple<? extends IMetaAttribute>>) p)
                .getElems();
        Tuple<?> ret = new Tuple<IMetaAttribute>(1, false);
        ret.setAttribute(0, elems);
        return ret;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public IPartialAggregate<Tuple<? extends IMetaAttribute>> merge(
            IPartialAggregate<Tuple<? extends IMetaAttribute>> p,
            Tuple<? extends IMetaAttribute> toMerge, boolean createNew) {
        return ((GeometryPartialAggregate) p).addElem(toMerge.restrict(restrictList, true));
    }
}
