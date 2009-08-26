package de.uniol.inf.is.odysseus.planmanagement.executor.console;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.planmanagement.configuration.AppEnv;
import de.uniol.inf.is.odysseus.base.planmanagement.event.error.ErrorEvent;
import de.uniol.inf.is.odysseus.base.planmanagement.event.error.IErrorEventListener;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.parameter.ParameterDefaultRoot;
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

public class ExecutorConsole implements CommandProvider,
		IPlanExecutionListener, IPlanModificationListener, IErrorEventListener {

	private IAdvancedExecutor executor;

	private String parser;

	private ConsoleFunctions support = new ConsoleFunctions();

	private String scheduler;

	private String schedulingStrat;

	public void bindExecutor(IAdvancedExecutor executor) {
		this.executor = executor;

		this.executor.addErrorEventListener(this);
		this.executor.addPlanExecutionListener(this);
		this.executor.addPlanModificationListener(this);
				
		try {
			this.parser = this.executor.getSupportedQueryParser().iterator().next();
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

		Iterator<String> parsers = this.executor.getSupportedQueryParser().iterator();
		if (parsers != null && parsers.hasNext()) {
			this.parser = parsers.next();
			return this.parser;
		}
		throw new Exception("No parser found");
	}

	public void _ps(CommandInterpreter ci) {
		Set<String> parserList = null;
		try {
			parserList = this.executor.getSupportedQueryParser();
		} catch (PlanManagementException e) {
			System.out.println(e.getMessage());
		}
		if (parserList != null) {
			System.out.println("Available parser:");
			for (String par : parserList) {
				System.out.print(par);

				if (par.equals(this.parser)) {
					System.out.print(" - Selected");
				}
				System.out.println("");
			}
		}
	}

	public void _parser(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
		if (args != null && args.length > 0) {
			try {
				if (this.executor.getSupportedQueryParser().contains(args[0])) {
					this.parser = args[0];
					System.out.println("New parser: " + args[0]);
				} else {
					System.out.println("No parser with this ID.");
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		} else {
			System.out.println("No query argument.");
		}
	}

	public void _bs(CommandInterpreter ci) {
		Set<String> bufferList = this.executor
				.getRegisteredBufferPlacementStrategies();
		if (bufferList != null) {
			String current = (String) this.executor.getConfiguration().get(SettingBufferPlacementStrategy.class).getValue();
			System.out.println("Available bufferplacement strategies:");
			if (current == null) {
				System.out.print("no strategy - SELECTED");
			}else{
				System.out.print("no strategy");
			}
			System.out.println("");
			for (String iBufferPlacementStrategy : bufferList) {
				System.out.print(iBufferPlacementStrategy);
				if (current != null && iBufferPlacementStrategy.equals(current)) {
					System.out.print(" - SELECTED");
				}
				System.out.println("");
			}
			
		}
	}

	public void _sstrats(CommandInterpreter ci) {
		 Set<String> list = this.executor.getRegisteredSchedulingStrategyFactories();
		if (list != null) {
			String current = executor.getCurrentSchedulingStrategy();
			System.out.println("Available Scheduling strategies:");
			
			System.out.println("");
			for (String iStrategy : list) {
				System.out.print(iStrategy.toString());
				if (current != null && iStrategy.equals(current)) {
					System.out.print(" - SELECTED");
				}
				System.out.println("");
			}
			System.out.print("no strategy");
		}
	}
	
	public void _sscheduler(CommandInterpreter ci) {
		 Set<String> list = this.executor.getRegisteredSchedulerFactories();
		if (list != null) {
			String current = executor.getCurrentScheduler();
			System.out.println("Available Schedulers:");
			
//			if (current == null) {
//				System.out.print(" - SELECTED");
//			}
			System.out.println("");
			for (String iStrategy : list) {
				System.out.print(iStrategy.toString());
				if (current != null && iStrategy.equals(current)) {
					System.out.print(" - SELECTED");
				}
				System.out.println("");
			}
		}
	}
	
	public void _scheduler(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
		if (args != null && args.length > 0) {
			executor.setScheduler(args[0],args[1]);
		}else {
			System.out.println("No query argument.");
		}
	}
	
	public void _buffer(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
		if (args != null && args.length > 0) {
			try {
				String bufferName = args[0];
				Set<String> list = this.executor
						.getRegisteredBufferPlacementStrategies();
				if (list.contains(bufferName)){
					this.executor.getConfiguration().set(
							new SettingBufferPlacementStrategy(bufferName));
					System.out.println("Strategy "+ bufferName+" set.");
					return;
				} else {
					this.executor.getConfiguration().set(
							new SettingBufferPlacementStrategy(null));
					if ("no strategy".equalsIgnoreCase(bufferName)){
						System.out.println("Current strategy removed.");
					}else{
						System.out.println("Strategy not found. Current strategy removed.");
					}
					return;
				}
				
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		} else {
			System.out.println("No query argument.");
		}
	}

	public void _man(CommandInterpreter ci) {
		System.out
				.println("ExecutorInfo - more detailed information on the excutor");
		System.out.println("");

		System.out.println("parser PARSERID - set current parser");
		System.out.println("");

		System.out.println("ps - show available parser");
		System.out.println("");

		System.out
				.println("buffer BUFFERID - set current bufferplacement strategy");
		System.out.println("");

		System.out.println("bs - show available bufferplacement strategy");
		System.out.println("");

		System.out.println("sscheduler - show available scheduler");
		System.out.println("");
		
		System.out.println("sstrats - show available scheduling strategy factories");
		System.out.println("");
		
		System.out.println("scheduler SCHEDULER STRATEGY - set current scheduler with strategy");
		System.out.println("");		
		
		System.out.println("schedule - start scheduling");
		System.out.println("");

		System.out.println("stopschedule - stop scheduling");
		System.out.println("");

		System.out.println("qs - show all registered queries");
		System.out.println("");

		System.out.println("qstop QUERYID - stop query with QUERYID");
		System.out.println("");

		System.out.println("qstart QUERYID - stop query with QUERYID");
		System.out.println("");

		System.out
				.println("meta QUERYID - dump meta data of the query with QUERYID (only if root is a sink)");
		System.out.println("");

		System.out
				.println("dumpp QUERYID - dump physical plan of the query with QUERYID");
		System.out.println("");

		System.out
				.println("dumpr - dump physical plan of all registered roots.");
		System.out.println("");

		System.out
				.println("dumpe - dump all physical operators of the current execution plan.");
		System.out.println("");

		System.out.println("remove QUERYID - remove query with QUERYID");
		System.out.println("");

		System.out
				.println("add QUERYSTRING [S] - add query [with console-output-sink]");
		System.out.println("Examples:");
		System.out
				.println("add 'CREATE STREAM test ( a INTEGER	) FROM ( ([0,4), 1), ([1,5), 3), ([7,20), 3) )'");
		System.out
				.println("add 'SELECT (a * 2) as value FROM test WHERE a > 2' S");
		System.out.println("");
	}
	
	public void _dumpe(CommandInterpreter ci) {
		IExecutionPlan plan = this.executor.getExecutionPlan();
		
		int i = 1;
		System.out.println("Registered source:");
		for (IIterableSource<?> isource : plan.getSources()) {
			System.out.println(i++ + ": " + isource.toString() + ", Owner:" + this.support.getOwnerIDs(isource.getOwner()));
		}
		
		System.out.println("");
		i= 1;
		System.out.println("Registered PartialPlans:");
		for (IPartialPlan partialPlan : plan.getPartialPlans()) {
			System.out.println(i++ + ": ");
			System.out.println(partialPlan.toString());
		}
	}

	@SuppressWarnings("unchecked")
	public void _dumpr(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
		int depth = 0;
		if (args != null && args.length > 0) {
			depth = Integer.valueOf(args[1]);

			if (depth < 1) {
				depth = 1;
			}
		}

		try {
			StringBuffer buff = new StringBuffer();
			System.out.println("Physical plan of all roots: ");
			int count = 1;
			for (IPhysicalOperator root : this.executor.getSealedPlan()
					.getRoots()) {
				buff = new StringBuffer();
				if (root.isSink()) {
					this.support.dumpPlan((ISink) root, depth, buff);
				} else {
					this.support.dumpPlan((ISource) root, depth, buff);
				}
				System.out.println(count++ + ". root:");
				System.out.println(buff.toString());
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void _dumpp(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
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
				IQuery query = this.executor.getSealedPlan()
						.getQuery(qnum);
				if (query != null) {
					StringBuffer buff = new StringBuffer();
					if (query.getSealedRoot().isSink()) {
						this.support.dumpPlan((ISink) query.getSealedRoot(), depth,
								buff);
					} else {
						this.support.dumpPlan((ISource) query.getSealedRoot(), depth,
								buff);
					}
					System.out.println("Physical plan of query: " + qnum);
					System.out.println(buff.toString());
				} else {
					System.out.println("Query not found.");
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		} else {
			System.out.println("No query argument.");
		}
	}

	@SuppressWarnings("unchecked")
	public void _meta(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
		if (args != null && args.length > 0) {
			int qnum = Integer.valueOf(args[0]);
			try {
				IQuery query = this.executor.getSealedPlan()
						.getQuery(qnum);
				if (query != null) {
					if (query.getSealedRoot().isSink()) {
						this.support.printPlanMetadata((ISink) query.getSealedRoot());

					} else {
						System.out.println("Root is no sink.");
					}
				}

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		} else {
			System.out.println("No query argument.");
		}
	}

	public void _add(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
		if (args != null && args.length > 0) {
			try {
				if (args.length > 1 && args[1].toUpperCase().equals("S")) {
					this.executor.addQuery(args[0], parser(),
							new ParameterDefaultRoot(new MySink()));
				} else {
					this.executor.addQuery(args[0], parser());
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

		} else {
			System.out.println("No query argument.");
		}
	}

	public void _qs(CommandInterpreter ci) {
		try {
			System.out
					.println("Current registered queries (ID | STARTED | PARSERID):");
			for (IQuery query : this.executor.getSealedPlan()
					.getQueries()) {
				System.out.println(query.getID() + " | " + query.isStarted());
			}
		} catch (PlanManagementException e) {
			e.printStackTrace();
		}
	}

	public void _remove(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
		if (args != null && args.length > 0) {
			int qnum = Integer.valueOf(args[0]);
			try {
				this.executor.removeQuery(qnum);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		} else {
			System.out.println("No query argument.");
		}
	}

	public void _qstop(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
		if (args != null && args.length > 0) {
			int qnum = Integer.valueOf(args[0]);
			try {
				this.executor.stopQuery(qnum);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		} else {
			System.out.println("No query argument.");
		}
	}

	public void _qstart(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
		if (args != null && args.length > 0) {
			int qnum = Integer.valueOf(args[0]);
			try {
				this.executor.startQuery(qnum);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		} else {
			System.out.println("No query argument.");
		}
	}

	public void _schedule(CommandInterpreter ci) {
		try {
			this.executor.startExecution();
		} catch (PlanManagementException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
		}
	}

	public void _stopschedule(CommandInterpreter ci) {
		try {
			this.executor.stopExecution();
		} catch (PlanManagementException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
		}
	}

	@Override
	public String getHelp() {
		String info = AppEnv.LINE_SEPERATOR
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
}
