package de.uniol.inf.is.odysseus.rcp.dashboard;

import org.eclipse.core.resources.IFile;

import com.google.common.base.Preconditions;

public abstract class AbstractDashboardPart implements IDashboardPart {

	private Configuration configuration;
	private IFile queryFile;
	
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
	public IFile getQueryFile() {
		return queryFile;
	}

	@Override
	public void setQueryFile(IFile file) {
		this.queryFile = Preconditions.checkNotNull(file, "QueryFile for DashboardPart must not be null!");
	}
}
