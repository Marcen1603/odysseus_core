package de.uniol.inf.is.odysseus.core.server.planmanagement.function;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.ExecutorBinder;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

public class FilterQueryIDsFunction extends AbstractQueryIDsFunction {

	private static final long serialVersionUID = -7472947357374882538L;

	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { {SDFDatatype.LIST}, { SDFDatatype.STRING } };

	private enum QInformation {
		QUERY_PRIORTIY, QUERY_BASE_PRIORITY, QUERY_STATE, QUERY_AC_QUERY, QUERY_NAME, QUERY_START_TS, QUERY_LAST_STATE_CHANGE_TS, QUERY_SHEDDING_FACTOR
	}

	List<QInformation> toRead;

	public FilterQueryIDsFunction() {
		super("filterQueryIDs", 2, ACC_TYPES);
	}

	@Override
	public List<Integer> getValue() {
		IServerExecutor exec = ExecutorBinder.getExecutor();
		IExecutionPlan plan = exec.getExecutionPlan(getSessions().get(0));
		List<Integer> queryIDs = getInputValue(0);
		Collection<IPhysicalQuery> queries = new ArrayList<IPhysicalQuery>();
		for (Integer i:queryIDs){
			queries.add(plan.getQueryById(i, getSessions().get(0)));
		}
		String predicateString = (String) getInputValue(1);
		return getValue(queries, predicateString);
	}



}
