package de.uniol.inf.is.odysseus.physicaloperator.relational;

import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.functions.ElementPartialAggregate;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.functions.Last;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class RelationalLast extends Last<RelationalTuple<?>, RelationalTuple<?>> {

    private static RelationalLast instance;

    private RelationalLast() {
        super();
    }

    public static RelationalLast getInstance() {
        if (instance == null) {
            instance = new RelationalLast();
        }
        return instance;
    }

    @Override
    public RelationalTuple<?> evaluate(IPartialAggregate<RelationalTuple<?>> p) {
        ElementPartialAggregate<RelationalTuple<?>> pa = (ElementPartialAggregate<RelationalTuple<?>>) p;
        return pa.getElem();
    }

}
