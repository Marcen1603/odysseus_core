package de.uniol.inf.is.odysseus.sensormanagement.application.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import net.miginfocom.swing.MigLayout;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.AbstractInstance;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.AbstractSensor;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.AbstractSensorManager;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.DisplayMap;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.Scene;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.utilities.JTreeItemClickListener;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.utilities.TreeCellRenderer;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.visualization.MapVisualization;
import de.uniol.inf.is.odysseus.sensormanagement.common.utilities.SimpleCallbackListener;

public abstract class Session extends JSplitPane
{	
	private static final long serialVersionUID = -8747863816084043141L;
	
	public enum PresentationStyle
	{
		TABBED,
		TILED
	};
	
	private String sessionName;
	private AbstractSensorManager sensorManager;
	protected List<ViewInstance> viewInstances = new ArrayList<ViewInstance>();
	private Map<String, ViewSensor> viewSensors = new HashMap<>();
	
	private JTree entityTree;
	private JTabbedPane viewTabbedPane;
	private JPanel viewTiledPanel;
	private JPanel presentationPanel;	
	private PresentationStyle presentationStyle;
	private List<Visualization>	visualizationList = new ArrayList<Visualization>();
	private List<MapVisualization> mapList = new ArrayList<MapVisualization>();
	
	public String getSessionName() { return sessionName; }
	public JTree getEntityTree() { return entityTree; }
	public DefaultTreeModel getTreeModel() { return ((DefaultTreeModel) entityTree.getModel()); }
	public DefaultMutableTreeNode getTreeRoot() { return (DefaultMutableTreeNode) entityTree.getModel().getRoot(); }
	public PresentationStyle getPresentationStyle()	{ return presentationStyle; }
	public JPanel getPresentationPanel() { return presentationPanel; }
	public MapVisualization getMap() { return mapList.size() > 0 ? mapList.get(0) : null; }
	public AbstractSensorManager getSensorManager() { return sensorManager; }	
	
	public Session(Scene scene) throws IOException
	{
		this.sessionName = scene.getName();
		
		entityTree = new JTree(new DefaultMutableTreeNode("Global"));		
		entityTree.setMinimumSize(new Dimension(150, 16));
		entityTree.setShowsRootHandles(true);
		entityTree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		entityTree.setCellRenderer(new SessionTreeCellRenderer());		
		entityTree.addMouseListener(new JTreeItemClickListener()
									{
										public void mouseDblClicked(MouseEvent e, Object item) 	{ entityTreeDoubleClick(item); }
										public void mouseClicked(MouseEvent e, Object item) 	{ }
									});		
		setLeftComponent(entityTree);
		
		presentationPanel = new JPanel(new BorderLayout());
		setRightComponent(presentationPanel);
		
		viewTabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);		
		viewTiledPanel = new JPanel(new MigLayout("", "[grow]", "[grow]"));
		
		presentationPanel.add(viewTabbedPane, BorderLayout.CENTER);
		presentationStyle = PresentationStyle.TABBED;
		
		setPresentationStyle(PresentationStyle.TILED);
		
		sensorManager = createSensorManager(scene);
		sensorManager.onInstanceAdded.addListener(new SimpleCallbackListener<AbstractSensorManager, AbstractInstance>() {
			@Override public void raise(AbstractSensorManager sourceManager, AbstractInstance instance) { 
				onInstanceAdded(instance); 
			}});
		sensorManager.onInstanceChanged.addListener(new SimpleCallbackListener<AbstractSensorManager, AbstractInstance>() {
			@Override public void raise(AbstractSensorManager sourceManager, AbstractInstance instance) { 
				onInstanceChanged(instance); 
			}});		
		sensorManager.onSensorAdded.addListener(new SimpleCallbackListener<AbstractSensorManager, AbstractSensor>() {
			@Override public void raise(AbstractSensorManager sourceManager, AbstractSensor sensor) { 
				onSensorAdded(sensor); 
			}});
		sensorManager.onSensorChanged.addListener(new SimpleCallbackListener<AbstractSensorManager, AbstractSensor>() {
			@Override public void raise(AbstractSensorManager sourceManager, AbstractSensor sensor) { 
				onSensorChanged(sensor); 
			}});
		sensorManager.onSensorRemoved.addListener(new SimpleCallbackListener<AbstractSensorManager, AbstractSensor>() {
			@Override public void raise(AbstractSensorManager sourceManager, AbstractSensor sensor) { 
				onSensorRemoved(sensor); 
			}});				
		
		for (DisplayMap displayMap : scene.getMapList())
		{
			try {
				MapVisualization map = new MapVisualization(this, displayMap);
				mapList.add(map);
				addVisualization(map);
			} catch (IOException e) {
				throw new RuntimeException("Could not create map visualization", e);
			}
		}
		
		sensorManager.start();
	}

	protected void onInstanceAdded(AbstractInstance instance) 
	{
		ViewInstance vi = getViewInstance(instance.getEthernetAddr());
		if (vi == null)
		{
			vi = createViewInstance(this, instance.getEthernetAddr(), getTreeRoot());			
			viewInstances.add(vi);				
		}
	
		vi.setInstance(instance);
		vi.updateTreePosition();
		vi.showInTree();
	}
	
	protected void onInstanceChanged(AbstractInstance instance) 
	{
		getViewInstance(instance.getEthernetAddr()).changed();
	}	

	protected void onSensorAdded(AbstractSensor sensor) 
	{
		ViewInstance vi = null;
		for (AbstractInstance instance : sensorManager.getInstances())
			if (instance.getSensors().contains(sensor))
				vi = getViewInstance(instance);
		
		if (vi == null)
			throw new RuntimeException("No ViewInstance found for instance of sensor " + sensor.getSensorModel().id);
		
		viewSensors.put(sensor.getSensorModel().id, createViewSensor(this, sensor, vi));
		
		onSensorChanged(sensor);	
	}	
	
	protected synchronized void onSensorChanged(AbstractSensor sensor) 
	{
		viewSensors.get(sensor.getSensorModel().id).changed();
	}

	protected synchronized void onSensorRemoved(AbstractSensor sensor) 
	{
		ViewSensor viewSensor = viewSensors.get(sensor.getSensorModel().id);
		viewSensor.remove();
		viewSensors.remove(sensor);
	}		
	
	public synchronized void addInstance(ViewInstance instance)
	{
		viewInstances.add(instance);		
	}
	
	public synchronized void removeInstance(ViewInstance instance) 
	{
		instance.remove();
		viewInstances.remove(instance);		
	}	
	
	public ViewInstance getViewInstance(AbstractInstance instance)
	{
		for (ViewInstance vi : viewInstances)
			if (vi.getInstance() == instance)
				return vi;
		
		return null;		
	}	

	public ViewSensor getViewSensor(String id)
	{
		return viewSensors.get(id);
	}		
	
	public void setPresentationStyle(PresentationStyle presentationStyle)
	{
		if (this.presentationStyle == presentationStyle) return;
		this.presentationStyle = presentationStyle;
		
		for (Visualization v : visualizationList)
			v.getParent().remove(v);
		
		// TODO: Preserve constraints of each component 
		
		switch (presentationStyle)
		{
		case TABBED:
			presentationPanel.remove(viewTiledPanel);
			presentationPanel.add(viewTabbedPane, BorderLayout.CENTER);
			
			for (Visualization v : visualizationList)
				viewTabbedPane.addTab("Live View: " + v.getDisplayName(), null, v, null);

			break;

		case TILED:
			presentationPanel.remove(viewTabbedPane);
			presentationPanel.add(viewTiledPanel, BorderLayout.CENTER);
			
			for (Visualization v : visualizationList)
				viewTiledPanel.add(v, v.getDisplayConstraints());
			
			break;
		}	
		
		presentationPanel.repaint();
	}
	
	public void toggleFullscreen(boolean fullscreen)
	{
		entityTree.setVisible(!fullscreen);
		entityTree.invalidate();
		revalidate();
	}
	
	public void remove()
	{
		for (ViewSensor vs : viewSensors.values())
			vs.stopVisualization();

		sensorManager.stop();		
	}	
	
	public abstract void debugBtn(int i);
	
	protected void entityTreeDoubleClick(Object obj) 
	{
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)obj;
		
		Object userObj = node.getUserObject();
		if (userObj instanceof ViewEntity)
		{
			((ViewEntity) userObj).treeDblClick();
		}
	}
	
	protected void startLiveViewBtnClick() 
	{
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) (entityTree.getSelectionPath().getLastPathComponent());   		
		if (selectedNode == null) return;
		
		Object obj = selectedNode.getUserObject();
		if (obj instanceof ViewEntity)
		{
			((ViewEntity) obj).startVisualization();
		}
	}	
	
	protected void stopLiveViewBtnClick() 
	{
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) (entityTree.getSelectionPath().getLastPathComponent());   		
		if (selectedNode == null) return;
		
		Object obj = selectedNode.getUserObject();
		if (obj instanceof ViewEntity)
		{				
			((ViewEntity) obj).stopVisualization();
		}		
	}
		
	public void addVisualization(Visualization visualization) 
	{		
		if (visualizationList.contains(visualization)) return;
		
		boolean asFrame = false;
//		asFrame = true;
		if (asFrame)
		{
			JFrame f = new JFrame();
			f.add(visualization);
			f.setVisible(true);
		}
		else
		{		
			switch (presentationStyle)
			{
			case TABBED:
				viewTabbedPane.addTab("Live View: " + visualization.getDisplayName(), null, visualization, null);
				viewTabbedPane.setSelectedComponent(visualization);
				break;
				
			case TILED:
//				if (visualization != map) return;
				
				viewTiledPanel.add(visualization, visualization.getDisplayConstraints());
				viewTiledPanel.revalidate();
				break;
			}
		}		
		
		visualizationList.add(visualization);
	}
	
	public void updateVisualizationConstraints(Visualization visualization)
	{
		Object constraint = visualization.getDisplayConstraints();
		if (visualizationList.contains(visualization))
		{
			if (presentationStyle == PresentationStyle.TILED)
			{
				viewTiledPanel.remove(visualization);
				viewTiledPanel.add(visualization, constraint);
				viewTiledPanel.revalidate();			
			}
		}
		else
			addVisualization(visualization);
	}
	
	public void removeVisualization(Visualization visualization) 
	{
		if (!visualizationList.contains(visualization)) return;
		
		visualizationList.remove(visualization);
		
		Container c = visualization.getParent();
		c.remove(visualization);
		c.repaint();		
	}		
	
	public void setMapConstraint(Object constraint) 
	{
		if (getMap() == null)
			throw new IllegalArgumentException("No map available. Cannot set constraints for map");
		
		getMap().setDisplayConstraints(constraint);
	}

	public ViewInstance getViewInstance(String ethernetAddr)
	{
		for (ViewInstance vi : viewInstances)
		{
			if (vi.getEthernetAddr().equals(ethernetAddr))
				return vi;
		}
		
		return null;
	}

	
	private class SessionTreeCellRenderer extends TreeCellRenderer
	{
		private static final long serialVersionUID = 1L;

		@Override
		public Icon getIconByNode(Object node) 
		{
			if (node instanceof DefaultMutableTreeNode) 
			{	
				Object obj = ((DefaultMutableTreeNode) node).getUserObject();
				if (obj instanceof ViewEntity)
					return ((ViewEntity) obj).getIcon();
	        }
			
			return globalIcon;
		}
	}

	public abstract double getNow();
	protected abstract AbstractSensorManager createSensorManager(Scene scene);	
	protected abstract ViewSensor createViewSensor(Session session, AbstractSensor sensor, ViewInstance instance);
	protected abstract ViewInstance createViewInstance(Session session, String ethernetAddr, DefaultMutableTreeNode parentNode);
}