package de.uniol.inf.is.odysseus.costmodel.physical;

import java.util.List;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.costmodel.DetailCost;

public class PhysicalCostModelConsole implements CommandProvider {

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
		
		IPhysicalQuery physicalQuery = serverExecutor.getExecutionPlan().getQueryById(queryID);
		if( physicalQuery != null ) {
			List<IPhysicalOperator> operators = physicalQuery.getPhysicalChilds();
			IPhysicalCost physicalCost = physicalCostModel.estimateCost(operators);
			
			System.out.println("Estimated logical costs:");
			for( IPhysicalOperator operator : physicalCost.getOperators() ) {
				System.out.println(operator.getClass().getSimpleName() + " [" + operator.getName() + "]: ");
				DetailCost detailCost = physicalCost.getDetailCost(operator);
				
				System.out.println("\tCPU = " + detailCost.getCpuCost());
				System.out.println("\tMEM = " + detailCost.getMemCost());
				System.out.println("\tNET = " + detailCost.getNetCost());
				System.out.println("\tWND = " + detailCost.getWindowSize());
				System.out.println("\tSEL = " + detailCost.getSelectivity());
				System.out.println("\tRAT = " + detailCost.getDatarate());
			}
			
			System.out.println();
			System.out.println("Summary: ");
			System.out.println("\tCPU = " + physicalCost.getCpuSum());
			System.out.println("\tMEM = " + physicalCost.getMemorySum());
			System.out.println("\tNET = " + physicalCost.getNetworkSum());
		} else {
			System.out.println("Physical query with id " + queryID + " not found");
		}
	}
}
