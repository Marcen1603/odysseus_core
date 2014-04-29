package de.uniol.inf.is.odysseus.costmodel.logical;

import java.util.Collection;
import java.util.List;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.costmodel.DetailCost;

public class LogicalCostModelConsole implements CommandProvider {

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
		
		IPhysicalQuery physicalQuery = serverExecutor.getExecutionPlan().getQueryById(queryID);
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
		return getAllOperators(plan.getLogicalPlan());
	}

	private static Collection<ILogicalOperator> getAllOperators(ILogicalOperator operator) {
		List<ILogicalOperator> operators = Lists.newArrayList();
		collectOperatorsImpl(operator, operators);
		return operators;
	}

	private static void collectOperatorsImpl(ILogicalOperator currentOperator, Collection<ILogicalOperator> list) {
		if (!list.contains(currentOperator)) {
			list.add(currentOperator);
			for (final LogicalSubscription subscription : currentOperator.getSubscriptions()) {
				collectOperatorsImpl(subscription.getTarget(), list);
			}

			for (final LogicalSubscription subscription : currentOperator.getSubscribedToSource()) {
				collectOperatorsImpl(subscription.getTarget(), list);
			}
		}
	}
}
