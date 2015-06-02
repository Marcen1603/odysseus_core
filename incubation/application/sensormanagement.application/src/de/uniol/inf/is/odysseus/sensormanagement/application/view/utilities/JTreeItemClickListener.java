package de.uniol.inf.is.odysseus.sensormanagement.application.view.utilities;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

public abstract class JTreeItemClickListener extends MouseAdapter
{
	@Override
	final public void mousePressed(MouseEvent e) 
	{
		if (e.getSource() instanceof JTree)
		{
			JTree tree = (JTree)e.getSource();
			int selRow = tree.getRowForLocation(e.getX(), e.getY());
		    TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
		    if (selRow != -1) 
		    {
		    	if (e.getClickCount() == 1)
		    		mouseClicked(e, selPath.getLastPathComponent());
		    	else
		    	if (e.getClickCount() == 2)
		        	mouseDblClicked(e, selPath.getLastPathComponent());
		    }
		}
 	}
	
	public abstract void mouseClicked(MouseEvent e, Object item);
	
	public abstract void mouseDblClicked(MouseEvent e, Object item);
	
}
