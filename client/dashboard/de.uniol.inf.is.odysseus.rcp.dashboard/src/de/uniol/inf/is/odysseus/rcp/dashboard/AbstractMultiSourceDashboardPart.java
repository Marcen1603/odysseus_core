package de.uniol.inf.is.odysseus.rcp.dashboard;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;

public abstract class AbstractMultiSourceDashboardPart extends AbstractDashboardPart {
	
	private List<IDashboardPartQueryTextProvider> queryTextProviders = new ArrayList<>();
	
	@Override
	public void setQueryTextProvider(IDashboardPartQueryTextProvider provider) {
		this.queryTextProvider = Preconditions.checkNotNull(provider, "QueryTextProvider for DashboardPart must not be null!");
		addQueryTextProvider(provider);
	}
	
	public void addQueryTextProvider(IDashboardPartQueryTextProvider provider) {
		Preconditions.checkNotNull(provider, "QueryTextProvider for DashboardPart must not be null!");
		this.queryTextProviders.add(provider);
	}
	
	public List<IDashboardPartQueryTextProvider> getQueryTextProviders() {
		return new ArrayList<IDashboardPartQueryTextProvider>(this.queryTextProviders);
	}

}
