package de.uniol.inf.is.odysseus.probabilistic.common;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.predicate.AndPredicate;
import de.uniol.inf.is.odysseus.core.server.predicate.ComplexPredicateHelper;
import de.uniol.inf.is.odysseus.core.server.predicate.NotPredicate;
import de.uniol.inf.is.odysseus.core.server.predicate.OrPredicate;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;

//import de.uniol.inf.is.odysseus.core.server.predicate.AndPredicate;
/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public final class PredicateUtils {

	public static boolean isAndPredicate(IPredicate<?> predicate) {
		if (predicate instanceof AndPredicate) {
			return ComplexPredicateHelper.isAndPredicate(predicate);
		} else if (predicate instanceof RelationalPredicate) {
			return ((RelationalPredicate) predicate).isAndPredicate();
		}
		return false;
	}

	public static boolean isOrPredicate(IPredicate<?> predicate) {
		if (predicate instanceof OrPredicate) {
			return ComplexPredicateHelper.isOrPredicate(predicate);
		} else if (predicate instanceof RelationalPredicate) {
			return ((RelationalPredicate) predicate).isOrPredicate();
		}
		return false;
	}

	public static boolean isNotPredicate(IPredicate<?> predicate) {
		if (predicate instanceof NotPredicate) {
			return ComplexPredicateHelper.isNotPredicate(predicate);
		} else if (predicate instanceof RelationalPredicate) {
			return ((RelationalPredicate) predicate).isNotPredicate();
		}
		return false;
	}

	@SuppressWarnings("rawtypes")
	public static List<IPredicate> splitPredicate(IPredicate<?> predicate) {
		if (isAndPredicate(predicate)) {
			if (predicate instanceof AndPredicate) {
				return ComplexPredicateHelper.splitPredicate(predicate);
			} else if (predicate instanceof RelationalPredicate) {
				return ((RelationalPredicate) predicate).splitPredicate();
			}
		}
		ArrayList<IPredicate> predicates = new ArrayList<IPredicate>();
		predicates.add(predicate);
		return predicates;
	}

	@SuppressWarnings("rawtypes")
	public static Set<SDFAttribute> getAttributes(IPredicate<?> predicate) {
		Set<SDFAttribute> attributes = new HashSet<SDFAttribute>();
		List<IPredicate> predicates = splitPredicate(predicate);
		for (IPredicate pre : predicates) {
			if (pre instanceof RelationalPredicate) {
				attributes.addAll(((RelationalPredicate) pre).getAttributes());
			} else {
				if (pre instanceof OrPredicate) {
					attributes.addAll(getAttributes(((OrPredicate) pre).getLeft()));
					attributes.addAll(getAttributes(((OrPredicate) pre).getRight()));
				} else if (pre instanceof NotPredicate) {
					attributes.addAll(getAttributes(((NotPredicate) pre).getChild()));
				}
			}
		}
		return attributes;
	}

	private PredicateUtils() {
	}
}
