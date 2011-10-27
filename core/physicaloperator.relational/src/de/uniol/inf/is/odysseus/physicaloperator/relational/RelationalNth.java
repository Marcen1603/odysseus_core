package de.uniol.inf.is.odysseus.physicaloperator.relational;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.functions.Nth;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.functions.NthPartialAggregate;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class RelationalNth extends Nth<RelationalTuple<?>, RelationalTuple<?>> {

    private final static Map<Integer, RelationalNth> instances = new HashMap<Integer, RelationalNth>();

    static public RelationalNth getInstance(Integer n) {
        RelationalNth instance = instances.get(n);
        if (instance == null) {
            instance = new RelationalNth(n);
            instances.put(n, instance);
        }
        return instance;
    }

    private RelationalNth(int n) {
        super(n);
    }

    @Override
    public RelationalTuple<?> evaluate(IPartialAggregate<RelationalTuple<?>> p) {
        NthPartialAggregate<RelationalTuple<?>> pa = (NthPartialAggregate<RelationalTuple<?>>) p;
        if (pa.getN() == this.getN()) {
            return pa.getElem();
        }
        return null;
    }
}
