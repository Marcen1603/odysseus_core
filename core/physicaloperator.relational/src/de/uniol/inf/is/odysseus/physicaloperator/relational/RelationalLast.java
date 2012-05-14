package de.uniol.inf.is.odysseus.physicaloperator.relational;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.ElementPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.Last;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class RelationalLast extends Last<Tuple<?>, Tuple<?>> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7968553167073220101L;
	private static Map<Integer,RelationalLast> instances = new HashMap<Integer, RelationalLast>();
	private int pos;

    private RelationalLast(int pos) {
        super();
        this.pos = pos;
    }

    public static RelationalLast getInstance(int pos) {
    	RelationalLast ret = instances.get(pos);
        if (ret == null) {
            ret = new RelationalLast(pos);
        }
        return ret;
    }

    @Override
    public Tuple<?> evaluate(IPartialAggregate<Tuple<?>> p) {
        ElementPartialAggregate<Tuple<?>> pa = (ElementPartialAggregate<Tuple<?>>) p;
        
		@SuppressWarnings("rawtypes")
		Tuple r = new Tuple(1);
		r.setAttribute(0, pa.getElem().getAttribute(pos));        
        return r;
    }

}
