package de.uniol.inf.is.odysseus.rcp.dashboard;

import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.viewer.model.stream.IStreamElementListener;

public interface IDashboardPart extends IStreamElementListener<Object>, IConfigurationListener {
	
	public boolean init( Configuration configuration );
	public Configuration getConfiguration();
	
	public void createPartControl( Composite parent, ToolBar toolbar );
	
	public void onStart( List<IPhysicalOperator> physicalRoots) throws Exception;
	public void onPause();
	public void onUnpause();
	public void onStop();
	
	public Map<String, String> onSave();
	public void onLoad(Map<String, String> saved);
	
	public void setQueryTextProvider( IDashboardPartQueryTextProvider file );
	public IDashboardPartQueryTextProvider getQueryTextProvider();
}
