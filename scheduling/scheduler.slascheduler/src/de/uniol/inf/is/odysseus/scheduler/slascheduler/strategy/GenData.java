package de.uniol.inf.is.odysseus.scheduler.slascheduler.strategy;

public class GenData {
	public static void main(String[] args) {
		int noOfUsers = 250;
		int baseTime = 10;
		System.out.println("#PARSER CQL");
		System.out.println("#TRANSCFG Standard");
		System.out.println("#BUFFERPLACEMENT Query Buffer Placement");
		System.out.println("#DOQUERYSHARING FALSE");
		System.out.println("#QUERY");
		for (int i = 1; i <= noOfUsers; i++) {
			System.out.println("create user test" + i
					+ " identified by 'test';");
			System.out.println("grant role DSUser to test" + i + ";");
			System.out
					.println("create sla sla"
							+ i
							+ " time "
							+ (baseTime * i)
							+ " with (0.9,1,0),(0.8,0.9,100),(0.5,0.8,200),(0.4,0.5,300),(0,0.4,400);");
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
		for (int i = 1; i <= noOfUsers; i++) {
			System.out.println("#LOGIN test" + i + " test");
			System.out.println("#QUERY");
			System.out.println("puffer" + i
					+ " = buffer({type = 'Normal'},testinput)");
			System.out.println("benchmark" + i
					+ " = benchmark({selectivity = 1.0, time = 5000},puffer"
					+ i + ")");

		}

		System.out.println("#LOGIN System manager");
		System.out
				.println("#SCHEDULER \"Time based SLA scheduler max\" \"Round Robin\"");
		System.out.println("#STARTSCHEDULER");
		
	}
}
