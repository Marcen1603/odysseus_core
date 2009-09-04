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
import de.uniol.inf.is.odysseus.viewer.ViewerStarter;
import de.uniol.inf.is.odysseus.viewer.ViewerStarterConfiguration;
import de.uniol.inf.is.odysseus.viewer.model.create.OdysseusModelProviderSink;



public class ExecutorConsole implements CommandProvider,
		IPlanExecutionListener, IPlanModificationListener, IErrorEventListener {
	
	
	private IAdvancedExecutor executor;

	private String parser;

	private ConsoleFunctions support = new ConsoleFunctions();

	private ViewerStarter wnd;

	private String[][] nexmarkQ = new String[][]{{
			// Q1
			"SELECT b.auction, DolToEur(b.price) AS euroPrice, b.bidder, b.datetime FROM nexmark:bid UNBOUNDED AS b",
			// Q2
			"SELECT auction, price FROM nexmark:bid WHERE auction=7 OR auction=20 OR auction=21 OR auction=59 OR auction=87",
			// Q3
			"SELECT p.name, p.city, p.state, a.id FROM nexmark:auction UNBOUNDED AS a, nexmark:person UNBOUNDED AS p WHERE a.seller=p.id AND a.category < 150 AND (p.state='Oregon' OR p.state='Idaho' OR p.state='California')" ,
			// Q4
			"SELECT AVG(q.final) FROM nexmark:category AS c, (SELECT MAX(b.price) AS final, a.category FROM nexmark:auction UNBOUNDED AS a, nexmark:bid UNBOUNDED AS b  WHERE a.id = b.auction AND b.datetime < a.expires AND  a.expires < Now() GROUP BY a.id, a.category) AS q WHERE q.category = c.id GROUP BY c.id",
			// Q7
			"SELECT b.auction, b.price, b.bidder FROM nexmark:bid RANGE 1000 SLIDE 1000 AS b,(SELECT MAX(price) AS max_price FROM nexmark:bid RANGE 1000 SLIDE 1000) AS sub WHERE sub.max_price = b.price",
			// Q8
			"SELECT p.id, p.name, a.reserve FROM nexmark:person RANGE 43200000 AS p, nexmark:auction RANGE 43200000 AS a WHERE p.id = a.seller"
	},{			// Q1
			"SELECT b.auction, DolToEur(b.price) AS euroPrice, b.bidder, b.datetime FROM nexmark:bid2 UNBOUNDED AS b",
			// Q2
			"SELECT auction, price FROM nexmark:bid2 WHERE auction=7 OR auction=20 OR auction=21 OR auction=59 OR auction=87",
			// Q3
			"SELECT p.name, p.city, p.state, a.id FROM nexmark:auction2 UNBOUNDED AS a, nexmark:person2 UNBOUNDED AS p WHERE a.seller=p.id AND a.category < 150 AND (p.state='Oregon' OR p.state='Idaho' OR p.state='California')" ,
			// Q4
			"SELECT AVG(q.final) FROM nexmark:category2 AS c, (SELECT MAX(b.price) AS final, a.category FROM nexmark:auction2 UNBOUNDED AS a, nexmark:bid2 UNBOUNDED AS b  WHERE a.id = b.auction AND b.datetime < a.expires AND  a.expires < Now() GROUP BY a.id, a.category) AS q WHERE q.category = c.id GROUP BY c.id",
			// Q7
			"SELECT b.auction, b.price, b.bidder	FROM nexmark:bid2 RANGE 1000 SLIDE 1000 AS b,(SELECT MAX(price) AS max_price FROM nexmark:bid2 RANGE 1000 SLIDE 1000) AS sub WHERE sub.max_price = b.price",
			// Q8
			"SELECT p.id, p.name, a.reserve FROM nexmark:person2 RANGE 43200000 AS p, nexmark:auction2 RANGE 43200000 AS a WHERE p.id = a.seller"
	}};

	
	public void bindExecutor(IAdvancedExecutor executor) {		
		
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

	public void _ps(CommandInterpreter ci) {
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

	public void _bs(CommandInterpreter ci) {
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

	public void _sstrats(CommandInterpreter ci) {
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

	public void _sscheduler(CommandInterpreter ci) {
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
			executor.setScheduler(args[0], args[1]);
		} else {
			ci.println("No query argument.");
		}
	}

	
	public void _viewer(CommandInterpreter ci){
		System.out.println("startviewer");
		try{
			ViewerStarterConfiguration cfg = new ViewerStarterConfiguration();
			//cfg.useOGL = viewerOGL;
			wnd = new ViewerStarter(null, cfg);
			Thread thread = new Thread(wnd, "ViewerThread");
			thread.start();
		}catch(Throwable e){
			e.printStackTrace();
		}
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
		ci.println("ExecutorInfo - more detailed information on the excutor");
		ci.println("");

		ci.println("parser PARSERID - set current parser");
		ci.println("");

		ci.println("ps - show available parser");
		ci.println("");

		ci.println("buffer BUFFERID - set current bufferplacement strategy");
		ci.println("");

		ci.println("bs - show available bufferplacement strategy");
		ci.println("");

		ci.println("sscheduler - show available scheduler");
		ci.println("");

		ci.println("sstrats - show available scheduling strategy factories");
		ci.println("");

		ci
				.println("scheduler SCHEDULER STRATEGY - set current scheduler with strategy");
		ci.println("");

		ci.println("schedule - start scheduling");
		ci.println("");

		ci.println("stopschedule - stop scheduling");
		ci.println("");

		ci.println("qs - show all registered queries");
		ci.println("");

		ci.println("qstop QUERYID - stop query with QUERYID");
		ci.println("");

		ci.println("qstart QUERYID - stop query with QUERYID");
		ci.println("");

		ci
				.println("meta QUERYID - dump meta data of the query with QUERYID (only if root is a sink)");
		ci.println("");

		ci
				.println("dumpp QUERYID - dump physical plan of the query with QUERYID");
		ci.println("");

		ci.println("dumpr - dump physical plan of all registered roots.");
		ci.println("");

		ci
				.println("dumpe - dump all physical operators of the current execution plan.");
		ci.println("");

		ci.println("remove QUERYID - remove query with QUERYID");
		ci.println("");

		ci
				.println("add QUERYSTRING [S] - add query [with console-output-sink]");
		ci.println("Examples:");
		ci
				.println("add 'CREATE STREAM test ( a INTEGER	) FROM ( ([0,4), 1), ([1,5), 3), ([7,20), 3) )'");
		ci.println("add 'SELECT (a * 2) as value FROM test WHERE a > 2' S");
		ci.println("");
	}

	public void _dumpe(CommandInterpreter ci) {
		IExecutionPlan plan = this.executor.getExecutionPlan();

		int i = 1;
		ci.println("Registered source:");
		for (IIterableSource<?> isource : plan.getSources()) {
			ci.println(i++ + ": " + isource.toString() + ", Owner:"
					+ this.support.getOwnerIDs(isource.getOwner()));
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
			ci.println("Physical plan of all roots: ");
			int count = 1;
			for (IPhysicalOperator root : this.executor.getSealedPlan()
					.getRoots()) {
				buff = new StringBuffer();
				if (root.isSink()) {
					this.support.dumpPlan((ISink) root, depth, buff);
				} else {
					this.support.dumpPlan((ISource) root, depth, buff);
				}
				ci.println(count++ + ". root:");
				ci.println(buff.toString());
			}
		} catch (Exception e) {
			ci.println(e.getMessage());
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
				IQuery query = this.executor.getSealedPlan().getQuery(qnum);
				if (query != null) {
					StringBuffer buff = new StringBuffer();
					if (query.getSealedRoot().isSink()) {
						this.support.dumpPlan((ISink) query.getSealedRoot(),
								depth, buff);
					} else {
						this.support.dumpPlan((ISource) query.getSealedRoot(),
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
	public void _meta(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
		if (args != null && args.length > 0) {
			int qnum = Integer.valueOf(args[0]);
			try {
				IQuery query = this.executor.getSealedPlan().getQuery(qnum);
				if (query != null) {
					if (query.getSealedRoot().isSink()) {
						this.support.printPlanMetadata((ISink) query
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
	
	public void _nmsN(CommandInterpreter ci){
		_nexmarkSourcesNIO(ci);
	}
	
	public void _nexmarkSourcesNIO(CommandInterpreter ci){
		String[] q = new String[4];
		q[0] = "CREATE STREAM nexmark:person2 (timestamp LONG,id INTEGER,name STRING,email STRING,creditcard STRING,city STRING,state STRING) CHANNEL localhost : 65440";
		q[1] = "CREATE STREAM nexmark:bid2 (timestamp LONG,	auction INTEGER, bidder INTEGER, datetime LONG,	price DOUBLE) CHANNEL localhost : 65442";
		q[2] = "CREATE STREAM nexmark:auction2 (timestamp LONG,	id INTEGER,	itemname STRING,	description STRING,	initialbid INTEGER,	reserve INTEGER,	expires LONG,	seller INTEGER ,category INTEGER) CHANNEL localhost : 65441";
		q[3] = "CREATE STREAM nexmark:category2 (id INTEGER, name STRING, description STRING, parentid INTEGER) CHANNEL localhost : 65443";
		for (String s:q){
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
	
	public void _nms(CommandInterpreter ci){
		_nexmarkSources(ci);
	}
	
	public void _nexmarkSources(CommandInterpreter ci){
		String[] q = new String[4];
		q[0] = "CREATE STREAM nexmark:person (timestamp LONG,id INTEGER,name STRING,email STRING,creditcard STRING,city STRING,state STRING) CHANNEL localhost : 65430";
		q[1] = "CREATE STREAM nexmark:bid (timestamp LONG,	auction INTEGER, bidder INTEGER, datetime LONG,	price DOUBLE) CHANNEL localhost : 65432";
		q[2] = "CREATE STREAM nexmark:auction (timestamp LONG,	id INTEGER,	itemname STRING,	description STRING,	initialbid INTEGER,	reserve INTEGER,	expires LONG,	seller INTEGER ,category INTEGER) CHANNEL localhost : 65431";
		q[3] = "CREATE STREAM nexmark:category (id INTEGER, name STRING, description STRING, parentid INTEGER) CHANNEL localhost : 65433";
		for (String s:q){
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

	public void _nmq(CommandInterpreter ci){
		String[] args = support.getArgs(ci);
		if (args != null && args.length >= 1) {
			int j=0;
			if (args.length > 1 && args[1].startsWith("n")){
				j = 1;
			}
			if ("*".equals(args[0])){
				for(String q:nexmarkQ[j]){
					addQuery(q);
				}
			}else{
				addQuery(nexmarkQ[j][Integer.parseInt(args[0])]);
			}
			
		}else{
			ci.println("usage [0-5]|* [nio]");
		}
		
		
	}
	

	private void addQuery(String q){
		try {
			this.executor.addQuery(q, parser());
		} catch (PlanManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
				ci.println(e.getMessage());
			}

		} else {
			ci.println("No query argument.");
		}
		// TODO: DAS HIER IST NUR EIN HACK!!
		blah();
	}

	public void _qs(CommandInterpreter ci) {
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

	public void _remove(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
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

	public void _qstop(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
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

	public void _qstart(CommandInterpreter ci) {
		String[] args = support.getArgs(ci);
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

	public void _schedule(CommandInterpreter ci) {
		try {
			this.executor.startExecution();
		} catch (PlanManagementException e) {
			ci.println(e.getMessage());
			ci.printStackTrace(e);
		}
	}

	public void _stopschedule(CommandInterpreter ci) {
		try {
			this.executor.stopExecution();
		} catch (PlanManagementException e) {
			ci.println(e.getMessage());
			ci.printStackTrace(e);
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
	
	public void blah(){
		if (wnd != null){
			ArrayList<IPhysicalOperator> queries = null;
			try {
				queries = this.executor.getSealedPlan().getRoots();
			} catch (PlanManagementException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (IPhysicalOperator query:queries){
				if (query == null) {
					continue;
				}
				if (query.isSink()){
					OdysseusModelProviderSink mp = new OdysseusModelProviderSink((ISink<?>)query);
					wnd.setModelProvider(mp);
				}
			}
		}
	}
}
