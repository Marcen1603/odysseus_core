multipleSourceSchedulerStartCode(operatorList)::=<<

	MultipleSourceScheduler multipleSourceScheduler = new MultipleSourceScheduler();
	
	$operatorList: {operator| 
	multipleSourceScheduler.addSource($operator$PO);
	}$
	multipleSourceScheduler.run();

>>