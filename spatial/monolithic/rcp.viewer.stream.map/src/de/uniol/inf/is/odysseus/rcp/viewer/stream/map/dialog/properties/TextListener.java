package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.dialog.properties;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Text;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.HeatmapLayerConfiguration;

public abstract class TextListener implements ModifyListener{

	
	HeatmapLayerConfiguration heatmapLayerConfiguration;
	TreeListener treeListener;
	Text correspondingText;
	
	public TextListener(HeatmapLayerConfiguration heatmapLayerConfiguration, TreeListener treeListener, Text correspondingText) {
		this.heatmapLayerConfiguration = heatmapLayerConfiguration;
		this.treeListener = treeListener;
		this.correspondingText = correspondingText;
	}	
	
	@Override
	abstract public void modifyText(ModifyEvent e);
}