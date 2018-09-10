package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.dialog;

import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.dialog.properties.MapPropertiesDialog;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.HeatmapLayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.LayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.TracemapLayerConfiguration;

public class AttributeListener extends SelectionAdapter {

	LayerConfiguration config;
	CCombo geoAttributeCombo;
	CCombo valueAttributeCombo;
	MapPropertiesDialog mapPropertiesDialog;

	public AttributeListener(LayerConfiguration config, CCombo geoAttributeCombo, CCombo valueAttributeCombo,
			MapPropertiesDialog mapPropertiesDialog) {
		this.config = config;
		this.geoAttributeCombo = geoAttributeCombo;
		this.valueAttributeCombo = valueAttributeCombo;
		this.mapPropertiesDialog = mapPropertiesDialog;
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		if (config instanceof HeatmapLayerConfiguration) {
			((HeatmapLayerConfiguration) config).setGeometricAttributePosition(geoAttributeCombo.getSelectionIndex());
			((HeatmapLayerConfiguration) config).setValueAttributePosition(valueAttributeCombo.getSelectionIndex());
		} else if (config instanceof TracemapLayerConfiguration) {
			((TracemapLayerConfiguration) config).setGeometricAttributePosition(geoAttributeCombo.getSelectionIndex());
			((TracemapLayerConfiguration) config).setValueAttributePosition(valueAttributeCombo.getSelectionIndex());
		}
		mapPropertiesDialog.setLayerConfiguration(config);
	}
}
