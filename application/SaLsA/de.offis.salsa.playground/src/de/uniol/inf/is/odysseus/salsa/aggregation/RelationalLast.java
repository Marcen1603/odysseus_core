package de.uniol.inf.is.odysseus.salsa.aggregation;

import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.functions.ElementPartialAggregate;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class RelationalLast extends Last<RelationalTuple<?>, RelationalTuple<?>> {

    @Override
    public RelationalTuple<?> evaluate(IPartialAggregate<RelationalTuple<?>> p) {
        ElementPartialAggregate<RelationalTuple<?>> pa = (ElementPartialAggregate<RelationalTuple<?>>) p;
        return pa.getElem();
    }

}
