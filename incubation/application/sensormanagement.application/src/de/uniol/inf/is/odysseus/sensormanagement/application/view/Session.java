package de.uniol.inf.is.odysseus.sensormanagement.application.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import de.uniol.inf.is.odysseus.sensormanagement.application.view.playback.scene.Scene;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.sensors.MapVisualization;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.utilities.JTreeItemClickListener;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.utilities.TreeCellRenderer;

public abstract class Session extends JSplitPane
{	
	public enum PresentationStyle
	{
		TABBED,
		TILED
	};
	
	private static final long serialVersionUID = 1L;

	private String 				sessionName;
	private Scene				scene;
	
	private JTree 				entityTree;
	private JTabbedPane 		viewTabbedPane;
	private JPanel 				viewTiledPanel;
	private JPanel				presentationPanel;	
	private PresentationStyle	presentationStyle;
	private List<Visualization>	visualizationList = new ArrayList<Visualization>();
	private MapVisualization	map;
	
	public String					getSessionName()		{ return sessionName; }
	public Scene					getScene()				{ return scene; }
	public JTree 					getEntityTree() 		{ return entityTree; }
	public DefaultTreeModel 		getTreeModel()			{ return ((DefaultTreeModel) entityTree.getModel()); }
	public DefaultMutableTreeNode 	getTreeRoot()			{ return (DefaultMutableTreeNode) entityTree.getModel().getRoot(); }
//	public JTabbedPane 				getViewTabbedPane() 	{ return viewTabbedPane; }
	public PresentationStyle		getPresentationStyle()	{ return presentationStyle; }
	public JPanel					getPresentationPanel()	{ return presentationPanel; }
	public MapVisualization			getMap()				{ return map; }

	public abstract double 			getNow();
	
	public Session(Scene scene)
	{
		this.scene = scene;
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
		
		if (scene.getMapImage() != null)
		{
			String mapImageFileName = scene.getPath() + scene.getMapImage();
			try {
				map = new MapVisualization(this, "Map", mapImageFileName);
			} catch (IOException e) {
				throw new RuntimeException("Could not create map visualization", e);
			}
			map.setDisplayConstraints("cell 0 0 2 2");
			addVisualization(map);
		}
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
				viewTabbedPane.addTab("Live View: " + v.displayName, null, v, null);

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
	
	public abstract void remove();		
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
	
	protected void startLoggingButtonClick() 
	{
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) (entityTree.getSelectionPath().getLastPathComponent());   		
		if (selectedNode == null) return;
			
		Object obj = selectedNode.getUserObject();
		if (obj instanceof ViewEntity)
		{
			((ViewEntity) obj).startLogging();
		}
	}

	protected void stopLoggingButtonClick() 
	{
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) (entityTree.getSelectionPath().getLastPathComponent());   		
		if (selectedNode == null) return;

		Object obj = selectedNode.getUserObject();
		if (obj instanceof ViewEntity)
		{
			((ViewEntity) obj).stopLogging();
		}
	}		
	
/*	public void addVisualization(Visualization visualization)
	{
		addVisualization(visualization, null);
	}*/
	
	public void addVisualization(Visualization visualization) 
	{			
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
				viewTabbedPane.addTab("Live View: " + visualization.displayName, null, visualization, null);
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
	
	public void removeVisualization(Visualization visualization) 
	{
		visualizationList.remove(visualization);
		
		Container c = visualization.getParent();
		c.remove(visualization);
		c.repaint();		
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
}