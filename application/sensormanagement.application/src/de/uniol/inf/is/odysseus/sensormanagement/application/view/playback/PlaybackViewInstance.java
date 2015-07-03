package de.uniol.inf.is.odysseus.sensormanagement.application.view.playback;

import javax.swing.Icon;
import javax.swing.tree.DefaultMutableTreeNode;

import de.uniol.inf.is.odysseus.sensormanagement.application.view.Session;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.ViewInstance;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.utilities.TreeCellRenderer;

public class PlaybackViewInstance extends ViewInstance
{
	public PlaybackViewInstance(Session session, String ethernetAddr, DefaultMutableTreeNode parentNode) 
	{
		super(session, ethernetAddr, parentNode);
	}

	@Override public String getDisplayName() 
	{
		return getInstance() != null ? getInstance().getName() : "";
	}

	@Override public void treeDblClick() 
	{
	}

	@Override public Icon getIcon() 
	{
		return TreeCellRenderer.sensorBoxIcon;
	}

	@Override
	public void startVisualization() 
	{
	}

	@Override
	public void stopVisualization() 
	{
	}
}
