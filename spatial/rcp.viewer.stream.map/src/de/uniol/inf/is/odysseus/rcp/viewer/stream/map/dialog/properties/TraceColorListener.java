package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.dialog.properties;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.TracemapLayerConfiguration;

public class TraceColorListener extends SelectionAdapter {

	TracemapLayerConfiguration tracemapLayerConfiguration;
	TreeListener treeListener;
	Label correspondingLabel;
	int id;

	public TraceColorListener(
			TracemapLayerConfiguration tracemapLayerConfiguration, int id,
			TreeListener treeListener, Label correspondingLabel) {
		this.tracemapLayerConfiguration = tracemapLayerConfiguration;
		this.treeListener = treeListener;
		this.correspondingLabel = correspondingLabel;
		this.id = id;
	}

	public void widgetSelected(SelectionEvent e) {
		Shell s = new Shell(Display.getDefault());
		ColorDialog cd = new ColorDialog(s); 
		cd.setText("Color for line with id " + id);
		cd.setRGB(new RGB(
				tracemapLayerConfiguration.getColorForId(id).getRed(),
				tracemapLayerConfiguration.getColorForId(id).getGreen(),
				tracemapLayerConfiguration.getColorForId(id).getBlue()));
		RGB newColor = cd.open();
		if (newColor == null) {
			return;
		}

		// The user has chosen a color
		Color color = new Color(Display.getDefault(), newColor.red,
				newColor.green, newColor.blue);
		tracemapLayerConfiguration.setColorForId(id, color);
		correspondingLabel.setBackground(color);
		treeListener.updateParentConfig(tracemapLayerConfiguration);	
	}
}
