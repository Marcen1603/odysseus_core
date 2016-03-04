package de.uniol.inf.is.odysseus.core.server.planmanagement.function;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.mep.MEP;

abstract public class AbstractQueryIDsFunction extends AbstractFunction<List<Integer>> {

	private static final long serialVersionUID = -7472947357374882538L;

	private enum QInformation {
		QUERY_PRIORTIY, QUERY_BASE_PRIORITY, QUERY_STATE, QUERY_AC_QUERY, QUERY_NAME, QUERY_START_TS, QUERY_LAST_STATE_CHANGE_TS, QUERY_SHEDDING_FACTOR
	}

	List<QInformation> toRead;

	private SDFExpression predicate;

	public AbstractQueryIDsFunction(String symbol, int arity,
			SDFDatatype[][] acceptedTypes) {
		super(symbol, arity, acceptedTypes, SDFDatatype.LIST, false);
	}

	protected List<Integer> getValue(Collection<IPhysicalQuery> queries, String predicateString) {
		List<Integer> ret = new ArrayList<Integer>();
		// All sessions bound to this function
		List<ISession> sessions = getSessions();
		if (sessions != null) {
			if (predicate == null) {
				
				predicate = new SDFExpression(
						predicateString, null, MEP.getInstance());
				toRead = new ArrayList<>(predicate.getAllAttributes().size());
				for (SDFAttribute attribute : predicate.getAllAttributes()) {
					toRead.add(getQInformation(attribute));
				}
			}

			for (IPhysicalQuery q : queries) {
				Tuple<IMetaAttribute> queryValues = getQueryValues(toRead, q);
				predicate.bindVariables(queryValues.getAttributes());
				if ((boolean)predicate.getValue()) {
					ret.add(q.getID());
				}
			}

		}
		return ret;
	}

	private Tuple<IMetaAttribute> getQueryValues(List<QInformation> toRead,
			IPhysicalQuery q) {
		Tuple<IMetaAttribute> t = new Tuple<IMetaAttribute>(toRead.size(),
				false);
		for (int i = 0; i < toRead.size(); i++) {
			t.setAttribute(i, getQueryValue(toRead.get(i), q));
		}

		return t;
	}

	private Object getQueryValue(QInformation qInformation, IPhysicalQuery q) {
		switch (qInformation) {
		case QUERY_PRIORTIY:
			return q.getPriority();
		case QUERY_BASE_PRIORITY:
			return q.getBasePriority();
		case QUERY_STATE:
			return q.getState().toString();
		case QUERY_AC_QUERY:
			return q.isACquery();
		case QUERY_NAME:
			return q.getName();
		case QUERY_LAST_STATE_CHANGE_TS:
			return q.getLastQueryStateChangeTS();
		case QUERY_START_TS:
			return q.getQueryStartTS();
		case QUERY_SHEDDING_FACTOR:
			return q.getSheddingFactor();
		default:
			return null;
		}
	}

	private QInformation getQInformation(SDFAttribute attribute) {
		if (attribute.getAttributeName().equalsIgnoreCase("priority")) {
			return QInformation.QUERY_PRIORTIY;
		}
		if (attribute.getAttributeName().equalsIgnoreCase("basepriority")) {
			return QInformation.QUERY_BASE_PRIORITY;
		}
		if (attribute.getAttributeName().equalsIgnoreCase("state")) {
			return QInformation.QUERY_STATE;
		}
		if (attribute.getAttributeName().equalsIgnoreCase("acquery")) {
			return QInformation.QUERY_AC_QUERY;
		}
		if (attribute.getAttributeName().equalsIgnoreCase("name")) {
			return QInformation.QUERY_NAME;
		}
		if (attribute.getAttributeName().equalsIgnoreCase("startTS")) {
			return QInformation.QUERY_START_TS;
		}		
		if (attribute.getAttributeName().equalsIgnoreCase("lastStateChangeTS")) {
			return QInformation.QUERY_LAST_STATE_CHANGE_TS;
		}		
		if (attribute.getAttributeName().equalsIgnoreCase("sheddingFactor")) {
			return QInformation.QUERY_SHEDDING_FACTOR;
		}		
		throw new IllegalArgumentException("Information "+attribute+" not defined");
	}

}
