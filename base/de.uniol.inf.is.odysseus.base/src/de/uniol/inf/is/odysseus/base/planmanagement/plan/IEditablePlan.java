package de.uniol.inf.is.odysseus.base.planmanagement.plan;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery;

public interface IEditablePlan extends IPlan {
	public boolean addQuery(IEditableQuery query);
	
	public IEditableQuery getQuery(int queryID);
	
	public IEditableQuery removeQuery(int queryID);
	
	public ArrayList<IEditableQuery> getEdittableQueries();
}
