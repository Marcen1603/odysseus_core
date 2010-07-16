package de.uniol.inf.is.odysseus.planmanagement.executor.console;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.osgi.framework.Bundle;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;
import org.osgi.service.prefs.PreferencesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.DataDictionary;
import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.QueryParseException;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.base.planmanagement.ICompilerListener;
import de.uniol.inf.is.odysseus.base.planmanagement.event.error.ErrorEvent;
import de.uniol.inf.is.odysseus.base.planmanagement.event.error.IErrorEventListener;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.AbstractQueryBuildParameter;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterDefaultRoot;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterInstallMetadataListener;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;
import de.uniol.inf.is.odysseus.base.usermanagement.User;
import de.uniol.inf.is.odysseus.base.wrapper.WrapperPlanFactory;
import de.uniol.inf.is.odysseus.broker.physicaloperator.BrokerWrapperPlanFactory;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.physicaloperator.base.FileSink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planexecution.IPlanExecutionListener;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planexecution.event.AbstractPlanExecutionEvent;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.planmanagement.executor.standardexecutor.SettingBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.planmanagement.optimization.reoptimization.planrules.ReoptimizeTimer;
import de.uniol.inf.is.odysseus.priority.IPriority;
import de.uniol.inf.is.odysseus.transformation.helper.broker.BrokerTransformationHelper;
import de.uniol.inf.is.odysseus.transformation.helper.relational.RelationalTransformationHelper;
import de.uniol.inf.is.odysseus.util.AbstractGraphWalker;
import de.uniol.inf.is.odysseus.util.PrintGraphVisitor;

public class ExecutorConsole implements CommandProvider,
		IPlanExecutionListener, IPlanModificationListener, IErrorEventListener, ICompilerListener {

	private static Logger logger = LoggerFactory.getLogger(ExecutorConsole.class);
	
	private static final String METHOD = "method";

	private static final String ARGUMENTS = "arguments";

	private static final String MACROS_NODE = "/macros";

	private IAdvancedExecutor executor;

	private String parser = null;
	
	private User currentUser = new User("Console");

	/**
	 * This is the bath to files, to read queries from. This path can be set by
	 * command addPath.
	 */
	private String path;

	private static ConsoleFunctions support = new ConsoleFunctions();

	private boolean usePriority = false;

	private boolean useObjectFusionConfig = false;

	private Lock preferencesLock = new ReentrantLock();

	private PreferencesService preferences = null;

	private Boolean isRecordingMacro = false;

	private Map<String, List<Command>> macros = new HashMap<String, List<Command>>();
	private String currentMacro = null;

	private ReoptimizeTimer reoptimizeTimer = null;

	private static class DelegateCommandInterpreter implements
			CommandInterpreter {
		final private CommandInterpreter ci;
		int i = 0;
		final private String[] args;

		public DelegateCommandInterpreter(CommandInterpreter ci, String[] args) {
			this.ci = ci;
			this.args = args;
		}

		@Override
		public Object execute(String cmd) {
			return ci.execute(cmd);
		}

		@Override
		public String nextArgument() {
			if (i < args.length) {
				return this.args[i++];
			}
			return null;
		}

		@Override
		public void print(Object o) {
			ci.print(o);
		}

		@Override
		public void printBundleResource(Bundle bundle, String resource) {
			ci.printBundleResource(bundle, resource);
		}

		@SuppressWarnings("unchecked")
		@Override
		public void printDictionary(Dictionary dic, String title) {
			ci.printDictionary(dic, title);
		}

		@Override
		public void printStackTrace(Throwable t) {
			ci.printStackTrace(t);
		}

		@Override
		public void println() {
			ci.println();
		}

		@Override
		public void println(Object o) {
			ci.println(o);
		}
	}

	private static class Command {

		private static final String DELIMITER = "|||";
		public String name;
		private String[] args;

		public void setArgs(String[] args2) {
			this.args = args2;
		}

		public String getArgsAsString() {
			StringBuilder builder = new StringBuilder();
			for (String arg : args) {
				if (arg.contains(DELIMITER)) {
					System.err.println("fixme: argument may not contain '|||'");
					throw new RuntimeException(
							"fixme: argument may not contain '|||'");
				}
				builder.append(DELIMITER);
				builder.append(arg);
			}
			return builder.toString();
		}

		public void setArgsFromString(String str) {
			String[] tmpArgs = str.split("\\|\\|\\|");
			ArrayList<String> newArgs = new ArrayList<String>();
			for (String tmp : tmpArgs) {
				if (tmp.isEmpty()) {
					continue;
				} else {
					StringBuilder builder = new StringBuilder();
					builder.append(tmp);
					newArgs.add(builder.toString());
				}
			}
			this.args = newArgs.toArray(new String[newArgs.size()]);
		}

		public String[] getArgs() {
			return this.args;
		}
	}

	private String[][] nexmarkQ = new String[][] {
			{
			// Q1
					"SELECT b.auction, DolToEur(b.price) AS euroPrice, b.bidder, b.datetime FROM nexmark:bid [UNBOUNDED] AS b",
					// Q2
					"SELECT auction, price FROM nexmark:bid WHERE auction=7 OR auction=20 OR auction=21 OR auction=59 OR auction=87",
					// Q3
					"SELECT p.name, p.city, p.state, a.id FROM nexmark:auction [UNBOUNDED] AS a, nexmark:person [UNBOUNDED] AS p WHERE a.seller=p.id AND a.category < 150 AND (p.state='Oregon' OR p.state='Idaho' OR p.state='California')",
					// Q4
					"SELECT AVG(q.final) FROM nexmark:category AS c, (SELECT MAX(b.price) AS final, a.category FROM nexmark:auction [UNBOUNDED] AS a, nexmark:bid [UNBOUNDED] AS b  WHERE a.id = b.auction AND b.datetime < a.expires AND  a.expires < Now() GROUP BY a.id, a.category) AS q WHERE q.category = c.id GROUP BY c.id",
					// Q7
					"SELECT b.auction, b.price, b.bidder FROM nexmark:bid [SIZE 1000 ADVANCE 1000 TIME] AS b,(SELECT MAX(price) AS max_price FROM nexmark:bid [SIZE 1000 ADVANCE 1000 TIME]) AS sub WHERE sub.max_price = b.price",
					// Q8
					"SELECT p.id, p.name, a.reserve FROM nexmark:person [SIZE 12 HOURS ADVANCE 1 TIME] AS p, nexmark:auction [SIZE 12 HOURS ADVANCE 1 TIME] AS a WHERE p.id = a.seller" },
			{ // Q1
					"SELECT b.auction, DolToEur(b.price) AS euroPrice, b.bidder, b.datetime FROM nexmark:bid2 [UNBOUNDED] AS b",
					// Q2
					"SELECT auction, price FROM nexmark:bid2 WHERE auction=7 OR auction=20 OR auction=21 OR auction=59 OR auction=87",
					// Q3
					"SELECT p.name, p.city, p.state, a.id FROM nexmark:auction2 [UNBOUNDED] AS a, nexmark:person2 [UNBOUNDED] AS p WHERE a.seller=p.id AND a.category < 150 AND (p.state='Oregon' OR p.state='Idaho' OR p.state='California')",
					// Q4
					"SELECT AVG(q.final) FROM nexmark:category2 AS c, (SELECT MAX(b.price) AS final, a.category FROM nexmark:auction2 [UNBOUNDED] AS a, nexmark:bid2 [UNBOUNDED] AS b  WHERE a.id = b.auction AND b.datetime < a.expires AND  a.expires < Now() GROUP BY a.id, a.category) AS q WHERE q.category = c.id GROUP BY c.id",
					// Q7
					"SELECT b.auction, b.price, b.bidder	FROM nexmark:bid2 [SIZE 1000 ADVANCE 1000 TIME] AS b,(SELECT MAX(price) AS max_price FROM nexmark:bid2 [SIZE 1000 ADVANCE 1000 TIME]) AS sub WHERE sub.max_price = b.price",
					// Q8
					"SELECT p.id, p.name, a.reserve FROM nexmark:person2 [SIZE 12 HOURS ADVANCE 1 TIME] AS p, nexmark:auction2 [SIZE 12 HOURS ADVANCE 1 TIME] AS a WHERE p.id = a.seller" } };

	@SuppressWarnings("unchecked")
	private ParameterTransformationConfiguration trafoConfigParam = new ParameterTransformationConfiguration(
			new TransformationConfiguration(new RelationalTransformationHelper(), "relational", ITimeInterval.class));

	private LinkedList<Command> currentCommands;

	private String outputputFilename;

	private boolean useBrokerConfig = false;

	public void bindExecutor(IAdvancedExecutor executor) {
		logger.debug("executor gebunden");
		
		this.executor = executor;

		this.executor.addErrorEventListener(this);
		this.executor.addPlanExecutionListener(this);
		this.executor.addPlanModificationListener(this);
		this.executor.addCompilerListener(this);
		
		System.out.println(executor.getCompiler());
		if (executor.getCompiler() != null){
			System.out.println("Rewrite Bound : "+executor.getCompiler().isRewriteBound());
			System.out.println("Transformation Bound :"+executor.getCompiler().isTransformationBound());
		}

		// Typically no compiler is loaded here, so the following
		// code will always break

		// try {
		// // Default is CQL
		// for (String p: this.executor.getSupportedQueryParser()){
		// if ("CQL".equalsIgnoreCase(p)){
		// parser = p;
		// }
		// }
		// // Only if CQL-Parser isn't bound, set another Parser
		// if (this.parser == null && executor.getSupportedQueryParser()!=null){
		// this.parser = this.executor.getSupportedQueryParser().iterator()
		// .next();
		// }
		// } catch (PlanManagementException e) {
		// System.out.println("Error setting parser.");
		// e.printStackTrace();
		// }
	}

	public void unbindExecutor(IAdvancedExecutor executor) {
		this.executor = null;
	}

	private String parser() throws Exception {
		if (this.parser != null && this.parser.length() > 0) {
			return parser;
		}

		Iterator<String> parsers = this.executor.getSupportedQueryParser()
				.iterator();
		if (parsers != null && parsers.hasNext()) {
			this.parser = parsers.next();
			return this.parser;
		}
		throw new Exception("No parser found");
	}

	/**
	 * _ExecutorInfo schreibt Informationen �ber die Ausf�hrungsumgebung in
	 * die Konsole. Kann in der OSGi-Konsole verwendet werden.
	 */
	@Help(description = "show internal information about the executor")
	public void _ExecutorInfo(CommandInterpreter ci) {
		System.out.print(this.executor.getInfos());
	}

	@Help(description = "show available parser")
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

	@Help(description = "sets the current parser", parameter = "<parser id>")
	public void _parser(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
		addCommand(args);
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

	@Help(description = "show available buffer placement strategies")
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

	@Help(description = "show available scheduling strategies")
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

	@Help(description = "show available schedulers")
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

	@Help(parameter = "<scheduler id id_strategy>", description = "set scheduler and it's strategy")
	public void _scheduler(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
		addCommand(args);
		if (args != null && args.length > 0) {
			executor.setScheduler(args[0], args[1]);
		} else {
			ci.println("No query argument.");
		}
	}

	@Help(parameter = "<buffer placement strategy id>", description = "set the buffer placement strategy")
	public void _buffer(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
		addCommand(args);
		if (args != null && args.length > 0) {
			try {
				String bufferName = args[0];
				Set<String> list = this.executor
						.getRegisteredBufferPlacementStrategies();
				if (list.contains(bufferName)) {
					this.executor.getConfiguration().set(
							new SettingBufferPlacementStrategy(bufferName));
					ci.println("Strategy " + bufferName + " set.");
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

	public void _man(CommandInterpreter ci) {
		TreeMap<String, Help> methodHelps = new TreeMap<String, Help>();
		// sort relevant methods by name
		for (Method method : this.getClass().getMethods()) {
			String methodName = method.getName();

			if (methodName.startsWith("_")) {
				if (method.isAnnotationPresent(Help.class)) {
					methodHelps.put(methodName.substring(1), method
							.getAnnotation(Help.class));
				}
			}
		}
		for (Map.Entry<String, Help> curHelp : methodHelps.entrySet()) {
			ci.print(curHelp.getKey());
			Help help = curHelp.getValue();
			if (!help.parameter().isEmpty()) {
				ci.print(" ");
				ci.print(help.parameter());
			}
			ci.print(" - ");
			ci.println(help.description());
		}
	}

	@Help(description = "dump all physical operators of the current execution plan")
	public void _dumpe(CommandInterpreter ci) {
		addCommand();
		IExecutionPlan plan = this.executor.getExecutionPlan();

		int i = 1;
//		ci.println("Registered source:");
//		for (IIterableSource<?> isource : plan.getSources()) {
//			ci.println(i++ + ": " + isource.toString() + ", Owner:"
//					+ support.getOwnerIDs(isource.getOwner()));
//		}

		ci.println("");
		i = 1;
		ci.println("Registered PartialPlans:");
		for (IPartialPlan partialPlan : plan.getPartialPlans()) {
			ci.println(i++ + ": ");
			ci.println(partialPlan.toString());
		}
	}

	@Help(description = "<on|off> - Adds or removes punctuation support inside every available PriorityPO.")
	public void _addPunctuations(CommandInterpreter ci) {
		boolean usePunctuations = false;
		String[] args = support.getArgs(ci);
		addCommand(args);
		try {
			if (args != null && args.length >= 1) {

				usePunctuations = toBoolean(args[0]);

				if (usePunctuations) {
					trafoConfigParam.getValue().setOption("usePunctuations",
							true);
				} else {
					if (trafoConfigParam.getValue()
							.getOption("usePunctuations") != null) {
						trafoConfigParam.getValue().setOption(
								"usePunctuations", null);
					}
				}

			}
		} catch (Exception e) {
			ci.println(e.getMessage());
		}
		ci.println("punctuations are "
				+ (usePunctuations ? "activated" : "deactivated"));

	}

	@Help(description = "<on|off> - Adds or removes load shedding support.")
	public void _addLoadShedding(CommandInterpreter ci) {
		boolean useLoadShedding = false;
		String[] args = support.getArgs(ci);
		addCommand(args);
		try {
			if (args != null && args.length >= 1) {

				useLoadShedding = toBoolean(args[0]);

				if (useLoadShedding) {
					trafoConfigParam.getValue().setOption("useLoadShedding",
							true);
				} else {
					if (trafoConfigParam.getValue()
							.getOption("useLoadShedding") != null) {
						trafoConfigParam.getValue().setOption(
								"useLoadShedding", null);
					}
				}

			}
		} catch (Exception e) {
			ci.println(e.getMessage());
		}
		ci.println("Load shedding is "
				+ (useLoadShedding ? "activated" : "deactivated"));

	}

	@Help(description = "<on|off> - Adds or removes optimized post priorsation functionality.")
	public void _addExtendedPostPriorisation(CommandInterpreter ci) {
		boolean useExtPostPriorisation = false;
		String[] args = support.getArgs(ci);
		addCommand(args);
		try {
			if (args != null && args.length >= 1) {

				useExtPostPriorisation = toBoolean(args[0]);

				if (useExtPostPriorisation) {
					trafoConfigParam.getValue().setOption(
							"useExtendedPostPriorisation", true);
				} else {
					if (trafoConfigParam.getValue().getOption(
							"useExtendedPostPriorisation") != null) {
						trafoConfigParam.getValue().setOption(
								"useExtendedPostPriorisation", null);
					}
				}

			}
		} catch (Exception e) {
			ci.println(e.getMessage());
		}
		ci.println("Extended Post Priorisation is "
				+ (useExtPostPriorisation ? "activated" : "deactivated"));

	}

	@SuppressWarnings("unchecked")
	@Help(description = "dump physical plan of all registered roots")
	public void _dumpr(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
		addCommand(args);
		int depth = 0;
		if (args != null && args.length > 0) {
			depth = Integer.valueOf(args[1]);

			if (depth < 1) {
				depth = 1;
			}
		}

		try {
			StringBuffer buff = new StringBuffer();
			ci.println("Physical plan of all roots: ");
			int count = 1;
			for (IPhysicalOperator root : this.executor.getSealedPlan()
					.getRoots()) {
				buff = new StringBuffer();
				if (root.isSink()) {
					support.dumpPlan((ISink) root, depth, buff);
				} else {
					support.dumpPlan((ISource) root, depth, buff);
				}
				ci.println(count++ + ". root:");
				ci.println(buff.toString());
			}
		} catch (Exception e) {
			ci.println(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Help(parameter = "<query id>", description = "dump the physical plan of a query")
	public void _dumpp(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
		addCommand(args);
		if (args != null && args.length > 0) {
			int qnum = Integer.valueOf(args[0]);
			int depth = 0;
			if (args != null && args.length > 1) {
				depth = Integer.valueOf(args[1]);
			}
			if (depth < 1) {
				depth = 1;
			}

			try {
				IQuery query = this.executor.getSealedPlan().getQuery(qnum);
				if (query != null) {
					for(int i = 0; i<query.getRoots().size(); i++){
						IPhysicalOperator curRoot = query.getRoots().get(i);
						StringBuffer buff = new StringBuffer();
						buff.append("Root No: " + i + "\n");
						if (curRoot.isSink()) {
							support.dumpPlan((ISink) curRoot, depth,
									buff);
						} else {
							support.dumpPlan((ISource) curRoot,
									depth, buff);
						}
						ci.println("Physical plan of query: " + qnum);
						ci.println(buff.toString());
					}
				} else {
					ci.println("Query not found.");
				}
			} catch (Exception e) {
				ci.println(e.getMessage());
			}
		} else {
			ci.println("No query argument.");
		}
	}

	@SuppressWarnings("unchecked")
	@Help(parameter = "<query id>", description = "dump meta data of the query with QUERYID (only if root is a sink)")
	public void _meta(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
		addCommand(args);
		if (args != null && args.length > 0) {
			int qnum = Integer.valueOf(args[0]);
			try {
				IQuery query = this.executor.getSealedPlan().getQuery(qnum);
				if (query != null) {
					for(int i = 0; i<query.getRoots().size(); i++){
						IPhysicalOperator curRoot = query.getRoots().get(i);
						if (curRoot.isSink()) {
							System.out.println("Root No: " + i);
							support
									.printPlanMetadata((ISink) curRoot);
	
						} else {
							ci.println("Root is no sink.");
						}
					}
				}

			} catch (Exception e) {
				ci.println(e.getMessage());
			}
		} else {
			ci.println("No query argument.");
		}
	}

	@Help(description = "register NIO nexmark sources and views")
	public void _nmsn(CommandInterpreter ci) {
		_nexmarkSourcesNIO(ci);
	}

	@Help(description = "register NIO nexmark sources and views")
	public void _nexmarkSourcesNIO(CommandInterpreter ci) {
		addCommand();
		String[] q = new String[8];
		q[0] = "CREATE STREAM nexmark:person2 (timestamp LONG,id INTEGER,name STRING,email STRING,creditcard STRING,city STRING,state STRING) CHANNEL localhost : 65440";
		q[4] = "CREATE STREAM nexmark:person2_v (timestamp LONG,id INTEGER,name STRING,email STRING,creditcard STRING,city STRING,state STRING) FROM (SELECT * FROM nexmark:person2 [UNBOUNDED ON timestamp])";
		q[1] = "CREATE STREAM nexmark:bid2 (timestamp LONG,	auction INTEGER, bidder INTEGER, datetime LONG,	price DOUBLE) CHANNEL localhost : 65442";
		q[5] = "CREATE STREAM nexmark:bid2_v (timestamp LONG,	auction INTEGER, bidder INTEGER, datetime LONG,	price DOUBLE) FROM (SELECT * FROM nexmark:bid2 [UNBOUNDED ON timestamp])";
		q[2] = "CREATE STREAM nexmark:auction2 (timestamp LONG,	id INTEGER,	itemname STRING,	description STRING,	initialbid INTEGER,	reserve INTEGER,	expires LONG,	seller INTEGER ,category INTEGER) CHANNEL localhost : 65441";
		q[6] = "CREATE STREAM nexmark:auction2_v (timestamp LONG,	id INTEGER,	itemname STRING,	description STRING,	initialbid INTEGER,	reserve INTEGER,	expires LONG,	seller INTEGER ,category INTEGER) FROM (SELECT * FROM nexmark:auction2 [UNBOUNDED ON timestamp])";
		q[3] = "CREATE STREAM nexmark:category2 (id INTEGER, name STRING, description STRING, parentid INTEGER) CHANNEL localhost : 65443";
		q[7] = "CREATE STREAM nexmark:category2_v (id INTEGER, name STRING, description STRING, parentid INTEGER) FROM (SELECT * FROM nexmark:category2 [UNBOUNDED])";
		for (String s : q) {
			try {
				this.executor.addQuery(s, parser(), currentUser, this.trafoConfigParam);
			} catch (PlanManagementException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		ci.println("Nexmark Sources with NIO added.");
	}

	@Help(description = "register nexmark sources")
	public void _nms(CommandInterpreter ci) {
		_nexmarkSources(ci);
	}

	@Help(description = "register nexmark sources")
	public void _nexmarkSources(CommandInterpreter ci) {
		addCommand();
		String[] q = new String[4];
		q[0] = "CREATE STREAM nexmark:person (timestamp LONG,id INTEGER,name STRING,email STRING,creditcard STRING,city STRING,state STRING) SOCKET localhost : 65430";
		q[1] = "CREATE STREAM nexmark:bid (timestamp LONG,	auction INTEGER, bidder INTEGER, datetime LONG,	price DOUBLE) SOCKET localhost : 65432";
		q[2] = "CREATE STREAM nexmark:auction (timestamp LONG,	id INTEGER,	itemname STRING,	description STRING,	initialbid INTEGER,	reserve INTEGER,	expires LONG,	seller INTEGER ,category INTEGER) SOCKET localhost : 65431";
		q[3] = "CREATE STREAM nexmark:category (id INTEGER, name STRING, description STRING, parentid INTEGER) SOCKET localhost : 65433";
		for (String s : q) {
			try {
				this.executor.addQuery(s, parser(), currentUser, this.trafoConfigParam);
			} catch (PlanManagementException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		ci.println("Nexmark Sources without NIO added.");
	}

	@Help(parameter = "<query id | *> [n]", description = "add nexmark queries (single query via id, or all via '*'). parameter 'n' registers a NIO query.")
	public void _nmq(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
		addCommand(args);
		if (args != null && args.length >= 1) {
			int j = 0;
			if (args.length > 1 && args[1].startsWith("n")) {
				j = 1;
			}
			if ("*".equals(args[0])) {
				for (String q : nexmarkQ[j]) {
					addQuery(q);
				}
			} else {
				addQuery(nexmarkQ[j][Integer.parseInt(args[0])]);
			}
		} else {
			ci.println("usage [0-5]|* [nio]");
		}

	}

	@Help(parameter = "<filename>", description = "Set a filename for result dump. Each result is dumped to this file. Call without parameter to unset.")
	public void _setOutputFilename(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
		addCommand(args);
		if (args.length > 0) {
			outputputFilename = args[0];
			ci.println("Output written to " + outputputFilename);
		} else {
			outputputFilename = "";
			ci.println("No output file set");
		}
	}

	public void _getOutputFilename(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
		addCommand(args);
		if (outputputFilename != null && outputputFilename.length() > 0) {
			ci.println("Output written to " + outputputFilename);
		} else {
			ci.println("No output file set");
		}
	}

	private void addQuery(String q) {
		try {
			if (outputputFilename == null || outputputFilename.length() == 0) {
				this.executor.addQuery(q, parser(), currentUser, new ParameterDefaultRoot(
						new MySink()), this.trafoConfigParam);
			} else {
				this.executor.addQuery(q, parser(), currentUser, new ParameterDefaultRoot(
						new FileSink(outputputFilename)), this.trafoConfigParam);
			}
		} catch (PlanManagementException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Help(parameter = "<on|off>", description = "turns usage of priorities on|off")
	public void _usePriorities(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
		addCommand(args);
		try {
			if (args.length == 1) {
				usePriority = toBoolean(args[0]);
				TransformationConfiguration trafoConfig;
				if (usePriority) {
					trafoConfig = new TransformationConfiguration(new RelationalTransformationHelper(), "relational",
							ITimeInterval.class, IPriority.class);
				} else {
					trafoConfig = new TransformationConfiguration(new RelationalTransformationHelper(), "relational",
							ITimeInterval.class);

				}
				this.trafoConfigParam = new ParameterTransformationConfiguration(
						trafoConfig);
			}
		} catch (IllegalArgumentException e) {
			ci.println(e.getMessage());
		}
		ci.println("priorities are "
				+ (usePriority ? "activated" : "deactivated"));
	}

	@SuppressWarnings("unchecked")
	@Help(parameter = "<on|off>", description = "turn usage of object fusion configuration on|off")
	public void _useObjectFusionConfig(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
		addCommand(args);
		try {
			if (args.length == 1) {
				useObjectFusionConfig = toBoolean(args[0]);
				TransformationConfiguration trafoConfig;
				if (useObjectFusionConfig) {
					trafoConfig = new TransformationConfiguration(
							new BrokerTransformationHelper(),
							"relational",
							ITimeInterval.class.getName(),
							"de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey",
							"de.uniol.inf.is.odysseus.latency.ILatency",
							"de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability",
							"de.uniol.inf.is.odysseus.objecttracking.metadata.IApplicationTime");
				} else {
					trafoConfig = new TransformationConfiguration(
							new RelationalTransformationHelper(),
							"relational",
							ITimeInterval.class);

				}
				this.trafoConfigParam = new ParameterTransformationConfiguration(
						trafoConfig);
			}
		} catch (IllegalArgumentException e) {
			ci.println(e.getMessage());
		}
		ci.println("Object fusion configuration is "
				+ (useObjectFusionConfig ? "activated" : "deactivated"));
	}

	@Help(parameter = "<on|off>", description = "turn usage of broker configuration on|off")
	public void _useBrokerConfig(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
		addCommand(args);
		try {
			if (args.length == 1) {
				useBrokerConfig = toBoolean(args[0]);
				if (useBrokerConfig) {
					this.trafoConfigParam.getValue().setOption(
							"IBrokerInterval", true);
				} else {
					this.trafoConfigParam.getValue().removeOption(
							"IBrokerInterval");
				}
			}
		} catch (IllegalArgumentException e) {
			ci.println(e.getMessage());
		}
		ci.println("Broker configuration is "
				+ (useBrokerConfig ? "activated" : "deactivated"));
	}

	private boolean toBoolean(String string) {
		if (string.equalsIgnoreCase("true")) {
			return true;
		}
		if (string.equalsIgnoreCase("on")) {
			return true;
		}
		if (string.equalsIgnoreCase("1")) {
			return true;
		}
		if (string.equalsIgnoreCase("false")) {
			return false;
		}
		if (string.equalsIgnoreCase("off")) {
			return false;
		}
		if (string.equalsIgnoreCase("0")) {
			return false;
		}
		throw new IllegalArgumentException("can't convert '" + string
				+ "' to boolean value");
	}

	@Help(parameter = "<query string without CREATE>", description = "CREATE Command\n\tExample: CREATE STREAM test ( a INTEGER	) FROM ( ([0,4), 1), ([1,5), 3), ([7,20), 3) )")
	public void _CREATE(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
		StringBuffer q = new StringBuffer("CREATE ");
		for (int i = 0; i < args.length - 1; i++) {
			q.append(args[i]).append(" ");
		}
		try {
			q.append(args[args.length - 1]);
			this.executor.addQuery(q.toString(), parser(), currentUser,
					this.trafoConfigParam);
		} catch (Exception e) {
			ci.println(e.getMessage());
		}
	}

	@Help(parameter = "<query string without SELECT> [<S>|<F> filename|<E>]", description = "add query [with console-output-sink|File output|eclipse output]\n\tExample:SELECT (a * 2) as value FROM test WHERE a > 2 <S>")
	public void _SELECT(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
		StringBuffer q = new StringBuffer("SELECT ");
		for (int i = 0; i < args.length - 2; i++) {
			q.append(args[i]).append(" ");
		}
		try {
			if (args[args.length - 1].toUpperCase().equals("<S>")) {
				q.append(args[args.length - 2]).append(" ");
				this.executor.addQuery(q.toString(), parser(), currentUser,
						new ParameterDefaultRoot(new MySink()),
						this.trafoConfigParam);
			} else if (args[args.length - 2].toUpperCase().equals("<F>")) {
				this.executor.addQuery(q.toString(), parser(), currentUser,
						new ParameterDefaultRoot(new FileSink(
								args[args.length - 1])), this.trafoConfigParam);

			} else if (args[args.length - 1].toUpperCase().equals("<E>")) {
				q.append(args[args.length - 2]).append(" ");
				this.addQueryWithEclipseConsoleOutput(q.toString());
			} else {
				q.append(args[args.length - 2]).append(" ");
				q.append(args[args.length - 1]).append(" ");
				this.executor.addQuery(q.toString(), parser(), currentUser,
						this.trafoConfigParam);
			}
		} catch (Exception e) {
			ci.println(e.getMessage());
		}
	}

	@Help(parameter = "-q <query string> [S|E] -r [true|false] [-m <true>|<false>]", description = "add query [with console-output-sink|eclipse-outputsink] \n"
			+ "[with|without restructuring the query plan, default true] \n"
			+ "[with|without metadata set in physical operators]\n"
			+ "\tExamples:\n\tadd 'CREATE STREAM test ( a INTEGER	) FROM ( ([0,4), 1), ([1,5), 3), ([7,20), 3) )'\n\tadd 'SELECT (a * 2) as value FROM test WHERE a > 2' S")
	public void _add(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
		addCommand(args);
		if (args != null && args.length > 0) {
			try {
				this.delegateAddQueryCmd(args);
			} catch (Exception e) {
				ci.println(e.getMessage());
			}

		} else {
			ci.println("No query argument.");
		}
	}

	@Help(parameter = "-f <filename> [S|E] [useProp] [-r <true>|<false>] [-m <true>|<false>]", description = "add query declared in <filename> [with console-output-sink] [filepath automatically read from user.files] \n" +
			"[with restructure or not] [with|without metadata set in physical operators]")
	public void _addFromFile(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
		addCommand(args);
		
		String filename = null;
		
		for(int i = 0; i<args.length; i++){
			if(args[i].equalsIgnoreCase("useProp")){
				this.path = System.getProperty("user.files");
			}
			
			else if(args[i].equalsIgnoreCase("-f")){
				filename = args[i+1];
				i++;
			}
		}

		if (args != null && args.length > 0) {
			BufferedReader br = null;
			File file = null;
			try {
				file = new File(this.path != null ? this.path + filename
						: filename);
				br = new BufferedReader(new FileReader(file));
			} catch (FileNotFoundException e) {
				ci.println("File not found: " + file.getAbsolutePath());
				return;
			}

			String queries = "";
			try {
				String line = null;
				while ((line = br.readLine()) != null) {
					queries += line + "\n";
				}
			} catch (IOException e) {
				ci.printStackTrace(e);
				return;
			}

			try {
				String[] newArgs = new String[args.length];
				newArgs[0] = "-q";
				newArgs[1] = queries;
				for (int i = 2; i < args.length; i++) {
					newArgs[i] = args[i];
				}

				this.delegateAddQueryCmd(newArgs);
			} catch (Exception e) {
				ci.printStackTrace(e);
			}
		}
	}

	
	@SuppressWarnings("unchecked")
	@Help(parameter = "<filename> [useProp]", description = "Add query declared in <filename> [filepath automatically read from user.files, otherwise in current directory]")
	public void _cyclicQueryFromFile(CommandInterpreter ci){
		String[] args = support.getArgs(ci);
		addCommand(args);
		
		String filename = null;
		
		for(int i = 0; i<args.length; i++){
			if(args[i].equalsIgnoreCase("useProp")){
				this.path = System.getProperty("user.files");
			}
			
			else if(args[i].equalsIgnoreCase("-f")){
				filename = args[i+1];
				i++;
			}
		}
		
		if (args != null && args.length > 0) {
			BufferedReader br = null;
			File file = null;
			try {
				file = new File(this.path != null ? this.path + filename
						: filename);
				br = new BufferedReader(new FileReader(file));
			} catch (FileNotFoundException e) {
				ci.println("File not found: " + file.getAbsolutePath());
				return;
			}

			String queries = "";
			try {
				String line = null;
				while ((line = br.readLine()) != null) {
					queries += line + "\n";
				}
			} catch (IOException e) {
				ci.printStackTrace(e);
				return;
			}
			
			ICompiler compiler = this.executor.getCompiler();
			try {
				List pos = BrokerWrapperPlanFactory.getAllBrokerPOs();
				
				List<IQuery> plans = compiler.translateQuery(queries, parser());
				
				// DEBUG: Print the logical plan.
				PrintGraphVisitor<ILogicalOperator> pv = new PrintGraphVisitor<ILogicalOperator>();
				AbstractGraphWalker walker = new AbstractGraphWalker();
				for(IQuery plan : plans){
					System.out.println("PRINT PARTIAL PLAN: ");
					walker.prefixWalk(plan.getLogicalPlan(), pv);
					System.out.println(pv.getResult());
					pv.clear();
					walker.clearVisited();
					System.out.println("PRINT END.");
				}
				
				// DEBUG:
				System.out.println("ExecutorConsole: trafoConfigHelper: " + this.trafoConfigParam.getValue().getTransformationHelper());
				
				// the last plan is the complete plan
				// so transform this one
				List<IPhysicalOperator> physPlan = compiler.transform(plans.get(plans.size() - 1).getLogicalPlan(), this.trafoConfigParam.getValue());
				
				
				int queryID = this.executor.addQuery(physPlan, currentUser, this.trafoConfigParam);
				this.executor.startQuery(queryID);
				
			} catch (QueryParseException e1) {
				e1.printStackTrace();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * This method is used by _addQuery and _addFromFile
	 * 
	 * @param args
	 *            The arguments that have been passed to the above methods by
	 *            the CommandInterpreter.
	 * @throws Exception
	 * @throws PlanManagementException
	 */
	private void delegateAddQueryCmd(String[] args)
			throws PlanManagementException, Exception {
		
		boolean eclipseConsole = false;
		boolean restructureParamSet = false;
		boolean restructure = false;
		String query = null;
		ArrayList<AbstractQueryBuildParameter> params = new ArrayList<AbstractQueryBuildParameter>();
		
		for(int i = 0; i<args.length; i++){
			if(args[i].equalsIgnoreCase("-q")){
				query = args[i+1];
				i++;
			}
			else if(args[i].equalsIgnoreCase("E")){
				eclipseConsole = true;
			}
			else if(args[i].equalsIgnoreCase("-r")){
				restructureParamSet = true;
				restructure = Boolean.getBoolean(args[i+1]);
				i++;
			}
			else if(args[i].equalsIgnoreCase("S")){
				params.add(new ParameterDefaultRoot(new MySink()));
			}
			else if(args[i].equalsIgnoreCase("-m")){
				boolean withMeta = Boolean.getBoolean(args[i+1]);
				i++;
				if(withMeta){
					params.add(ParameterInstallMetadataListener.TRUE);
				}else{
					params.add(ParameterInstallMetadataListener.FALSE);
				}
			}
		}
		
		params.add(this.trafoConfigParam);
		
		AbstractQueryBuildParameter[] paramsArray = new AbstractQueryBuildParameter[params.size()];
		for(int i = 0; i<params.size(); i++){
			paramsArray[i] = params.get(i);
		}
		
		if(eclipseConsole){
			this.addQueryWithEclipseConsoleOutput(query);
			return;
		}
		if(!eclipseConsole && !restructureParamSet){
			this.executor.addQuery(query, parser(), currentUser, paramsArray);
			return;
		}
		if(!eclipseConsole && restructureParamSet){
			if(restructure){
				this.executor.addQuery(query, parser(), currentUser, paramsArray);
				return;
			}
			else{
				this.executor.addQuery(query, parser(), currentUser, false, null, paramsArray);
				return;
			}
		}
		
//		// a CREATE statement has no arguments
//		// a QUERY statement does have to have arguments
//		if (args.length == 1) {
//			this.executor.addQuery(args[0], parser(), currentUser, this.trafoConfigParam);
//		}
//		// a QUERY statement can have arguments
//		else if (args.length == 2) {
//			// the second argument can be for sink, so 'S'
//			// or it can be for doRestruct, so 'true' or 'false'
//			if (args[1].equalsIgnoreCase("S")) {
//				this.executor.addQuery(args[0], parser(), currentUser,
//						new ParameterDefaultRoot(new MySink()),
//						this.trafoConfigParam);
//			} else if (args[1].equalsIgnoreCase("E")) {
//				this.addQueryWithEclipseConsoleOutput(args[0]);
//			} else if (args[1].equalsIgnoreCase("TRUE")) {
//				this.executor
//						.addQuery(args[0], parser(), currentUser, this.trafoConfigParam);
//			} else if (args[1].equalsIgnoreCase("FALSE")) {
//				this.executor.addQuery(args[0], parser(), currentUser, false, null,
//						this.trafoConfigParam);
//			}
//		} else if (args.length == 3) {
//			if (args[1].toUpperCase().equals("S")) {
//				// the thrid argument is setting the restructuring mode.
//				if (args[2].equalsIgnoreCase("TRUE")
//						|| args[2].equalsIgnoreCase("FALSE")) {
//					this.executor.addQuery(args[0], parser(), currentUser, args[2]
//							.equalsIgnoreCase("TRUE") ? true : false, null,
//							new ParameterDefaultRoot(new MySink()),
//							this.trafoConfigParam);
//				}
//				// the third argument are the rule names, restructuring is set
//				// per default to true
//				else {
//					StringTokenizer tokens = new StringTokenizer(args[2], ",",
//							false);
//					Set<String> ruleNames = new HashSet<String>();
//					while (tokens.hasMoreTokens()) {
//						ruleNames.add(tokens.nextToken());
//					}
//
//					this.executor.addQuery(args[0], parser(), currentUser, true, ruleNames,
//							new ParameterDefaultRoot(new MySink()),
//							this.trafoConfigParam);
//				}
//			} else {
//
//				if (args[1].equalsIgnoreCase("TRUE")
//						|| args[1].equalsIgnoreCase("FALSE")) {
//
//					StringTokenizer tokens = new StringTokenizer(args[2], ",",
//							false);
//					Set<String> ruleNames = new HashSet<String>();
//					while (tokens.hasMoreTokens()) {
//						ruleNames.add(tokens.nextToken());
//					}
//
//					this.executor.addQuery(args[0], parser(), currentUser, args[2]
//							.equalsIgnoreCase("TRUE") ? true : false,
//							ruleNames, this.trafoConfigParam);
//
//				} else {
//					StringTokenizer tokens = new StringTokenizer(args[2], ",",
//							false);
//					Set<String> ruleNames = new HashSet<String>();
//					while (tokens.hasMoreTokens()) {
//						ruleNames.add(tokens.nextToken());
//					}
//
//					this.executor.addQuery(args[0], parser(), currentUser, true, ruleNames,
//							this.trafoConfigParam);
//				}
//			}
//		}
		
		
		
		// the following is not allowed any more
//		} else if (args.length == 4) {
//			if (args[1].equalsIgnoreCase("S")) {
//				// get the rule names
//				StringTokenizer tokens = new StringTokenizer(args[3], ",",
//						false);
//				Set<String> ruleNames = new HashSet<String>();
//				while (tokens.hasMoreTokens()) {
//					ruleNames.add(tokens.nextToken());
//				}
//
//				this.executor.addQuery(args[0], parser(), currentUser, args[2]
//						.equalsIgnoreCase("TRUE") ? true : false, ruleNames,
//						new ParameterDefaultRoot(new MySink()),
//						this.trafoConfigParam);
//			} else {
//				// get the rule names
//				StringTokenizer tokens = new StringTokenizer(args[3], ",",
//						false);
//				Set<String> ruleNames = new HashSet<String>();
//				while (tokens.hasMoreTokens()) {
//					ruleNames.add(tokens.nextToken());
//				}
//
//				this.executor.addQuery(args[0], parser(), currentUser, args[2]
//						.equalsIgnoreCase("TRUE") ? true : false, ruleNames,
//						this.trafoConfigParam);
//			}
//		}

	}
	
	

	@Help(parameter = "<path>", description = "Sets the path from which to read files."
			+ "E. g. setPath 'C:\\Users\\name\\' and addFromFile 'queries.txt' uses the file C:\\Users\\name\\queries.txt")
	public void _setPath(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
		addCommand(args);

		if (args == null || args.length == 0) {
			ci.println("No path defined.");
			return;
		}

		this.path = args[0].endsWith("/") ? args[0] : args[0] + "/";

		ci.println("Path has been set. New path: " + this.path);
	}

	@Help(description = "show registered queries")
	public void _lsqueries(CommandInterpreter ci) {
		addCommand();
		try {
			System.out
					.println("Current registered queries (ID | STARTED | PARSERID):");
			for (IQuery query : this.executor.getSealedPlan().getQueries()) {
				ci.println(query.getID() + " | " + query.isRunning() + " | "
						+ query.getParserId());
			}
		} catch (PlanManagementException e) {
			e.printStackTrace();
		}
	}

	@Help(description = "show registered sources")
	public void _lssources(CommandInterpreter ci) {
		addCommand();
		System.out.println("Current registered sources");
		for (Entry<String, ILogicalOperator> e : DataDictionary.getInstance()
				.getViews()) {
			ci.println(e.getKey() + " | " + e.getValue());
		}
	}

	@Help(parameter = "<query id>", description = "remove query")
	public void _remove(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
		addCommand(args);
		if (args != null && args.length > 0) {
			int qnum = Integer.valueOf(args[0]);
			try {
				this.executor.removeQuery(qnum);
			} catch (Exception e) {
				ci.println(e.getMessage());
			}
		} else {
			ci.println("No query argument.");
		}
	}

	@Help(parameter = "<macro name>", description = "remove macro")
	public void _removemacro(CommandInterpreter ci) {
		String name = ci.nextArgument();
		if (name == null) {
			_man(ci);
			return;
		}
		if (!this.macros.containsKey(name)) {
			return;
		}

		this.macros.remove(name);

		this.preferencesLock.lock();
		try {
			if (this.preferences == null) {
				return;
			}

			Preferences macrosNode = this.preferences.getSystemPreferences()
					.node(MACROS_NODE);
			if (macrosNode.nodeExists(name)) {
				Preferences macroNode = macrosNode.node(name);
				macroNode.removeNode();
				macrosNode.flush();
			}
			ci.println("macro removed");
		} catch (BackingStoreException e) {
			ci.println("could not remove macro from backing store: "
					+ e.getMessage());
		} finally {
			this.preferencesLock.unlock();
		}
	}

	@Help(description = "remove all macros")
	public void _clearmacros(CommandInterpreter ci) {
		this.macros.clear();

		this.preferencesLock.lock();
		try {
			if (this.preferences == null) {
				return;
			}

			this.preferences.getSystemPreferences().node(MACROS_NODE)
					.removeNode();
			ci.println("macros cleared");
		} catch (BackingStoreException e) {
			ci.println("could not remove macros from backing store: "
					+ e.getMessage());
		} finally {
			this.preferencesLock.unlock();
		}
	}

	@Help(description = "removes all registered sources")
	public void _clearsources(CommandInterpreter ci) {
		WrapperPlanFactory.clearSources();
		DataDictionary.getInstance().sourceTypeMap.clear();
		DataDictionary.getInstance().entityMap.clear();
		DataDictionary.getInstance().clearViews();
	}

	@Help(parameter = "<query id>", description = "stop execution of query")
	public void _qstop(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
		addCommand(args);
		if (args != null && args.length > 0) {
			int qnum = Integer.valueOf(args[0]);
			try {
				this.executor.stopQuery(qnum);
			} catch (Exception e) {
				ci.println(e.getMessage());
			}
		} else {
			ci.println("No query argument.");
		}
	}

	@Help(parameter = "<query id>", description = "start execution of query")
	public void _qstart(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
		addCommand(args);
		if (args != null && args.length > 0) {
			int qnum = Integer.valueOf(args[0]);
			try {
				this.executor.startQuery(qnum);
			} catch (Exception e) {
				ci.println(e.getMessage());
			}
		} else {
			ci.println("No query argument.");
		}
	}

	@Help(description = "start scheduler")
	public void _schedule(CommandInterpreter ci) {
		addCommand();
		try {
			this.executor.startExecution();
		} catch (PlanManagementException e) {
			ci.println(e.getMessage());
			ci.printStackTrace(e);
		}
	}

	@Help(description = "stop scheduler")
	public void _stopschedule(CommandInterpreter ci) {
		addCommand();
		try {
			this.executor.stopExecution();
		} catch (PlanManagementException e) {
			ci.println(e.getMessage());
			ci.printStackTrace(e);
		}
	}

	@Help(description = "show all available macros")
	public void _lsmacros(CommandInterpreter ci) {
		ci.println("Macros:");
		for (String name : macros.keySet()) {
			ci.print("\t");
			ci.println(name);
		}
	}

	@Help(parameter = "<macro name>", description = "show macro content")
	public void _macro(CommandInterpreter ci) {
		String macroName = ci.nextArgument();
		if (macroName == null) {
			_man(ci);
			return;
		}

		List<Command> macro = this.macros.get(macroName);
		if (macro == null) {
			ci.println("No such macro: " + macroName);
			return;
		}

		ci.println("Macro " + macroName);
		for (Command cmd : macro) {
			ci.print("\t");
			ci.print(cmd.name.substring(1));
			for (String arg : cmd.getArgs()) {
				ci.print(" ");
				boolean containsSpace = arg.contains(" ");
				if (containsSpace) {
					ci.print("'");
				}
				ci.print(arg);
				if (containsSpace) {
					ci.print("'");
				}
			}
			ci.println();
		}
	}

	private void addCommand(String... args) {
		synchronized (isRecordingMacro) {
			if (!isRecordingMacro) {
				return;
			}
			Command command = new Command();
			StackTraceElement trace = Thread.currentThread().getStackTrace()[2];
			command.name = trace.getMethodName();
			args = args == null ? new String[0] : args;
			command.setArgs(args);
			this.currentCommands.add(command);
		}
	}

	@Help(parameter = "<macro name>", description = "execute macro")
	public void _runmacro(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
		if (args.length != 1) {
			_man(ci);
			return;
		}
		addCommand(args);
		String name = args[0];

		List<Command> macro = this.macros.get(name);
		if (macro == null) {
			ci.println("no such macro: " + name);
			return;
		}
		ci.println("--- running macro: " + name + " ---");
		try {
			for (Command command : macro) {

				Method m = this.getClass().getMethod(command.name,
						CommandInterpreter.class);

				CommandInterpreter delegateCi = new DelegateCommandInterpreter(
						ci, command.getArgs());

				m.invoke(this, delegateCi);
			}
			ci.println("--- macro done ---");
		} catch (Exception e) {
			ci.println("error during execution of macro");
			ci.printStackTrace(e);
		}
	}

	@Help(parameter = "<file name> [useProp]", description = "runs commands contained in a file.\n"
			+ "The filename can be specified in an absolute manner or, together with \"setPath\" command in a relative manner.\n"
			+ "If useProp option is set, then the path to the file will be automatically read from system property user.files.")
	public void _runFromFile(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);

		addCommand(args);

		if (args.length == 2 && args[1].equalsIgnoreCase("useProp")) {
			this.path = System.getProperty("user.files");
		}

		BufferedReader br = null;
		File file = null;
		try {
			file = new File(this.path != null ? this.path + args[0] : args[0]);
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			ci.println("File not found: " + file.getAbsolutePath());
			return;
		}

		CommandInterpreter delegateCi;
		try {
			ci.println("--- running macro from file: " + file.getAbsolutePath()
					+ " ---");
			String line = null;
			while ((line = br.readLine()) != null) {
				String commandString = line;
				System.out.println("Command: " + commandString);

				StringTokenizer tokens = new StringTokenizer(commandString,
						" \n\t'", true);
				ArrayList<String> tokenList = new ArrayList<String>();
				while (tokens.hasMoreTokens()) {
					String token = tokens.nextToken();
					// this is for complex tokens that are in ' '
					if (token.equals("'")) {
						boolean isEnd = false;
						String complexArgument = "";
						while (!isEnd) {
							String innerToken = tokens.nextToken();
							if (!innerToken.equals("'")) {
								complexArgument += innerToken;
							} else {
								isEnd = true;
							}
						}
						tokenList.add(complexArgument);
					} else if (!token.equals(" ") && !token.equals("\n")
							&& !token.equals("\t")) {
						tokenList.add(token);
					}
				}

				String[] commandAndArgs = new String[tokenList.size()];
				for (int i = 0; i < tokenList.size(); i++) {
					commandAndArgs[i] = tokenList.get(i);
				}

				String[] currentArgs = new String[commandAndArgs.length - 1];
				System.arraycopy(commandAndArgs, 1, currentArgs, 0,
						commandAndArgs.length - 1);
				Command command = new Command();
				command.name = "_" + commandAndArgs[0];
				command.setArgs(currentArgs);

				Method m = this.getClass().getMethod(command.name,
						CommandInterpreter.class);

				delegateCi = new DelegateCommandInterpreter(ci, command
						.getArgs());

				m.invoke(this, delegateCi);

			}

			ci.println("--- macro from file " + file.getAbsolutePath()
					+ " done ---");
		} catch (IOException e) {
			ci.printStackTrace(e);
			return;
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.getTargetException().printStackTrace();
		}

	}

	@Help(parameter = "<macro name>", description = "begin macro recording")
	public void _startrecord(CommandInterpreter ci) {
		synchronized (isRecordingMacro) {
			if (isRecordingMacro) {
				ci
						.println("you are already recording macro. nested recording is not possible.");
				return;
			}
			String macroName = ci.nextArgument();
			if (macroName == null) {
				_man(ci);
				return;
			}

			if (this.macros.containsKey(macroName)) {
				ci.println("a macro already exists under the name '"
						+ macroName
						+ "'. you have to remove it first (removeMacro "
						+ macroName + ")");
				return;
			}
			this.currentCommands = new LinkedList<Command>();
			this.currentMacro = macroName;
			this.macros.put(macroName, this.currentCommands);
			isRecordingMacro = true;
			ci.println("macro recording started");
		}
	}

	@Help(description = "finish recording of macro")
	public void _stoprecord(CommandInterpreter ci) {
		synchronized (isRecordingMacro) {
			if (!isRecordingMacro) {
				return;
			}
			if (this.currentCommands.isEmpty()) {
				ci
						.println("macro recording stopped - emtpy macro isn't stored.");
				return;
			}
			this.preferencesLock.lock();
			try {
				if (this.preferences == null) {
					ci
							.println("macro cannot be persisted. it is still available during this session.");
					return;
				}

				Preferences macrosNode = this.preferences
						.getSystemPreferences().node(MACROS_NODE);
				Preferences currentMacroNode = macrosNode.node(currentMacro);
				Integer i = 0;
				for (Command command : this.currentCommands) {
					Preferences commandNode = currentMacroNode.node(i
							.toString());
					commandNode.put(METHOD, command.name);
					commandNode.put(ARGUMENTS, command.getArgsAsString());
					++i;
				}
				try {
					macrosNode.flush();
				} catch (BackingStoreException e) {
					e.printStackTrace();
					return;
				}

			} finally {
				this.preferencesLock.unlock();
				this.currentCommands = null;
				this.currentMacro = null;
				isRecordingMacro = false;
				ci.print("macro recording stopped");
			}
		}
	}

	@Override
	public String getHelp() {

		String info = "\n---Odysseus Executor Console---\n";
		info += "\tman - show all executor commands\n";
		return info;
	}

	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
	}

	@Override
	public void planExecutionEvent(AbstractPlanExecutionEvent<?> eventArgs) {
	}

	@Override
	public void sendErrorEvent(ErrorEvent eventArgs) {
	}

	public void bindPreferences(PreferencesService preferences) {
		preferencesLock.lock();
		try {
			this.preferences = preferences;
			restoreMacros();
		} finally {
			preferencesLock.unlock();
		}
	}

	private void restoreMacros() {
		Preferences macrosNode = this.preferences.getSystemPreferences().node(
				MACROS_NODE);
		try {
			for (String macroName : macrosNode.childrenNames()) {
				Preferences macroNode = macrosNode.node(macroName);
				String[] commands = macroNode.childrenNames();
				List<Command> commandList = new ArrayList<Command>(
						commands.length);
				// command names could be unsorted, so create names
				// by counting
				for (int i = 0; i < commands.length; ++i) {
					Preferences commandNode = macroNode.node(String.valueOf(i));
					Command command = new Command();
					command.name = commandNode.get(METHOD, null);
					command.setArgsFromString(commandNode.get(ARGUMENTS, null));
					commandList.add(command);
				}
				this.macros.put(macroName, commandList);
			}
		} catch (Exception e) {
			System.err.println("could not load macros: " + e.getMessage());
			this.preferences = null;
		}
	}

	public void unbindPreferences(PreferencesService preferences) {
		preferencesLock.lock();
		try {
			this.preferences = null;
		} finally {
			preferencesLock.unlock();
		}
	}

	private void addQueryWithEclipseConsoleOutput(String query) {
		try {

			Class<?> eclipseConsoleSink = Class
					.forName("de.uniol.inf.is.odysseus.broker.console.client.EclipseConsoleSink");
			Object ecs = eclipseConsoleSink.newInstance();
			IPhysicalOperator ecSink = (IPhysicalOperator) ecs;

			this.executor.addQuery(query, parser(), currentUser, new ParameterDefaultRoot(
					ecSink), this.trafoConfigParam);
		} catch (ClassNotFoundException e) {
			System.err.println("Eclipse Console Plugin is missing!");
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}

	@Help(parameter = "<period>", description = "activate timebased reoptimization of the execution plan every <period> milliseconds")
	public void _startreoptimizetimer(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
		if (args.length != 1) {
			_man(ci);
			return;
		}
		addCommand(args);
		Long period;
		try {
			period = Long.parseLong(args[0]);
		} catch (NumberFormatException e1) {
			ci.println("Period could not be parsed. Use an integer.");
			return;
		}
		if (period <= 0) {
			ci.println("Period should be positive.");
			return;
		}

		try {
			if (this.reoptimizeTimer != null) {
				this.executor.getSealedPlan().removeReoptimzeRule(
						this.reoptimizeTimer);
				ci.println("Old ReoptimizeTimer removed.");
			}
			this.reoptimizeTimer = new ReoptimizeTimer(period);
			this.executor.getSealedPlan()
					.addReoptimzeRule(this.reoptimizeTimer);
			ci
					.println("ReoptimizeTimer with " + period
							+ " ms period started.");
		} catch (PlanManagementException e) {
			System.err.println(e.getMessage());
		}
	}

	@Help(description = "deactivate timebased reoptimization of the execution plan")
	public void _stopreoptimizetimer(CommandInterpreter ci) {
		addCommand();

		if (this.reoptimizeTimer == null) {
			ci.println("There is no active ReoptimizeTimer.");
			return;
		}
		try {
			this.executor.getSealedPlan().removeReoptimzeRule(
					this.reoptimizeTimer);
			this.reoptimizeTimer = null;
			ci.println("ReoptimizeTimer removed.");
		} catch (PlanManagementException e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void parserBound(String parserID) {
		System.out.println("Parser "+parserID+" bound");
	}

	@Override
	public void rewriteBound() {
		System.out.println("Rewrite bound");
	}

	@Override
	public void transformationBound() {
		System.out.println("Transformation bound");
	}

}
