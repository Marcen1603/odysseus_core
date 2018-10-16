package de.uniol.inf.is.odysseus.rcp.dashboard.queryprovider;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartQueryTextProvider;

public class RunningQueryProvider implements IDashboardPartQueryTextProvider {

	private final String queryName;
	
	public RunningQueryProvider( String queryName ) {
		// Preconditions.checkArgument(!Strings.isNullOrEmpty(queryName), "QueryName must not be null or empty!");
		
		this.queryName = queryName;
	}
	
	public String getQueryName() {
		return queryName;
	}
	
	@Override
	public ImmutableList<String> getQueryText() {
		return ImmutableList.of();
	}

}
