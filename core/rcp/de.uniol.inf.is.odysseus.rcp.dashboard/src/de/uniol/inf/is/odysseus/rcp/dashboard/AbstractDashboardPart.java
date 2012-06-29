package de.uniol.inf.is.odysseus.rcp.dashboard;

import java.util.List;

import com.google.common.collect.ImmutableList;

public abstract class AbstractDashboardPart implements IDashboardPart {

	private Configuration configuration;
	private List<String> query;
	
	@Override
	public boolean init(Configuration configuration) {
		this.configuration = configuration;
		
		return true;
	}

	@Override
	public Configuration getConfiguration() {
		return configuration;
	}

	@Override
	public void setQuery(List<String> queryLines) {
		this.query = queryLines;
	}

	@Override
	public ImmutableList<String> getQuery() {
		return ImmutableList.copyOf(query);
	}

}
