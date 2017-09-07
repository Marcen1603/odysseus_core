package de.uniol.inf.is.odysseus.server.monitoring.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

public class QueryManager {
	List<IPhysicalQuery> subquerys = new ArrayList<IPhysicalQuery>();

	public QueryManager(IPhysicalQuery q) {
		devideQuery(q);
		addEventListener();
	}

	private void devideQuery(IPhysicalQuery q){
		//TODO: Bei Threaded Buffern den Plan splitten
		subquerys.add(q);
	}

	private void addEventListener() {
		for (IPhysicalQuery q : subquerys) {
			new QueryEventmanager(q);
		}
	}
}
