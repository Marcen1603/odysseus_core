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
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.SettingMaxConcurrentOptimizations;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.SettingRefuseOptimizationAtMemoryLoad;
import de.uniol.inf.is.odysseus.planmanagement.optimization.console.OptimizationTestSink.OutputMode;
import de.uniol.inf.is.odysseus.planmanagement.optimization.reoptimization.planrules.ReoptimizeTimer;
import de.uniol.inf.is.odysseus.planmanagement.optimization.reoptimization.planrules.SystemLoadListener;

/**
 * custom OSGi console to test planoptimization scenarios
 * 
 * @author Tobias Witt
 * 
 */
public class OptimizationTestConsole implements
		org.eclipse.osgi.framework.console.CommandProvider {

	private IAdvancedExecutor executor;

	@SuppressWarnings("unchecked")
	private ParameterTransformationConfiguration trafoConfigParam = new ParameterTransformationConfiguration(
			new TransformationConfiguration("relational", ITimeInterval.class));

	public void bindExecutor(IAdvancedExecutor executor) {
		this.executor = executor;
		System.out.println("executor gebunden");
	}

	@Override
	public String getHelp() {
		return " --- Optimization Test Console Functions --- \n"
				+ "  m - default planmigration test\n";
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

	public void _m(CommandInterpreter ci) {
		try {
			nmsn(ci);
			Collection<Integer> queryIds = this.executor
					.addQuery(
							"SELECT bid.price FROM nexmark:bid2 [SIZE 5 SECONDS ADVANCE 1 TIME] AS bid, nexmark:auction2 [SIZE 5 SECONDS ADVANCE 1 TIME] AS auction WHERE auction.id=bid.auction",
//							"SELECT bid.price FROM nexmark:bid2 AS bid, nexmark:auction2 AS auction WHERE auction.id=bid.auction",
//							"SELECT bid.price FROM nexmark:bid2 [SIZE 1 SECONDS ADVANCE 1 TIME] AS bid WHERE bid.price > 1",
//							"SELECT bid3.price FROM nexmark:bid2 AS bid3 WHERE bid3.price > 1",
//							"SELECT bid.price FROM nexmark:bid2 AS bid",
							parser(), new ParameterDefaultRoot(
									new OptimizationTestSink(OutputMode.COUNT)),
							this.trafoConfigParam);
			this.executor.startExecution();

			IEditablePlan plan = (IEditablePlan) this.executor.getSealedPlan();
			IEditableQuery query = plan.getQuery(queryIds.iterator().next());
			IPhysicalOperator op = query.getRoot();
			System.out.println(op.getName());

			this.executor.getOptimizerConfiguration().set(
					new SettingMaxConcurrentOptimizations(1));
			this.executor.getOptimizerConfiguration().set(
					new SettingRefuseOptimizationAtMemoryLoad(65.0));
			
			try {
				// Datenraten sind erst nach 30 Sekunden verfuegbar, sonst sind Kosten NaN
				Thread.sleep(3000);
			} catch (InterruptedException e) {}
			System.out.println("reoptimize...");
			query.reoptimize();

		} catch (PlanManagementException e) {
			e.printStackTrace();
		}
	}
	
	public void _testReoptimizationRules(CommandInterpreter ci) {
		try {
			nmsn(ci);
			Collection<Integer> queryIds = this.executor
					.addQuery("SELECT bid3.price FROM nexmark:bid2 AS bid3 WHERE bid3.price > 1",
							parser(), new ParameterDefaultRoot(
									new OptimizationTestSink(OutputMode.NONE)),
							this.trafoConfigParam);
			this.executor.startExecution();
			
			System.out.println("---------------------------------------------");
			System.out.println("test firing optimization at 20% memory usage");
			System.out.println("---------------------------------------------");
			SystemLoadListener loadListener = new SystemLoadListener(this.executor.newSystemMonitor(3000L), 80.0, 20.0);
			this.executor.getSealedPlan().addReoptimzeRule(loadListener);
			Thread.sleep(4000);
			try {
				int[] i = new int[6000000];
				Thread.sleep(3000);
				i = null;
				System.gc();
			} catch (InterruptedException e) {}
			this.executor.getSealedPlan().removeReoptimzeRule(loadListener);
			
			
			System.out.println("---------------------------------------------");
			System.out.println("test firing optimization every 3 seconds");
			System.out.println("---------------------------------------------");
			ReoptimizeTimer optTimer = new ReoptimizeTimer(3000);
			this.executor.getSealedPlan().addReoptimzeRule(optTimer);
			Thread.sleep(3500);
			this.executor.getSealedPlan().removeReoptimzeRule(optTimer);
			

		} catch (PlanManagementException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void _testAdvancedOptimizerRequestHandling(CommandInterpreter ci) {
		try {
			nmsn(ci);
			Collection<Integer> queryIds = this.executor
					.addQuery("SELECT bid3.price FROM nexmark:bid2 AS bid3 WHERE bid3.price > 1",
							parser(), new ParameterDefaultRoot(
									new OptimizationTestSink(OutputMode.NONE)),
							this.trafoConfigParam);
			this.executor.startExecution();
			
			System.out.println("---------------------------------------------");
			System.out.println("test request queuing and resuming at high load");
			System.out.println("---------------------------------------------");
			this.executor.getOptimizerConfiguration().set(
					new SettingRefuseOptimizationAtMemoryLoad(20.0));
			try {
				int[] i = new int[6000000];
				Thread.sleep(1000);
				this.executor.getSealedPlan().getQuery(queryIds.iterator().next()).reoptimize();
				Thread.sleep(8000);
				i = null;
				System.gc();
			} catch (InterruptedException e) {}
			System.out.println("end high mem usage");
			

		} catch (PlanManagementException e) {
			e.printStackTrace();
		} 
	}

	private String parser() {
		return "CQL";
	}

}
