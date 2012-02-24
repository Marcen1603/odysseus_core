package de.uniol.inf.is.odysseus.scheduler.slascheduler.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import de.uniol.inf.is.odysseus.core.server.sla.factories.PenaltyFactory;
import de.uniol.inf.is.odysseus.core.server.sla.factories.ScopeFactory;
import de.uniol.inf.is.odysseus.core.sla.unit.TimeUnit;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.CostFunctionFactory;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.PriorityFunctionFactory;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.StarvationFreedomFactory;

public class GenQueries {

	private static final double SECS_TO_NANOS = 1000000000;

	private static int statsNumSources = 0;
	private static int statsNumBuffers = 0;
	private static int statsNumBenchmarks = 0;
	private static long statsNumElements = 0;
	private static long statsEstExecTime = 0;

	private static final String NEWLINE = "\n";
	private static final String TAB = "\t";
	private static final double SF_DECAY = 0.1;

	private static final String PRIO_FUNC_NAME = PriorityFunctionFactory.MAX;
	private static final boolean SLA_QUERY_SHARING_ENABLED = false;
	private static final String SF_FUNC_NAME = StarvationFreedomFactory.QUEUE_SIZE;
	private static final int TIME_SLICE = 10;
	private static final String QUERY_SHARING_COST_MODEL = "none";
	private static final String COST_FUNC_NAME = CostFunctionFactory.QUADRATIC_COST_FUNCTION;
	private static final double OP_SELECTIVITY = 1.0;
	public static final int OP_PROCESSING_TIME = 1500; // realistic 1500
	private static final int NUMBER_OF_USERS = 50;
	private static final int NUMBER_OF_QUERIES_PER_USER = 4;
	private static final int NUMBER_OF_SLAS = 5;
	private static final String PENALTY_NAME = PenaltyFactory.ABSOLUTE_PENALTY;
	private static final int NUMBER_OF_SERVICE_LEVELS = 3;
	private static final String SLA_SCOPE = ScopeFactory.SCOPE_AVERAGE;
	private static final boolean COMPLEX_QUERIES_ENABLED = false;

	private static final int ALTERNATIVE_SLA_RATIO = 10;
	private static int ALTERNATIVE_SLA_COUNTER = 0;
	private static final boolean ALTERNATIVE_SLA_ENABLED = true;
	private static final int ALTERNATIVE_BEST_SLA_PRIO = 10;

	private static final int NUMBER_OF_SIMULATIONS = 10;
	private static int currentNumberOfSimulation = 0;

	private static final int DATA_RATE_BURST = 10000;
	private static final int DATA_RATE_HIGH = 100;
	private static final int DATA_RATE_MID = 100;
	private static final int DATA_RATE_LOW = 10;
	private static final int DATA_RATE_VERY_LOW = 1;

	private static String odysseusDefaultHome = String.format("%s/%sodysseus/", System.getProperty("user.home"),getDot(System.getProperty("os.name")));
	private static String homeDir;
	static{
		homeDir = System.getenv("ODYSSEUS_HOME");
		if (homeDir == null || homeDir.length() == 0){
			homeDir = odysseusDefaultHome;
		}
	}
	
	/**
	 * Returns a dot on specific operating systems: unix,linux, and mac.
	 * 
	 */
	private static String getDot(String os){
		os = os.toLowerCase();
		if((os.indexOf( "win" ) >= 0)){
			//Windows
			return "";
		}else if((os.indexOf( "mac" ) >= 0)){
			//Macintosh 
			return ".";
		}else if((os.indexOf( "nix") >=0 || os.indexOf( "nux") >=0)){
			//Unix
			return ".";
		}else{
			//All other
			return "";
		}
	}

	static String[] testinput = new String[] {
			" := testproducer({invertedpriorityratio = 10, parts = [[1000000, 5]]})",
			" := testproducer({invertedpriorityratio = 10, parts = [[1000000, 100000]]})",
			" := testproducer({invertedpriorityratio = 10, parts = [[1000, 100], [10000, 1000], [1000, 100]]})" };

	static int[][] dataRates0 = { { 1000000, 5 } };
	static int[][] dataRates1 = { { 1000000, 100000 } };
	static int[][] dataRates2 = { { 1000, 100 }, { 10000, 1000 }, { 1000, 100 } };

	static int[][] dataRates3 = { calcDataRate(60, DATA_RATE_VERY_LOW),
			calcDataRate(60, DATA_RATE_LOW), calcDataRate(60, DATA_RATE_MID),
			calcDataRate(60, DATA_RATE_HIGH),
			calcDataRate(60, DATA_RATE_BURST),
			calcDataRate(60, DATA_RATE_HIGH), calcDataRate(60, DATA_RATE_MID),
			calcDataRate(60, DATA_RATE_LOW),
			calcDataRate(120, DATA_RATE_VERY_LOW) };

	static int[][] dataRates4 = { calcDataRate(180, DATA_RATE_VERY_LOW),
			calcDataRate(60, DATA_RATE_BURST),
			calcDataRate(120, DATA_RATE_VERY_LOW),
			calcDataRate(60, DATA_RATE_BURST),
			calcDataRate(180, DATA_RATE_VERY_LOW) };

	static int[][] dataRates5 = { calcDataRate(120, DATA_RATE_LOW),
			calcDataRate(120, DATA_RATE_MID), calcDataRate(120, DATA_RATE_LOW),
			calcDataRate(120, DATA_RATE_MID), calcDataRate(120, DATA_RATE_LOW) };

	static int[][] dataRates6 = { calcDataRate(90, DATA_RATE_LOW),
			calcDataRate(10, DATA_RATE_HIGH), calcDataRate(110, DATA_RATE_LOW),
			calcDataRate(10, DATA_RATE_HIGH), calcDataRate(110, DATA_RATE_LOW),
			calcDataRate(10, DATA_RATE_HIGH), calcDataRate(110, DATA_RATE_LOW),
			calcDataRate(10, DATA_RATE_HIGH), calcDataRate(110, DATA_RATE_LOW),
			calcDataRate(10, DATA_RATE_HIGH), calcDataRate(110, DATA_RATE_LOW),
			calcDataRate(10, DATA_RATE_HIGH), calcDataRate(110, DATA_RATE_LOW),
			calcDataRate(10, DATA_RATE_HIGH), calcDataRate(110, DATA_RATE_LOW),
			calcDataRate(10, DATA_RATE_HIGH), calcDataRate(110, DATA_RATE_LOW),
			calcDataRate(10, DATA_RATE_HIGH), calcDataRate(110, DATA_RATE_LOW),
			calcDataRate(10, DATA_RATE_HIGH), calcDataRate(110, DATA_RATE_LOW),
			calcDataRate(10, DATA_RATE_HIGH), calcDataRate(110, DATA_RATE_LOW),
			calcDataRate(10, DATA_RATE_HIGH), calcDataRate(110, DATA_RATE_LOW),
			calcDataRate(10, DATA_RATE_HIGH), calcDataRate(110, DATA_RATE_LOW),
			calcDataRate(10, DATA_RATE_HIGH), calcDataRate(110, DATA_RATE_LOW),
			calcDataRate(10, DATA_RATE_HIGH), calcDataRate(20, DATA_RATE_LOW) };

	static int[][] dataRates7 = { calcDataRate(30, DATA_RATE_LOW),
			calcDataRate(10, DATA_RATE_HIGH), calcDataRate(110, DATA_RATE_LOW),
			calcDataRate(10, DATA_RATE_HIGH), calcDataRate(110, DATA_RATE_LOW),
			calcDataRate(10, DATA_RATE_HIGH), calcDataRate(110, DATA_RATE_LOW),
			calcDataRate(10, DATA_RATE_HIGH), calcDataRate(110, DATA_RATE_LOW),
			calcDataRate(10, DATA_RATE_HIGH), calcDataRate(110, DATA_RATE_LOW),
			calcDataRate(10, DATA_RATE_HIGH), calcDataRate(110, DATA_RATE_LOW),
			calcDataRate(10, DATA_RATE_HIGH), calcDataRate(110, DATA_RATE_LOW),
			calcDataRate(10, DATA_RATE_HIGH), calcDataRate(110, DATA_RATE_LOW),
			calcDataRate(10, DATA_RATE_HIGH), calcDataRate(110, DATA_RATE_LOW),
			calcDataRate(10, DATA_RATE_HIGH), calcDataRate(110, DATA_RATE_LOW),
			calcDataRate(10, DATA_RATE_HIGH), calcDataRate(110, DATA_RATE_LOW),
			calcDataRate(10, DATA_RATE_HIGH), calcDataRate(110, DATA_RATE_LOW),
			calcDataRate(10, DATA_RATE_HIGH), calcDataRate(110, DATA_RATE_LOW),
			calcDataRate(10, DATA_RATE_HIGH), calcDataRate(110, DATA_RATE_LOW),
			calcDataRate(10, DATA_RATE_HIGH), calcDataRate(80, DATA_RATE_LOW) };

	static int[][][] DATA_RATES = { dataRates0, dataRates1, dataRates2,
			dataRates3, dataRates4, dataRates5, dataRates6, dataRates7 };

	static int[] DATA_RATE_INDICES = { 6, 7 };
	static int dataRatePos = 0;

	public static void main(String[] args) {
		String scriptSLA = createScriptSLA();
		writeScriptFile("sla.qry", scriptSLA);
		while (currentNumberOfSimulation < NUMBER_OF_SIMULATIONS) {
			String scriptOps = createScriptOps();
			writeScriptFile("ops" + currentNumberOfSimulation + ".qry",
					scriptOps);
			currentNumberOfSimulation++;
		}
		String scriptRun = "///OdysseusScript" + NEWLINE + "#PARSER CQL"
				+ NEWLINE + "#TRANSCFG Standard" + NEWLINE + "#STARTQUERIES";
		writeScriptFile("run.qry", scriptRun);
		System.out.println(scriptSLA);
		System.out.println(createScriptOps());
	}

	private static String createScriptSLA() {
		StringBuilder sb = new StringBuilder();
		sb.append(createSettingComment());
		sb.append("#PARSER CQL").append(NEWLINE);
		sb.append(createGlobalSettings());
		sb.append("#QUERY").append(NEWLINE);
		for (int i = 0; i < NUMBER_OF_USERS; i++) {
			sb.append(createUser(i));
		}

		// create SLA
		if (ALTERNATIVE_SLA_ENABLED) {
			sb.append(createAlternativeSLA());
		} else {
			if (SLA_SCOPE.equals(ScopeFactory.SCOPE_AVERAGE)) {
				for (int i = 0; i < NUMBER_OF_SLAS; i++) {
					sb.append(createSLA(i, (i + 3) * 1000,
							ScopeFactory.SCOPE_AVERAGE, 300,
							TimeUnit.s.toString(),
							calcThresholds(ScopeFactory.SCOPE_AVERAGE, i),
							calcPenaltyCosts(i), PENALTY_NAME));
				}
			} else if (SLA_SCOPE.equals(ScopeFactory.SCOPE_RATE)) {
				for (int i = 0; i < NUMBER_OF_SLAS; i++) {
					sb.append(createSLA(i, (i + 1) * 2000,
							ScopeFactory.SCOPE_RATE, 300,
							TimeUnit.s.toString(),
							calcThresholds(ScopeFactory.SCOPE_RATE, i),
							calcPenaltyCosts(i), PENALTY_NAME));
				}
			}
		}

		sb.append(NEWLINE);

		sb.append(createStatisticsComments());
		return sb.toString();
	}

	private static String createScriptOps() {
		StringBuilder sb = new StringBuilder();
		// Create queries
		sb.append("/// Move the following code into a new script file!")
				.append(NEWLINE);
		sb.append(createSettingComment());
		sb.append(createGlobalSettings());
		for (int i = 0; i < NUMBER_OF_USERS; i++) {
			for (int k = 0; k < NUMBER_OF_QUERIES_PER_USER; k++) {
				sb.append(createTestInput(i, k));
			}
		}
		sb.append("#PARSER PQL").append(NEWLINE);
		for (int i = 0; i < NUMBER_OF_USERS; i++) {
			sb.append(createLogin(i));
			for (int k = 0; k < NUMBER_OF_QUERIES_PER_USER; k++) {
				if (COMPLEX_QUERIES_ENABLED) {
					if (k == 0) {
						sb.append(createSimpleQuery(i, i % NUMBER_OF_SLAS));
					} else {
						sb.append(createComplexQuery(i, i % NUMBER_OF_SLAS,
								k + 1));
					}
				} else {
					sb.append(createSimpleQuery(i, i % NUMBER_OF_SLAS));
				}
			}
		}
		// sb.append("#STARTQUERIES").append(NEWLINE);
		return sb.toString();
	}

	private static String createUser(int number) {
		StringBuilder sb = new StringBuilder();
		sb.append("create user test").append(number)
				.append(" identified by 'test';").append(NEWLINE)
				.append("grant role DSUser to test").append(number).append(";")
				.append(NEWLINE);
		return sb.toString();
	}

	private static String createSimpleQuery(int number, int slaNumber) {
		StringBuilder sb = new StringBuilder();
		sb.append(createQueryParams(slaNumber));
		sb.append(createBuffer(number, 0));
		sb.append(createBenchmark(number, 0, slaNumber, true));
		// sb.append("#STARTQUERIES").append(NEWLINE);
		statsEstExecTime += OP_PROCESSING_TIME;
		return sb.toString();
	}

	private static String createBenchmark(int number, int subnumber,
			int slaNumber, boolean isSink) {
		StringBuilder sb = new StringBuilder();
		sb.append("benchmark").append(number)
				.append(formatSubnumber(subnumber));
		if (isSink) {
			if (ALTERNATIVE_SLA_ENABLED) {
				sb.append("{priority=").append(getALternativeSLANumber())
						.append("}");
			} else {
				sb.append("{priority=").append(NUMBER_OF_SLAS - slaNumber)
						.append("}");
			}

		}
		sb.append(" = benchmark({selectivity = ").append(OP_SELECTIVITY)
				.append(", time = ").append(OP_PROCESSING_TIME)
				.append("},puffer").append(number)
				.append(formatSubnumber(subnumber)).append(")").append(NEWLINE);
		statsNumBenchmarks++;
		return sb.toString();
	}

	private static String createBenchmark2In(int number, int subnumber,
			int slaNumber, boolean isSink) {
		StringBuilder sb = new StringBuilder();
		sb.append("benchmark").append(number)
				.append(formatSubnumber(subnumber));
		if (isSink) {
			if (ALTERNATIVE_SLA_ENABLED) {
				sb.append("{priority=").append(getALternativeSLANumber())
						.append("}");
			} else {
				sb.append("{priority=").append(NUMBER_OF_SLAS - slaNumber)
						.append("}");
			}
		}
		sb.append(" = benchmark({selectivity = ").append(OP_SELECTIVITY)
				.append(", time = ").append(OP_PROCESSING_TIME)
				.append("},puffer").append(number);
		if (subnumber == 0) {
			sb.append(formatSubnumber(subnumber)).append(", puffer")
					.append(number).append(formatSubnumber(subnumber + 1))
					.append(")").append(NEWLINE);
		} else {
			sb.append(formatSubnumber(subnumber + 1)).append(", benchmark")
					.append(number).append(formatSubnumber(subnumber - 1))
					.append(")").append(NEWLINE);
		}
		statsNumBenchmarks++;
		return sb.toString();
	}

	private static String createBuffer(int number, int subNumber) {
		StringBuilder sb = new StringBuilder();
		sb.append("puffer").append(number).append(formatSubnumber(subNumber))
				.append(" = buffer({type = 'Normal'},testinput").append(number)
				.append(formatSubnumber(subNumber)).append(")").append(NEWLINE);
		statsNumBuffers++;
		return sb.toString();
	}

	private static String createComplexQuery(int number, int slaNumber,
			int numOfSources) {
		StringBuilder sb = new StringBuilder();
		sb.append(createQueryParams(slaNumber));
		for (int i = 0; i < numOfSources; i++) {
			sb.append(createBuffer(number, i));
		}
		for (int i = 0; i < numOfSources - 1; i++) {
			sb.append(createBenchmark2In(number, i, slaNumber,
					(i == numOfSources - 2) ? true : false));
		}
		// sb.append("#STARTQUERIES").append(NEWLINE);
		statsEstExecTime += (numOfSources - 1) * OP_PROCESSING_TIME;
		return sb.toString();
	}

	private static String formatSubnumber(int number) {
		return "" + ((char) ('A' + currentNumberOfSimulation)) + number;
	}

	private static String createQueryParams(int slaNumber) {
		StringBuilder sb = new StringBuilder();
		if (ALTERNATIVE_SLA_ENABLED) {
			ALTERNATIVE_SLA_COUNTER++;
			sb.append("#SLA sla").append(getALternativeSLANumber())
					.append(NEWLINE);
		} else {
			sb.append("#SLA sla").append(slaNumber).append(NEWLINE);
		}
		sb.append("#ADDQUERY").append(NEWLINE);
		return sb.toString();
	}

	private static int getALternativeSLANumber() {
		boolean isHighPrio = ALTERNATIVE_SLA_COUNTER % ALTERNATIVE_SLA_RATIO == 0;
		if (isHighPrio) {
			return ALTERNATIVE_BEST_SLA_PRIO;
		} else {
			return 1;
		}
	}

	private static String createSLA(int number, double metricValue,
			String scope, int windowSize, String windowUnit,
			double[] slThreshold, int[] penaltyCost, String penaltyName) {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE SLA sla").append(number).append(" WITH")
				.append(NEWLINE);
		sb.append(TAB).append("METRIC (latency, ").append(metricValue)
				.append(", ms),").append(NEWLINE);
		sb.append(TAB).append("SCOPE (").append(scope).append("),")
				.append(NEWLINE);
		sb.append(TAB).append("IN (").append(windowSize).append(", ")
				.append(windowUnit).append("),").append(NEWLINE);
		for (int i = 0; i < slThreshold.length; i++) {
			sb.append(createServiceLevel(slThreshold[i], penaltyName,
					penaltyCost[i]));
			if (i != slThreshold.length - 1) {
				sb.append(",");
			} else {
				sb.append(";");
			}
			sb.append(NEWLINE);
		}

		return sb.toString();
	}

	private static String createAlternativeSLA() {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE SLA sla").append(ALTERNATIVE_BEST_SLA_PRIO)
				.append(" WITH").append(NEWLINE);
		sb.append("METRIC (latency, 4000.0, ms),").append(NEWLINE);
		sb.append("SCOPE (average),").append(NEWLINE);
		sb.append("IN (300, s),").append(NEWLINE);
		sb.append("SERVICELEVEL (2000.0, PENALTY (absolute, 1000)),").append(
				NEWLINE);
		sb.append("SERVICELEVEL (3000.0, PENALTY (absolute, 4000)),").append(
				NEWLINE);
		sb.append("SERVICELEVEL (4000.0, PENALTY (absolute, 10000));").append(
				NEWLINE);
		sb.append("CREATE SLA sla1 WITH").append(NEWLINE);
		sb.append("METRIC (latency, 10000.0, ms),").append(NEWLINE);
		sb.append("SCOPE (average),").append(NEWLINE);
		sb.append("IN (300, s),").append(NEWLINE);
		sb.append("SERVICELEVEL (5000.0, PENALTY (absolute, 200)),").append(
				NEWLINE);
		sb.append("SERVICELEVEL (7500.0, PENALTY (absolute, 600)),").append(
				NEWLINE);
		sb.append("SERVICELEVEL (10000.0, PENALTY (absolute, 2000));").append(
				NEWLINE);
		return sb.toString();
	}

	private static String createServiceLevel(double threshold,
			String penaltyName, int penaltyCost) {
		StringBuilder sb = new StringBuilder();
		sb.append(TAB).append("SERVICELEVEL (").append(threshold)
				.append(", PENALTY (").append(penaltyName).append(", ")
				.append(penaltyCost).append("))");
		return sb.toString();
	}

	private static String createGlobalSettings() {
		StringBuilder sb = new StringBuilder();
		sb.append("#TRANSCFG Standard Latency").append(NEWLINE);
		sb.append("#BUFFERPLACEMENT None").append(NEWLINE);
		sb.append("#ODYSSEUS_PARAM sla_starvationFreedomDecay ")
				.append(SF_DECAY).append(NEWLINE);
		sb.append("#ODYSSEUS_PARAM sla_prioFuncName ").append(PRIO_FUNC_NAME)
				.append(NEWLINE);
		sb.append("#ODYSSEUS_PARAM sla_querySharing ")
				.append(SLA_QUERY_SHARING_ENABLED).append(NEWLINE);
		sb.append("#ODYSSEUS_PARAM sla_starvationFreedomFuncName ")
				.append(SF_FUNC_NAME).append(NEWLINE);
		sb.append("#ODYSSEUS_PARAM scheduler_TimeSlicePerStrategy ")
				.append(TIME_SLICE).append(NEWLINE);
		sb.append("#ODYSSEUS_PARAM sla_querySharingCostModel ")
				.append(QUERY_SHARING_COST_MODEL).append(NEWLINE);
		sb.append("#ODYSSEUS_PARAM sla_costFunctionName ")
				.append(COST_FUNC_NAME).append(NEWLINE);
		sb.append("#DOQUERYSHARING FALSE").append(NEWLINE);
		return sb.toString();
	}

	private static String createLogin(int userNumber) {
		StringBuilder sb = new StringBuilder();
		sb.append("#LOGIN test").append(userNumber).append(" test")
				.append(NEWLINE);
		return sb.toString();
	}

	private static String createTestInput(int number, int subNumber) {
		StringBuilder sb = new StringBuilder();
		sb.append("#PARSER PQL").append(NEWLINE);
		sb.append("#QUERY").append(NEWLINE);
		sb.append("testinput")
				.append(number)
				.append(formatSubnumber(subNumber))
				.append(" := testproducer({invertedpriorityratio = 10, parts = [")
				.append(createTestInputParam()).append("]})").append(NEWLINE);
		sb.append("#PARSER CQL").append(NEWLINE);
		sb.append("#QUERY").append(NEWLINE);
		sb.append("GRANT READ ON testinput TO Public;").append(NEWLINE);
		statsNumSources++;
		return sb.toString();
	}

	private static String createTestInputParam() {
		StringBuilder sb = new StringBuilder();
		int[][] param = DATA_RATES[nextDataRateIndex()];
		for (int i = 0; i < param.length; i++) {
			if (i != 0) {
				sb.append(", ");
			}
			sb.append("[");
			for (int k = 0; k < param[i].length; k++) {
				sb.append(param[i][k]);
				if (k == 0)
					sb.append(", ");
			}
			sb.append("]");
			statsNumElements += param[i][0];
		}
		return sb.toString();
	}

	private static double[] calcThresholds(String scope, int slNumber) {
		double[] thresholds = new double[NUMBER_OF_SERVICE_LEVELS];
		if (scope.equals(ScopeFactory.SCOPE_AVERAGE)) {
			for (int i = 0; i < NUMBER_OF_SERVICE_LEVELS; i++) {
				thresholds[i] = 1000 * (i + 1) + slNumber * 1000;
			}
		} else if (scope.equals(ScopeFactory.SCOPE_NUMBER)) {
			throw new RuntimeException("not implemented");
		} else if (scope.equals(ScopeFactory.SCOPE_SINGLE)) {
			throw new RuntimeException("not implemented");
		} else if (scope.equals(ScopeFactory.SCOPE_RATE)) {
			for (int i = 0; i < NUMBER_OF_SERVICE_LEVELS; i++) {
				thresholds[i] = 1.0 - ((i + 1) * 0.1) - (slNumber * 0.1);
			}
		}
		return thresholds;
	}

	private static int[] calcPenaltyCosts(int slNumber) {
		int penaltyCost[] = new int[NUMBER_OF_SERVICE_LEVELS];
		for (int i = 0; i < penaltyCost.length; i++) {
			penaltyCost[i] = 1000 * (i + 1) + 2000
					* (NUMBER_OF_SLAS - slNumber);
		}
		return penaltyCost;
	}

	private static String createSettingComment() {
		StringBuilder sb = new StringBuilder();
		sb.append("/// Settings of generated query:").append(NEWLINE)
				.append("///\t PRIO_FUNC_NAME=").append(PRIO_FUNC_NAME)
				.append(NEWLINE).append("///\t SLA_QUERY_SHARING_ENABLED=")
				.append(SLA_QUERY_SHARING_ENABLED).append(NEWLINE)
				.append("///\t SF_FUNC_NAME=").append(SF_FUNC_NAME)
				.append(NEWLINE).append("///\t QUERY_SHARING_COST_MODEL=")
				.append(QUERY_SHARING_COST_MODEL).append(NEWLINE)
				.append("///\t COST_FUNC_NAME=").append(COST_FUNC_NAME)
				.append(NEWLINE).append("///\t DATA_RATE_INDICES=")
				.append(arrayToString(DATA_RATE_INDICES)).append(NEWLINE)
				.append("///\t OP_SELECTIVITY=").append(OP_SELECTIVITY)
				.append(NEWLINE).append("///\t OP_PROCESSING_TIME=")
				.append(OP_PROCESSING_TIME).append(NEWLINE)
				.append("///\t NUMBER_OF_USERS=").append(NUMBER_OF_USERS)
				.append(NEWLINE).append("///\t NUMBER_OF_QUERIES_PER_USER=")
				.append(NUMBER_OF_QUERIES_PER_USER).append(NEWLINE)
				.append("///\t NUMBER_OF_SLAS=").append(NUMBER_OF_SLAS)
				.append(NEWLINE).append("///\t PENALTY_NAME=")
				.append(PENALTY_NAME).append(NEWLINE)
				.append("///\t NUMBER_OF_SERVICE_LEVELS=")
				.append(NUMBER_OF_SERVICE_LEVELS).append(NEWLINE)
				.append("///\t SLA_SCOPE=").append(SLA_SCOPE).append(NEWLINE)
				.append("///\t COMPLEX_QUERIES_ENABLED=")
				.append(COMPLEX_QUERIES_ENABLED).append(NEWLINE)
				.append("///\t ALTERNATIVE_SLA_ENABLED=")
				.append(ALTERNATIVE_SLA_ENABLED).append(NEWLINE)
				.append("///\t ALTERNATIVE_SLA_RATIO=")
				.append(ALTERNATIVE_SLA_RATIO).append(NEWLINE)
				.append("///\t ALTERNATIVE_BEST_SLA_PRIO=")
				.append(ALTERNATIVE_BEST_SLA_PRIO).append(NEWLINE);

		return sb.toString();
	}

	private static Object arrayToString(int[] array) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < array.length; i++) {
			if (i > 0) {
				sb.append(", ");
			}
			sb.append(array[i]);
		}
		sb.append("]");
		return sb.toString();
	}

	private static int[] calcDataRate(int lengthInSeconds, int load) {
		int[] dataRate = new int[2];
		dataRate[0] = load * lengthInSeconds;
		dataRate[1] = load;
		return dataRate;
	}

	private static String createStatisticsComments() {
		StringBuilder sb = new StringBuilder();
		sb.append("/// Statistics: ").append(NEWLINE);
		sb.append("///\t number of sources: ").append(statsNumSources)
				.append(NEWLINE);
		sb.append("///\t number of buffers: ").append(statsNumBuffers)
				.append(NEWLINE);
		sb.append("///\t number of benchmark operators: ")
				.append(statsNumBenchmarks).append(NEWLINE);
		sb.append("///\t number of generated tuples: ")
				.append(statsNumElements).append(NEWLINE);
		sb.append("///\t estimated execution time: ")
				.append(statsEstExecTime / SECS_TO_NANOS * statsNumElements)
				.append(" seconds").append(NEWLINE);
		return sb.toString();
	}

	private static int nextDataRateIndex() {
		int next = DATA_RATE_INDICES[dataRatePos];
		dataRatePos++;
		if (dataRatePos >= DATA_RATE_INDICES.length) {
			dataRatePos = 0;
		}
		return next;
	}

	private static void writeScriptFile(String fileName, String code) {
		File file = new File(homeDir, fileName);
		try {
			FileWriter writer = new FileWriter(file);
			writer.write(code);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
