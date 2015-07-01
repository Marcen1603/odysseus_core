package de.uniol.inf.is.odysseus.sensormanagement.application.view.utilities;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultTreeCellRenderer;

public abstract class TreeCellRenderer extends DefaultTreeCellRenderer
{
	private static final long serialVersionUID = 1L;
	
	static public Icon globalIcon		= UIManager.getIcon("FileView.directoryIcon");
	static public Icon sensorBoxIcon 	= UIManager.getIcon("FileView.computerIcon");
	static public Icon baseBoxIcon 		= UIManager.getIcon("FileView.hardDriveIcon");
	static public Icon sensorIcon 		= new ImageIcon("res/sensor.png");
	static public Icon sensorIconRec	= new ImageIcon("res/sensor_rec.png");
	static public Icon sensorIconPlay	= new ImageIcon("res/sensor_play.png");
	static public Icon sensorIconPause	= new ImageIcon("res/sensor_pause.png");
	
	public abstract Icon getIconByNode(Object node);
	
    @Override
    final public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) 
    {
    	super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
    	setIcon(getIconByNode(value));
    	
        return this;
    }
}