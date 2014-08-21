package de.uniol.inf.is.odysseus.rcp.commands;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;

public abstract class AbstractStartQueriesCommand extends AbstractQueryCommand {

	static final IQueryCommandAction start = new StartQueryCommandAction();
	
	static final IQueryCommandAction resume = new ResumeQueryCommandAction();


	public Object execute(List<Integer> selectedObj) {
		// Split to queries that should be started and that should be resumed
		List<QueryState> states = getQueryStates(selectedObj);
		List<Integer> toStart = new ArrayList<>();
		List<Integer> toResume = new ArrayList<>();
		for (int i=0;i<selectedObj.size();i++){
			if (states.get(i) == QueryState.SUSPENDED){
				toResume.add(selectedObj.get(i));
			}else{
				toStart.add(selectedObj.get(i));
			}
		}
		execute(toStart,start);
		execute(toResume,resume);
		return null;
	}
}
