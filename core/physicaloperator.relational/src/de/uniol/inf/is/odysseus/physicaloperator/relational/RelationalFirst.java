package de.uniol.inf.is.odysseus.physicaloperator.relational;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.ElementPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.First;
import de.uniol.inf.is.odysseus.relational.base.Tuple;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class RelationalFirst extends First<Tuple<?>, Tuple<?>> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 9099860331313991458L;
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
    public Tuple<?> evaluate(IPartialAggregate<Tuple<?>> p) {
        ElementPartialAggregate<Tuple<?>> pa = (ElementPartialAggregate<Tuple<?>>) p;
        return pa.getElem();
    }

}
