package de.uniol.inf.is.odysseus.costmodel.logical;

import java.util.Collection;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.costmodel.DetailCost;
import de.uniol.inf.is.odysseus.costmodel.logical.impl.OperatorEstimatorRegistry;

public class LogicalCostModelConsole implements CommandProvider {

	static private final ISession superUser = SessionManagement.instance.loginSuperUser(null);

	private static ILogicalCostModel logicalCostModel;
	private static IServerExecutor serverExecutor;

	// called by OSGi-DS
	public static void bindLogicalCostModel(ILogicalCostModel serv) {
		logicalCostModel = serv;
	}

	// called by OSGi-DS
	public static void unbindLogicalCostModel(ILogicalCostModel serv) {
		if (logicalCostModel == serv) {
			logicalCostModel = null;
		}
	}

	// called by OSGi-DS
	public static void bindExecutor(IExecutor serv) {
		serverExecutor = (IServerExecutor)serv;
	}

	// called by OSGi-DS
	public static void unbindExecutor(IExecutor serv) {
		if (serverExecutor == serv) {
			serverExecutor = null;
		}
	}

	@Override
	public String getHelp() {
		StringBuilder sb = new StringBuilder();

		sb.append("---- Logical cost model ----\n");
		sb.append("    estimateLogical <queryID>          - Estimates the current costs for the logical query with the given id.\n");
		sb.append("    listRegisteredLogicalEstimators/ls - Lists all logical operators which have logical estimators.\n");

		return sb.toString();
	}

	public void _estimateLogical( CommandInterpreter ci ) {
		String queryIDString = ci.nextArgument();
		if( Strings.isNullOrEmpty(queryIDString)) {
			System.out.println("usage: estimateLogical <queryid>");
			return;
		}

		int queryID = 0;
		try {
			queryID = Integer.valueOf(queryIDString);
		} catch( Throwable t ) {
			System.out.println("usage: estimateLogical <queryid>");
			return;
		}

		IPhysicalQuery physicalQuery = serverExecutor.getExecutionPlan(superUser).getQueryById(queryID, superUser);
		if( physicalQuery != null ) {
			ILogicalQuery logicalQuery = physicalQuery.getLogicalQuery();

			Collection<ILogicalOperator> logicalOperators = getAllOperators(logicalQuery);
			ILogicalCost logicalCost = logicalCostModel.estimateCost(logicalOperators);

			System.out.println("Estimated logical costs:");
			for( ILogicalOperator operator : logicalCost.getOperators() ) {
				System.out.println(operator.getClass().getSimpleName() + " [" + operator.getName() + "]: ");
				DetailCost detailCost = logicalCost.getDetailCost(operator);

				System.out.print("\tCPU = " + format(detailCost.getCpuCost()));
				System.out.print(",\tMEM = " + format(detailCost.getMemCost()));
				System.out.print(",\tNET = " + format(detailCost.getNetCost()));
				System.out.print(",\tSEL = " + format(detailCost.getSelectivity()));
				System.out.print(",\tRAT = " + format(detailCost.getDatarate()));
				System.out.print(",\tWND = " + format(detailCost.getWindowSize()));
				System.out.println();
			}

			System.out.println("Summary: ");
			System.out.print("\tCPU = " + format(logicalCost.getCpuSum()));
			System.out.print(",\tMEM = " + format(logicalCost.getMemorySum()));
			System.out.print(",\tNET = " + format(logicalCost.getNetworkSum()));
		} else {
			System.out.println("Logical query with id = " + queryID + " not found");
		}
	}

	private static String format(Object text ) {
		return String.format("%-6.4f", text);
	}

	private static Collection<ILogicalOperator> getAllOperators(ILogicalQuery plan) {
		return plan.getLogicalPlan().getOperators();
	}

	public void _lsRegisteredLogicalEstimators( CommandInterpreter ci ) {
		for( Class<? extends ILogicalOperator> clazz : OperatorEstimatorRegistry.getRegisteredLogicalOperators() ) {
			System.out.println("\t" + clazz);
		}
	}

	public void _listRegisteredLogicalEstimators( CommandInterpreter ci ) {
		for( Class<? extends ILogicalOperator> clazz : OperatorEstimatorRegistry.getRegisteredLogicalOperators() ) {
			System.out.println("\t" + clazz);
		}
	}
}
