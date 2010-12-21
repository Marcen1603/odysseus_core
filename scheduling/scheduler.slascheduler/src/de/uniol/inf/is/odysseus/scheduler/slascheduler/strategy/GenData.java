package de.uniol.inf.is.odysseus.scheduler.slascheduler.strategy;

public class GenData {

	static String[] slas = new String[] {
			"with (0.9,1,0),(0.8,0.9,100),(0.5,0.8,200),(0.4,0.5,300),(0,0.4,400);", // 0
			"with (0.8,1,0),(0.6,0.8,100),(0.4,0.6,100),(0.2,0.4,100),(0,0.2,100);", // 1
			"with (0.8,1,0),(0.6,0.8,100),(0.4,0.6,200),(0.2,0.4,400),(0,0.2,800);", // 2
			"with (0.8,1,0),(0.6,0.8,200),(0.4,0.6,400),(0.2,0.4,800),(0,0.2,1600);", // 3
			"with (0.0,0.00001,10000),(0.00001,1.0,0);",
			"with (0.8,1,0),(0.6,0.8,100),(0.4,0.6,200),(0.2,0.4,300),(0,0.2,400);" };

	static String[] testinput = new String[] {
			"testinput := testproducer({invertedpriorityratio = 10, parts = [[1000000, 5]]})",
			"testinput := testproducer({invertedpriorityratio = 10, parts = [[1000000, 100000]]})",
			"testinput := testproducer({invertedpriorityratio = 10, parts = [[1000, 100], [10000, 1000], [1000, 100]]})" };

	static String[] scheduler = new String[] { "" };
	static String homeDir = System.getProperty("user.home") + "/odysseus/";


	public static void main(String[] args) {
		int noOfUsers = 150;
		int baseTime = 5;
		int baseTimeOffset = 10;
		int sameLevel = 5;
		int sla = 2;
		int testInputNo = 0;
		boolean prio = false;
		boolean variateSLA = false;
		boolean variateSLATime = true;
		boolean dumpToFile = false;


		System.out.println("#DEFINE PROC_TIME 1000");
		System.out.println("#LOGIN System manager");
		if (!prio) {
			System.out.println("#ODYSSEUS_PARAM sla_history_size 10000");
		}
		System.out.println("#ODYSSEUS_PARAM scheduler_TimeSlicePerStrategy 10");
		System.out.println("#ODYSSEUS_PARAM debug_Scheduler_maxLines 1000000");
		System.out.println("#ODYSSEUS_PARAM scheduler_DebugFileName Versuch5");
		System.out.println("#PARSER CQL");
		System.out.println("#TRANSCFG Benchmark");
		System.out.println("#BUFFERPLACEMENT None");
		System.out.println("#DOQUERYSHARING FALSE");
		System.out.println("#QUERY");
		for (int i = 0; i < noOfUsers; i++) {
			System.out.println("create user test" + i
					+ " identified by 'test';");
			System.out.println("grant role DSUser to test" + i + ";");
			System.out.println("create sla sla"
					+ i
					+ " time "
					+ (baseTimeOffset+
					+ (variateSLATime ? (baseTime * ((i / sameLevel) + 1))
							: baseTime)));
			if (variateSLA) {
				System.out.println("with (0.0,0.1,"
						+ (noOfUsers - ((i / sameLevel)) * sameLevel) * 100
						+ "),(0.1,0.9,"
						+ (noOfUsers - ((i / sameLevel)) * sameLevel) * 100
						+ "),(0.9,1.0,0);");
			} else {
				System.out.println(" " + slas[sla]);
			}
			System.out.println("create tenant tenant" + i + " with sla" + i
					+ ";");
			System.out.println("add user test" + i + " to tenant" + i + ";");
		}
		System.out.println("#PARSER PQL");
		System.out.println("#QUERY");
		System.out.println(testinput[testInputNo]);
		System.out.println("#PARSER CQL");
		System.out.println("#QUERY");
		System.out.println("GRANT READ ON testinput TO Public;");

		System.out.println("#PARSER PQL");
		long run = System.currentTimeMillis();
		for (int i = 0; i < noOfUsers; i++) {
			System.out.println("#LOGIN test" + i + " test");
			System.out.println("#QUERY");
			System.out.println("puffer" + i
					+ " = buffer({type = 'Normal'},testinput)");
			System.out
					.println("benchmark"
							+ i
							+ " = benchmark({selectivity = 1.0, time = ${PROC_TIME}},puffer"
							+ i + ")");
				System.out.println("latencyCalc" + i
						+ " = CALCLATENCY(benchmark" + i + ")");	
				
			if (dumpToFile) {
				System.out.println("fileSink" + i + "{priority="
						+ (noOfUsers - ((i / sameLevel)) * sameLevel) + "}"
						+ " = FILESINK({file='" + homeDir + run + "/Query_" + i
						+ ".csv',fileType='csv',CACHESIZE=1000000},latencyCalc"
						+ i + ")");
			}
		}

		if (prio) {
			System.out
					.println("#SCHEDULER \"Simple Dynamic Priority Scheduler\" \"Round Robin\"");
		} else {
			System.out
					.println("#SCHEDULER \"Time based SLA scheduler max\" \"Round Robin\"");
		}
		System.out.println("#STARTSCHEDULER");

	}
}
