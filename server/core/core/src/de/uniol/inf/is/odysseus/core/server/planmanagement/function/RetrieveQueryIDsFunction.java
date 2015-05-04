package de.uniol.inf.is.odysseus.core.server.planmanagement.function;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.predicate.TuplePredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.ExecutorBinder;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.mep.MEP;

public class RetrieveQueryIDsFunction extends AbstractFunction<List<Integer>> {

	private static final long serialVersionUID = -7472947357374882538L;

	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { { SDFDatatype.STRING } };

	private enum QInformation {
		QUERY_PRIORTIY, QUERY_STATE
	}

	List<QInformation> toRead;

	private SDFExpression predicate;

	public RetrieveQueryIDsFunction() {
		super("retrieveQueryIDs", 1, ACC_TYPES, SDFDatatype.LIST_INTEGER, false);
	}

	@Override
	public List<Integer> getValue() {
		// All sessions bound to this function
		List<Integer> ret = new ArrayList<Integer>();
		List<ISession> sessions = getSessions();
		if (sessions != null) {
			if (predicate == null) {
				String predicateString = (String) getInputValue(0);
				predicate = new SDFExpression(
						predicateString, MEP.getInstance());
				toRead = new ArrayList<>(predicate.getAllAttributes().size());
				for (SDFAttribute attribute : predicate.getAllAttributes()) {
					toRead.add(getQInformation(attribute));
				}
			}

			IServerExecutor exec = ExecutorBinder.getExecutor();
			IExecutionPlan plan = exec.getExecutionPlan();
			Collection<IPhysicalQuery> queries = plan.getQueries();
			for (IPhysicalQuery q : queries) {
				Tuple<IMetaAttribute> queryValues = getQueryValues(toRead, q);
				predicate.bindVariables(queryValues.getAttributes());
				if (predicate.getValue()) {
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
		case QUERY_STATE:
			return q.getState();
		}

		return null;
	}

	private QInformation getQInformation(SDFAttribute attribute) {
		if (attribute.getAttributeName().equalsIgnoreCase("priority")) {
			return QInformation.QUERY_PRIORTIY;
		}
		if (attribute.getAttributeName().equalsIgnoreCase("state")) {
			return QInformation.QUERY_STATE;
		}
		throw new IllegalArgumentException("Information "+attribute+" not defined");
	}

}
