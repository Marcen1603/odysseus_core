package de.uniol.inf.is.odysseus.core.server.logicaloperator.builder;

import java.util.List;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.event.error.ParameterException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

public class PhysicalOperatorParameter extends
		AbstractParameter<IPhysicalOperator> {

	private static final long serialVersionUID = 5085997007957491203L;

	@Override
	protected void internalAssignment() {
		final String queryName;
		final String operatorName;
		if (inputValue instanceof List) {
			@SuppressWarnings("unchecked")
			List<String> params = (List<String>) inputValue;
			queryName = params.get(0);
			operatorName = params.size() > 1 ? params.get(1) : null;
		} else if (inputValue instanceof String) {
			queryName = (String) inputValue;
			operatorName = null;
		} else {
			throw new ParameterException("Unable to read values!");
		}
		setValue(findOperator(queryName, operatorName));
	}

	private IPhysicalOperator findOperator(String queryName, String opName) {
		IServerExecutor executor = getServerExecutor();

		IPhysicalQuery query = executor.getExecutionPlan().getQueryByName(
				queryName);
		if (query == null) {
			throw new ParameterException("Query with name " + queryName
					+ " not found for operator ");
		}

		IPhysicalOperator op = null;
		if (opName != null) {
			op = query.getOperator(opName);
		} else {
			List<IPhysicalOperator> roots = query.getRoots();
			if (roots.size() > 1) {
				throw new ParameterException("Query " + queryName
						+ " contains multiple sinks! Must use name option");
			}
			op = roots.get(0);
		}
		return op;
	}

	@Override
	protected String getPQLStringInternal() {
		// TODO Auto-generated method stub
		return null;
	}

}
