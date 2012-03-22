package de.uniol.inf.is.odysseus.rcp.views.query;

import java.util.Collection;

public interface IQueryViewDataProvider {
    
	public void init(QueryView view);
    public Collection<? extends IQueryViewData> getData();
    
}
