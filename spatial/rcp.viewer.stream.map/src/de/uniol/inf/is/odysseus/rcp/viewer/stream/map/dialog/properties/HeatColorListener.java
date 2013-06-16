package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.dialog.properties;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Label;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.HeatmapLayerConfiguration;

/**
 * A listener for a colorDialog in the TreeListener, so we can change the given HeatmapLayerConfiguration
 * if the colorDialog changed the color
 * @author Tobias Brandt
 *
 */

public abstract class HeatColorListener extends SelectionAdapter{

	
	HeatmapLayerConfiguration heatmapLayerConfiguration;
	TreeListener treeListener;
	Label correspondingLabel;
	
	public HeatColorListener(HeatmapLayerConfiguration heatmapLayerConfiguration, TreeListener treeListener, Label correspondingLabel) {
		this.heatmapLayerConfiguration = heatmapLayerConfiguration;
		this.treeListener = treeListener;
		this.correspondingLabel = correspondingLabel;
	}	
	
	abstract public void widgetSelected(SelectionEvent e);
}
