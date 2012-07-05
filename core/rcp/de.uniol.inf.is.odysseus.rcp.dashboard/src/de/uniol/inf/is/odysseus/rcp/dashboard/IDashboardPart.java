package de.uniol.inf.is.odysseus.rcp.dashboard;

import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.swt.widgets.Composite;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.viewer.model.stream.IStreamElementListener;

public interface IDashboardPart extends IStreamElementListener<Object>, IConfigurationListener {
	
	public boolean init( Configuration configuration );
	public Configuration getConfiguration();
	
	public void createPartControl( Composite parent );
	
	public void onStart( List<IPhysicalOperator> physicalRoots) throws Exception;
	public void onPause();
	public void onUnpause();
	public void onStop();
	
	public Map<String, String> onSave();
	public void onLoad(Map<String, String> saved);
	
	public void setQueryFile( IFile file );
	public IFile getQueryFile();
}
