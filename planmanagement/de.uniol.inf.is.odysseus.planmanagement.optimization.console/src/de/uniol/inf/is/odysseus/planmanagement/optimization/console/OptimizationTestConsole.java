package de.uniol.inf.is.odysseus.planmanagement.optimization.console;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
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
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
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

/**
 * custom OSGi console to test planoptimization scenarios
 * 
 * @author Tobias Witt
 * 
 */
public class OptimizationTestConsole implements
		org.eclipse.osgi.framework.console.CommandProvider {

	private IAdvancedExecutor executor;

	private static ConsoleFunctions support = new ConsoleFunctions();

	/**
	 * This is the basebath to files. This path can be set by command setPath.
	 */
	private String basepath;

	@SuppressWarnings("unchecked")
	private ParameterTransformationConfiguration trafoConfigParam = new ParameterTransformationConfiguration(
			new TransformationConfiguration("relational", ITimeInterval.class));

	public void bindExecutor(IAdvancedExecutor executor) {
		this.executor = executor;
		System.out.println("executor gebunden");
	}

	public void _setPath(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);

		if (args == null || args.length == 0) {
			ci.println("No path defined.");
			return;
		}

		this.basepath = args[0].endsWith("/") ? args[0] : args[0] + "/";

		ci.println("Path has been set. New path: " + this.basepath);
	}

	@Override
	public String getHelp() {
		return " --- Optimization Test Console Functions --- \n"
				+ "  m - default planmigration test\n";
	}

	private void dumpRoots(){
		int depth = 0;
		StringBuffer buff = new StringBuffer();
		System.out.println("Physical plan of all roots: ");
		int count = 1;
		try {
			for (IPhysicalOperator root : this.executor.getSealedPlan()
					.getRoots()) {
				buff = new StringBuffer();
				if (root.isSink()) {
					support.dumpPlan((ISink) root, depth, buff);
				} else {
					support.dumpPlan((ISource) root, depth, buff);
				}
				System.out.println(count++ + ". root:");
				System.out.println(buff.toString());
			}
		} catch (PlanManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	public void _migrateTest(CommandInterpreter ci) {
		try {
			nmsn(ci);
			Collection<Integer> queryIds = this.executor
					.addQuery(
							"SELECT bid.price FROM nexmark:bid2 [SIZE 5 SECONDS ADVANCE 1 TIME] AS bid, nexmark:auction2 [SIZE 5 SECONDS ADVANCE 1 TIME] AS auction WHERE auction.id=bid.auction",
							// "SELECT bid.price FROM nexmark:bid2 AS bid, nexmark:auction2 AS auction WHERE auction.id=bid.auction",
							// "SELECT bid.price FROM nexmark:bid2 [SIZE 5 SECONDS ADVANCE 1 TIME] AS bid WHERE bid.price > 1",
							// "SELECT bid3.price FROM nexmark:bid2 AS bid3 WHERE bid3.price > 1",
							// "SELECT bid.price FROM nexmark:bid2 AS bid",
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

			// Datenraten sind erst nach 30 Sekunden verfuegbar, sonst sind
			// Kosten NaN
			waitFor(35000);
			System.out.println("reoptimize...");
			query.reoptimize();
			waitFor(6000);
			// System.out.println(AbstractTreeWalker.prefixWalk2(query.getRoot(),
			// new PhysicalPlanToStringVisitor()));
			System.out.println("done...");

		} catch (PlanManagementException e) {
			e.printStackTrace();
		}
	}

	public void _testReoptimizationRules(CommandInterpreter ci) {
		try {
			nmsn(ci);
			Collection<Integer> queryIds = this.executor
					.addQuery(
							"SELECT bid3.price FROM nexmark:bid2 AS bid3 WHERE bid3.price > 1",
							parser(), new ParameterDefaultRoot(
									new OptimizationTestSink(OutputMode.NONE)),
							this.trafoConfigParam);
			this.executor.startExecution();

			System.out.println("---------------------------------------------");
			System.out.println("test firing optimization at 20% memory usage");
			System.out.println("---------------------------------------------");
			SystemLoadListener loadListener = new SystemLoadListener(
					this.executor.newSystemMonitor(3000L), 80.0, 20.0);
			this.executor.getSealedPlan().addReoptimzeRule(loadListener);
			Thread.sleep(4000);
			try {
				int[] i = new int[6000000];
				Thread.sleep(3000);
				i = null;
				System.gc();
			} catch (InterruptedException e) {
			}
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
					.addQuery(
							"SELECT bid3.price FROM nexmark:bid2 AS bid3 WHERE bid3.price > 1",
							parser(), new ParameterDefaultRoot(
									new OptimizationTestSink(OutputMode.NONE)),
							this.trafoConfigParam);
			this.executor.startExecution();

			System.out.println("---------------------------------------------");
			System.out
					.println("test request queuing and resuming at high load");
			System.out.println("---------------------------------------------");
			this.executor.getOptimizerConfiguration().set(
					new SettingRefuseOptimizationAtMemoryLoad(20.0));
			try {
				int[] i = new int[6000000];
				Thread.sleep(1000);
				this.executor.getSealedPlan().getQuery(
						queryIds.iterator().next()).reoptimize();
				Thread.sleep(8000);
				i = null;
				System.gc();
			} catch (InterruptedException e) {
			}
			System.out.println("end high mem usage");

		} catch (PlanManagementException e) {
			e.printStackTrace();
		}
	}

	private enum EvalQuery {
		GOOD, BAD, MIG
	};

	public void _evalMigration(CommandInterpreter ci) {
		_e(ci);
	}

	public void _em(CommandInterpreter ci) {
		// TODO: Hack zum einfachen Testen ;-)
		basepath = "c:/development/";
		nmsn(ci);
		EvalQuery eq = EvalQuery.MIG;
		try {
			eval(eq, 120, 5, "" + System.currentTimeMillis()+eq);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void _e(CommandInterpreter ci) {
		// TODO: Hack zum einfachen Testen ;-)
		basepath = "c:/development/";
		nmsn(ci);
		for (EvalQuery eq : EvalQuery.values()) {
			try {
				eval(eq, 120, 5, "" + System.currentTimeMillis()+eq);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void eval(EvalQuery evalQuery, int secondsPerRun, int runs,
			String filename) throws IOException {
		String newline = System.getProperty("line.separator");
		String sep = ";";

		List<List<Measure>> r = new ArrayList<List<Measure>>(runs);

		for (int i = 0; i < runs; i++) {
			r.add(e(evalQuery, secondsPerRun));
			waitFor(10000); // Zwischen zwei Aufrufen warten --> Quellenconnect
							// etc.
		}

		FileWriter fw = new FileWriter(basepath + "" + filename + ".csv");
		for (int i = 0; i < runs; i++) {
			fw.write("time_elapsed_" + i + sep + "tuples_passed_" + i
					+ evalQuery + sep + "cpu_load_" + i + evalQuery + sep
					+ "memory_usage_" + i + evalQuery + sep + sep);
		}
		fw.write("MEDIAN(time_elapsed)" + sep + "MEDIAN(tuples_passed) "
				+ evalQuery + sep + "MEDIAN(cpu_load) " + evalQuery + sep
				+ "MEDIAN(memory_usage) " + evalQuery + newline);
		for (int s = 0; s < secondsPerRun; s++) {
			// Median berechnen
			long m_time_elapsed = -1;
			DescriptiveStatistics m_tuples_passed = new DescriptiveStatistics();
			;
			DescriptiveStatistics m_cpu_load = new DescriptiveStatistics();
			;
			DescriptiveStatistics m_memory_usage = new DescriptiveStatistics();
			;
			for (int i = 0; i < runs; i++) {
				Measure m = r.get(i).get(s);
				m_time_elapsed = m.time_elapsed;
				m_tuples_passed.addValue(m.tuples_passed);
				m_cpu_load.addValue(m.cpu_load);
				m_memory_usage.addValue(m.memory_usage);
				fw.write(m.time_elapsed + sep + m.tuples_passed + sep
						+ m.cpu_load + sep + m.memory_usage + sep + sep);
			}
			// Median für jeden Messpunkt ausgeben
			fw.write(m_time_elapsed + sep
					+ (long) Math.round(m_tuples_passed.getPercentile(50))
					+ sep + (long) Math.round(m_cpu_load.getPercentile(50))
					+ sep + (long) Math.round(m_memory_usage.getPercentile(50))
					+ newline);

		}
		fw.close();
	}

	public List<Measure> e(EvalQuery evalQuery, int seconds) {
		List<Measure> measures = new ArrayList<Measure>(seconds);
		System.out.println(evalQuery + " " + seconds);
		String newline = System.getProperty("line.separator");
		String sep = ";";
		try {

			OptimizationTestSink sink = new OptimizationTestSink(OutputMode.COUNT);
			Collection<Integer> queryIds = this.executor
					.addQuery(
							"SELECT seller.name AS seller, bidder.name AS bidder, auction.itemname AS item, bid.price AS price FROM nexmark:auction2 [SIZE 20 SECONDS ADVANCE 1 TIME] AS auction, nexmark:bid2 [SIZE 20 SECONDS ADVANCE 1 TIME] AS bid, nexmark:person2 [SIZE 20 SECONDS ADVANCE 1 TIME] AS seller, nexmark:person2 [SIZE 20 SECONDS ADVANCE 1 TIME] AS bidder WHERE seller.id=auction.seller AND auction.id=bid.auction AND bid.bidder=bidder.id AND bid.price>260",
							parser(), new ParameterDefaultRoot(sink),
							this.trafoConfigParam);
			IEditablePlan plan = (IEditablePlan) this.executor.getSealedPlan();
			IEditableQuery query = plan.getQuery(queryIds.iterator().next());

			// manipulate: push select to top
			if (evalQuery == EvalQuery.BAD || evalQuery == EvalQuery.MIG) {
				System.out.println("Rebuilding Query to Bad Query");
				IPhysicalOperator root = query.getRoot();
				System.out.println(root.getName());
				IPhysicalOperator project = ((ISink<?>) root)
						.getSubscribedToSource(0).getTarget();
				System.out.println(project.getName());
				IPhysicalOperator lastJoin = ((ISink<?>) project)
						.getSubscribedToSource(0).getTarget();
				System.out.println(lastJoin.getName());
				IPhysicalOperator secondJoin = ((ISink<?>) lastJoin)
						.getSubscribedToSource(0).getTarget();
				System.out.println(secondJoin.getName());
				IPhysicalOperator firstJoin = ((ISink<?>) secondJoin)
						.getSubscribedToSource(0).getTarget();
				System.out.println(firstJoin.getName());
				IPhysicalOperator window = ((ISink<?>) firstJoin)
						.getSubscribedToSource(1).getTarget();
				System.out.println(window.getName());
				IPhysicalOperator select = ((ISink<?>) window)
						.getSubscribedToSource(0).getTarget();
				System.out.println(select.getName());
				IPhysicalOperator meta = ((ISink<?>) select)
						.getSubscribedToSource(0).getTarget();
				System.out.println(meta.getName());

				// remove select before join
				PhysicalRestructHelper.removeSubscription(window, select);
				PhysicalRestructHelper.removeSubscription(select, meta);
				PhysicalRestructHelper.appendOperator(window, meta);

				// put select after join
				PhysicalRestructHelper.removeSubscription(project, lastJoin);
				RelationalPredicate predicate = (RelationalPredicate) ((SelectPO<?>) select)
						.getPredicate();
				RelationalPredicate newPredicate = new RelationalPredicate(
						predicate.getExpression());
				newPredicate.init(lastJoin.getOutputSchema(), null);
				SelectPO<?> newSelect = new SelectPO(newPredicate);
				newSelect.setOutputSchema(lastJoin.getOutputSchema().clone());
				PhysicalRestructHelper.appendOperator(newSelect, lastJoin);
				PhysicalRestructHelper.appendOperator(project, newSelect);
				query.initializePhysicalPlan(root);
				this.executor.updateExecutionPlan();
			}
			
			dumpRoots();

			MemoryMXBean memBean = ManagementFactory.getMemoryMXBean();
			ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
			if (!threadBean.isThreadCpuTimeSupported()) {
				System.err.println("Messung nicht möglich!!!");
			}
			Map<Long, Long> lastTime = new HashMap<Long, Long>();

			waitFor(1000);
			this.executor.startExecution();
			long startTime = System.currentTimeMillis();
			System.out
					.println("----------Evaluation Start------------------------------");
			System.out.println("time_elapsed" + sep + "tuples_passed "
					+ evalQuery + sep + "cpu_load " + evalQuery + sep
					+ "memory_usage " + evalQuery);
			// first time minus old cpu load
			for (long id : threadBean.getAllThreadIds()) {
				long t = threadBean.getThreadCpuTime(id);
				lastTime.put(id, t);
			}
			for (int i = 0; i < seconds; i++) {
				waitFor(1000);
				long cputime = 0L;
				long currentTime = System.currentTimeMillis();
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
				Measure m = new Measure((long) Math
						.floor(((currentTime - startTime) / 1000)), sink
						.getCount(), cputime, memBean.getHeapMemoryUsage()
						.getUsed());
				measures.add(m);
				System.out.println(m.time_elapsed + sep + m.tuples_passed + sep
						+ m.cpu_load + sep + m.memory_usage);
				if (evalQuery == EvalQuery.MIG && i == 50) {
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
			System.out
					.println("----------Evaluation End--------------------------------");

			executor.removeQuery(query.getID());
			lsqueries();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return measures;
	}

	public void lsqueries() {
		try {
			System.out
					.println("Current registered queries (ID | STARTED | PARSERID):");
			for (IQuery query : this.executor.getSealedPlan().getQueries()) {
				System.out.println(query.getID() + " | " + query.isRunning()
						+ " | " + query.getParserId());
			}
		} catch (PlanManagementException e) {
			e.printStackTrace();
		}
	}

	public void _ntest(CommandInterpreter ci) {
		for (int i = 0; i < 10; i++) {
			_nexmarkDatarate(ci);
		}
	}

	public void _nexmarkDatarate(CommandInterpreter ci) {
		try {
			nmsn(ci);
			OptimizationTestSink sinkAuction = new OptimizationTestSink(
					OutputMode.COUNT);
			OptimizationTestSink sinkBid = new OptimizationTestSink(
					OutputMode.COUNT);
			OptimizationTestSink sinkPerson = new OptimizationTestSink(
					OutputMode.COUNT);
			this.executor.addQuery("SELECT * FROM nexmark:auction2", parser(),
					new ParameterDefaultRoot(sinkAuction),
					this.trafoConfigParam);
			this.executor.addQuery("SELECT * FROM nexmark:bid2", parser(),
					new ParameterDefaultRoot(sinkBid), this.trafoConfigParam);
			this.executor
					.addQuery("SELECT * FROM nexmark:person2", parser(),
							new ParameterDefaultRoot(sinkPerson),
							this.trafoConfigParam);

			this.executor.startExecution();
			for (int i = 0; i < 120; i++) {
				waitFor(1000);
				System.out.println("sinkAuction tuples/sec: "
						+ sinkAuction.getCount());
				System.out.println("sinkBid tuples/sec: " + sinkBid.getCount());
				System.out.println("sinkPerson tuples/sec: "
						+ sinkPerson.getCount());
				sinkAuction.reset();
				sinkBid.reset();
				sinkPerson.reset();
			}

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

class Measure {
	public long time_elapsed;
	public long tuples_passed;
	public long cpu_load;
	public long memory_usage;

	public Measure(long timeElapsed, long tuplesPassed, long cpuLoad,
			long memoryUsage) {
		time_elapsed = timeElapsed;
		tuples_passed = tuplesPassed;
		cpu_load = cpuLoad;
		memory_usage = memoryUsage;
	}

}
