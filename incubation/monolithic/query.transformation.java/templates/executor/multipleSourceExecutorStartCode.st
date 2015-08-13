multipleSourceExecutorStartCode(operatorList)::=<<

	MultipleSourceExecutor multipleSourceExecutor = new MultipleSourceExecutor();
	
	$operatorList: {operator| 
	 multipleSourceExecutor.addSource($operator$PO);
	}$
	multipleSourceExecutor.run();

>>