package de.uniol.inf.is.odysseus.sensormanagement.application.view;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import de.uniol.inf.is.odysseus.sensormanagement.application.model.AbstractInstance;

public abstract class ViewInstance extends ViewEntity
{
	private String ethernetAddr;
	private AbstractInstance instance;
	
	public String getEthernetAddr() { return ethernetAddr; }
	public AbstractInstance getInstance() { return instance; }
	
	public ViewInstance(Session session, String ethernetAddr, DefaultMutableTreeNode parentNode) 
	{
		super(session, parentNode);
		
		this.ethernetAddr = ethernetAddr;
		instance = null;
	}
	
	public void setInstance(AbstractInstance instance) 
	{
		this.instance = instance;
	}		

	public void showInTree()
	{
		synchronized (getSession().getEntityTree())
		{
			// Expand to first sensor, if sensors are available, expand to box otherwise
			TreePath path;
			if (instance != null && instance.getSensors().size() > 0) 
				path = new TreePath(getSession().getViewSensor(instance.getSensors().get(0).getSensorModel().id).getNode().getPath());
			else
				path = new TreePath(getNode().getPath());
			
			getSession().getEntityTree().expandPath(path);
			getSession().getEntityTree().scrollPathToVisible(path);
		}
	}
	
	public void updateTreePosition()
	{
		synchronized (getSession().getEntityTree())
		{
/*				try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode) getNode().getParent();
			
			for (int i=0; i<parent.getChildCount(); i++)
			{
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) parent.getChildAt(i);
				ViewInstance vi = (ViewInstance) node.getUserObject();
				
				if ((instance != null && vi.instance == null) || (this.toString().compareTo(vi.toString()) <= 0))
				{
					parent.insert(getNode(), i);
					break;
				}
			}
			
			getSession().getTreeModel().nodeStructureChanged(parent);
		}
	}
}
