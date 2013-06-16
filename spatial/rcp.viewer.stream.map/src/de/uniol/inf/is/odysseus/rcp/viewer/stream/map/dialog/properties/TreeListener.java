package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.dialog.properties;

import java.util.HashMap;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
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
import org.eclipse.swt.widgets.Tree;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.HeatmapLayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.LayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.TracemapLayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.heatmap.Heatmap;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.tracemap.TraceLayer;

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
				// Show the settings for the heatmap
				Heatmap heatmap = (Heatmap) selection.getFirstElement();
				createHeatmapMenu(heatmap);

			} else if (selection.getFirstElement() instanceof TraceLayer) {
				// Show the settings for the tracemap
				TraceLayer tracemap = (TraceLayer) selection.getFirstElement();
				createTraceLayerpMenu(tracemap);				

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

	/**
	 * Updates the config of the parent (necessary for ok-click)
	 * 
	 * @param config
	 */
	void updateParentConfig(LayerConfiguration config) {
		parentDialog.setLayerConfiguration(config);
	}
	
	private void createTraceLayerpMenu(TraceLayer tracemap) {
		// Remove everything except the Tree on the left
		removeContent(container);
		TracemapLayerConfiguration newConfig = new TracemapLayerConfiguration(tracemap.getConfig());
		
		// New container
		Composite tracemapContainer = new Composite(container, SWT.NONE);
		tracemapContainer.setLayout(new GridLayout(1, false));
		tracemapContainer.setLayoutData(new GridData(SWT.FILL,
				SWT.FILL, true, true, 1, 1));
		
		// Show the settings for the tracemap
		Group settingsContainer = new Group(tracemapContainer, SWT.NONE);
		settingsContainer.setLayoutData(new GridData(SWT.FILL,
				SWT.FILL, true, true, 1, 1));
		settingsContainer.setText("Tracemap settings");
		settingsContainer.setLayout(new GridLayout(2, false));

		// Position of geometry-Attribute
		Label geoAttrLabel = new Label(settingsContainer, SWT.NONE);
		geoAttrLabel.setText("Position Geo-Attribute: ");

		Spinner geoAttrInput = new Spinner(settingsContainer, SWT.NONE);
		geoAttrInput.setValues(newConfig.getGeometricAttributePosition(), 0,
				255, 0, 1, 1);
		geoAttrInput.addSelectionListener(new SpinnerListener(newConfig,
				geoAttrInput, this) {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TracemapLayerConfiguration tracemapLayerConfig = (TracemapLayerConfiguration) layerConfig;
				tracemapLayerConfig.setGeometricAttributePosition((spinner
						.getSelection()));
				treeListener.updateParentConfig(tracemapLayerConfig);
			}
		});
		
		// Mark end of line
		Label markEndIfLineLabel = new Label(settingsContainer, SWT.NONE);
		markEndIfLineLabel.setText("Mark end of line: ");
		
		Button markEndIfLineButton = new Button(settingsContainer, SWT.CHECK);
		markEndIfLineButton.setEnabled(true);
		markEndIfLineButton.setSelection(newConfig.isMarkEndpoint());
		markEndIfLineButton.addSelectionListener(new ButtonListener(
				newConfig, this, markEndIfLineButton) {

			@Override
			public void widgetSelected(SelectionEvent e) {
				TracemapLayerConfiguration tracemapLayerConfiguration = (TracemapLayerConfiguration) layerConfiguration;
				tracemapLayerConfiguration
						.setMarkEndpoint((correspondingButton.getSelection()));
				treeListener.updateParentConfig(tracemapLayerConfiguration);
			}
		});
		
		// Width of lines
		Label lineWidthLabel = new Label(settingsContainer, SWT.NONE);
		lineWidthLabel.setText("Width of lines: ");

		Spinner lineWidthInput = new Spinner(settingsContainer, SWT.NONE);
		lineWidthInput.setValues(newConfig.getLineWidth(), 0,
				255, 0, 1, 1);
		lineWidthInput.addSelectionListener(new SpinnerListener(newConfig,
				lineWidthInput, this) {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TracemapLayerConfiguration tracemapLayerConfig = (TracemapLayerConfiguration) layerConfig;
				tracemapLayerConfig.setLineWidth((spinner
						.getSelection()));
				treeListener.updateParentConfig(tracemapLayerConfig);
			}
		});
		
		// Automatic transparency
		Label autoTransparencyLabel = new Label(settingsContainer, SWT.NONE);
		autoTransparencyLabel.setText("Automatic transparency: ");
		Button autoTransparencyButton = new Button(settingsContainer, SWT.CHECK);
		autoTransparencyButton.setEnabled(true);
		autoTransparencyButton.setSelection(newConfig.isAutoTransparency());
		autoTransparencyButton.addSelectionListener(new ButtonListener(
				newConfig, this, autoTransparencyButton) {

			@Override
			public void widgetSelected(SelectionEvent e) {
				TracemapLayerConfiguration tracemapLayerConfiguration = (TracemapLayerConfiguration) layerConfiguration;
				tracemapLayerConfiguration
						.setAutoTransparency(correspondingButton.getSelection());
				treeListener.updateParentConfig(tracemapLayerConfiguration);
			}
		});
		
		// Number of elements to show
		Label numElements = new Label(settingsContainer, SWT.NONE);
		numElements.setText("Number of elements to show (if no auto-transparency): ");
		
		Spinner numElementsInput = new Spinner(settingsContainer, SWT.NONE);
		numElementsInput.setValues(newConfig.getNumOfLineElements(), 1,
				Integer.MAX_VALUE, 0, 1, 1);
		numElementsInput.addSelectionListener(new SpinnerListener(newConfig,
				numElementsInput, this) {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TracemapLayerConfiguration tracemapLayerConfig = (TracemapLayerConfiguration) layerConfig;
				tracemapLayerConfig.setNumOfLineElements(spinner.getSelection());
				treeListener.updateParentConfig(tracemapLayerConfig);
			}
		});
		
		
		// Color settings
		// Show the chooser and a label with the selected color next to
		// it
		for (Integer key : newConfig.getColors().keySet()) {
			Label colorLabel = new Label(settingsContainer, SWT.NONE);
			colorLabel.setText("Choose color for id: " + key);

			Composite colorContainer = new Composite(settingsContainer,
					SWT.NONE);
			colorContainer.setLayout(new GridLayout(2, false));
			colorContainer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
					true, true, 1, 0));

			Button colorButton = new Button(colorContainer, SWT.PUSH);
			colorButton.setText("Change color ...");

			Label colorView = new Label(colorContainer, SWT.NONE);
			colorView.setText("      ");
			colorView.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true,
					true, 1, 1));
			colorView.setBackground(newConfig.getColorForId(key));

			colorButton.addSelectionListener(new TraceColorListener(newConfig,
					key, this, colorView));
		}
		
		// Show the statistics for the tracemap
		Group statisticsContainer = new Group(tracemapContainer, SWT.NONE);
		statisticsContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		statisticsContainer.setText("Tracemap statistics");
		statisticsContainer.setLayout(new GridLayout(1, false));
		
		// Length of the traces
		HashMap<Integer, Double> distances = tracemap.getAllLineDistances();
		for(Integer key : distances.keySet()) {
			Label lenTraceLabel = new Label(statisticsContainer, SWT.NONE);
		lenTraceLabel.setText("Length of trace " + key + ": "
				+ String.valueOf(distances.get(key) + " km"));		
		}
		
//		// Speed of the trace
//				Label speedTraceLabel = new Label(statisticsContainer, SWT.NONE);
//				speedTraceLabel.setText("Speed of trace " + 1 + " (km/h): "
//						+ String.valueOf(3));
				
		// Redraw the container
		container.layout();
	}

	private void createHeatmapMenu(Heatmap heatmap) {
		// Create a new configuration, that is used, if the user clicks
		// ok
		HeatmapLayerConfiguration newConfig = new HeatmapLayerConfiguration(
				heatmap.getConfig());

		// Remove everything except the Tree on the left
		removeContent(container);

		// New container
		Composite heatmapContainer = new Composite(container, SWT.NONE);
		heatmapContainer.setLayout(new GridLayout(2, false));
		heatmapContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));

		// Show the settings for the heatmap
		Group settingsContainer = new Group(heatmapContainer, SWT.NONE);
		settingsContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		settingsContainer.setText("Heatmap settings");
		settingsContainer.setLayout(new GridLayout(2, false));

		// Position of geometry-Attribute
		Label geoAttrLabel = new Label(settingsContainer, SWT.NONE);
		geoAttrLabel.setText("Position Geo-Attribute: ");

		Spinner geoAttrInput = new Spinner(settingsContainer, SWT.NONE);
		geoAttrInput.setValues(newConfig.getGeometricAttributePosition(), 0,
				255, 0, 1, 1);
		geoAttrInput.addSelectionListener(new SpinnerListener(newConfig,
				geoAttrInput, this) {
			@Override
			public void widgetSelected(SelectionEvent e) {
				HeatmapLayerConfiguration heatmapLayerConfig = (HeatmapLayerConfiguration) layerConfig;
				heatmapLayerConfig.setGeometricAttributePosition((spinner
						.getSelection()));
				treeListener.updateParentConfig(heatmapLayerConfig);
			}
		});

		// Colors
		Label colorMinLabel = new Label(settingsContainer, SWT.NONE);
		colorMinLabel.setText("Color for min-value: ");

		// Show the chooser and a label with the selected color next to
		// it
		Composite minColorContainer = new Composite(settingsContainer, SWT.NONE);
		minColorContainer.setLayout(new GridLayout(2, false));
		minColorContainer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, true, 1, 0));

		Button colorMinButton = new Button(minColorContainer, SWT.PUSH);
		colorMinButton.setText("Change color ...");

		Label minColorView = new Label(minColorContainer, SWT.NONE);
		minColorView.setText("      ");
		minColorView.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true,
				true, 1, 1));
		minColorView.setBackground(newConfig.getMinColor());

		colorMinButton.addSelectionListener(new HeatColorListener(newConfig, this,
				minColorView) {
			public void widgetSelected(SelectionEvent e) {
				Shell s = new Shell(Display.getDefault());
				ColorDialog cd = new ColorDialog(s);
				cd.setText("Color for min-value");
				cd.setRGB(new RGB(heatmapLayerConfiguration.getMaxColor()
						.getRed(), heatmapLayerConfiguration.getMaxColor()
						.getGreen(), heatmapLayerConfiguration.getMaxColor()
						.getBlue()));
				RGB newColor = cd.open();
				if (newColor == null) {
					return;
				}

				// The user has chosen a color
				Color color = new Color(Display.getDefault(), newColor.red,
						newColor.green, newColor.blue);
				correspondingLabel.setBackground(color);
				heatmapLayerConfiguration.setMinColor(color);
				treeListener.updateParentConfig(heatmapLayerConfiguration);
			}
		});

		Label colorMaxLabel = new Label(settingsContainer, SWT.NONE);
		colorMaxLabel.setText("Color for max-value: ");

		// Show the chooser and a label with the selected color next to
		// it
		Composite maxColorContainer = new Composite(settingsContainer, SWT.NONE);
		maxColorContainer.setLayout(new GridLayout(2, false));
		maxColorContainer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, true, 1, 0));

		Button colorMaxButton = new Button(maxColorContainer, SWT.PUSH);
		colorMaxButton.setText("Change color ...");

		Label maxColorView = new Label(maxColorContainer, SWT.NONE);
		maxColorView.setText("      ");
		maxColorView.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true,
				true, 1, 1));
		maxColorView.setBackground(newConfig.getMaxColor());

		colorMaxButton.addSelectionListener(new HeatColorListener(newConfig, this,
				maxColorView) {
			public void widgetSelected(SelectionEvent e) {
				Shell s = new Shell(Display.getDefault());
				ColorDialog cd = new ColorDialog(s);
				cd.setText("Color for max-value");
				cd.setRGB(new RGB(heatmapLayerConfiguration.getMaxColor()
						.getRed(), heatmapLayerConfiguration.getMaxColor()
						.getGreen(), heatmapLayerConfiguration.getMaxColor()
						.getBlue()));
				RGB newColor = cd.open();
				if (newColor == null) {
					return;
				}

				// The user has chosen a color
				Color color = new Color(Display.getDefault(), newColor.red,
						newColor.green, newColor.blue);
				correspondingLabel.setBackground(color);
				heatmapLayerConfiguration.setMaxColor(color);
				treeListener.updateParentConfig(heatmapLayerConfiguration);
			}
		});

		// Transparency
		Label transparencyLabel = new Label(settingsContainer, SWT.NONE);
		transparencyLabel.setText("Opacity (0 = transparent): ");

		Spinner transparencyInput = new Spinner(settingsContainer, SWT.NONE);
		transparencyInput.setValues(newConfig.getAlpha(), 0, 255, 0, 1, 1);
		transparencyInput.addSelectionListener(new SpinnerListener(newConfig,
				transparencyInput, this) {
			@Override
			public void widgetSelected(SelectionEvent e) {
				HeatmapLayerConfiguration heatmapLayerConfig = (HeatmapLayerConfiguration) layerConfig;
				heatmapLayerConfig.setAlpha(spinner.getSelection());
				treeListener.updateParentConfig(heatmapLayerConfig);
			}
		});

		// Number of tiles
		Label numTilesWidthLabel = new Label(settingsContainer, SWT.NONE);
		numTilesWidthLabel.setText("Number of tiles horizontal: ");

		Spinner numTilesWidthInput = new Spinner(settingsContainer, SWT.NONE);
		numTilesWidthInput.setValues(newConfig.getNumTilesWidth(), 0,
				Integer.MAX_VALUE, 0, 1, 1);
		numTilesWidthInput.addSelectionListener(new SpinnerListener(newConfig,
				numTilesWidthInput, this) {
			@Override
			public void widgetSelected(SelectionEvent e) {
				HeatmapLayerConfiguration heatmapLayerConfig = (HeatmapLayerConfiguration) layerConfig;
				heatmapLayerConfig.setNumTilesWidth(spinner.getSelection());
				treeListener.updateParentConfig(heatmapLayerConfig);
			}
		});

		Label numTilesHeightLabel = new Label(settingsContainer, SWT.NONE);
		numTilesHeightLabel.setText("Number of tiles vertical: ");

		Spinner numTilesHeightInput = new Spinner(settingsContainer, SWT.NONE);
		numTilesHeightInput.setValues(newConfig.getNumTilesHeight(), 0,
				Integer.MAX_VALUE, 0, 1, 1);
		numTilesHeightInput.addSelectionListener(new SpinnerListener(newConfig,
				numTilesHeightInput, this) {
			@Override
			public void widgetSelected(SelectionEvent e) {
				HeatmapLayerConfiguration heatmapLayerConfig = (HeatmapLayerConfiguration) layerConfig;
				heatmapLayerConfig.setNumTilesHeight(spinner.getSelection());
				treeListener.updateParentConfig(heatmapLayerConfig);
			}
		});

		// Position of layer
		Label autoPositionLabel = new Label(settingsContainer, SWT.NONE);
		autoPositionLabel.setText("Automatic positioning: ");
		Button autoPositionButton = new Button(settingsContainer, SWT.CHECK);
		autoPositionButton.setEnabled(true);
		autoPositionButton.setSelection(newConfig.isAutoPosition());
		autoPositionButton.addSelectionListener(new ButtonListener(newConfig,
				this, autoPositionButton) {

			@Override
			public void widgetSelected(SelectionEvent e) {
				HeatmapLayerConfiguration heatmapLayerConfiguration = (HeatmapLayerConfiguration) layerConfiguration;
				heatmapLayerConfiguration.setAutoPosition(correspondingButton
						.getSelection());
				treeListener.updateParentConfig(heatmapLayerConfiguration);
			}
		});

		// SouthWest
		// Latitude
		Label manPosSWLatLabel = new Label(settingsContainer, SWT.NONE);
		manPosSWLatLabel.setText("Southwest Latitude: ");

		Spinner manPosSWLatInput = new Spinner(settingsContainer, SWT.NONE);
		manPosSWLatInput.setValues((int) newConfig.getLatSW() * 1000, -90000,
				90000, 3, 1000, 1000);
		manPosSWLatInput.addSelectionListener(new SpinnerListener(newConfig,
				manPosSWLatInput, this) {
			@Override
			public void widgetSelected(SelectionEvent e) {
				double value = spinner.getSelection() / (Math.pow(10, spinner.getDigits()));
				HeatmapLayerConfiguration heatmapLayerConfig = (HeatmapLayerConfiguration) layerConfig;
				heatmapLayerConfig.setLatSW(value);
				treeListener.updateParentConfig(heatmapLayerConfig);
			}
		});
		
		// Longitude
		Label manPosSWLngLabel = new Label(settingsContainer, SWT.NONE);
		manPosSWLngLabel.setText("Southwest Longitude: ");

		Spinner manPosSWLngInput = new Spinner(settingsContainer, SWT.NONE);
		manPosSWLngInput.setValues((int) newConfig.getLngSW() * 1000, -180000,
				180000, 3, 1000, 1000);
		manPosSWLngInput.addSelectionListener(new SpinnerListener(newConfig,
				manPosSWLngInput, this) {
			@Override
			public void widgetSelected(SelectionEvent e) {
				double value = spinner.getSelection() / (Math.pow(10, spinner.getDigits()));
				HeatmapLayerConfiguration heatmapLayerConfig = (HeatmapLayerConfiguration) layerConfig;
				heatmapLayerConfig.setLngSW(value);
				treeListener.updateParentConfig(heatmapLayerConfig);
			}
		});
		
		// NorthEast
		// Latitude
		Label manPosNELatLabel = new Label(settingsContainer, SWT.NONE);
		manPosNELatLabel.setText("Northeast Latitude: ");

		Spinner manPosNELatInput = new Spinner(settingsContainer, SWT.NONE);
		manPosNELatInput.setValues((int) newConfig.getLatNE() * 1000, -90000,
				90000, 3, 1000, 1000);
		manPosNELatInput.addSelectionListener(new SpinnerListener(newConfig,
				manPosNELatInput, this) {
			@Override
			public void widgetSelected(SelectionEvent e) {
				double value = spinner.getSelection() / (Math.pow(10, spinner.getDigits()));
				HeatmapLayerConfiguration heatmapLayerConfig = (HeatmapLayerConfiguration) layerConfig;
				heatmapLayerConfig.setLatNE(value);
				treeListener.updateParentConfig(heatmapLayerConfig);
			}
		});
		
		// Longitude
		Label manPosNELngLabel = new Label(settingsContainer, SWT.NONE);
		manPosNELngLabel.setText("Northeast Longitude: ");

		Spinner manPosNELngInput = new Spinner(settingsContainer, SWT.NONE);
		manPosNELngInput.setValues((int) newConfig.getLngNE() * 1000, -180000,
				180000, 3, 1000, 1000);
		manPosNELngInput.addSelectionListener(new SpinnerListener(newConfig,
				manPosNELngInput, this) {
			@Override
			public void widgetSelected(SelectionEvent e) {
				double value = spinner.getSelection() / (Math.pow(10, spinner.getDigits()));
				HeatmapLayerConfiguration heatmapLayerConfig = (HeatmapLayerConfiguration) layerConfig;
				heatmapLayerConfig.setLngNE(value);
				treeListener.updateParentConfig(heatmapLayerConfig);
			}
		});
		
		// Show the statistics for this layer
		Group statisticsContainer = new Group(heatmapContainer, SWT.NONE);
		statisticsContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, true, 1, 1));
		statisticsContainer.setText("Heatmap statistics");
		statisticsContainer.setLayout(new GridLayout(1, false));

		// Maximal value
		Label maxValueLabel = new Label(statisticsContainer, SWT.NONE);
		maxValueLabel.setText("Maximal value of a tile: "
				+ String.valueOf(heatmap.getMaxValue()));

		// Minimal value
		Label minValueLabel = new Label(statisticsContainer, SWT.NONE);
		minValueLabel.setText("Minimal value of a tile: "
				+ String.valueOf(heatmap.getMinValue()));

		// Sum value
		Label sumValueLabel = new Label(statisticsContainer, SWT.NONE);
		sumValueLabel.setText("Sum of all values: "
				+ String.valueOf(heatmap.getTotalValue()));

		// Number of elements
		Label numElementsLabel = new Label(statisticsContainer, SWT.NONE);
		numElementsLabel.setText("Number of elements: "
				+ String.valueOf(heatmap.getTotalNumberOfElements()));

		// Average value
		Label avgValueLabel = new Label(statisticsContainer, SWT.NONE);
		avgValueLabel.setText("Average value of an element: "
				+ String.valueOf(heatmap.getAverageValueOfElement()));

		// Number of Tiles
		Label numTilesLabel = new Label(statisticsContainer, SWT.NONE);
		numTilesLabel.setText("Number of tiles: "
				+ String.valueOf(heatmap.getNumberOfTiles()));

		// Average sum of tile
		Label avgTileValueLabel = new Label(statisticsContainer, SWT.NONE);
		avgTileValueLabel.setText("Average sum of a tile: "
				+ String.valueOf(heatmap.getAverageValueOfTile()));
		
		
		// Give the OK-Button the possibility to set a new config
		parentDialog.setLayerConfiguration(newConfig);
		// Redraw the container
		container.layout();
	}
}
