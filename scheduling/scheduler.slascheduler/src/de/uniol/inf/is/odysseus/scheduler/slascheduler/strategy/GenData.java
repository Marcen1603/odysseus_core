package de.uniol.inf.is.odysseus.scheduler.slascheduler.strategy;

public class GenData {
	
	static String[] slas = new String[]{"with (0.9,1,0),(0.8,0.9,100),(0.5,0.8,200),(0.4,0.5,300),(0,0.4,400);",
		"with (0.8,1,0),(0.6,0.8,100),(0.4,0.6,200),(0.2,0.4,400),(0,0.2,800);",
		"with (0.0,0.00001,10000),(0.00001,1.0,0);",
		"with (0.8,1,0),(0.6,0.8,100),(0.4,0.6,200),(0.2,0.4,300),(0,0.2,400);"};
	
	
	public static void main(String[] args) {
		int noOfUsers = 150;
		int baseTime = 5;
		int sameLevel = 5;
		int sla = 3;
		boolean prio = true;
		System.out.println("#DEFINE PROC_TIME 5000");
		System.out.println("#LOGIN System manager");
		if (!prio){
			System.out.println("#ODYSSEUS_PARAM sla_history_size 1000");
		}
		System.out.println("#PARSER CQL");
		System.out.println("#TRANSCFG Standard");
		System.out.println("#BUFFERPLACEMENT Query Buffer Placement");
		System.out.println("#DOQUERYSHARING FALSE");
		System.out.println("#QUERY");
		for (int i = 0; i < noOfUsers; i++) {
			System.out.println("create user test" + i
					+ " identified by 'test';");
			System.out.println("grant role DSUser to test" + i + ";");
			System.out
					.println("create sla sla"
							+ i
							+ " time "
							+ (baseTime * ((i/sameLevel)+1))
							+ " "+ slas[sla]);
			System.out.println("create tenant tenant" + i + " with sla" + i
					+ ";");
			System.out.println("add user test" + i + " to tenant" + i + ";");
		}
		System.out.println("#PARSER PQL");
		System.out.println("#QUERY");
		System.out
				.println("testinput := testproducer({invertedpriorityratio = 10, parts = [[100000, 500]]})");
		System.out.println("#PARSER CQL");
		System.out.println("#QUERY");
		System.out.println("GRANT READ ON testinput TO Public;");
		
		System.out.println("#PARSER PQL");
		System.out.println("#TRANSCFG Standard");
		System.out.println("#BUFFERPLACEMENT Query Buffer Placement");
		for (int i = 0; i < noOfUsers; i++) {
			System.out.println("#LOGIN test" + i + " test");
			System.out.println("#QUERY");
			System.out.println("puffer" + i
					+ " = buffer({type = 'Normal'},testinput)");
			System.out.println("benchmark" + i
					+ "{priority="+ (noOfUsers-((i/sameLevel))*sameLevel) +"}"
					+ " = benchmark({selectivity = 1.0, time = ${PROC_TIME}},puffer"
					+ i + ")");

		}
		
		if (prio){
			System.out
			.println("#SCHEDULER \"Simple Dynamic Priority Scheduler\" \"Round Robin\"");
		}else{
		System.out
				.println("#SCHEDULER \"Time based SLA scheduler max\" \"Round Robin\"");
		}
		System.out.println("#STARTSCHEDULER");
		
	}
}
