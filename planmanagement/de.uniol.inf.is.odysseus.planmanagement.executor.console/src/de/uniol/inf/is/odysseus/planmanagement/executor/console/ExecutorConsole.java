package de.uniol.inf.is.odysseus.planmanagement.executor.console;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.osgi.framework.Bundle;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;
import org.osgi.service.prefs.PreferencesService;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.planmanagement.configuration.AppEnv;
import de.uniol.inf.is.odysseus.base.planmanagement.event.error.ErrorEvent;
import de.uniol.inf.is.odysseus.base.planmanagement.event.error.IErrorEventListener;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterDefaultRoot;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.physicaloperator.base.IIterableSource;
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
import de.uniol.inf.is.odysseus.priority.IPriority;
import de.uniol.inf.is.odysseus.viewer.ViewerStarter;
import de.uniol.inf.is.odysseus.viewer.ViewerStarterConfiguration;
import de.uniol.inf.is.odysseus.viewer.model.create.OdysseusModelProviderSink;

public class ExecutorConsole implements CommandProvider,
		IPlanExecutionListener, IPlanModificationListener, IErrorEventListener {

	private static final String METHOD = "method";

	private static final String ARGUMENTS = "arguments";

	private static final String MACROS_NODE = "/macros";

	private IAdvancedExecutor executor;

	private String parser;

	private static ConsoleFunctions support = new ConsoleFunctions();

	private ViewerStarter wnd;

	private boolean usePriority = false;

	private Lock preferencesLock = new ReentrantLock();

	private PreferencesService preferences = null;

	private Boolean isRecordingMacro = false;

	private Map<String, List<Command>> macros = new HashMap<String, List<Command>>();
	private String currentMacro = null;

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
			String[] tmpArgs = str.split(DELIMITER);
			ArrayList<String> newArgs = new ArrayList<String>();
			for (String tmp : tmpArgs) {
				if (tmp.isEmpty()) {
					continue;
				} else {
					StringBuilder builder = new StringBuilder();
					boolean containsSpace = tmp.contains(" ");
					if (containsSpace) {
						builder.append('\'');
					}
					builder.append(tmp);
					if (containsSpace) {
						builder.append('\'');
					}
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
					"SELECT b.auction, DolToEur(b.price) AS euroPrice, b.bidder, b.datetime FROM nexmark:bid UNBOUNDED AS b",
					// Q2
					"SELECT auction, price FROM nexmark:bid WHERE auction=7 OR auction=20 OR auction=21 OR auction=59 OR auction=87",
					// Q3
					"SELECT p.name, p.city, p.state, a.id FROM nexmark:auction UNBOUNDED AS a, nexmark:person UNBOUNDED AS p WHERE a.seller=p.id AND a.category < 150 AND (p.state='Oregon' OR p.state='Idaho' OR p.state='California')",
					// Q4
					"SELECT AVG(q.final) FROM nexmark:category AS c, (SELECT MAX(b.price) AS final, a.category FROM nexmark:auction UNBOUNDED AS a, nexmark:bid UNBOUNDED AS b  WHERE a.id = b.auction AND b.datetime < a.expires AND  a.expires < Now() GROUP BY a.id, a.category) AS q WHERE q.category = c.id GROUP BY c.id",
					// Q7
					"SELECT b.auction, b.price, b.bidder FROM nexmark:bid RANGE 1000 SLIDE 1000 AS b,(SELECT MAX(price) AS max_price FROM nexmark:bid RANGE 1000 SLIDE 1000) AS sub WHERE sub.max_price = b.price",
					// Q8
					"SELECT p.id, p.name, a.reserve FROM nexmark:person RANGE 43200000 AS p, nexmark:auction RANGE 43200000 AS a WHERE p.id = a.seller" },
			{ // Q1
					"SELECT b.auction, DolToEur(b.price) AS euroPrice, b.bidder, b.datetime FROM nexmark:bid2 UNBOUNDED AS b",
					// Q2
					"SELECT auction, price FROM nexmark:bid2 WHERE auction=7 OR auction=20 OR auction=21 OR auction=59 OR auction=87",
					// Q3
					"SELECT p.name, p.city, p.state, a.id FROM nexmark:auction2 UNBOUNDED AS a, nexmark:person2 UNBOUNDED AS p WHERE a.seller=p.id AND a.category < 150 AND (p.state='Oregon' OR p.state='Idaho' OR p.state='California')",
					// Q4
					"SELECT AVG(q.final) FROM nexmark:category2 AS c, (SELECT MAX(b.price) AS final, a.category FROM nexmark:auction2 UNBOUNDED AS a, nexmark:bid2 UNBOUNDED AS b  WHERE a.id = b.auction AND b.datetime < a.expires AND  a.expires < Now() GROUP BY a.id, a.category) AS q WHERE q.category = c.id GROUP BY c.id",
					// Q7
					"SELECT b.auction, b.price, b.bidder	FROM nexmark:bid2 RANGE 1000 SLIDE 1000 AS b,(SELECT MAX(price) AS max_price FROM nexmark:bid2 RANGE 1000 SLIDE 1000) AS sub WHERE sub.max_price = b.price",
					// Q8
					"SELECT p.id, p.name, a.reserve FROM nexmark:person2 RANGE 43200000 AS p, nexmark:auction2 RANGE 43200000 AS a WHERE p.id = a.seller" } };

	@SuppressWarnings("unchecked")
	private ParameterTransformationConfiguration trafoConfigParam = new ParameterTransformationConfiguration(
			new TransformationConfiguration("relational", ITimeInterval.class));

	private LinkedList<Command> currentCommands;

	public void bindExecutor(IAdvancedExecutor executor) {
		System.out.println("executor gebunden");
		this.executor = executor;

		this.executor.addErrorEventListener(this);
		this.executor.addPlanExecutionListener(this);
		this.executor.addPlanModificationListener(this);

		try {
			this.parser = this.executor.getSupportedQueryParser().iterator()
					.next();
		} catch (PlanManagementException e) {
			System.out.println("Error setting parser.");
		}
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

	@Help(description = "show available parser")
	public void _lsparser(CommandInterpreter ci) {
		Set<String> parserList = null;
		try {
			parserList = this.executor.getSupportedQueryParser();
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

	@Help(parameter = "<scheduler id>", description = "set scheduler")
	public void _scheduler(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
		addCommand(args);
		if (args != null && args.length > 0) {
			executor.setScheduler(args[0], args[1]);
		} else {
			ci.println("No query argument.");
		}
	}

	@Help(description = "show query viewer")
	public void _viewer(CommandInterpreter ci) {
		System.out.println("startviewer");
		addCommand(support.getArgs(ci));
		try {
			ViewerStarterConfiguration cfg = new ViewerStarterConfiguration();
			// cfg.useOGL = viewerOGL;
			wnd = new ViewerStarter(null, cfg);
			Thread thread = new Thread(wnd, "ViewerThread");
			thread.start();
		} catch (Throwable e) {
			e.printStackTrace();
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
		ci.println("Registered source:");
		for (IIterableSource<?> isource : plan.getSources()) {
			ci.println(i++ + ": " + isource.toString() + ", Owner:"
					+ support.getOwnerIDs(isource.getOwner()));
		}

		ci.println("");
		i = 1;
		ci.println("Registered PartialPlans:");
		for (IPartialPlan partialPlan : plan.getPartialPlans()) {
			ci.println(i++ + ": ");
			ci.println(partialPlan.toString());
		}
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
					StringBuffer buff = new StringBuffer();
					if (query.getSealedRoot().isSink()) {
						support.dumpPlan((ISink) query.getSealedRoot(), depth,
								buff);
					} else {
						support.dumpPlan((ISource) query.getSealedRoot(),
								depth, buff);
					}
					ci.println("Physical plan of query: " + qnum);
					ci.println(buff.toString());
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
					if (query.getSealedRoot().isSink()) {
						support
								.printPlanMetadata((ISink) query
										.getSealedRoot());

					} else {
						ci.println("Root is no sink.");
					}
				}

			} catch (Exception e) {
				ci.println(e.getMessage());
			}
		} else {
			ci.println("No query argument.");
		}
	}

	@Help(description = "register NIO nexmark sources")
	public void _nmsN(CommandInterpreter ci) {
		_nexmarkSourcesNIO(ci);
	}

	@Help(description = "register NIO nexmark sources")
	public void _nmsn(CommandInterpreter ci) {
		_nexmarkSourcesNIO(ci);
	}

	@Help(description = "register NIO nexmark sources")
	public void _nexmarkSourcesNIO(CommandInterpreter ci) {
		addCommand();
		String[] q = new String[4];
		q[0] = "CREATE STREAM nexmark:person2 (timestamp LONG,id INTEGER,name STRING,email STRING,creditcard STRING,city STRING,state STRING) CHANNEL localhost : 65440";
		q[1] = "CREATE STREAM nexmark:bid2 (timestamp LONG,	auction INTEGER, bidder INTEGER, datetime LONG,	price DOUBLE) CHANNEL localhost : 65442";
		q[2] = "CREATE STREAM nexmark:auction2 (timestamp LONG,	id INTEGER,	itemname STRING,	description STRING,	initialbid INTEGER,	reserve INTEGER,	expires LONG,	seller INTEGER ,category INTEGER) CHANNEL localhost : 65441";
		q[3] = "CREATE STREAM nexmark:category2 (id INTEGER, name STRING, description STRING, parentid INTEGER) CHANNEL localhost : 65443";
		for (String s : q) {
			try {
				this.executor.addQuery(s, parser());
			} catch (PlanManagementException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
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
		q[0] = "CREATE STREAM nexmark:person (timestamp LONG,id INTEGER,name STRING,email STRING,creditcard STRING,city STRING,state STRING) CHANNEL localhost : 65430";
		q[1] = "CREATE STREAM nexmark:bid (timestamp LONG,	auction INTEGER, bidder INTEGER, datetime LONG,	price DOUBLE) CHANNEL localhost : 65432";
		q[2] = "CREATE STREAM nexmark:auction (timestamp LONG,	id INTEGER,	itemname STRING,	description STRING,	initialbid INTEGER,	reserve INTEGER,	expires LONG,	seller INTEGER ,category INTEGER) CHANNEL localhost : 65431";
		q[3] = "CREATE STREAM nexmark:category (id INTEGER, name STRING, description STRING, parentid INTEGER) CHANNEL localhost : 65433";
		for (String s : q) {
			try {
				this.executor.addQuery(s, parser());
			} catch (PlanManagementException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
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
			// TODO remove wenn viewer auf events reagiert
			blah();
		} else {
			ci.println("usage [0-5]|* [nio]");
		}

	}

	private void addQuery(String q) {
		try {
			this.executor.addQuery(q, parser(), new ParameterDefaultRoot(
					new MySink()));
		} catch (PlanManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
					trafoConfig = new TransformationConfiguration("relational",
							ITimeInterval.class, IPriority.class);
				} else {
					trafoConfig = new TransformationConfiguration("relational",
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

	// .println("add QUERYSTRING [S] - add query [with console-output-sink]");
	// ci.println("Examples:");
	// ci
	// .println("add 'CREATE STREAM test ( a INTEGER	) FROM ( ([0,4), 1), ([1,5), 3), ([7,20), 3) )'");
	// ci.println("add 'SELECT (a * 2) as value FROM test WHERE a > 2' S");
	// ci.println("");
	@Help(parameter = "<query string> [S]", description = "add query [with console-output-sink]\n\tExamples:\n\tadd 'CREATE STREAM test ( a INTEGER	) FROM ( ([0,4), 1), ([1,5), 3), ([7,20), 3) )'\n\tadd 'SELECT (a * 2) as value FROM test WHERE a > 2' S")
	public void _add(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
		addCommand(args);
		if (args != null && args.length > 0) {
			try {
				if (args.length > 1 && args[1].toUpperCase().equals("S")) {
					this.executor.addQuery(args[0], parser(),
							new ParameterDefaultRoot(new MySink()),
							this.trafoConfigParam);
				} else {
					this.executor.addQuery(args[0], parser(),
							this.trafoConfigParam);
				}
			} catch (Exception e) {
				ci.println(e.getMessage());
			}

		} else {
			ci.println("No query argument.");
		}
		// TODO: DAS HIER IST NUR EIN HACK!!
		blah();
	}

	@Help(description = "show registered queries")
	public void _lsqueries(CommandInterpreter ci) {
		addCommand();
		try {
			System.out
					.println("Current registered queries (ID | STARTED | PARSERID):");
			for (IQuery query : this.executor.getSealedPlan().getQueries()) {
				ci.println(query.getID() + " | " + query.isStarted());
			}
		} catch (PlanManagementException e) {
			e.printStackTrace();
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
				ci.print(arg);
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
		String info = AppEnv.LINE_SEPARATOR
				+ "----------------------------------";
		info += "Executor Console";
		info += "man - show all executor commands";
		return info;
	}

	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		System.out.println("Modification Event: " + eventArgs.getID());
	}

	@Override
	public void planExecutionEvent(AbstractPlanExecutionEvent<?> eventArgs) {
		System.out.println("Execution Event: " + eventArgs.getID());
	}

	@Override
	public void sendErrorEvent(ErrorEvent eventArgs) {
		System.out.println("Error Event: " + eventArgs.getMessage());
	}

	public void blah() {
		if (wnd != null) {
			ArrayList<IPhysicalOperator> queries = null;
			try {
				queries = this.executor.getSealedPlan().getRoots();
			} catch (PlanManagementException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (IPhysicalOperator query : queries) {
				if (query == null) {
					continue;
				}
				if (query.isSink()) {
					OdysseusModelProviderSink mp = new OdysseusModelProviderSink(
							(ISink<?>) query);
					wnd.setModelProvider(mp);
				}
			}
		}
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

}
