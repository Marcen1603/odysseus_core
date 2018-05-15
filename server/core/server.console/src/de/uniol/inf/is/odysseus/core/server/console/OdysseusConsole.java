package de.uniol.inf.is.odysseus.core.server.console;

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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.osgi.framework.Bundle;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;
import org.osgi.service.prefs.PreferencesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.bugreport.BugreportSender;
import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.planmanagement.ViewInformation;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.core.server.event.error.ErrorEvent;
import de.uniol.inf.is.odysseus.core.server.event.error.IErrorEventListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.ICompilerListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planexecution.IPlanExecutionListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planexecution.event.AbstractPlanExecutionEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterDoRewrite;
import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.ParameterBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.report.IReport;
import de.uniol.inf.is.odysseus.report.IReportGenerator;
import de.uniol.inf.is.odysseus.updater.FeatureUpdateUtility;

@SuppressWarnings({ "rawtypes" })
public class OdysseusConsole implements CommandProvider, IPlanExecutionListener, IPlanModificationListener,
		IErrorEventListener, ICompilerListener {

	private static Logger logger = LoggerFactory.getLogger(OdysseusConsole.class);

	private static final String METHOD = "method";

	private static final String ARGUMENTS = "arguments";

	private static final String MACROS_NODE = "/macros";

	private IServerExecutor executor;

	private static IReportGenerator reportGenerator;
	
	private String parser = null;

	// TODOO: CREATE SESSION!!
	private ISession currentUser = null;

	/**
	 * This is the bath to files, to read queries from. This path can be set by
	 * command addPath.
	 */
	private String path;

	private static ConsoleFunctions support = new ConsoleFunctions();

	private Lock preferencesLock = new ReentrantLock();

	private PreferencesService preferences = null;

	private Boolean isRecordingMacro = false;

	private Map<String, List<Command>> macros = new HashMap<String, List<Command>>();
	private String currentMacro = null;

	// called by OSGi-DS
	public static void bindReportGenerator(IReportGenerator serv) {
		reportGenerator = serv;
	}

	// called by OSGi-DS
	public static void unbindReportGenerator(IReportGenerator serv) {
		if (reportGenerator == serv) {
			reportGenerator = null;
		}
	}
	
	private static class DelegateCommandInterpreter implements CommandInterpreter {
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
					throw new RuntimeException("fixme: argument may not contain '|||'");
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
				}

				StringBuilder builder = new StringBuilder();
				builder.append(tmp);
				newArgs.add(builder.toString());
			}
			this.args = newArgs.toArray(new String[newArgs.size()]);
		}

		public String[] getArgs() {
			return this.args;
		}
	}

	private ParameterTransformationConfiguration trafoConfigParam = new ParameterTransformationConfiguration(
			new TransformationConfiguration(ITimeInterval.class));

	private LinkedList<Command> currentCommands;

	private String outputputFilename;

	@SuppressWarnings("unused")
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
		throw new IllegalArgumentException("can't convert '" + string + "' to boolean value");
	}

	// -----------------------------------------------------------------------------------------------
	// Executor
	// -----------------------------------------------------------------------------------------------

	@Help(description = "show internal information about the executor")
	public void _ExecutorInfo(CommandInterpreter ci) {
		ci.print(this.executor.getInfos());
	}

	public void bindExecutor(IExecutor executor) {
		logger.trace("executor gebunden");

		this.executor = (IServerExecutor) executor;

		this.executor.addErrorEventListener(this);
		this.executor.addPlanExecutionListener(this);
		this.executor.addPlanModificationListener(this);

		currentUser = SessionManagement.instance.loginSuperUser(null);
	}

	public void unbindExecutor(IExecutor executor) {
		this.executor = null;
	}

	// -----------------------------------------------------------------------------------------------
	// Bounding events
	// -----------------------------------------------------------------------------------------------

	@Override
	public void rewriteBound() {
	}

	@Override
	public void transformationBound() {
	}

	@Override
	public void planGeneratorBound() {
	}

	// -----------------------------------------------------------------------------------------------
	// User Management
	// -----------------------------------------------------------------------------------------------

	@Help(parameter = "<login username password>", description = "Login user with name, password and tenantname.")
	public void _login(CommandInterpreter ci) {
		// TODO: switch user here!
		// String[] args = support.getArgs(ci);
		// try {
		// if (args.length == 3) {
		// ITenant tenant = UserManagementProvider.getTenant(args[2]);
		// currentUser =
		// UserManagementProvider.getSessionmanagement().login(args[0],
		// args[1].getBytes(), tenant);
		// if (currentUser != null) {
		// ci.println("User " + args[0] + " successfully logged in.");
		// } else {
		// ci.println("Error logging in user " + args[0]);
		// }
		// } else {
		// ci.println("Must be username and password");
		// }
		// } catch (Exception e) {
		// ci.println(e.getMessage());
		// }
		currentUser = SessionManagement.instance.loginSuperUser(null);
	}

	@Help(parameter = "<logout>", description = "Logout current user.")
	public void _logout(CommandInterpreter ci) {
		try {
			SessionManagement.instance.logout(currentUser);
			currentUser = null;
			ci.println("Current user logged out ");
		} catch (Exception e) {
			ci.println(e.getMessage());
		}
	}

	// -----------------------------------------------------------------------------------------------
	// Parser
	// -----------------------------------------------------------------------------------------------

	private String parser() throws Exception {
		if (this.parser != null && this.parser.length() > 0) {
			return parser;
		}

		Iterator<String> parsers = this.executor.getSupportedQueryParsers(currentUser).iterator();
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
			parserList = this.executor.getSupportedQueryParsers(currentUser);
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
				ci.print(par);

				if (par.equals(this.parser)) {
					ci.print(" - Selected");
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
				if (this.executor.getSupportedQueryParsers(currentUser).contains(args[0])) {
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

	@Override
	public void parserBound(String parserID) {
	}

	// -----------------------------------------------------------------------------------------------
	// Buffer placement
	// -----------------------------------------------------------------------------------------------

	@Help(description = "show available buffer placement strategies")
	public void _lsbuffer(CommandInterpreter ci) {
		Set<String> bufferList = this.executor.getRegisteredBufferPlacementStrategiesIDs(currentUser);
		if (bufferList != null) {
			ParameterBufferPlacementStrategy p = this.executor.getConfiguration(currentUser)
					.get(ParameterBufferPlacementStrategy.class);
			String current = null;
			if (p != null) {
				current = p.getValue().getName();
			}
			ci.println("Available bufferplacement strategies:");
			if (current == null) {
				ci.print("no strategy - SELECTED");
			} else {
				ci.print("no strategy");
			}
			ci.println("");
			for (String iBufferPlacementStrategy : bufferList) {
				ci.print(iBufferPlacementStrategy);
				if (current != null && iBufferPlacementStrategy.equals(current)) {
					ci.print(" - SELECTED");
				}
				ci.println("");
			}

		}
	}

	@Help(parameter = "<buffer placement strategy id>", description = "set the buffer placement strategy")
	public void _buffer(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
		addCommand(args);
		if (args != null && args.length > 0) {
			try {
				String bufferName = args[0];
				Set<String> list = this.executor.getRegisteredBufferPlacementStrategiesIDs(currentUser);
				if (list.contains(bufferName)) {
					this.executor.getConfiguration(currentUser).set(new ParameterBufferPlacementStrategy(
							executor.getBufferPlacementStrategy(bufferName, currentUser)));
					ci.println("Strategy " + bufferName + " set.");
					return;
				}

				this.executor.getConfiguration(currentUser).set(new ParameterBufferPlacementStrategy());
				if ("no strategy".equalsIgnoreCase(bufferName)) {
					ci.println("Current strategy removed.");
				} else {
					ci.println("Strategy not found. Current strategy removed.");
				}
				return;

			} catch (Exception e) {
				ci.println(e.getMessage());
			}
		} else {
			ci.println("No query argument.");
		}
	}

	// -----------------------------------------------------------------------------------------------
	// Scheduling
	// -----------------------------------------------------------------------------------------------

	@Help(description = "show available scheduling strategies")
	public void _lsschedulingstrategies(CommandInterpreter ci) {
		Set<String> list = this.executor.getRegisteredSchedulingStrategies(currentUser);
		if (list != null) {
			String current = executor.getCurrentSchedulingStrategyID(currentUser);
			ci.println("Available Scheduling strategies:");

			ci.println("");
			for (String iStrategy : list) {
				ci.print(iStrategy.toString());
				if (current != null && iStrategy.equals(current)) {
					ci.print(" - SELECTED");
				}
				ci.println("");
			}
			ci.print("no strategy");
		}
	}

	@Help(description = "show available schedulers")
	public void _lsscheduler(CommandInterpreter ci) {
		Set<String> list = this.executor.getRegisteredSchedulers(currentUser);
		if (list != null) {
			String current = executor.getCurrentSchedulerID(currentUser);
			ci.println("Available Schedulers:");

			// if (current == null) {
			// ci.print(" - SELECTED");
			// }
			ci.println("");
			for (String iStrategy : list) {
				ci.print(iStrategy.toString());
				if (current != null && iStrategy.equals(current)) {
					ci.print(" - SELECTED");
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
			executor.setScheduler(args[0], args[1], currentUser);
		} else {
			ci.println("No query argument.");
		}
	}

	@Help(description = "start scheduler")
	public void _schedule(CommandInterpreter ci) {
		addCommand();
		try {
			this.executor.startExecution(currentUser);
		} catch (PlanManagementException e) {
			ci.println(e.getMessage());
			ci.printStackTrace(e);
		}
	}

	@Help(description = "stop scheduler")
	public void _stopschedule(CommandInterpreter ci) {
		addCommand();
		try {
			this.executor.stopExecution(currentUser);
		} catch (PlanManagementException e) {
			ci.println(e.getMessage());
			ci.printStackTrace(e);
		}
	}

	// -----------------------------------------------------------------------------------------------
	// Help - System
	// -----------------------------------------------------------------------------------------------

	public void _man(CommandInterpreter ci) {
		TreeMap<String, Help> methodHelps = new TreeMap<String, Help>();
		// sort relevant methods by name
		for (Method method : this.getClass().getMethods()) {
			String methodName = method.getName();

			if (methodName.startsWith("_")) {
				if (method.isAnnotationPresent(Help.class)) {
					methodHelps.put(methodName.substring(1), method.getAnnotation(Help.class));
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

	// -----------------------------------------------------------------------------------------------
	// Plan-"Visualization"
	// -----------------------------------------------------------------------------------------------

	@Help(description = "dump all physical operators of the current execution plan")
	public void _dumpe(CommandInterpreter ci) {
		addCommand();
		IExecutionPlan plan = this.executor.getExecutionPlan(currentUser);

		int i = 1;
		// ci.println("Registered source:");
		// for (IIterableSource<?> isource : plan.getSources()) {
		// ci.println(i++ + ": " + isource.toString() + ", Owner:"
		// + support.getOwnerIDs(isource.getOwner()));
		// }

		ci.println("");
		i = 1;
		ci.println("Registered Physical Queries:");
		for (IPhysicalQuery p : plan.getQueries(currentUser)) {
			ci.println(i++ + ": ");
			ci.println(p.toString());
		}
	}

	@Help(description = "dump physical plan of all registered roots")
	public void _dumpr(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
		addCommand(args);
		int depth = 0;
		if (args != null && args.length > 0) {
			depth = Integer.valueOf(args[0]);

			if (depth < 1) {
				depth = 1;
			}
		}

		try {
			StringBuffer buff = new StringBuffer();
			ci.println("Physical plan of all roots: ");
			int count = 1;
			for (IPhysicalOperator root : this.executor.getExecutionPlan(currentUser).getRoots(currentUser)) {
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

	@Help(parameter = "<query id>", description = "dump the physical plan of a query")
	public void _dumpp(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
		addCommand(args);
		if (args != null && args.length > 0) {
			int qnum = Integer.valueOf(args[0]);
			int depth = 0;
			if (args.length > 1) {
				depth = Integer.valueOf(args[1]);
			}
			if (depth < 1) {
				depth = 1;
			}

			try {
				IPhysicalQuery query = this.executor.getExecutionPlan(currentUser).getQueryById(qnum, currentUser);
				if (query != null) {
					for (int i = 0; i < query.getRoots().size(); i++) {
						IPhysicalOperator curRoot = query.getRoots().get(i);
						StringBuffer buff = new StringBuffer();
						buff.append("Root No: " + i + "\n");
						if (curRoot.isSink()) {
							support.dumpPlan((ISink) curRoot, depth, buff);
						} else {
							support.dumpPlan((ISource) curRoot, depth, buff);
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

	@Help(parameter = "<query id>", description = "dump meta data of the query with QUERYID (only if root is a sink)")
	public void _meta(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
		addCommand(args);
		if (args != null && args.length > 0) {
			int qnum = Integer.valueOf(args[0]);
			try {
				IPhysicalQuery query = this.executor.getExecutionPlan(currentUser).getQueryById(qnum, currentUser);
				if (query != null) {
					for (int i = 0; i < query.getRoots().size(); i++) {
						IPhysicalOperator curRoot = query.getRoots().get(i);
						if (curRoot.isSink()) {
							ci.println("Root No: " + i);
							support.printPlanMetadata((ISink) curRoot);

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

	// -----------------------------------------------------------------------------------------------
	// Execute queries
	// -----------------------------------------------------------------------------------------------

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

	@Help(parameter = "-q <query string> [S|E] -r [true|false] [-m <true>|<false>]", description = "add query [with console-output-sink|eclipse-outputsink] \n"
			+ "[with|without restructuring the query plan, default true] \n"
			+ "[with|without metadata set in physical operators]\n")
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

	@Help(parameter = "-f <filename> ", description = "run query from <filename> for current parser (Hint set Parser to OdysseusScript before executing .qry-Files) [with console-output-sink] [filepath automatically read from user.files] \n"
			+ "[with restructure or not] [with|without metadata set in physical operators]")
	public void _runscript(CommandInterpreter ci) {
		_addFromFile(ci);
	}

	@Help(parameter = "-f <filename> [S|E] [useProp] [-r <true>|<false>] [-m <true>|<false>]", description = "add query declared in <filename> for current parser [with console-output-sink] [filepath automatically read from user.files] \n"
			+ "[with restructure or not] [with|without metadata set in physical operators]")
	public void _addFromFile(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
		addCommand(args);

		String filename = getFilename(args);

		if (args.length > 0) {
			String queries = readQuery(ci, filename);
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

	private String readQuery(CommandInterpreter ci, String filename) {
		String queries = "";
		BufferedReader br = null;

		File file = null;
		try {
			file = new File(this.path != null ? this.path + filename : filename);
			br = new BufferedReader(new FileReader(file));
		} catch (IOException e) {
			ci.println("File not found: " + file.getAbsolutePath());
			return "";
		}

		try {
			String line = null;
			while ((line = br.readLine()) != null) {
				queries += line + "\n";
			}
		} catch (IOException e) {
			ci.printStackTrace(e);
			return "";
		}

		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return queries;
	}

	private String getFilename(String[] args) {
		String filename = null;

		for (int i = 0; i < args.length; i++) {
			if (args[i].equalsIgnoreCase("useProp")) {
				this.path = System.getProperty("user.files");
			}

			else if (args[i].equalsIgnoreCase("-f")) {
				filename = args[i + 1];
				i++;
			}
		}
		return filename;
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
	private void delegateAddQueryCmd(String[] args) throws PlanManagementException, Exception {

		boolean eclipseConsole = false;
		boolean restructure = false;
		String query = null;
		final List<IQueryBuildSetting<?>> params = new ArrayList<IQueryBuildSetting<?>>();

		for (int i = 0; i < args.length; i++) {
			if (args[i].equalsIgnoreCase("-q")) {
				query = args[i + 1];
				i++;
			} else if (args[i].equalsIgnoreCase("E")) {
				eclipseConsole = true;
			} else if (args[i].equalsIgnoreCase("-r")) {
				restructure = Boolean.getBoolean(args[i + 1]);
				i++;
				// } else if (args[i].equalsIgnoreCase("S")) {
				// params.add(new ParameterDefaultRoot(new ConsoleSink()));
			} else if (args[i].equalsIgnoreCase("-m")) {
				// boolean withMeta = Boolean.getBoolean(args[i + 1]);
				i++;
			}
		}

		params.add(this.trafoConfigParam);
		params.add(restructure ? ParameterDoRewrite.TRUE : ParameterDoRewrite.FALSE);

		IQueryBuildSetting[] paramsArray = new IQueryBuildSetting[params.size()];
		for (int i = 0; i < params.size(); i++) {
			paramsArray[i] = params.get(i);
		}

		if (eclipseConsole) {
			this.addQueryWithEclipseConsoleOutput(query);
			return;
		}

		this.executor.addQuery(query, parser(), currentUser, Context.empty());
		return;
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
		DateFormat dateFormat = DateFormat.getDateTimeInstance();
		try {
			ci.println("Current registered queries (ID | NAME | STATE | START DATE/TIME | LAST QUERY STATE CHANGE):");
			for (IPhysicalQuery query : this.executor.getExecutionPlan(currentUser).getQueries(currentUser)) {
				ci.println(query.getID() + " | " + query.getName() + " | " + query.getState().name() +
						" | " + dateFormat.format(new Date(query.getQueryStartTS())) +
						" | " + dateFormat.format(new Date(query.getLastQueryStateChangeTS())));
			}
		} catch (PlanManagementException e) {
			e.printStackTrace();
		}
	}

	@Help(description = "show registered sources")
	public void _lssources(CommandInterpreter ci) {
		addCommand();
		ci.println("Current registered sources");
		for (ViewInformation e : this.executor.getStreamsAndViewsInformation(currentUser)) {
			ci.println(e.getName() + " | " + e.getOutputSchema());
		}
	}

	@Help(parameter = "<query id>", description = "remove query")
	public void _remove(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
		addCommand(args);
		if (args != null && args.length > 0) {
			int qnum = Integer.valueOf(args[0]);
			try {
				this.executor.removeQuery(qnum, currentUser);
			} catch (Exception e) {
				ci.println(e.getMessage());
			}
		} else {
			ci.println("No query argument.");
		}
	}

	// @Help(description = "removes all registered sources")
	// public void _clearsources(CommandInterpreter ci) {
	// WrapperPlanFactory.clearSources();
	// DataDictionary.getInstance().clear();
	// }

	// @Help(parameter = "<query id>", description = "stop execution of query")
	// public void _qstop(CommandInterpreter ci) {
	// String[] args = support.getArgs(ci);
	// addCommand(args);
	// if (args != null && args.length > 0) {
	// int qnum = Integer.valueOf(args[0]);
	// try {
	// this.executor.stopQuery(qnum);
	// } catch (Exception e) {
	// ci.println(e.getMessage());
	// }
	// } else {
	// ci.println("No query argument.");
	// }
	// }

	@Help(parameter = "<query id>", description = "start execution of query")
	public void _qstart(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
		addCommand(args);
		if (args != null && args.length > 0) {
			int qnum = Integer.valueOf(args[0]);
			try {
				this.executor.startQuery(qnum, currentUser);
			} catch (Exception e) {
				ci.println(e.getMessage());
			}
		} else {
			ci.println("No query argument.");
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
			ci.println("--- running macro from file: " + file.getAbsolutePath() + " ---");
			String line = null;
			while ((line = br.readLine()) != null) {
				String commandString = line;
				ci.println("Command: " + commandString);

				StringTokenizer tokens = new StringTokenizer(commandString, " \n\t'", true);
				ArrayList<String> tokenList = new ArrayList<String>();
				while (tokens.hasMoreTokens()) {
					String token = tokens.nextToken();
					// this is for complex tokens that are in ' '
					if (token.startsWith("'") || token.startsWith("\"")) {
						String start = token.substring(0, 1);
						boolean isEnd = false;
						String complexArgument = token.substring(1);
						while (!isEnd) {
							String innerToken = tokens.nextToken();
							if (!innerToken.endsWith(start)) {
								complexArgument += innerToken;
							} else {
								complexArgument += innerToken.substring(0, innerToken.length() - 1);
								isEnd = true;
							}
						}
						tokenList.add(complexArgument);
					} else if (!token.equals(" ") && !token.equals("\n") && !token.equals("\t")) {
						tokenList.add(token);
					}
				}

				String[] commandAndArgs = new String[tokenList.size()];
				for (int i = 0; i < tokenList.size(); i++) {
					commandAndArgs[i] = tokenList.get(i);
				}

				String[] currentArgs = new String[commandAndArgs.length - 1];
				System.arraycopy(commandAndArgs, 1, currentArgs, 0, commandAndArgs.length - 1);
				Command command = new Command();
				command.name = "_" + commandAndArgs[0];
				command.setArgs(currentArgs);

				Method m = this.getClass().getMethod(command.name, CommandInterpreter.class);

				delegateCi = new DelegateCommandInterpreter(ci, command.getArgs());

				m.invoke(this, delegateCi);

			}

			ci.println("--- macro from file " + file.getAbsolutePath() + " done ---");
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
		} catch (NegativeArraySizeException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// -----------------------------------------------------------------------------------------------
	// Update
	// -----------------------------------------------------------------------------------------------

	@Help(parameter = "", description = "tries to update odysseus")
	public void _updateFeatures(CommandInterpreter ci) {
		FeatureUpdateUtility.checkForAndInstallUpdates(this.currentUser);
	}

	@Help(parameter = "", description = "checks if there are updates for odysseus")
	public void _updateCheck(CommandInterpreter ci) {
		FeatureUpdateUtility.checkForUpdates(currentUser);
	}

	// -----------------------------------------------------------------------------------------------
	// -----------------------------------------------------------------------------------------------

	@Help(parameter = "", description = "checks if there are updates for odysseus")
	public void _version(CommandInterpreter ci) {
		String version = FeatureUpdateUtility.getVersionNumber(currentUser);
		ci.println(version);
	}

	// -----------------------------------------------------------------------------------------------
	// -----------------------------------------------------------------------------------------------

	@Help(parameter = "", description = "restarts odysseus")
	public void _restart(CommandInterpreter ci) {
		FeatureUpdateUtility.restart(this.currentUser);
	}

	// -----------------------------------------------------------------------------------------------
	// Install
	// -----------------------------------------------------------------------------------------------

	@Help(parameter = "", description = "lists features that can be installed")
	public void _installableFeatures(CommandInterpreter ci) {
		List<IInstallableUnit> units = FeatureUpdateUtility.getInstallableFeatures(this.currentUser);
		ci.println("Following installable were found in the update site:");
		for (IInstallableUnit unit : units) {
			ci.println(" - " + unit.getId());
		}
	}

	@Help(parameter = "", description = "lists current installed bundles")
	public void _installedFeatures(CommandInterpreter ci) {
		List<IInstallableUnit> units = FeatureUpdateUtility.getInstalledFeatures(this.currentUser);
		ci.println("Following features are installed:");
		for (IInstallableUnit unit : units) {
			ci.println(" - " + unit.getId());
		}
	}

	@Help(parameter = "", description = "install feature")
	public void _installFeature(CommandInterpreter ci) {
		String id = ci.nextArgument();
		if (id != null && !id.isEmpty()) {
			FeatureUpdateUtility.installFeature(id, this.currentUser);
		} else {
			ci.println("You have to provide an id of a feature!");
		}
	}

	@Help(parameter = "", description = "change location of current update site")
	public void _setUpdateSite(CommandInterpreter ci) {
		String id = ci.nextArgument();
		if (id != null && !id.isEmpty()) {
			FeatureUpdateUtility.setRepositoryLocation(id, this.currentUser);
		} else {
			ci.println("You have to provide an update site!");
		}
	}

	@Help(parameter = "", description = "reset location of current update site to default location")
	public void _resetUpdateSite(CommandInterpreter ci) {
		FeatureUpdateUtility.clearRepositoryLocation(this.currentUser);
	}

	@Help(parameter = "", description = "uninstall feature")
	public void _uninstallFeature(CommandInterpreter ci) {
		String id = ci.nextArgument();
		if (id != null && !id.isEmpty()) {
			FeatureUpdateUtility.uninstallFeature(id, this.currentUser);
		} else {
			ci.println("You have to provide an id of a feature!");
		}
	}

	// -----------------------------------------------------------------------------------------------
	// -----------------------------------------------------------------------------------------------

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
	public void errorEventOccured(ErrorEvent eventArgs) {
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

			// Class<?> eclipseConsoleSink = Class
			// .forName("de.uniol.inf.is.odysseus.broker.console.client.EclipseConsoleSink");
			// Object ecs = eclipseConsoleSink.newInstance();
			// IPhysicalOperator ecSink = (IPhysicalOperator) ecs;

			this.executor.addQuery(query, parser(), currentUser, Context.empty());
		} catch (ClassNotFoundException e) {
			System.err.println("Eclipse Console Plugin is missing!");
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}

	// -----------------------------------------------------------------------------------------------
	// Makro Stuff
	// -----------------------------------------------------------------------------------------------

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

			Preferences macrosNode = this.preferences.getSystemPreferences().node(MACROS_NODE);
			if (macrosNode.nodeExists(name)) {
				Preferences macroNode = macrosNode.node(name);
				macroNode.removeNode();
				macrosNode.flush();
			}
			ci.println("macro removed");
		} catch (BackingStoreException e) {
			ci.println("could not remove macro from backing store: " + e.getMessage());
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

			this.preferences.getSystemPreferences().node(MACROS_NODE).removeNode();
			ci.println("macros cleared");
		} catch (BackingStoreException e) {
			ci.println("could not remove macros from backing store: " + e.getMessage());
		} finally {
			this.preferencesLock.unlock();
		}
	}

	private void restoreMacros() {
		Preferences macrosNode = this.preferences.getSystemPreferences().node(MACROS_NODE);
		try {
			for (String macroName : macrosNode.childrenNames()) {
				Preferences macroNode = macrosNode.node(macroName);
				String[] commands = macroNode.childrenNames();
				List<Command> commandList = new ArrayList<Command>(commands.length);
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

	/**
	 * Reads a file from the current working directory and executes each line as
	 * if it comes from the console
	 *
	 * @param ci
	 *            the CommandInterpreter of the console
	 */
	@Help(parameter = "<filename> ", description = "Reads a file from the current working or absolute directory and executes each line as if it comes from the console.\n"
			+ "\tskips blank and commented (with //) lines.")
	public void _runfile(CommandInterpreter ci) {
		StringBuilder sb = new StringBuilder();
		String arg = ci.nextArgument();
		while (arg != null) {
			sb.append(arg + " ");
			arg = ci.nextArgument();
		}
		File file = new File(sb.toString().trim());
		logger.debug("--- running macro from file: " + file.getAbsolutePath() + " ---");

		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(file));

			String zeile = null;
			while ((zeile = in.readLine()) != null) {
				zeile = zeile.trim();
				if (!zeile.isEmpty() && (!zeile.startsWith("//"))) {
					logger.debug("Running command: " + zeile);
					ci.execute(zeile);
				}
			}
		} catch (FileNotFoundException e) {
			logger.error("File not found (" + file.getAbsolutePath() + ")");
		} catch (IOException e) {
			logger.error("Error while reading from file");
		}
		logger.debug("--- macro from file " + file.getAbsolutePath() + " done ---");

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

				Method m = this.getClass().getMethod(command.name, CommandInterpreter.class);

				CommandInterpreter delegateCi = new DelegateCommandInterpreter(ci, command.getArgs());

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
				ci.println("you are already recording macro. nested recording is not possible.");
				return;
			}
			String macroName = ci.nextArgument();
			if (macroName == null) {
				_man(ci);
				return;
			}

			if (this.macros.containsKey(macroName)) {
				ci.println("a macro already exists under the name '" + macroName
						+ "'. you have to remove it first (removeMacro " + macroName + ")");
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
				ci.println("macro recording stopped - emtpy macro isn't stored.");
				return;
			}
			this.preferencesLock.lock();
			try {
				if (this.preferences == null) {
					ci.println("macro cannot be persisted. it is still available during this session.");
					return;
				}

				Preferences macrosNode = this.preferences.getSystemPreferences().node(MACROS_NODE);
				Preferences currentMacroNode = macrosNode.node(currentMacro);
				Integer i = 0;
				for (Command command : this.currentCommands) {
					Preferences commandNode = currentMacroNode.node(i.toString());
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

	// ----------------------------------------------------
	// Bug reporting
	// ----------------------------------------------------
	@Help(description = "send bug report")
	public void _sendBugReport(CommandInterpreter ci) {
		if (reportGenerator == null) {
			ci.println("Error sending bug report. No reports found!");
			return;
		}
		IReport report = reportGenerator.generateReport(currentUser);
		Map<String, String> reportMap = report.getReportMap();
		boolean result = false;
		try {
			result = BugreportSender.send(getJiraUser(), getJiraPassword(), getJiraUrl(), "Console Report", "", reportMap);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if (result == true) {
				ci.println("Bug report send successfully");
			}else {
				ci.println("Error sending bug report");
			}

		}
	}
	
	//TODO!
	
	private String getJiraUser() {
		return OdysseusConfiguration.instance.get("BUGREPORT_USER", "odysseus_studio");
	}

	private String getJiraPassword() {
		return OdysseusConfiguration.instance.get("BUGREPORT_PASSWORD", "jhf4hdds673");
	}
	
	private String getJiraUrl() {
		return OdysseusConfiguration.instance.get("BUGREPORT_BASEURL",
				"https://jira.odysseus.informatik.uni-oldenburg.de/");
	}
	
	

	// Removed stuff from priority

	// @Help(description =
	// "<on|off> - Adds or removes punctuation support inside every available
	// PriorityPO.")
	// public void _addPunctuations(CommandInterpreter ci) {
	// boolean usePunctuations = false;
	// String[] args = support.getArgs(ci);
	// addCommand(args);
	// try {
	// if (args != null && args.length >= 1) {
	//
	// usePunctuations = toBoolean(args[0]);
	//
	// if (usePunctuations) {
	// trafoConfigParam.getValue().setOption("usePunctuations",
	// true);
	// } else {
	// if (trafoConfigParam.getValue()
	// .getOption("usePunctuations") != null) {
	// trafoConfigParam.getValue().setOption(
	// "usePunctuations", null);
	// }
	// }
	//
	// }
	// } catch (Exception e) {
	// ci.println(e.getMessage());
	// }
	// ci.println("punctuations are "
	// + (usePunctuations ? "activated" : "deactivated"));
	//
	// }
	//
	// @Help(description = "<on|off> - Adds or removes load shedding support.")
	// public void _addLoadShedding(CommandInterpreter ci) {
	// boolean useLoadShedding = false;
	// String[] args = support.getArgs(ci);
	// addCommand(args);
	// try {
	// if (args != null && args.length >= 1) {
	//
	// useLoadShedding = toBoolean(args[0]);
	//
	// if (useLoadShedding) {
	// trafoConfigParam.getValue().setOption("useLoadShedding",
	// true);
	// } else {
	// if (trafoConfigParam.getValue()
	// .getOption("useLoadShedding") != null) {
	// trafoConfigParam.getValue().setOption(
	// "useLoadShedding", null);
	// }
	// }
	//
	// }
	// } catch (Exception e) {
	// ci.println(e.getMessage());
	// }
	// ci.println("Load shedding is "
	// + (useLoadShedding ? "activated" : "deactivated"));
	//
	// }
	//
	// @Help(description =
	// "<on|off> - Adds or removes optimized post priorsation functionality.")
	// public void _addExtendedPostPriorisation(CommandInterpreter ci) {
	// boolean useExtPostPriorisation = false;
	// String[] args = support.getArgs(ci);
	// addCommand(args);
	// try {
	// if (args != null && args.length >= 1) {
	//
	// useExtPostPriorisation = toBoolean(args[0]);
	//
	// if (useExtPostPriorisation) {
	// trafoConfigParam.getValue().setOption(
	// "useExtendedPostPriorisation", true);
	// } else {
	// if (trafoConfigParam.getValue().getOption(
	// "useExtendedPostPriorisation") != null) {
	// trafoConfigParam.getValue().setOption(
	// "useExtendedPostPriorisation", null);
	// }
	// }
	//
	// }
	// } catch (Exception e) {
	// ci.println(e.getMessage());
	// }
	// ci.println("Extended Post Priorisation is "
	// + (useExtPostPriorisation ? "activated" : "deactivated"));
	//
	// }

}
