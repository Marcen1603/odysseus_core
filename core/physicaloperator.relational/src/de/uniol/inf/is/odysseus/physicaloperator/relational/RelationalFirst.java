package de.uniol.inf.is.odysseus.physicaloperator.relational;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.ElementPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.First;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class RelationalFirst extends First<Tuple<?>, Tuple<?>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9099860331313991458L;
	private static Map<Integer, RelationalFirst> instances = new HashMap<Integer, RelationalFirst>();
	private int pos;

	private RelationalFirst(int pos) {
		super();
		this.pos = pos;
	}

	public static RelationalFirst getInstance(int pos) {
		RelationalFirst ret = instances.get(pos);
		if (ret == null) {
			ret = new RelationalFirst(pos);
		}
		return ret;
	}

	@Override
	public Tuple<?> evaluate(IPartialAggregate<Tuple<?>> p) {
		ElementPartialAggregate<Tuple<?>> pa = (ElementPartialAggregate<Tuple<?>>) p;

		@SuppressWarnings("rawtypes")
		Tuple r = new Tuple(1, false);
		r.setAttribute(0, pa.getElem().getAttribute(pos));
		return r;
	}

}
