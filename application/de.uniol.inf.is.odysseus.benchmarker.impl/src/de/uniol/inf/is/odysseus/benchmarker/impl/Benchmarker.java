package de.uniol.inf.is.odysseus.benchmarker.impl;
//package de.uniol.inf.is.odysseus.benchmarker.impl;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//import java.util.concurrent.CountDownLatch;
//
//import org.json.XML;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import de.uniol.inf.is.odysseus.queryexecution.ExecutionEnvironment;
//import de.uniol.inf.is.odysseus.queryexecution.po.base.access.Router;
//import de.uniol.inf.is.odysseus.queryexecution.po.base.access.help.IdentityTransformation;
//import de.uniol.inf.is.odysseus.queryexecution.po.base.buffer.BufferedPipe;
//import de.uniol.inf.is.odysseus.queryexecution.po.base.metadata.LatencyCalculationPipe;
//import de.uniol.inf.is.odysseus.queryexecution.po.base.object.IntervalLatencyPriority;
//import de.uniol.inf.is.odysseus.queryexecution.po.base.object.IntervalProbabilityLatency;
//import de.uniol.inf.is.odysseus.queryexecution.po.base.object.IntervalProbabilityLatencyPrediction;
//import de.uniol.inf.is.odysseus.queryexecution.po.base.object.PosNegLatencyPriority;
//import de.uniol.inf.is.odysseus.queryexecution.po.base.operators.AbstractSource;
//import de.uniol.inf.is.odysseus.queryexecution.po.base.operators.IIterableSource;
//import de.uniol.inf.is.odysseus.queryexecution.po.base.operators.IPipe;
//import de.uniol.inf.is.odysseus.queryexecution.po.base.operators.ISource;
//import de.uniol.inf.is.odysseus.queryexecution.po.base.operators.OpenFailedException;
//import de.uniol.inf.is.odysseus.queryexecution.po.benchmark.BenchmarkDataProducer;
//import de.uniol.inf.is.odysseus.queryexecution.po.relational.object.MVRelationalTuple;
//import de.uniol.inf.is.odysseus.queryexecution.po.relational.object.RelationalTuple;
//import de.uniol.inf.is.odysseus.queryexecution.scheduler.bufferplacement.FullBufferPlacementStrategy;
//import de.uniol.inf.is.odysseus.queryexecution.scheduler.bufferplacement.IBufferPlacementStrategy;
//import de.uniol.inf.is.odysseus.queryexecution.scheduler.bufferplacement.NoBufferPlacementStrategy;
//import de.uniol.inf.is.odysseus.queryexecution.scheduler.bufferplacement.StandardBufferPlacementStrategy;
//import de.uniol.inf.is.odysseus.queryexecution.scheduler.partitioning.IPartitioningStrategy;
//import de.uniol.inf.is.odysseus.queryexecution.scheduler.partitioning.SplittBeforeIIteratable;
//import de.uniol.inf.is.odysseus.queryoptimization.trafo.benchmark.BenchmarkTransform;
//import de.uniol.inf.is.odysseus.queryoptimization.trafo.factories.PriorityMode;
//import de.uniol.inf.is.odysseus.querytranslation.CQLParser;
//import de.uniol.inf.is.odysseus.querytranslation.QueryParseException;
//import de.uniol.inf.is.odysseus.querytranslation.logicalops.AbstractLogicalOperator;
//import de.uniol.inf.is.odysseus.querytranslation.parser.ParseException;
//import de.uniol.inf.is.odysseus.ruleengine.RuleEngineInterface;
//import de.uniol.inf.is.odysseus.viewer.ViewerStarter;
//import de.uniol.inf.is.odysseus.viewer.model.create.OdysseusModelProviderSink;
//import de.uniol.inf.is.odysseus.wrapper.WrapperPlanFactory;
//
//public class Benchmarker  {
//
//	private static boolean useMultithreaded = false;
//	private static boolean useViewer = false;
//	private static final Logger logger = LoggerFactory.getLogger( Benchmarker.class );
//	
//	public static void main(String[] args) throws Exception {
//
//		String query = null;
//		int queryNTimes = 1;
//		String priorities = "";
//		int genDataCount = 10000;
//		int genDataRate = 1;
//		int timeSlicePerStrategy = 1000;
//		int elementsPerSlice = 1000;
//		double selSelectivity = 1;
//		double joinSelectivity = 1;
//		String prioritymode = "normal";
//		String lang = "CQL_BENCHMARK";
//		String strategy = "roundrobin";
//		String alterSchedstrat = "biggestqueue";
//		// String server = "localhost";
//		// int port = 55555;
//		int simpleOpPTime = 100;
//		int complexOpPTime = 500;
//		long resultsToRead = -1;
//		boolean usePN = false;
//		boolean usePNID = false;
//		boolean useMV = false;
//		boolean useIter = false;
//		boolean usePrediction = false;
//		
//		String fileName = null;
//		String bufferPlacementString = "STANDARD";
//		
//		boolean useMultiQueryApproach = false;
//		boolean printPlanMetadata = false;
//		boolean printOutput = false;
//
//		boolean foundErrorInArgs = false;
//		try {
//
//			for (int i = 0; i < args.length; i = i + 2) {
//
//				if ("-query".equalsIgnoreCase(args[i])) {
//					query = args[i + 1];
//				}else if ("-queryNTimes".equalsIgnoreCase(args[i])) {
//					queryNTimes = Integer.parseInt(args[i + 1]);
//				}else if ("-priorities".equalsIgnoreCase(args[i])) {
//					priorities = args[i + 1];
//				}else if ("-pmode".equalsIgnoreCase(args[i])) {
//					prioritymode = args[i + 1];
//				}else if ("-schedstrat".equalsIgnoreCase(args[i])) {
//					strategy = args[i + 1];
//				}else if ("-useFairScheduling".equalsIgnoreCase(args[i])) {
//					useIter = true;
//					i = i - 1;
//				}else if ("-alterSchedstrat".equalsIgnoreCase(args[i])) {
//					alterSchedstrat = args[i + 1];
//				}else if ("-lang".equalsIgnoreCase(args[i])) {
//					if ("sparql".equalsIgnoreCase(args[i + 1])) {
//						lang = "S2PARQL";
//					} else if ("cql".equalsIgnoreCase(args[i + 1])) {
//						lang = "CQL";
//					} else if ("cql_bench".equalsIgnoreCase(args[i + 1])) {
//						lang = "CQL_BENCHMARK";
//					} else if("procedural".equalsIgnoreCase(args[i + 1])){
//						lang = "procedural";
//					}
//				}else if ("-genDataCount".equalsIgnoreCase(args[i])) {
//					genDataCount = Integer.parseInt(args[i + 1]);
//				}else if ("-genDataRate".equalsIgnoreCase(args[i])) {
//					genDataRate = Integer.parseInt(args[i + 1]);
//				}else if ("-resultsToRead".equalsIgnoreCase(args[i])) {
//					resultsToRead = Integer.parseInt(args[i + 1]);
//				}else if ("-timeSlicePerStrategy".equalsIgnoreCase(args[i])) {
//					timeSlicePerStrategy = Integer.parseInt(args[i + 1]);
//				}else if ("-elementsPerSlice".equalsIgnoreCase(args[i])) {
//					elementsPerSlice = Integer.parseInt(args[i + 1]);
//				}else if ("-selSelectivity".equalsIgnoreCase(args[i])) {
//					selSelectivity = Double.parseDouble(args[i + 1]);
//				}else if ("-joinSelectivity".equalsIgnoreCase(args[i])) {
//					joinSelectivity = Double.parseDouble(args[i + 1]);
//				}else if ("-simpleOpPTime".equalsIgnoreCase(args[i])) {
//					simpleOpPTime = Integer.parseInt(args[i + 1]);
//				}else if ("-complexOpPTime".equalsIgnoreCase(args[i])) {
//					complexOpPTime = Integer.parseInt(args[i + 1]);
//				}else if ("-usePN".equalsIgnoreCase(args[i])) {
//					usePN = true;
//					i = i - 1; // da nur ein Param
//				}else if ("-usePNID".equalsIgnoreCase(args[i])) {
//					usePNID = true;
//					i = i - 1; // da nur ein Param
//				}else if("-useMV".equalsIgnoreCase(args[i])){
//					useMV = true;
//					i = i - 1; // da nur ein Param
//				}
//				else if ("-file".equalsIgnoreCase(args[i])) {
//					fileName = args[i + 1];
//				}else if("-bufferPlacement".equals(args[i])){
//					bufferPlacementString = args[i+1];
//				}else if ("-useMultiQueryApproach".equalsIgnoreCase(args[i])) {
//					useMultiQueryApproach = true;
//					i = i - 1; // da nur ein Param
//				}else if ("-printPlanMetadata".equalsIgnoreCase(args[i])) {
//					printPlanMetadata = true;
//					i = i - 1; // da nur ein Param
//				}else if ("-printOutput".equalsIgnoreCase(args[i])) {
//					printOutput = true;
//					i = i - 1; // da nur ein Param
//				}else if ("-viewer".equalsIgnoreCase(args[i])) {
//					useViewer = true;
//					i = i - 1; // da nur ein Param
//				}else if ("-multithreaded".equalsIgnoreCase(args[i])) {
//					useMultithreaded = true;
//					i = i - 1;// da nur ein Param
//				}else if ("-usePrediction".equalsIgnoreCase(args[i])){
//					usePrediction = true;
//					i = i-1;// da nur ein Param
//				}else if ("-help".equalsIgnoreCase(args[i])) {
//					showHelpAndExit();
//				}else{
//					System.err.println("Illegal Argument "+args[i]);
//					foundErrorInArgs = true;
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			showHelpAndExit();
//		}
//		
//		if (foundErrorInArgs){
//			showHelpAndExit();
//		}
//
//		if (query == null) {
//			showHelpAndExit();
//		}
//
//		/*** VIEWER START **/
//		if (useViewer){	
//			wnd = new ViewerStarter();
//			Thread thread = new Thread(wnd, "ViewerThread");
//			thread.start();
//		}			
//		/*** VIEWER END **/
//
//		
//		Benchmarker b = new Benchmarker();
//		logger.debug("new Benchmarker created()");
//		b.init(priorities, lang, genDataCount, genDataRate, usePN || usePNID);
//		
//
//		// IBufferFactory<RelationalTuple<IntervalLatencyPriority>> factory;
//		PriorityMode prioMode = PriorityMode.translate(prioritymode);
//		
//		IBufferPlacementStrategy bufferPlacement = null;
//		
//		if(bufferPlacementString.equalsIgnoreCase("FULL")){
//			bufferPlacement = new FullBufferPlacementStrategy(prioMode);
//		}else if(bufferPlacementString.equalsIgnoreCase("NO")){
//			bufferPlacement = new NoBufferPlacementStrategy();
//		}else if(bufferPlacementString.equalsIgnoreCase("STANDARD")){
//			bufferPlacement = new StandardBufferPlacementStrategy(prioMode);
//		}else{
//			bufferPlacement = new FullBufferPlacementStrategy(prioMode);
//		}
//		
//		
//		IPartitioningStrategy partitionStrategy = new SplittBeforeIIteratable();
//		//IPartitioningStrategy partitionStrategy = null;
//	
//		BenchmarkTransform.setParams(selSelectivity,
//				joinSelectivity, simpleOpPTime, complexOpPTime);
//		ExecutionEnvironment env = new ExecutionEnvironment();
//		env.initScheduler(strategy, alterSchedstrat,useMultithreaded, 
//						  elementsPerSlice, timeSlicePerStrategy, 
//						  bufferPlacement, useIter, partitionStrategy);
//		if (usePN) {
//			env.setParameters(ExecutionEnvironment.Parameter.USE_PN_APPROACH);
//		} else if (usePNID) {
//			env.setParameters(ExecutionEnvironment.Parameter.USE_PNID_APPROACH);
//		} else if(useMV){
//			env.setParameters(ExecutionEnvironment.Parameter.USE_MV_APPROACH);
//		} 
//		// no else if, because predictions
//		// are independent of mv, ti or pn
//		if(usePrediction){
//			// if predictions are used, mv will also be used at the moment
//			env.setParameters(ExecutionEnvironment.Parameter.USE_MV_APPROACH, ExecutionEnvironment.Parameter.USE_PREDICTION);
//		}
//
//		env.setPriorityMode(prioMode);
//		//env.setScheduler(scheduler);
//
//		System.out.println("<run time='" + System.currentTimeMillis() + "'>");
//		System.out.println("  <query>" + XML.escape(query) + "</query>");
//		System.out.println("  <queryNTimes>" + queryNTimes + "</queryNTimes>");
//		System.out.println("  <priostring>" + XML.escape(priorities)
//				+ "</priostring>");
//		System.out.println("  <prioritymode>" + prioritymode
//				+ "</prioritymode>");
//		System.out.println("  <scheduling_strategy>" + strategy
//				+ "</scheduling_strategy>");
//		System.out.println("  <scheduling_fair>" + useIter
//				+ "</scheduling_fair>");
//		System.out.println("  <alterSchedstrat>" + alterSchedstrat
//				+ "</alterSchedstrat>");
//		System.out.println("  <genDataCount>" + genDataCount
//				+ "</genDataCount>");
//		System.out.println("  <resultsToRead>" + resultsToRead
//				+ "</resultsToRead>");
//		System.out.println("  <genDataRate>" + genDataRate + "</genDataRate>");
//		System.out.println("  <timeSlicePerStrategy>" + timeSlicePerStrategy
//				+ "</timeSlicePerStrategy>");
//		System.out.println("  <elementsPerSlice>" + elementsPerSlice
//				+ "</elementsPerSlice>");
//		System.out.println("  <selSelectivity>" + selSelectivity
//				+ "</selSelectivity>");
//		System.out.println("  <joinSelectivity>" + joinSelectivity
//				+ "</joinSelectivity>");
//		System.out.println("  <simpleOpPTime>" + simpleOpPTime
//				+ "</simpleOpPTime>");
//		System.out.println("  <complexOpPTime>" + complexOpPTime
//				+ "</complexOpPTime>");
//		System.out.println("  <lang>" + lang + "</lang>");
//		System.out.println("  <processingApproach>"
//				+ (usePN ? "PN" : (usePNID ? "PNID" : "INTERVALL"))
//				+ "</processingApproach>");
//		System.out.println("  <useMultiQueryApproach>" + useMultiQueryApproach
//				+ "</useMultiQueryApproach>");
//		System.out.println("  <multiThreaded>" + useMultithreaded
//				+ "</multiThreaded>");		
//		AbstractBenchmarkResult result = b.start(query,queryNTimes, env, lang, resultsToRead, usePN
//				|| usePNID, useMV, usePrediction, fileName, useMultiQueryApproach, printPlanMetadata, printOutput);
//
//		System.out.println(result);
//		System.out.println("</run>");
//		// ToDo: Evtl. verlagern
//		Router.getInstance().stopRouting();
//	}
//
//	private static void showHelpAndExit() {
//		System.out.println("usage: LabdataBenchmark");
//		System.out.println("-query The queries to execute, seperated by ';' ");
//		System.out
//				.println("-useMultiQueryApproach if Flag set, every query is scheduled one its own");
//		System.out
//		.println("-queryNTimes executes the query n times parallel");  
//		System.out.println("-priorities  SINNVOLL?? ");
//		System.out.println("-lang Language of the query: cql|sparql|cql_bench");
//		System.out
//				.println("-usePN Flag (!) to indicate positive/negativ approach, default interval approach");
//		System.out
//				.println("-usePNID Flag (!) to indicate positive/negativ ID approach, default interval approach");
//		System.out
//				.println("-useMV Flag (!) to indicate that measurement values are used. Not for use with -usePN or -usePNID option.");
//		System.out
//				.println("-pmode Which Prioritymode to use: normal | direct | weakorder | prioorder | prioorder2");
//		System.out
//				.println("-schedstrat Which Schedulingstrategy: roundrobin | biggestqueue | roundrobin+ | hipqueue | hipqueue+ | aurora_(min_latency|min_cost|min_mem)+ | staticrandom | dynamicrandom | greedy | chain");
//		System.out
//				.println("-useFairScheduling Aurora_/Chain/Greedy are scheduled in fair mode, i.e. if one source has proceeded, next source will be select"); 
//		System.out
//				.println("-alterSchedstrat If hipqueue used this strategy is used for normal prio mode");
//		System.out
//				.println("-resultsToRead How many elements are used in the benchmark at most");
//		System.out
//				.println("-timeSlicePerStrategy How many milliseconds is one scheduling slice at most");
//		System.out
//				.println("-elementsPerSlice How many elements are read in one scheduling slice at most");
//		System.out
//			.println("-printPlanMetadata Print the metadata like join selectivity or datarate for each operator. Not for use with -useMultiQueryApproach");
//		System.out
//			.println("-printOutput Prints every Output that arrives at the ISink");
//		System.out.println("-viewer Starts the viewer-component");
//		// System.out.println("-server Dataserver to connect");
//		// System.out.println("-port Port of Dataserver to connect");
//		System.out.println("for cql_bench:");
//		System.out
//				.println("-genDataCount How many elements are generated by each contained source");
//		System.out
//				.println("-genDataRate How many elements are generated by second");
//		System.out
//				.println("-selSelectivity The selectivity of each select operator (double)");
//		System.out
//				.println("-joinSelectivity The selectivity of each join operator (double)");
//		System.out
//				.println("-simpleOpPTime The processing time of each select operator (long)");
//		System.out
//				.println("-complexOpPTime The processing time of each join operator (long)");
//		System.out.println("-bufferPlacement Defines a buffer placement strategy. Values: \"no\" | \"full\" (default) | \"standard\".");
//		System.exit(0);
//	}
//
//	public void init(String priority, String queryLang, int genDataCount,
//			int genDataRate, boolean usePN) throws Exception {
//		logger.debug("Init Benchmarker");
//		WrapperPlanFactory.init();
//		// System.out.println("init "+server+" "+port+" "+queryLang+" "+
//		// genDataCount
//		// );
//		ISource po = null;
//		CQLParser.getInstance().parse("CREATE STREAM test:labdata (" + "timestamp LONG,"
//				+ "epoch INTEGER," + "moteid INTEGER," + "temperature DOUBLE,"
//				+ "humidity DOUBLE," + "light DOUBLE," + "voltage DOUBLE) FROM ()");
//		if (queryLang.equals("CQL_BENCHMARK")) {
//			po = new BenchmarkDataProducer<IntervalLatencyPriority, RelationalTuple<IntervalLatencyPriority>, RelationalTuple<IntervalLatencyPriority>>(
//					new IdentityTransformation<RelationalTuple<IntervalLatencyPriority>>() {
//						// private int counter = -1;
//						private int maxCounter = 1000; // Anzahl
//						// unterschiedlicher
//						// Datensaetze
//						Random rnd = new Random(978799898);
//
//						@Override
//						public RelationalTuple<IntervalLatencyPriority> transform(
//								RelationalTuple<IntervalLatencyPriority> inElem) {
//							IntervalLatencyPriority l = new IntervalLatencyPriority(System.nanoTime());
//							// if (counter > maxCounter) counter = -1;
//							// counter++;
//							RelationalTuple<IntervalLatencyPriority> r = new RelationalTuple<IntervalLatencyPriority>(
//									3);
//							r.setAttribute(0, -1); // Timestamp egal
//							r.setAttribute(1, -1); // epoch egal
//							r.setAttribute( 2, rnd.nextInt(maxCounter) );
////							if (val <= 50) {
////								r.setAttribute(2, 999);
////							} else if (val <= 20) {
////								r.setAttribute(2, 888);
////							} else if (val <= 30) {
////								r.setAttribute(2, 777);
////							} else if (val <= 40) {
////								r.setAttribute(2, 666);
////							} else if (val <= 50) {
////								r.setAttribute(2, 555);
////							} else if (val <= 60) {
////								r.setAttribute(2, 444);
////							} else if (val <= 70) {
////								r.setAttribute(2, 333);
////							} else if (val <= 80) {
////								r.setAttribute(2, 222);
////							} else if (val <= 90) {
////								r.setAttribute(2, 111);
////							} else {
////								r.setAttribute(2, 1000);
////							}
//							r.setMetadata(l);
//							return r;
//						}
//					}, genDataCount, genDataRate);
//
//			WrapperPlanFactory.putAccessPlan("test:labdata", po);
//		}
//
//		CQLParser.getInstance().parse("CREATE STREAM test:labdata2 (" + "timestamp LONG,"
//				+ "epoch INTEGER," + "moteid INTEGER," + "temperature DOUBLE,"
//				+ "humidity DOUBLE," + "light DOUBLE,"
//				+ "voltage DOUBLE) FROM (SELECT * FROM test:labdata) "
//				+ priority);
//		logger.debug("Init Benchmarker done");
//	}
//
//	public AbstractBenchmarkResult start(String query, int queryNTimes, ExecutionEnvironment env,
//			String queryLang, long resultsToRead, boolean usePN, boolean useMV, boolean usePrediction,
//			String fileName, boolean useMultiQueryApproach,
//			boolean printPlanMetadata, boolean printOutput) throws ParseException,
//			OpenFailedException, QueryParseException {
//		logger.debug("Starting ");
//		List<AbstractLogicalOperator> plans = new ArrayList<AbstractLogicalOperator>(); 
//		for (int i=0;i<queryNTimes;i++){	
//			plans.addAll(env.translateAndRestruct(queryLang, query));
//		}
//		AbstractBenchmarkResult result = new AbstractBenchmarkResult();
//		result.setWriteResultsToFile(fileName != null);
//		result.setFileName(fileName);
//
//		CountDownLatch latch = new CountDownLatch(plans.size());
//		MySink sink = null;
//		if(useMV){
//			if(usePrediction){
//				sink = new MySink<IntervalProbabilityLatencyPrediction>(latch, result, resultsToRead, printOutput);
//			}else{
//				sink = new MySink<IntervalProbabilityLatency>(latch, result,
//						resultsToRead, printOutput);
//			}
//		}
//		else if (!usePN) {
//			sink = new MySink<IntervalLatencyPriority>(latch, result,
//					resultsToRead, printOutput);
//		} else {
//			sink = new MySink<PosNegLatencyPriority>(latch, result,
//					resultsToRead, printOutput);
//		}
//
//		sink.setNoOfInputPort(plans.size());
//		List<IIterableSource<?>> buffer = new ArrayList<IIterableSource<?>>(
//				plans.size());
//
////		int i = 0;
//		for (AbstractLogicalOperator plan : plans) {
//			
//			RuleEngineInterface.dumpPlan(plan, "  ");
//
//			LatencyCalculationPipe latency = null;
//			BufferedPipe bp = null;
//			if(useMV){
//				if(usePrediction){
//					bp = new BufferedPipe<MVRelationalTuple<IntervalProbabilityLatencyPrediction>>();
//					bp.subscribe(sink, 0);
//					buffer.add(bp);
//					latency = new LatencyCalculationPipe<MVRelationalTuple<IntervalProbabilityLatencyPrediction>>();
//				}else{
//					bp = new BufferedPipe<MVRelationalTuple<IntervalProbabilityLatency>>();
//					bp.subscribe(sink, 0);
//					buffer.add(bp);
//					latency = new LatencyCalculationPipe<MVRelationalTuple<IntervalProbabilityLatency>>();
//				}
//			} else if (!usePN) {
//				bp = new BufferedPipe<RelationalTuple<IntervalLatencyPriority>>();
//				bp.subscribe(sink, 0);
//				buffer.add(bp);
//				latency = new LatencyCalculationPipe<RelationalTuple<IntervalLatencyPriority>>();
//			} else {
//				bp = new BufferedPipe<RelationalTuple<PosNegLatencyPriority>>();
//				bp.subscribe(sink, 0);
//				buffer.add(bp);
//				latency = new LatencyCalculationPipe<RelationalTuple<PosNegLatencyPriority>>();
//			}
//			latency.subscribe(bp, 0);
//			// Setzen von Metadaten f�r bestimmte Scheduling-Strategien notwendig
//			BenchmarkTransform.setMetadataItems(latency, 1, 1, 1);
//
//			ISource<?> execplan = env.transform(queryLang, plan, false);
//			((IPipe) execplan).subscribe(latency, 0);
//			if (useMultiQueryApproach) {
//				env.addQuery(queryLang, Thread.NORM_PRIORITY, plan, sink);
//				System.out.println("  <plan>");
//				StringBuffer dumpTo = new StringBuffer();
//				ExecutionEnvironment.dumpPlan(sink, 2, dumpTo);
//				System.out.print(XML.escape(dumpTo.toString()));
//				System.out.println("  </plan>");
//			}
//
//		}
//		
//		/*** VIEWER START **/
//		if (useViewer){	
//			OdysseusModelProviderSink mp = new OdysseusModelProviderSink(sink);
//			wnd.setModelProvider(mp);
//		}			
//		/*** VIEWER END **/
//		
//		if (!useMultiQueryApproach) {
//			env.addQuery(queryLang, Thread.NORM_PRIORITY, null, sink);
//			System.out.println("  <plan>");
//			StringBuffer dumpTo = new StringBuffer();
//			ExecutionEnvironment.dumpPlan(sink, 2, dumpTo);
//			System.out.print(XML.escape(dumpTo.toString()));
//			System.out.println("  </plan>");
//		} else {
//			sink.open();
//		}
//		result.setStartTime(System.currentTimeMillis());
//		
//		
//		env.startScheduling();
//
//		try {
//			latch.await();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//
//		result.setEndTime(System.currentTimeMillis());
//		env.haltExecution();
//
//		if (!useMultiQueryApproach && printPlanMetadata) {
//			ExecutionEnvironment.printPlanMetadata(sink);
//		}
//
//		return result;
//	}
//
//}
