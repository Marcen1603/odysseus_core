package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.dialog.properties;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.heatmap.Heatmap;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.linemap.LinemapLayer;

public class TreeListener implements ISelectionChangedListener {

	Composite container;
	MapPropertiesDialog parentDialog;

	public TreeListener(Composite container, MapPropertiesDialog parentDialog) {
		this.container = container;
		this.parentDialog = parentDialog;
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		if (event.getSelection().isEmpty()) {
			return;
		}
		if (event.getSelection() instanceof TreeSelection) {
			IStructuredSelection selection = (IStructuredSelection) event
					.getSelection();

			if (selection.getFirstElement() instanceof Heatmap) {
				// Remove everything except the Tree on the left
				removeContent(container);

				// New container, so we have the boxes above each other, not next to each other
				Composite heatmapContainer = new Composite(container, SWT.NONE);
				heatmapContainer.setLayout(new GridLayout(1, false));
				heatmapContainer.setLayoutData(new GridData(SWT.FILL,
						SWT.FILL, true, true, 1, 1));
				
				// Show the settings for the heatmap
				Group settingsContainer = new Group(heatmapContainer, SWT.NONE);
				settingsContainer.setLayoutData(new GridData(SWT.FILL,
						SWT.FILL, true, true, 1, 1));
				settingsContainer.setText("Heatmap settings");
				settingsContainer.setLayout(new GridLayout(2, false));
				
				 // Position of geometry-Attribute
				Label geoAttrLabel = new Label(settingsContainer, SWT.NONE);
				geoAttrLabel.setText("Position Geo-Attribute: ");

				Text geoAttrInput = new Text(settingsContainer, SWT.BORDER);
				geoAttrInput.setText("0");
				
				// Colors
				Label colorMinLabel = new Label(settingsContainer, SWT.NONE);
				colorMinLabel.setText("Color for min-value: ");

				Button colorMinButton = new Button(settingsContainer, SWT.PUSH);
				colorMinButton.setText("Change color ...");
				colorMinButton.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						Shell s = new Shell(Display.getDefault());
						ColorDialog cd = new ColorDialog(s);
						cd.setText("Color for min-value");
						cd.setRGB(new RGB(0, 255, 0));
						RGB newColor = cd.open();
						if (newColor == null) {
							return;
						}
					}
				});
				
				Label colorMaxLabel = new Label(settingsContainer, SWT.NONE);
				colorMaxLabel.setText("Color for max-value: ");

				Button colorMaxButton = new Button(settingsContainer, SWT.PUSH);
				colorMaxButton.setText("Change color ...");
				colorMaxButton.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						Shell s = new Shell(Display.getDefault());
						ColorDialog cd = new ColorDialog(s);
						cd.setText("Color for max-value");
						cd.setRGB(new RGB(255, 0, 0));
						RGB newColor = cd.open();
						if (newColor == null) {
							return;
						}
					}
				});
				
				// Number of tiles
				Label numTilesWidthLabel = new Label(settingsContainer, SWT.NONE);
				numTilesWidthLabel.setText("Number of tiles horizontal: ");

				Spinner numTilesWidthInput = new Spinner(settingsContainer, SWT.NONE);
				numTilesWidthInput.setValues(10, 0, Integer.MAX_VALUE, 0, 1, 1);
				
				Label numTilesHeightLabel = new Label(settingsContainer, SWT.NONE);
				numTilesHeightLabel.setText("Number of tiles vertical: ");
				
				Spinner numTilesHeightInput = new Spinner(settingsContainer, SWT.NONE);
				numTilesHeightInput.setValues(10, 0, Integer.MAX_VALUE, 0, 1, 1);
				
				// Position of layer				
				Label autoPositionLabel = new Label(settingsContainer, SWT.NONE);
				autoPositionLabel.setText("Automatic positioning: ");

				Button autoPositionButton = new Button(settingsContainer, SWT.CHECK);
				autoPositionButton.setEnabled(true);
				
				Label manPosSWLabel = new Label(settingsContainer, SWT.NONE);
				manPosSWLabel.setText("Southwest: ");				
				
				Label manPosNELabel = new Label(settingsContainer, SWT.NONE);
				manPosNELabel.setText("Northeast: ");	
				
				// Show the statistics for this layer
				Group statisticsContainer = new Group(heatmapContainer, SWT.NONE);
				statisticsContainer.setLayoutData(new GridData(SWT.FILL,
						SWT.FILL, true, true, 1, 1));
				statisticsContainer.setText("Heatmap statistics");
				statisticsContainer.setLayout(new GridLayout(2, false));

				// Give Ok-Button the functionality
				
				// Redraw the container
				container.layout();

				// Do we need this here?
				// Heatmap heatmap = (Heatmap) selection.getFirstElement();
			} else if (selection.getFirstElement() instanceof LinemapLayer) {
				// Show the settings for the linemap
			} else {
				// Show normal content - not for thematic maps

				// Remove everything except the Tree on the left
				removeContent(container);

				// Fill with standard-content
				parentDialog.createStandardContent(container);
			}

		}

	}

	/**
	 * Removes everything from the container except the Tree (on the left)
	 * 
	 * @param container
	 */
	private void removeContent(Composite container) {
		// Remove old content
		for (Control child : container.getChildren()) {
			// But leave the TreeView there
			if (!child.getClass().equals(Tree.class))
				child.dispose();
		}
	}

}
