/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.planmanagement.optimization.console;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.ThreadMXBean;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.osgi.framework.console.CommandInterpreter;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.planmanagement.plan.IEditablePlan;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterDefaultRoot;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalPlanToStringVisitor;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalRestructHelper;
import de.uniol.inf.is.odysseus.physicaloperator.base.SelectPO;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.SettingMaxConcurrentOptimizations;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.SettingRefuseOptimizationAtMemoryLoad;
import de.uniol.inf.is.odysseus.planmanagement.optimization.console.OptimizationTestSink.OutputMode;
import de.uniol.inf.is.odysseus.planmanagement.optimization.reoptimization.planrules.ReoptimizeTimer;
import de.uniol.inf.is.odysseus.planmanagement.optimization.reoptimization.planrules.SystemLoadListener;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
import de.uniol.inf.is.odysseus.util.AbstractTreeWalker;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
/**
 * custom OSGi console to test planoptimization scenarios
 * 
 * @author Tobias Witt
 * 
 */
@Component(immediate = true)
@Service(value = org.eclipse.osgi.framework.console.CommandProvider.class)
@Reference(name = "IAdvancedExecutor", referenceInterface = IAdvancedExecutor.class, bind = "bindExecutor", unbind = "unbindExecutor", cardinality = ReferenceCardinality.MANDATORY_UNARY, policy = ReferencePolicy.DYNAMIC)
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

	public void unbindExecutor(IAdvancedExecutor executor) {
		this.executor = null;
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
//							"SELECT bid.price FROM nexmark:bid2 [SIZE 5 SECONDS ADVANCE 1 TIME] AS bid, nexmark:auction2 [SIZE 5 SECONDS ADVANCE 1 TIME] AS auction WHERE auction.id=bid.auction",
//							"SELECT bid.price FROM nexmark:bid2 AS bid, nexmark:auction2 AS auction WHERE auction.id=bid.auction",
							"SELECT bid.price FROM nexmark:bid2 [SIZE 5 SECONDS ADVANCE 1 TIME] AS bid WHERE bid.price > 1",
//							"SELECT bid3.price FROM nexmark:bid2 AS bid3 WHERE bid3.price > 1",
//							"SELECT bid.price FROM nexmark:bid2 AS bid",
							parser(), new ParameterDefaultRoot(
									new OptimizationTestSink(OutputMode.HASH)),
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
			
			// Datenraten sind erst nach 30 Sekunden verfuegbar, sonst sind Kosten NaN
			waitFor(3000);
			System.out.println("reoptimize...");
			query.reoptimize();
			waitFor(6000);
			System.out.println(AbstractTreeWalker.prefixWalk2(query.getRoot(), new PhysicalPlanToStringVisitor()));

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
	
	private enum EvalQuery {GOOD,BAD,MIG};
	
	//_evalOverhead
	public void _e(CommandInterpreter ci) {
		EvalQuery evalQuery = EvalQuery.MIG;
		try {
			nmsn(ci);
			OptimizationTestSink sink = new OptimizationTestSink(OutputMode.COUNT);
			Collection<Integer> queryIds = this.executor
					.addQuery(
							"SELECT seller.name AS seller, bidder.name AS bidder, auction.itemname AS item, bid.price AS price FROM nexmark:auction2 [SIZE 20 SECONDS ADVANCE 1 TIME] AS auction, nexmark:bid2 [SIZE 20 SECONDS ADVANCE 1 TIME] AS bid, nexmark:person2 [SIZE 20 SECONDS ADVANCE 1 TIME] AS seller, nexmark:person2 [SIZE 20 SECONDS ADVANCE 1 TIME] AS bidder WHERE seller.id=auction.seller AND auction.id=bid.auction AND bid.bidder=bidder.id AND bid.price>200",
							//"SELECT seller.name AS seller, bidder.name AS bidder, auction.itemname AS item, bid.price AS price FROM nexmark:auction2 [SIZE 5 SECONDS ADVANCE 1 TIME] AS auction, nexmark:bid2 [SIZE 5 SECONDS ADVANCE 1 TIME] AS bid, nexmark:person2 [SIZE 5 SECONDS ADVANCE 1 TIME] AS seller, nexmark:person2 [SIZE 5 SECONDS ADVANCE 1 TIME] AS bidder WHERE seller.id=auction.seller AND auction.id=bid.auction AND bid.bidder=bidder.id AND bid.price>200",
							parser(), new ParameterDefaultRoot(sink),
							this.trafoConfigParam);
			IEditablePlan plan = (IEditablePlan) this.executor.getSealedPlan();
			IEditableQuery query = plan.getQuery(queryIds.iterator().next());
			
			// manipulate: push select to top
			if (evalQuery == EvalQuery.BAD || evalQuery == EvalQuery.MIG) {
				IPhysicalOperator root = query.getRoot();
				System.out.println(root.getName());
				IPhysicalOperator project = ((ISink<?>)root).getSubscribedToSource(0).getTarget();
				System.out.println(project.getName());
				IPhysicalOperator lastJoin = ((ISink<?>)project).getSubscribedToSource(0).getTarget();
				System.out.println(lastJoin.getName());
				IPhysicalOperator secondJoin = ((ISink<?>)lastJoin).getSubscribedToSource(0).getTarget();
				System.out.println(secondJoin.getName());
				IPhysicalOperator firstJoin = ((ISink<?>)secondJoin).getSubscribedToSource(0).getTarget();
				System.out.println(firstJoin.getName());
				IPhysicalOperator window = ((ISink<?>)firstJoin).getSubscribedToSource(1).getTarget();
				System.out.println(window.getName());
				IPhysicalOperator select = ((ISink<?>)window).getSubscribedToSource(0).getTarget();
				System.out.println(select.getName());
				IPhysicalOperator meta = ((ISink<?>)select).getSubscribedToSource(0).getTarget();
				System.out.println(meta.getName());
				
				// remove select before join
				PhysicalRestructHelper.removeSubscription(window, select);
				PhysicalRestructHelper.removeSubscription(select, meta);
				PhysicalRestructHelper.appendOperator(window, meta);
				
				// put select after join
				PhysicalRestructHelper.removeSubscription(project, lastJoin);
				RelationalPredicate predicate = (RelationalPredicate) ((SelectPO<?>)select).getPredicate();
				RelationalPredicate newPredicate = new RelationalPredicate(predicate.getExpression());
				newPredicate.init(lastJoin.getOutputSchema(),null);
				SelectPO<?> newSelect = new SelectPO(newPredicate);
				newSelect.setOutputSchema(lastJoin.getOutputSchema().clone());
				PhysicalRestructHelper.appendOperator(newSelect, lastJoin);
				PhysicalRestructHelper.appendOperator(project, newSelect);
				query.initializePhysicalPlan(root);
				this.executor.updateExecutionPlan();
				//System.out.println(AbstractTreeWalker.prefixWalk2(query.getRoot(), new PhysicalPlanToStringVisitor()));
			}
			
			MemoryMXBean memBean = ManagementFactory.getMemoryMXBean();
			ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
			Map<Long, Long> lastTime = new HashMap<Long, Long>();
			
			waitFor(1000);
			this.executor.startExecution();
			long startTime = System.currentTimeMillis();
			System.out.println("----------Evaluation Start------------------------------");
			System.out.println("time_elapsed;tuples_passed;cpu_load;memory_usage");
			// first time minus old cpu load
			for (long id : threadBean.getAllThreadIds()) {
				long t = threadBean.getThreadCpuTime(id);
				lastTime.put(id, t);
			}
			for (int i=0; i<60; i++) {
				waitFor(1000);
				long cputime = 0L;
				for (long id : threadBean.getAllThreadIds()) {
					long t = threadBean.getThreadCpuTime(id);
					if (lastTime.containsKey(id)) {
						long last = lastTime.get(id);
						lastTime.put(id, t);
						t -= last;
					} else {
						lastTime.put(id, t);
					}
					cputime += t;
				}
				System.out.println((System.currentTimeMillis()-startTime)
						+";"+sink.getCount()
						+";"+cputime
						+";"+memBean.getHeapMemoryUsage().getUsed());
				if (evalQuery == EvalQuery.MIG && i==25) {
					final IQuery q = query;
					Runnable reopt = new Runnable() {
						@Override
						public void run() {
							q.reoptimize();
						}
					};
					new Thread(reopt).start();
				}
			}
			System.out.println("----------Evaluation End--------------------------------");
			
			/*this.executor.startExecution();
			waitFor(3000);
			query.reoptimize();
			waitFor(3000);*/
			//System.out.println(AbstractTreeWalker.prefixWalk2(query.getRoot(), new PhysicalPlanToStringVisitor()));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//_evalOverhead2
	public void _f(CommandInterpreter ci) {
		EvalQuery evalQuery = EvalQuery.MIG;
		try {
			nmsn(ci);
			OptimizationTestSink sink = new OptimizationTestSink(OutputMode.COUNT);
			Collection<Integer> queryIds = this.executor
					.addQuery(
							"SELECT bid.price FROM nexmark:bid2 [SIZE 5 SECONDS ADVANCE 1 TIME] AS bid, nexmark:bid2 [SIZE 5 SECONDS ADVANCE 1 TIME] AS bid2 WHERE bid2.auction=bid.auction AND bid.price > bid2.price AND bid.price > 200",
							parser(), new ParameterDefaultRoot(sink),
							this.trafoConfigParam);
			IEditablePlan plan = (IEditablePlan) this.executor.getSealedPlan();
			IEditableQuery query = plan.getQuery(queryIds.iterator().next());
			
			// manipulate: push select to top
			if (evalQuery == EvalQuery.BAD) {
				IPhysicalOperator root = query.getRoot();
				System.out.println(root.getName());
				IPhysicalOperator project = ((ISink<?>)root).getSubscribedToSource(0).getTarget();
				System.out.println(project.getName());
				IPhysicalOperator lastJoin = ((ISink<?>)project).getSubscribedToSource(0).getTarget();
				System.out.println(lastJoin.getName());
				IPhysicalOperator window = ((ISink<?>)lastJoin).getSubscribedToSource(0).getTarget();
				System.out.println(window.getName());
				IPhysicalOperator select = ((ISink<?>)window).getSubscribedToSource(0).getTarget();
				System.out.println(select.getName());
				IPhysicalOperator meta = ((ISink<?>)select).getSubscribedToSource(0).getTarget();
				System.out.println(meta.getName());
				
				// remove select before join
				PhysicalRestructHelper.removeSubscription(window, select);
				PhysicalRestructHelper.removeSubscription(select, meta);
				PhysicalRestructHelper.appendOperator(window, meta);
				
				// put select after join
				PhysicalRestructHelper.removeSubscription(project, lastJoin);
				RelationalPredicate predicate = (RelationalPredicate) ((SelectPO<?>)select).getPredicate();
				RelationalPredicate newPredicate = new RelationalPredicate(predicate.getExpression());
				newPredicate.init(lastJoin.getOutputSchema(),null);
				SelectPO<?> newSelect = new SelectPO(newPredicate);
				newSelect.setOutputSchema(lastJoin.getOutputSchema().clone());
				PhysicalRestructHelper.appendOperator(newSelect, lastJoin);
				PhysicalRestructHelper.appendOperator(project, newSelect);
				query.initializePhysicalPlan(root);
				this.executor.updateExecutionPlan();
			}
			
			MemoryMXBean memBean = ManagementFactory.getMemoryMXBean();
			ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
			Map<Long, Long> lastTime = new HashMap<Long, Long>();
			
			waitFor(1000);
			this.executor.startExecution();
			long startTime = System.currentTimeMillis();
			System.out.println("----------Evaluation Start------------------------------");
			System.out.println("time_elapsed;tuples_passed;cpu_load;memory_usage");
			// first time minus old cpu load
			for (long id : threadBean.getAllThreadIds()) {
				long t = threadBean.getThreadCpuTime(id);
				lastTime.put(id, t);
			}
			for (int i=0; i<30; i++) {
				waitFor(1000);
				long cputime = 0L;
				for (long id : threadBean.getAllThreadIds()) {
					long t = threadBean.getThreadCpuTime(id);
					if (lastTime.containsKey(id)) {
						long last = lastTime.get(id);
						lastTime.put(id, t);
						t -= last;
					} else {
						lastTime.put(id, t);
					}
					cputime += t;
				}
				System.out.println((System.currentTimeMillis()-startTime)
						+";"+sink.getCount()
						+";"+cputime
						+";"+memBean.getHeapMemoryUsage().getUsed());
				if (evalQuery == EvalQuery.MIG && i==10) {
					final IQuery q = query;
					Runnable reopt = new Runnable() {
						@Override
						public void run() {
							q.reoptimize();
						}
					};
					new Thread(reopt).start();
				}
			}
			System.out.println("----------Evaluation End--------------------------------");
			
			/*this.executor.startExecution();
			waitFor(3000);
			query.reoptimize();
			waitFor(3000);*/
			//System.out.println(AbstractTreeWalker.prefixWalk2(query.getRoot(), new PhysicalPlanToStringVisitor()));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void waitFor(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private String parser() {
		return "CQL";
	}

}
