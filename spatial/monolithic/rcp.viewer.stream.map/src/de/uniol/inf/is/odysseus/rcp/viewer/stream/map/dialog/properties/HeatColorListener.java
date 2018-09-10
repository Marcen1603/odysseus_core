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
	MapPropertiesDialog mapPropertiesDialog;
	Label correspondingLabel;
	
	public HeatColorListener(HeatmapLayerConfiguration heatmapLayerConfiguration, MapPropertiesDialog mapPropertiesDialog, Label correspondingLabel) {
		this.heatmapLayerConfiguration = heatmapLayerConfiguration;
		this.mapPropertiesDialog = mapPropertiesDialog;
		this.correspondingLabel = correspondingLabel;
	}	
	
	@Override
	abstract public void widgetSelected(SelectionEvent e);
}
