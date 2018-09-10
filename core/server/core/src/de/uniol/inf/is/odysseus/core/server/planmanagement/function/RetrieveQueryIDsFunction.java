package de.uniol.inf.is.odysseus.core.server.planmanagement.function;

import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.ExecutorBinder;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

public class RetrieveQueryIDsFunction extends AbstractQueryIDsFunction {

	private static final long serialVersionUID = -7472947357374882538L;

	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { { SDFDatatype.STRING } };

	private enum QInformation {
		QUERY_PRIORTIY, QUERY_BASE_PRIORITY, QUERY_STATE, QUERY_AC_QUERY, QUERY_NAME, QUERY_START_TS, QUERY_LAST_STATE_CHANGE_TS, QUERY_SHEDDING_FACTOR
	}

	List<QInformation> toRead;

	public RetrieveQueryIDsFunction() {
		super("retrieveQueryIDs", 1, ACC_TYPES);
	}

	@Override
	public List<Integer> getValue() {

		IServerExecutor exec = ExecutorBinder.getExecutor();
		IExecutionPlan plan = exec.getExecutionPlan(getSessions().get(0));
		Collection<IPhysicalQuery> queries = plan.getQueries(getSessions().get(0));
		String predicateString = (String) getInputValue(0);
		return getValue(queries, predicateString);
	}



}
