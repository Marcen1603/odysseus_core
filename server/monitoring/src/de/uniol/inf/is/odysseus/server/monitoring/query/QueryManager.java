package de.uniol.inf.is.odysseus.server.monitoring.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IUpdateEventListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.server.monitoring.physicaloperator.OperatorLatency;

public class QueryManager implements IUpdateEventListener{
	List<IPhysicalQuery> subquerys = new ArrayList<IPhysicalQuery>();
	private IPhysicalQuery query; 

	public QueryManager(IPhysicalQuery q) {
		query=q;
		devideQuery();
		addEventListener();
	}

	private void devideQuery(){
		//TODO: Threaded Buffer splitting the plan
		subquerys.add(query);
	}

	private void addEventListener() {
		for (IPhysicalQuery q : subquerys) {
			new QueryEventListener(q);
		}
	}
	

	@Override
	public void eventOccured(String type) {
		// TODO Auto-generated method stub
		
	}
}
