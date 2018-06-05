package de.uniol.inf.is.odysseus.rcp.dashboard.part.map.dialog.properties;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Label;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.HeatmapLayerConfiguration;

/**
 * A listener for a colorDialog in the TreeListener, so we can change the given HeatmapLayerConfiguration
 * if the colorDialog changed the color
 * @author Tobias Brandt
 *
 */

public abstract class HeatColorListener extends SelectionAdapter{

	
	HeatmapLayerConfiguration heatmapLayerConfiguration;
	HeatmapPropertiesDialog heatmapPropertiesDialog;
	Label correspondingLabel;
	
	public HeatColorListener(HeatmapLayerConfiguration heatmapLayerConfiguration, HeatmapPropertiesDialog heatmapPropertiesDialog, Label correspondingLabel) {
		this.heatmapLayerConfiguration = heatmapLayerConfiguration;
		this.heatmapPropertiesDialog = heatmapPropertiesDialog;
		this.correspondingLabel = correspondingLabel;
	}	
	
	@Override
	abstract public void widgetSelected(SelectionEvent e);
}
