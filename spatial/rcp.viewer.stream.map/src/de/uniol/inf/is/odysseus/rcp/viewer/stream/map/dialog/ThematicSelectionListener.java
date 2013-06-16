package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.dialog;

import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.HeatmapLayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.LayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.TracemapLayerConfiguration;

public class ThematicSelectionListener extends SelectionAdapter{

	LayerConfiguration config;
	CCombo combobox;
	CCombo streamSelectionBox;
	PropertyTitleDialog propertyDialog;
	
	public ThematicSelectionListener(LayerConfiguration config, CCombo comboBox, CCombo streamSelectionBox, PropertyTitleDialog propertyDialog) {
		this.config = config;
		this.combobox = comboBox;
		this.streamSelectionBox = streamSelectionBox;
		this.propertyDialog = propertyDialog;
	}
	
	@Override
	public void widgetSelected(SelectionEvent e) {
		if (this.combobox.getText().equals("Heatmap")) {
			// User has selected the Heatmap
			if (!(this.config instanceof HeatmapLayerConfiguration))
				this.config = new HeatmapLayerConfiguration("");
			final HeatmapLayerConfiguration heatmapLayerConfiguration = (HeatmapLayerConfiguration) this.config;

			// Set a few standard-properties (covers whole world, has SRID 4326)
			heatmapLayerConfiguration.setCoverageGeographic(-180.0, 180.0,
					-85.0511, 85.0511);
			heatmapLayerConfiguration.setCoverageProjected(-180.0, 180.0,
					-85.0511, 85.0511);
			heatmapLayerConfiguration.setSrid(4326);
			heatmapLayerConfiguration.setQuery(streamSelectionBox.getText());			
			this.config = heatmapLayerConfiguration;
			
			propertyDialog.setLayerConfiguration(config);			
		} else if(this.combobox.getText().equals("Linemap")) {
			// User has selected the Linemap
			if (!(this.config instanceof TracemapLayerConfiguration))
				this.config = new TracemapLayerConfiguration("");
			final TracemapLayerConfiguration linemapLayerConfiguration = (TracemapLayerConfiguration) this.config;

			linemapLayerConfiguration.setQuery(streamSelectionBox.getText());
			this.config = linemapLayerConfiguration;
			
			propertyDialog.setLayerConfiguration(config);
		}
	
	}
}
