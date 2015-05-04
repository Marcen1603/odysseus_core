package de.uniol.inf.is.odysseus.core.server.planmanagement.function;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.core.predicate.TuplePredicate;
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

	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {
			{ SDFDatatype.STRING }, { SDFDatatype.STRING },
			{ SDFDatatype.BOOLEAN } };

	private TuplePredicate predicate;
	
	public RetrieveQueryIDsFunction() {
		super("retrieveQueryIDs", 3, ACC_TYPES, SDFDatatype.LIST_INTEGER, false);
	}


	@Override
	public List<Integer> getValue() {
		// All sessions bound to this function
		List<Integer> ret = new ArrayList<Integer>();
		List<ISession> sessions = getSessions();
		if (sessions != null) {
			if (predicate == null) {
				String predicateString = (String) getInputValue(0);
				String getAttribute = (String) getInputValue(1);
				boolean asc = (Boolean) getInputValue(2);
				predicate = new TuplePredicate(new SDFExpression(predicateString, MEP.getInstance()));
				System.out.println(predicate.getAttributes());
				
			}
			IServerExecutor exec = ExecutorBinder.getExecutor();
			IExecutionPlan plan = exec.getExecutionPlan();
			Collection<IPhysicalQuery> queries = plan.getQueries();
			for (IPhysicalQuery q : queries) {
				if (true) {
					ret.add(q.getID());
				}
			}

		}
		return ret;
	}

}
