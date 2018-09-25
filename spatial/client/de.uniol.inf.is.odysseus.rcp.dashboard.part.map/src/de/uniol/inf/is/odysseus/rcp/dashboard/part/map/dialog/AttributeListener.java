package de.uniol.inf.is.odysseus.rcp.dashboard.part.map.dialog;

import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.dialog.properties.AbstractMapPropertiesDialog;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.HeatmapLayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.LayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.TracemapLayerConfiguration;

public class AttributeListener extends SelectionAdapter {

	LayerConfiguration config;
	CCombo geoAttributeCombo;
	CCombo latAttributeCombo;
	CCombo lngAttributeCombo;
	CCombo valueAttributeCombo;
	AbstractMapPropertiesDialog propertiesDialog;

	public AttributeListener(LayerConfiguration config, CCombo geoAttributeCombo, CCombo latAttributeCombo,
			CCombo lngAttributeCombo, CCombo valueAttributeCombo, AbstractMapPropertiesDialog propertiesDialog) {
		this.config = config;
		this.geoAttributeCombo = geoAttributeCombo;
		this.latAttributeCombo = latAttributeCombo;
		this.lngAttributeCombo = lngAttributeCombo;
		this.valueAttributeCombo = valueAttributeCombo;
		this.propertiesDialog = propertiesDialog;
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
		propertiesDialog.setLayerConfiguration(config);
	}
}
