package de.uniol.inf.is.odysseus.rcp.queryview.logical;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.rcp.views.query.IQueryViewData;
import de.uniol.inf.is.odysseus.rcp.views.query.IQueryViewDataProvider;
import de.uniol.inf.is.odysseus.rcp.views.query.QueryView;

public class LogicalQueryViewDataProvider implements IQueryViewDataProvider {

	@Override
	public void init(QueryView view) {
	}

	@Override
	public Collection<? extends IQueryViewData> getData() {
		IExecutor executor = LogicalQueryViewDataProviderPlugIn.getClientExecutor();
		
		List<ILogicalQuery> logicalQueries = getLogicalQueries(executor);
		
		return Lists.transform(logicalQueries, new Function<ILogicalQuery, IQueryViewData>() {
			@Override
			public IQueryViewData apply(ILogicalQuery logicalQuery) {
				return new LogicalQueryViewData(logicalQuery);
			}
		});
	}

	@Override
	public void dispose() {
	}

	private static List<ILogicalQuery> getLogicalQueries( IExecutor executor ) {
		Collection<Integer> logicalQueryIds = executor.getLogicalQueryIds();
		
		List<ILogicalQuery> logicalQueries = Lists.newArrayListWithCapacity(logicalQueryIds.size());
		for( Integer id : logicalQueryIds ) {
			logicalQueries.add( executor.getLogicalQuery(id));
		}
		
		return logicalQueries;
	}
}
