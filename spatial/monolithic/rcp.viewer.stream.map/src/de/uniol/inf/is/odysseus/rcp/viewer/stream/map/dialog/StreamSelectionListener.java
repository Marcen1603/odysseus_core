package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.dialog;

import java.util.Collection;

import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.LayerUpdater;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.HeatmapLayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.LayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.TracemapLayerConfiguration;

public class StreamSelectionListener extends SelectionAdapter {

	LayerConfiguration config;
	CCombo combobox;
	CCombo streamSelectionBox;
	CCombo geometrieSelect;
	CCombo visualizationSelect;
	Collection<LayerUpdater> connections;
	PropertyTitleDialog propertyDialog;

	public StreamSelectionListener(LayerConfiguration config, CCombo combobox,
			CCombo streamSelectionBox, CCombo geometrieSelect,
			CCombo visualizationSelect, Collection<LayerUpdater> connections,
			PropertyTitleDialog propertyDialog) {
		this.config = config;
		this.combobox = combobox;
		this.streamSelectionBox = streamSelectionBox;
		this.propertyDialog = propertyDialog;
		this.geometrieSelect = geometrieSelect;
		this.visualizationSelect = visualizationSelect;
		this.connections = connections;
	}

	@Override
	public void widgetSelected(SelectionEvent e) {

		// Show the right Schema-Attributes for selected stream
		geometrieSelect.removeAll();
		SDFSchema schema = ((LayerUpdater) connections.toArray()[streamSelectionBox
				.getSelectionIndex()]).getConnection().getOutputSchema();

		for (int i = 0; i < schema.size(); i++) {
			geometrieSelect.add(schema.getAttribute(i).getAttributeName(), i);
		}

		visualizationSelect.removeAll();
		for (int i = 0; i < schema.size(); i++) {
			visualizationSelect.add(schema.getAttribute(i).getAttributeName(),
					i);
		}

		// Pre-select standard attribute-positions
		if (geometrieSelect.getSelectionIndex() < 0) {
			geometrieSelect.select(0);

		}
		if (visualizationSelect.getSelectionIndex() < 0) {
			visualizationSelect.select(1);
		}

		if (this.combobox.getText().equals("Heatmap")) {
			// User has selected the Heatmap
			if (!(this.config instanceof HeatmapLayerConfiguration))
				this.config = new HeatmapLayerConfiguration("");
			final HeatmapLayerConfiguration heatmapLayerConfiguration = (HeatmapLayerConfiguration) this.config;

			heatmapLayerConfiguration.setQuery(streamSelectionBox.getText());
			heatmapLayerConfiguration
					.setGeometricAttributePosition(geometrieSelect
							.getSelectionIndex());
			heatmapLayerConfiguration
					.setValueAttributePosition(visualizationSelect
							.getSelectionIndex());
			this.config = heatmapLayerConfiguration;

			propertyDialog.setLayerConfiguration(config);
		} else if (this.combobox.getText().equals("Tracemap")) {
			// User has selected the Tracemap
			if (!(this.config instanceof TracemapLayerConfiguration))
				this.config = new TracemapLayerConfiguration("");
			final TracemapLayerConfiguration tracemapLayerConfiguration = (TracemapLayerConfiguration) this.config;

			tracemapLayerConfiguration.setQuery(streamSelectionBox.getText());
			tracemapLayerConfiguration
					.setGeometricAttributePosition(geometrieSelect
							.getSelectionIndex());
			tracemapLayerConfiguration
					.setValueAttributePosition(visualizationSelect
							.getSelectionIndex());
			this.config = tracemapLayerConfiguration;

			propertyDialog.setLayerConfiguration(config);
		}
	}
}
