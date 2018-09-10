package de.uniol.inf.is.odysseus.costmodel.physical;

import java.util.List;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.costmodel.DetailCost;
import de.uniol.inf.is.odysseus.costmodel.physical.impl.OperatorEstimatorRegistry;

public class PhysicalCostModelConsole implements CommandProvider {

	static private final ISession currentUser = SessionManagement.instance.loginSuperUser(null);


	private static IPhysicalCostModel physicalCostModel;
	private static IServerExecutor serverExecutor;

	// called by OSGi-DS
	public static void bindPhysicalCostModel(IPhysicalCostModel serv) {
		physicalCostModel = serv;
	}

	// called by OSGi-DS
	public static void unbindPhysicalCostModel(IPhysicalCostModel serv) {
		if (physicalCostModel == serv) {
			physicalCostModel = null;
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

		sb.append("---- Physical cost model ----\n");
		sb.append("    estimatePhysical <queryID>          - Estimates the current costs for the logical query with the given id.\n");
		sb.append("    listRegisteredPhysicalEstimators/ls - Lists all physical operators which have physical estimators.\n");

		return sb.toString();
	}

	public void _estimatePhysical( CommandInterpreter ci ) {
		String queryIDString = ci.nextArgument();
		if( Strings.isNullOrEmpty(queryIDString)) {
			System.out.println("usage: estimatePhysical <queryid>");
			return;
		}

		int queryID = 0;
		try {
			queryID = Integer.valueOf(queryIDString);
		} catch( Throwable t ) {
			System.out.println("usage: estimatePhysical <queryid>");
			return;
		}

		IPhysicalQuery physicalQuery = serverExecutor.getExecutionPlan(currentUser).getQueryById(queryID, currentUser);
		if( physicalQuery != null ) {
			List<IPhysicalOperator> operators = physicalQuery.getPhysicalChilds();
			IPhysicalCost physicalCost = physicalCostModel.estimateCost(operators);

			System.out.println("Estimated physical costs:");
			for( IPhysicalOperator operator : physicalCost.getOperators() ) {
				System.out.println(operator.getClass().getSimpleName() + " [" + operator.getName() + "]:");
				DetailCost detailCost = physicalCost.getDetailCost(operator);

				System.out.print("\tCPU = " + format(detailCost.getCpuCost()));
				System.out.print(",\tMEM = " + format(detailCost.getMemCost()));
				System.out.print(",\tNET = " + format(detailCost.getNetCost()));
				System.out.print(",\tSEL = " + format(detailCost.getSelectivity()));
				System.out.print(",\tRAT = " + format(detailCost.getDatarate()));
				System.out.print(",\tWND = " + format(detailCost.getWindowSize()));
				System.out.println();
			}

			System.out.println("Summary: ");
			System.out.print("\tCPU = " + format(physicalCost.getCpuSum()));
			System.out.print(",\tMEM = " + format(physicalCost.getMemorySum()));
			System.out.print(",\tNET = " + format(physicalCost.getNetworkSum()));
		} else {
			System.out.println("Physical query with id " + queryID + " not found");
		}
	}

	private static String format(Object text ) {
		return String.format("%-6.4f", text);
	}

	public void _lsRegisteredPhysicalEstimators( CommandInterpreter ci ) {
		for( Class<? extends IPhysicalOperator> clazz : OperatorEstimatorRegistry.getRegisteredPhysicalOperators() ) {
			System.out.println("\t" + clazz);
		}
	}

	public void _listRegisteredPhysicalEstimators( CommandInterpreter ci ) {
		for( Class<? extends IPhysicalOperator> clazz : OperatorEstimatorRegistry.getRegisteredPhysicalOperators() ) {
			System.out.println("\t" + clazz);
		}
	}
}
