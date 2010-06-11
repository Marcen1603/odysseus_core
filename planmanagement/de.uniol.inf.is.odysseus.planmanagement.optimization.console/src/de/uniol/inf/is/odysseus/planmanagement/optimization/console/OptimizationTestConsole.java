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
import java.util.Set;

import org.eclipse.osgi.framework.console.CommandInterpreter;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.planmanagement.plan.IEditablePlan;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterDefaultRoot;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;
import de.uniol.inf.is.odysseus.base.usermanagement.User;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalRestructHelper;
import de.uniol.inf.is.odysseus.physicaloperator.base.SelectPO;
import de.uniol.inf.is.odysseus.physicaloperator.base.access.Router;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.NoOptimizerLoadedException;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.planmanagement.executor.standardexecutor.SettingBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.SettingMaxConcurrentOptimizations;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.SettingRefuseOptimizationAtMemoryLoad;
import de.uniol.inf.is.odysseus.planmanagement.optimization.console.OptimizationTestSink.OutputMode;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.reoptimization.planrules.ReoptimizeTimer;
import de.uniol.inf.is.odysseus.planmanagement.optimization.reoptimization.planrules.SystemLoadListener;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
import de.uniol.inf.is.odysseus.latency.ILatency;

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

	OptimizationTestSink sink = null;
	
	User currentUser = new User("OptimizationTestConsole");

	/**
	 * This is the basebath to files. This path can be set by command setPath.
	 */
	private String basepath;

	@SuppressWarnings("unchecked")
	private ParameterTransformationConfiguration trafoConfigParam = new ParameterTransformationConfiguration(
			new TransformationConfiguration("relational", ITimeInterval.class, ILatency.class));

	private String currentScheduler;

	private String currentBuffer;

	private Object parser;

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

	private void dumpRoots() {
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
				this.executor.addQuery(s, parser(), currentUser);
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
							parser(), currentUser, new ParameterDefaultRoot(
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
			this.executor
					.addQuery(
							"SELECT bid3.price FROM nexmark:bid2 AS bid3 WHERE bid3.price > 1",
							parser(), currentUser, new ParameterDefaultRoot(
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
				@SuppressWarnings("unused")
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
							parser(), currentUser, new ParameterDefaultRoot(
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
				@SuppressWarnings("unused")
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
		GOOD, BAD, MIG, GOOD_REMOVE, BAD_REMOVE
	};

	public void _evalMigration(CommandInterpreter ci) {
		_e(ci);
	}

	public void _em1(CommandInterpreter ci) {
		// TODO: Hack zum einfachen Testen ;-)
		basepath = "c:/development/";
		nmsn(ci);
		EvalQuery eq = EvalQuery.MIG;
		try {
			eval(eq, 120, 1, "" + System.currentTimeMillis() + eq);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void _eg(CommandInterpreter ci) {
		// TODO: Hack zum einfachen Testen ;-)
		basepath = "c:/development/";
		nmsn(ci);
		EvalQuery eq = EvalQuery.GOOD;
		try {
			eval(eq, 120, 5, "" + System.currentTimeMillis()+ eq+"_"+currentBuffer+"_"+currentScheduler);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void _er(CommandInterpreter ci) {
		// TODO: Hack zum einfachen Testen ;-)
		basepath = "c:/development/";
		nmsn(ci);
		EvalQuery eq = EvalQuery.GOOD_REMOVE;
		try {
			eval(eq, 500, 5, "" + System.currentTimeMillis() + eq);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void _erb(CommandInterpreter ci) {
		// TODO: Hack zum einfachen Testen ;-)
		basepath = "c:/development/";
		nmsn(ci);
		EvalQuery eq = EvalQuery.BAD_REMOVE;
		try {
			eval(eq, 500, 5, "" + System.currentTimeMillis() + eq);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void _em(CommandInterpreter ci) {
		// TODO: Hack zum einfachen Testen ;-)
		basepath = "c:/development/";
		nmsn(ci);
		EvalQuery eq = EvalQuery.MIG;
		try {
			eval(eq, 120, 5, "" + System.currentTimeMillis() + eq);
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
				eval(eq, 120, 5, "" + System.currentTimeMillis() + eq);
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

	public void setOptimizationSink(OptimizationTestSink sink) {
		synchronized (this) {
			this.sink = sink;
		}
	}

	public List<Measure> e(EvalQuery evalQuery, int seconds) {
		List<Measure> measures = new ArrayList<Measure>(seconds);
		System.out.println(evalQuery + " " + seconds);
		String sep = ";";
		try {

			setOptimizationSink(new OptimizationTestSink(OutputMode.COUNT));

			Collection<Integer> queryIds = this.executor
					.addQuery(
							"SELECT seller.name AS seller, bidder.name AS bidder, auction.itemname AS item, bid.price AS price FROM nexmark:auction2 [SIZE 20 SECONDS ADVANCE 1 TIME] AS auction, nexmark:bid2 [SIZE 20 SECONDS ADVANCE 1 TIME] AS bid, nexmark:person2 [SIZE 20 SECONDS ADVANCE 1 TIME] AS seller, nexmark:person2 [SIZE 20 SECONDS ADVANCE 1 TIME] AS bidder WHERE seller.id=auction.seller AND auction.id=bid.auction AND bid.bidder=bidder.id AND bid.price>260",
							parser(), currentUser, new ParameterDefaultRoot(sink),
							this.trafoConfigParam);
			System.out.println("QueryIDs: " + queryIds);
			IEditablePlan plan = (IEditablePlan) this.executor.getSealedPlan();
			IEditableQuery query = plan.getQuery(queryIds.iterator().next());

			// manipulate: push select to top
			if (evalQuery == EvalQuery.BAD || evalQuery == EvalQuery.MIG
					|| evalQuery == EvalQuery.BAD_REMOVE) {
				_helpMakeQueryBad(query);
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
				synchronized (this) {
					Measure m = new Measure((long) Math
							.floor(((currentTime - startTime) / 1000)), sink
							.getCount(), cputime, memBean.getHeapMemoryUsage()
							.getUsed());
					measures.add(m);
					System.out.println(m.time_elapsed + sep + m.tuples_passed
							+ sep + m.cpu_load + sep + m.memory_usage);
				}

				if (evalQuery == EvalQuery.MIG && i == 30) {
					final IQuery q = query;
					Runnable reopt = new Runnable() {
						@Override
						public void run() {
							q.reoptimize();
							System.out
									.println("------------------------------------> Reopt durch, ggf. Senke anpassen");
							setOptimizationSink((OptimizationTestSink) q
									.getSealedRoot());
						}
					};
					new Thread(reopt).start();
				} else if ((evalQuery == EvalQuery.GOOD_REMOVE || evalQuery == EvalQuery.BAD_REMOVE)
						&& (i == 100 || i == 200 || i == 300 || i == 400)) {
					System.out.println("Removing Query");
					executor.removeQuery(query.getID());
					System.out.println("Removing Query done");
					waitFor(10000);
					// Neu zählen
					sink.reset();
					// setOptimizationSink(new
					// OptimizationTestSink(OutputMode.COUNT));
					queryIds = this.executor
							.addQuery(
									"SELECT seller.name AS seller, bidder.name AS bidder, auction.itemname AS item, bid.price AS price FROM nexmark:auction2 [SIZE 20 SECONDS ADVANCE 1 TIME] AS auction, nexmark:bid2 [SIZE 20 SECONDS ADVANCE 1 TIME] AS bid, nexmark:person2 [SIZE 20 SECONDS ADVANCE 1 TIME] AS seller, nexmark:person2 [SIZE 20 SECONDS ADVANCE 1 TIME] AS bidder WHERE seller.id=auction.seller AND auction.id=bid.auction AND bid.bidder=bidder.id AND bid.price>260",
									parser(), currentUser, new ParameterDefaultRoot(sink),
									this.trafoConfigParam);
					plan = (IEditablePlan) this.executor.getSealedPlan();
					System.out.println("QueryIDs: " + queryIds);
					query = plan.getQuery(queryIds.iterator().next());
					if (evalQuery == EvalQuery.BAD_REMOVE) {
						_helpMakeQueryBad(query);
					}
				}
			}
			System.out
					.println("----------Evaluation End--------------------------------");

			System.out.println(Router.getInstance().getRouterReceiver());

			executor.removeQuery(query.getID());
			System.out.println(Router.getInstance().getRouterReceiver());
			this.executor.stopExecution();

			lsqueries();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return measures;
	}

	private void _helpMakeQueryBad(IEditableQuery query)
			throws OpenFailedException, NoOptimizerLoadedException,
			QueryOptimizationException {
		System.out.println("Rebuilding Query to Bad Query");
		IPhysicalOperator root = query.getRoot();
		System.out.println(root.getName());
		IPhysicalOperator project = ((ISink<?>) root).getSubscribedToSource(0)
				.getTarget();
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
		IPhysicalOperator select = ((ISink<?>) window).getSubscribedToSource(0)
				.getTarget();
		System.out.println(select.getName());
		IPhysicalOperator meta = ((ISink<?>) select).getSubscribedToSource(0)
				.getTarget();
		System.out.println(meta.getName());

		// remove select before join
		PhysicalRestructHelper.removeSubscription(window, select);
		PhysicalRestructHelper.removeSubscription(select, meta);
		PhysicalRestructHelper.appendOperator(window, meta);

		// put select after join
		PhysicalRestructHelper.removeSubscription(project, lastJoin);
		RelationalPredicate predicate = (RelationalPredicate) ((SelectPO<?>) select)
				.getPredicate();
		RelationalPredicate newPredicate = new RelationalPredicate(predicate
				.getExpression());
		newPredicate.init(lastJoin.getOutputSchema(), null);
		SelectPO<?> newSelect = new SelectPO(newPredicate);
		newSelect.setName("New Selection");
		newSelect.setOutputSchema(lastJoin.getOutputSchema().clone());
		PhysicalRestructHelper.appendOperator(newSelect, lastJoin);
		PhysicalRestructHelper.appendOperator(project, newSelect);
		query.initializePhysicalPlan(root);
		this.executor.updateExecutionPlan();
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
			this.executor.addQuery("SELECT * FROM nexmark:auction2", parser(), currentUser,
					new ParameterDefaultRoot(sinkAuction),
					this.trafoConfigParam);
			this.executor.addQuery("SELECT * FROM nexmark:bid2", parser(), currentUser,
					new ParameterDefaultRoot(sinkBid), this.trafoConfigParam);
			this.executor
					.addQuery("SELECT * FROM nexmark:person2", parser(), currentUser,
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

	public void _buffer(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
		if (args != null && args.length > 0) {
			try {
				String bufferName = args[0];
				Set<String> list = this.executor
						.getRegisteredBufferPlacementStrategies();
				if (list.contains(bufferName)) {
					this.executor.getConfiguration().set(
							new SettingBufferPlacementStrategy(bufferName));
					ci.println("Strategy " + bufferName + " set.");
					currentBuffer = bufferName;
					return;
				} else {
					this.executor.getConfiguration().set(
							new SettingBufferPlacementStrategy(null));
					if ("no strategy".equalsIgnoreCase(bufferName)) {
						ci.println("Current strategy removed.");
					} else {
						ci
								.println("Strategy not found. Current strategy removed.");
					}
					return;
				}

			} catch (Exception e) {
				ci.println(e.getMessage());
			}
		} else {
			ci.println("No query argument.");
		}
	}

	public void _lsbuffer(CommandInterpreter ci) {
		Set<String> bufferList = this.executor
				.getRegisteredBufferPlacementStrategies();
		if (bufferList != null) {
			String current = (String) this.executor.getConfiguration().get(
					SettingBufferPlacementStrategy.class).getValue();
			ci.println("Available bufferplacement strategies:");
			if (current == null) {
				System.out.print("no strategy - SELECTED");
			} else {
				System.out.print("no strategy");
			}
			ci.println("");
			for (String iBufferPlacementStrategy : bufferList) {
				System.out.print(iBufferPlacementStrategy);
				if (current != null && iBufferPlacementStrategy.equals(current)) {
					System.out.print(" - SELECTED");
				}
				ci.println("");
			}

		}
	}

	public void _lsschedulingstrategies(CommandInterpreter ci) {
		Set<String> list = this.executor
				.getRegisteredSchedulingStrategyFactories();
		if (list != null) {
			String current = executor.getCurrentSchedulingStrategy();
			ci.println("Available Scheduling strategies:");

			ci.println("");
			for (String iStrategy : list) {
				System.out.print(iStrategy.toString());
				if (current != null && iStrategy.equals(current)) {
					System.out.print(" - SELECTED");
				}
				ci.println("");
			}
			System.out.print("no strategy");
		}
	}

	public void _lsscheduler(CommandInterpreter ci) {
		Set<String> list = this.executor.getRegisteredSchedulerFactories();
		if (list != null) {
			String current = executor.getCurrentScheduler();
			ci.println("Available Schedulers:");

			// if (current == null) {
			// System.out.print(" - SELECTED");
			// }
			ci.println("");
			for (String iStrategy : list) {
				System.out.print(iStrategy.toString());
				if (current != null && iStrategy.equals(current)) {
					System.out.print(" - SELECTED");
				}
				ci.println("");
			}
		}
	}

	public void _scheduler(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
		if (args != null && args.length > 0) {
			currentScheduler = args[0];
			executor.setScheduler(args[0], args[1]);
		} else {
			ci.println("No query argument.");
		}
	}

	public void _lsparser(CommandInterpreter ci) {
		Set<String> parserList = null;
		try {
			parserList = this.executor.getSupportedQueryParser();
			// "Set" Default-Parser
			if (parser == null && parserList.size() > 0) {
				parser = parserList.iterator().next();
			}
		} catch (PlanManagementException e) {
			ci.println(e.getMessage());
		}
		if (parserList != null) {
			ci.println("Available parser:");
			for (String par : parserList) {
				System.out.print(par);

				if (par.equals(this.parser)) {
					System.out.print(" - Selected");
				}
				ci.println("");
			}
		}
	}

	public void _parser(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
		if (args != null && args.length > 0) {
			try {
				if (this.executor.getSupportedQueryParser().contains(args[0])) {
					this.parser = args[0];
					ci.println("New parser: " + args[0]);
				} else {
					ci.println("No parser with this ID.");
				}
			} catch (Exception e) {
				ci.println(e.getMessage());
			}
		} else {
			ci.println("No query argument.");
		}
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
