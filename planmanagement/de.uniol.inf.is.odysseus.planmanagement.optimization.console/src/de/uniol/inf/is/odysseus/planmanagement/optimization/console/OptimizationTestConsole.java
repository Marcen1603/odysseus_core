package de.uniol.inf.is.odysseus.planmanagement.optimization.console;

import java.util.Collection;

import org.eclipse.osgi.framework.console.CommandInterpreter;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.planmanagement.plan.IEditablePlan;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterDefaultRoot;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel.PlanExecutionCostCalculator;

/**
 * custom OSGi console to test planoptimization scenarios
 * 
 * @author Tobias Witt
 *
 */
public class OptimizationTestConsole implements	org.eclipse.osgi.framework.console.CommandProvider {
	
	private IAdvancedExecutor executor;
	
	@SuppressWarnings("unchecked")
	private ParameterTransformationConfiguration trafoConfigParam = new ParameterTransformationConfiguration(
			new TransformationConfiguration("relational", ITimeInterval.class));
	
	public void bindExecutor(IAdvancedExecutor executor){
		this.executor = executor;
		System.out.println("executor gebunden");
	}

	@Override
	public String getHelp() {
		return " --- Optimization Test Console Functions --- \n"
			+"  m - default planmigration test\n";
	}
	
	private void nmsn(CommandInterpreter ci) {
		String[] q = new String[4];
		q[0] = "CREATE STREAM nexmark:person2 (timestamp LONG,id INTEGER,name STRING,email STRING,creditcard STRING,city STRING,state STRING) CHANNEL localhost : 65440";
		q[1] = "CREATE STREAM nexmark:bid2 (timestamp LONG,	auction INTEGER, bidder INTEGER, datetime LONG,	price DOUBLE) CHANNEL localhost : 65442";
		q[2] = "CREATE STREAM nexmark:auction2 (timestamp LONG,	id INTEGER,	itemname STRING,	description STRING,	initialbid INTEGER,	reserve INTEGER,	expires LONG,	seller INTEGER ,category INTEGER) CHANNEL localhost : 65441";
		q[3] = "CREATE STREAM nexmark:category2 (id INTEGER, name STRING, description STRING, parentid INTEGER) CHANNEL localhost : 65443";
		for (String s : q) {
			try {
				this.executor.addQuery(s, parser());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		ci.println("Nexmark Sources with NIO added.");
	}
	
	public void _m (CommandInterpreter ci) {
		try {
			nmsn(ci);
			/*Collection<Integer> queryIds = this.executor.addQuery("SELECT * FROM nexmark:bid2", 
					parser(), new ParameterDefaultRoot(new OptimizationTestSink(false)), this.trafoConfigParam);*/
			Collection<Integer> queryIds = this.executor.addQuery("SELECT bid.price FROM nexmark:bid2 AS bid, nexmark:auction2 AS auction WHERE auction.id=bid.auction", 
					parser(), new ParameterDefaultRoot(new OptimizationTestSink(false)), this.trafoConfigParam);
			this.executor.startExecution();
			
			IEditablePlan plan = (IEditablePlan)this.executor.getSealedPlan();
			IEditableQuery query = plan.getQuery(queryIds.iterator().next());
			
			IPhysicalOperator op = query.getRoot();
			System.out.println(op.getName());
			
			//query.reoptimize();
			
			while (true) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {}
				System.out.println("Plan cost test...");
				new PlanExecutionCostCalculator().calculateCost(op);
			}
			
			//AbstractTreeWalker.prefixWalk2(op, new CopyPhysicalPlanVisitor());
			
		} catch (PlanManagementException e) {
			e.printStackTrace();
		}
	}

	private String parser() {
		return "CQL";
	}

}
