package de.uniol.inf.is.odysseus.rcp.dashboard.part.map.dialog;

import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.HeatmapLayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.LayerConfiguration;
//import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.TracemapLayerConfiguration;

public class ThematicSelectionListener extends SelectionAdapter {

	LayerConfiguration config;
	CCombo combobox;
	CCombo streamSelectionBox;
	CCombo geometrieSelect;
	CCombo visualizationSelect;
	PropertyTitleDialog propertyDialog;

	public ThematicSelectionListener(LayerConfiguration config,
			CCombo comboBox, CCombo geometrieSelect,
			CCombo visualizationSelect,	PropertyTitleDialog propertyDialog) {
		this.config = config;
		this.combobox = comboBox;
		this.propertyDialog = propertyDialog;
		this.geometrieSelect = geometrieSelect;
		this.visualizationSelect = visualizationSelect;
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
			
			heatmapLayerConfiguration
					.setGeometricAttributePosition(geometrieSelect
							.getSelectionIndex());
			heatmapLayerConfiguration
					.setValueAttributePosition(visualizationSelect
							.getSelectionIndex());
			
			heatmapLayerConfiguration.setSrid(4326);
			heatmapLayerConfiguration
					.setGeometricAttributePosition(geometrieSelect
							.getSelectionIndex());
			heatmapLayerConfiguration
					.setValueAttributePosition(visualizationSelect
							.getSelectionIndex());
			this.config = heatmapLayerConfiguration;

			propertyDialog.setLayerConfiguration(config);
//		} else if (this.combobox.getText().equals("Tracemap")) {
//			// User has selected the Tracemap
//			if (!(this.config instanceof TracemapLayerConfiguration))
//				this.config = new TracemapLayerConfiguration("");
//			final TracemapLayerConfiguration tracemapLayerConfiguration = (TracemapLayerConfiguration) this.config;
//
//			tracemapLayerConfiguration.setSrid(4326);
//			
//			tracemapLayerConfiguration.setQuery(streamSelectionBox.getText());
//			tracemapLayerConfiguration
//					.setGeometricAttributePosition(geometrieSelect
//							.getSelectionIndex());
//			tracemapLayerConfiguration
//					.setValueAttributePosition(visualizationSelect
//							.getSelectionIndex());
//			
//			tracemapLayerConfiguration
//					.setGeometricAttributePosition(geometrieSelect
//							.getSelectionIndex());
//			tracemapLayerConfiguration
//					.setValueAttributePosition(visualizationSelect
//							.getSelectionIndex());
//			this.config = tracemapLayerConfiguration;
//
//			propertyDialog.setLayerConfiguration(config);
		}
	}
}
