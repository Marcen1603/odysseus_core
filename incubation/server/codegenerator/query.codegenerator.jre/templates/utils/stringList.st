stringList(listVariable, list)::=<<

	List<String> $listVariable$ = new ArrayList<String>();
	
	$list : {entry |
		$listVariable$.add("$entry$");
	}
	$
	

>>