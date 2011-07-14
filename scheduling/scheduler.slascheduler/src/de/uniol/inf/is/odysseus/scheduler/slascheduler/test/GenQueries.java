package de.uniol.inf.is.odysseus.scheduler.slascheduler.test;

import de.uniol.inf.is.odysseus.scheduler.slascheduler.CostFunctionFactory;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.PriorityFunctionFactory;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.StarvationFreedomFactory;
import de.uniol.inf.is.odysseus.sla.factories.PenaltyFactory;
import de.uniol.inf.is.odysseus.sla.factories.ScopeFactory;

public class GenQueries {

	private static final String NEWLINE = "\n";
	private static final String TAB = "\t";
	private static final double SF_DECAY = 0.1;

	private static int[] slThresholdsAvg = { 500, 1000 };
	private static int[] penaltyCosts = { 500, 1000, 1500 };

	private static final String PRIO_FUNC_NAME = PriorityFunctionFactory.MAX;
	private static final boolean SLA_QUERY_SHARING_ENABLED = false;
	private static final String SF_FUNC_NAME = StarvationFreedomFactory.QUEUE_SIZE;
	private static final int TIME_SLICE = 10;
	private static final String QUERY_SHARING_COST_MODEL = "none";
	private static final String COST_FUNC_NAME = CostFunctionFactory.QUADRATIC_COST_FUNCTION;
	private static final int TEST_INPUT_NUMBER = 0;
	private static final double OP_SELECTIVITY = 1.0;
	private static final int OP_PROCESSING_TIME = 1000;
	private static final int NUMBER_OF_USERS = 3;
	private static final int NUMBER_OF_SLAS = 3;
	private static final String PENALTY_NAME = PenaltyFactory.ABSOLUTE_PENALTY;
	private static final int NUMBER_OF_SERVICE_LEVELS = 3;

	static String[] testinput = new String[] {
			"testinput := testproducer({invertedpriorityratio = 10, parts = [[1000000, 5]]})",
			"testinput := testproducer({invertedpriorityratio = 10, parts = [[1000000, 100000]]})",
			"testinput := testproducer({invertedpriorityratio = 10, parts = [[1000, 100], [10000, 1000], [1000, 100]]})" };

	public static void main(String[] args) {
		StringBuilder sb = new StringBuilder();
		sb.append("#PARSER CQL").append(NEWLINE);
		sb.append(createGlobalSettings());
		sb.append("#QUERY").append(NEWLINE);
		for (int i = 0; i < NUMBER_OF_USERS; i++) {
			sb.append(createUser(i));
		}

		for (int i = 0; i < NUMBER_OF_SLAS; i++) {
			sb.append(createSLA(i, (i + 1) * 200, ScopeFactory.SCOPE_AVERAGE,
					10, "d", calcThresholds(ScopeFactory.SCOPE_AVERAGE, i),
					calcPenaltyCosts(i), PENALTY_NAME));
		}
		sb.append(NEWLINE);

		sb.append("/// Move the following code into a new script file!")
				.append(NEWLINE);
		sb.append(createGlobalSettings());
		sb.append(createTestInput());
		sb.append("#PARSER PQL").append(NEWLINE);
		for (int i = 0; i < NUMBER_OF_USERS; i++) {
			sb.append(createLogin(i));
			sb.append(createQuery(i, i % NUMBER_OF_SLAS));
		}

		System.out.println(sb.toString());
	}

	private static String createUser(int number) {
		StringBuilder sb = new StringBuilder();
		sb.append("create user test").append(number)
				.append(" identified by 'test';").append(NEWLINE)
				.append("grant role DSUser to test").append(number).append(";")
				.append(NEWLINE);
		return sb.toString();
	}

	private static String createQuery(int number, int slaNumber) {
		StringBuilder sb = new StringBuilder();
		sb.append("#SLA sla").append(slaNumber).append(NEWLINE);
		sb.append("#ADDQUERY").append(NEWLINE);
		sb.append("puffer").append(number)
				.append(" = buffer({type = 'Normal'},testinput)")
				.append(NEWLINE);
		sb.append("benchmark").append(number)
				.append(" = benchmark({selectivity = ").append(OP_SELECTIVITY)
				.append(", time = ").append(OP_PROCESSING_TIME)
				.append("},puffer").append(number).append(")").append(NEWLINE);
		sb.append("#STARTQUERIES").append(NEWLINE);
		return sb.toString();
	}

	private static String createSLA(int number, double metricValue,
			String scope, int windowSize, String windowUnit, int[] slThreshold,
			int[] penaltyCost, String penaltyName) {
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

	private static String createServiceLevel(int threshold, String penaltyName,
			int penaltyCost) {
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

	private static String createTestInput() {
		StringBuilder sb = new StringBuilder();
		sb.append("#PARSER PQL").append(NEWLINE);
		sb.append("#QUERY").append(NEWLINE);
		sb.append(testinput[TEST_INPUT_NUMBER]).append(NEWLINE);
		sb.append("#PARSER CQL").append(NEWLINE);
		sb.append("#QUERY").append(NEWLINE);
		sb.append("GRANT READ ON testinput TO Public;").append(NEWLINE);
		return sb.toString();
	}

	private static int[] calcThresholds(String scope, int slNumber) {
		int[] thresholds = new int[NUMBER_OF_SERVICE_LEVELS];
		if (scope.equals(ScopeFactory.SCOPE_AVERAGE)) {
			for (int i = 0; i < NUMBER_OF_SERVICE_LEVELS; i++) {
				thresholds[i] = 100 * (i + 1) + slNumber * 200;
			}
		} else if (scope.equals(ScopeFactory.SCOPE_NUMBER)) {
			throw new RuntimeException("not implemented");
		} else if (scope.equals(ScopeFactory.SCOPE_SINGLE)) {
			throw new RuntimeException("not implemented");
		}
		return thresholds;
	}

	private static int[] calcPenaltyCosts(int slNumber) {
		int penaltyCost[] = new int[NUMBER_OF_SERVICE_LEVELS];
		for (int i = 0; i < penaltyCost.length; i++) {
			penaltyCost[i] = 500 * (i + 1) + 1000
					* (NUMBER_OF_SERVICE_LEVELS - slNumber);
		}
		return penaltyCost;
	}

}
