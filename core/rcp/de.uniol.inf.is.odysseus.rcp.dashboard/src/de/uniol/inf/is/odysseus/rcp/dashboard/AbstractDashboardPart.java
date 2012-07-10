package de.uniol.inf.is.odysseus.rcp.dashboard;

import java.util.List;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;

public abstract class AbstractDashboardPart implements IDashboardPart {

	private Configuration configuration;
	private IDashboardPartQueryTextProvider queryTextProvider;
	private List<IPhysicalOperator> roots;
	
	@Override
	public boolean init(Configuration configuration) {
		this.configuration = configuration;
		this.configuration.addListener(this);
		
		return true;
	}

	@Override
	public Configuration getConfiguration() {
		return configuration;
	}
	
	public List<IPhysicalOperator> getRoots() {
		return ImmutableList.copyOf(roots);
	}
	
	@Override
	public IDashboardPartQueryTextProvider getQueryTextProvider() {
		return queryTextProvider;
	}

	@Override
	public void setQueryTextProvider(IDashboardPartQueryTextProvider provider) {
		this.queryTextProvider = Preconditions.checkNotNull(provider, "QueryTextProvider for DashboardPart must not be null!");
	}

	@Override
	public void onStart(List<IPhysicalOperator> physicalRoots) throws Exception {
		roots = physicalRoots;
	}
	
	@Override
	public void onPause() {
	}
	
	@Override
	public void onUnpause() {
	}
	
	@Override
	public void onStop() {
	}
	
	@Override
	public Map<String, String> onSave() {
		return Maps.newHashMap();
	}
	
	@Override
	public void onLoad(Map<String, String> saved) {
	}
}
