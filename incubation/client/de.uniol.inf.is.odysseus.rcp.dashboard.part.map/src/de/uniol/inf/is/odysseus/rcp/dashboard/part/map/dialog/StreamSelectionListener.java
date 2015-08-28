package de.uniol.inf.is.odysseus.rcp.dashboard.part.map.dialog;



import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.HeatmapLayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.LayerConfiguration;
//import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.TracemapLayerConfiguration;

public class StreamSelectionListener extends SelectionAdapter {
	
	IPhysicalOperator operator;
	LayerConfiguration config;
	CCombo combobox;
	CCombo geometrieSelect;
	CCombo latSelect;
	CCombo lngSelect;
	CCombo visualizationSelect;
	AddDialog propertyDialog;

	public StreamSelectionListener(IPhysicalOperator operator, LayerConfiguration config, CCombo combobox, CCombo geometrieSelect,
			CCombo latSelect, CCombo lngSelect, CCombo visualizationSelect, AddDialog propertyDialog) {
		this.operator = operator;
		this.combobox = combobox;
		this.propertyDialog = propertyDialog;
		this.geometrieSelect = geometrieSelect;
		this.latSelect = latSelect;
		this.lngSelect = lngSelect;
		this.visualizationSelect = visualizationSelect;
	}

	@Override
	public void widgetSelected(SelectionEvent e) {

		// Show the right Schema-Attributes for selected stream
		geometrieSelect.removeAll();
		SDFSchema schema = operator.getOutputSchema();

		for (int i = 0; i < schema.size(); i++) {
			geometrieSelect.add(schema.getAttribute(i).getAttributeName(), i);
		}
		
		for (int i = 0; i < schema.size(); i++) {
			latSelect.add(schema.getAttribute(i).getAttributeName(), i);
		}
		
		for (int i = 0; i < schema.size(); i++) {
			lngSelect.add(schema.getAttribute(i).getAttributeName(), i);
		}

		visualizationSelect.removeAll();
		for (int i = 0; i < schema.size(); i++) {
			visualizationSelect.add(schema.getAttribute(i).getAttributeName(), i);
		}

		// Pre-select standard attribute-positions
		if (geometrieSelect.getSelectionIndex() < 0) {
			geometrieSelect.select(0);

		}
		if (latSelect.getSelectionIndex() < 0) {
			latSelect.select(0);

		}
		if (lngSelect.getSelectionIndex() < 0) {
			lngSelect.select(0);

		}
		if (visualizationSelect.getSelectionIndex() < 0) {
			visualizationSelect.select(1);
		}

		if (this.combobox.getText().equals("Heatmap")) {
			// User has selected the Heatmap
			if (!(this.config instanceof HeatmapLayerConfiguration))
				this.config = new HeatmapLayerConfiguration("");
			final HeatmapLayerConfiguration heatmapLayerConfiguration = (HeatmapLayerConfiguration) this.config;

			heatmapLayerConfiguration.setGeometricAttributePosition(geometrieSelect.getSelectionIndex());
			heatmapLayerConfiguration.setLatAttribute(latSelect.getSelectionIndex());
			heatmapLayerConfiguration.setLngAttribute(lngSelect.getSelectionIndex());
			heatmapLayerConfiguration.setValueAttributePosition(visualizationSelect.getSelectionIndex());
			this.config = heatmapLayerConfiguration;

			propertyDialog.setLayerConfiguration(config);
			// } else if (this.combobox.getText().equals("Tracemap")) {
			// // User has selected the Tracemap
			// if (!(this.config instanceof TracemapLayerConfiguration))
			// this.config = new TracemapLayerConfiguration("");
			// final TracemapLayerConfiguration tracemapLayerConfiguration =
			// (TracemapLayerConfiguration) this.config;
			//
			// tracemapLayerConfiguration.setQuery(streamSelectionBox.getText());
			// tracemapLayerConfiguration
			// .setGeometricAttributePosition(geometrieSelect
			// .getSelectionIndex());
			// tracemapLayerConfiguration
			// .setValueAttributePosition(visualizationSelect
			// .getSelectionIndex());
			// this.config = tracemapLayerConfiguration;
			//
			// propertyDialog.setLayerConfiguration(config);
		}
	}
}
