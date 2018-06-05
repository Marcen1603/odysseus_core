package de.uniol.inf.is.odysseus.core.server.logicaloperator.builder;

import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.event.error.ParameterException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

public class PhysicalOperatorParameter extends AbstractParameter<IPhysicalOperator> {

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
		IPhysicalOperator op = findOperator(queryName, operatorName);
		if (op == null) {
			throw new ParameterException("Cannot find " + operatorName + " in query " + queryName);
		}
		setValue(op);
	}

	private IPhysicalOperator findOperator(String queryName, String opName) {
		IServerExecutor executor = getServerExecutor();

		IPhysicalQuery query = executor.getExecutionPlan(getCaller())
				.getQueryByName(Resource.specialCreateResource(queryName, getCaller().getUser()), getCaller());
		if (query == null) {
			// Try to find by number:
			try {
				int queryID = Integer.parseInt(queryName);
				query = executor.getExecutionPlan(getCaller()).getQueryById(queryID, getCaller());
			} catch (NumberFormatException e) {
				throw new ParameterException("Query with name/id " + queryName + " not found for operator ");
			}
		}

		if (query == null) {
			throw new ParameterException("Query with name/id " + queryName + " not found for operator ");
		}

		IPhysicalOperator op = null;
		if (opName != null) {
			op = query.getOperator(opName);

			// try to find operator by hash code
			if (op == null) {

				try {
					int hashCode = Integer.parseInt(opName);
					Set<IPhysicalOperator> roots = query.getAllOperators();
					for (IPhysicalOperator po : roots) {
						if (po.hashCode() == hashCode) {
							op = po;
							break;
						}
					}
				} catch (NumberFormatException e) {
					throw new ParameterException(
							"Operator with name " + opName + " not found. " + opName + " is no hash code either.");
				}
			}

		} else {
			List<IPhysicalOperator> roots = query.getRoots();
			if (roots.size() > 1) {
				throw new ParameterException("Query " + queryName + " contains multiple sinks! Must use name option");
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
