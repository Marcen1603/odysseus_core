package de.uniol.inf.is.odysseus.sensormanagement.application.view;

import javax.swing.Icon;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

public abstract class ViewEntity
{
	private DefaultMutableTreeNode node;	
	private Session session;
	
	public Session getSession() 			{ return session; 	}
	public DefaultMutableTreeNode getNode() { return node;		} 
	
	public ViewEntity(Session session, DefaultMutableTreeNode parentNode)
	{
		this.session = session;
		
		node = new DefaultMutableTreeNode(this);
		parentNode.add(node);
		session.getTreeModel().nodeStructureChanged(parentNode);						
	}
	
	public abstract String getDisplayName();
	
	@Override
	public String toString()
	{
		return getDisplayName();
	}				
	
	public void changed() 
	{
		session.getTreeModel().nodeChanged(node);		
	}		
	
	public void remove()
	{
		stopVisualization();
		TreeNode parentNode = node.getParent();
		node.removeFromParent();
		session.getTreeModel().nodeStructureChanged(parentNode);			
	}		
	
	public abstract void treeDblClick();
	public abstract Icon getIcon();
	
	public abstract void startVisualization();
	public abstract void stopVisualization();
}
