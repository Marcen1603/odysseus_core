package de.uniol.inf.is.odysseus.physicaloperator.relational;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.Nth;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.NthPartialAggregate;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class RelationalNth extends Nth<Tuple<?>, Tuple<?>> {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8236978756283300379L;
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
    public Tuple<?> evaluate(IPartialAggregate<Tuple<?>> p) {
        NthPartialAggregate<Tuple<?>> pa = (NthPartialAggregate<Tuple<?>>) p;
        if (pa.getN() == this.getN()) {
            return pa.getElem();
        }
        return null;
    }
}
