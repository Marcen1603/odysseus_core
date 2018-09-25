package de.uniol.inf.is.odysseus.rcp.dashboard.part.map.dialog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.HeatmapLayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.LayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.TracemapLayerConfiguration;

/**
 * When the user selects the data source in the map component (when choosing the
 * type of the map), this listener updates the other comboBoxes with the
 * attributes of the chosen data source.
 * 
 * @author Tobias Brandt
 *
 */
public class StreamSelectionListener extends SelectionAdapter {

	private List<IPhysicalOperator> operators;
	private LayerConfiguration config;
	private CCombo dataSourceCombo;
	private CCombo combobox;
	private CCombo geometrieSelect;
	private CCombo latSelect;
	private CCombo lngSelect;
	private CCombo visualizationSelect;
	private AddMapLayerDialog propertyDialog;

	public StreamSelectionListener(Collection<IPhysicalOperator> operators, LayerConfiguration config,
			CCombo dataSourceCombo, CCombo combobox, CCombo geometrieSelect, CCombo latSelect, CCombo lngSelect,
			CCombo visualizationSelect, AddMapLayerDialog propertyDialog) {
		this.operators = new ArrayList<IPhysicalOperator>(operators);
		this.dataSourceCombo = dataSourceCombo;
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
		latSelect.removeAll();
		lngSelect.removeAll();
		visualizationSelect.removeAll();

		// Use the selected data source (operator)
		if (operators.size() > 0) {
			SDFSchema schema = operators.get(dataSourceCombo.getSelectionIndex()).getOutputSchema();

			for (int i = 0; i < schema.size(); i++) {
				geometrieSelect.add(schema.getAttribute(i).getAttributeName(), i);
			}

			for (int i = 0; i < schema.size(); i++) {
				latSelect.add(schema.getAttribute(i).getAttributeName(), i);
			}

			for (int i = 0; i < schema.size(); i++) {
				lngSelect.add(schema.getAttribute(i).getAttributeName(), i);
			}

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
				visualizationSelect.select(0);
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
			} else if (this.combobox.getText().equals("Tracemap")) {
				// User has selected the Tracemap
				if (!(this.config instanceof TracemapLayerConfiguration))
					this.config = new TracemapLayerConfiguration("");
				final TracemapLayerConfiguration tracemapLayerConfiguration = (TracemapLayerConfiguration) this.config;

				tracemapLayerConfiguration.setGeometricAttributePosition(geometrieSelect.getSelectionIndex());
				tracemapLayerConfiguration.setValueAttributePosition(visualizationSelect.getSelectionIndex());
				this.config = tracemapLayerConfiguration;

				propertyDialog.setLayerConfiguration(config);
			}
		} else {
			propertyDialog.setMessage("No query available. Thematic maps won't be possible.", IMessageProvider.INFORMATION);
		}
	}
}
