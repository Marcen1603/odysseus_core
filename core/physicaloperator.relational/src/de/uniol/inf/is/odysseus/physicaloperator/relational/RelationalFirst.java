package de.uniol.inf.is.odysseus.physicaloperator.relational;

import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.functions.ElementPartialAggregate;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.functions.First;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class RelationalFirst extends First<RelationalTuple<?>, RelationalTuple<?>> {

    private static RelationalFirst instance;

    private RelationalFirst() {
        super();
    }

    public static RelationalFirst getInstance() {
        if (instance == null) {
            instance = new RelationalFirst();
        }
        return instance;
    }

    @Override
    public RelationalTuple<?> evaluate(IPartialAggregate<RelationalTuple<?>> p) {
        ElementPartialAggregate<RelationalTuple<?>> pa = (ElementPartialAggregate<RelationalTuple<?>>) p;
        return pa.getElem();
    }

}
