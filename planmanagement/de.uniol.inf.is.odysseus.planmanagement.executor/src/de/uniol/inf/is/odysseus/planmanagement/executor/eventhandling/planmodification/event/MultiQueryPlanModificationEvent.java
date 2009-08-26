package de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.event;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.planmanagement.executor.IPlanManager;

public class MultiQueryPlanModificationEvent extends
		AbstractPlanModificationEvent<ArrayList<IQuery>> {
	
	public static String QUERIES_ADD = "QUERIES_ADD";

	public MultiQueryPlanModificationEvent(IPlanManager sender, String id,
			ArrayList<IQuery> value) {
		super(sender,  id, value);
	}
}
