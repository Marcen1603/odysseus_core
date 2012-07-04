package de.uniol.inf.is.odysseus.rcp.dashboard;

import java.util.List;

import org.eclipse.core.resources.IFile;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;

public abstract class AbstractDashboardPart implements IDashboardPart {

	private Configuration configuration;
	private IFile queryFile;
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
	public IFile getQueryFile() {
		return queryFile;
	}

	@Override
	public void setQueryFile(IFile file) {
		this.queryFile = Preconditions.checkNotNull(file, "QueryFile for DashboardPart must not be null!");
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
}
