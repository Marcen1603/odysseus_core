package de.uniol.inf.is.odysseus.rcp.queryview.logical;

import java.util.ArrayList;
import java.util.Collection;

import de.uniol.inf.is.odysseus.rcp.views.query.IQueryViewData;
import de.uniol.inf.is.odysseus.rcp.views.query.IQueryViewDataProvider;
import de.uniol.inf.is.odysseus.rcp.views.query.QueryView;

public class LogicalQueryViewDataProvider implements IQueryViewDataProvider {

	@Override
	public void init(QueryView view) {
	}

	@Override
	public Collection<? extends IQueryViewData> getData() {
		return new ArrayList<IQueryViewData>();
	}

	@Override
	public void dispose() {
	}

}
