package de.uniol.inf.is.odysseus.sensormanagement.application.view.live;

import javax.swing.Icon;
import javax.swing.tree.DefaultMutableTreeNode;

import de.uniol.inf.is.odysseus.sensormanagement.application.model.live.LiveInstance;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.Session;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.ViewInstance;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.utilities.TreeCellRenderer;
import de.uniol.inf.is.odysseus.sensormanagement.client.executor.ILoggable;

public class LiveViewInstance extends ViewInstance implements ILoggable
{
	public LiveViewInstance(Session session, String ethernetAddr, DefaultMutableTreeNode parentNode) 
	{
		super(session, ethernetAddr, parentNode);
	}
	
	@Override public LiveInstance getInstance() { return (LiveInstance) super.getInstance(); }
	
	@Override public void startLogging() { getInstance().startLogging(); }
	@Override public void stopLogging()  { getInstance().stopLogging(); }
	
	@Override public void treeDblClick()
	{
		new SensorBoxConfigDialog((LiveSession) getSession(), getInstance());			
	}

	@Override public Icon getIcon() 
	{
		return TreeCellRenderer.sensorBoxIcon;
	}

	@Override
	public String getDisplayName() 
	{
		return (getInstance() != null) ? getInstance().toString() : ("Connecting to " + this.getEthernetAddr() + "...");
	}
	
	@Override public void startVisualization() {}
	@Override public void stopVisualization() {}

}
