package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.dialog.properties;

import java.util.HashMap;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
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
				createTraceLayerMenu(tracemap);

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

	private void createTraceLayerMenu(TraceLayer tracemap) {
		// Remove everything except the Tree on the left
		removeContent(container);
		TracemapLayerConfiguration newConfig = new TracemapLayerConfiguration(
				tracemap.getConfig());

		// New container for scrolling
		ScrolledComposite scrollContainer = new ScrolledComposite(container,
				SWT.V_SCROLL | SWT.BORDER);
		scrollContainer.setLayout(new GridLayout(1, false));
		scrollContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));

		Composite tracemapContainer = new Composite(scrollContainer, SWT.NONE);
		tracemapContainer.setLayout(new GridLayout(1, false));
		tracemapContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));

		// Show the settings for the tracemap
		Group settingsContainer = new Group(tracemapContainer, SWT.NONE);
		settingsContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
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

		// Position of value-Attribute
		Label valueAttrLabel = new Label(settingsContainer, SWT.NONE);
		valueAttrLabel.setText("Position Value-Attribute: ");

		Spinner valueAttrInput = new Spinner(settingsContainer, SWT.NONE);
		valueAttrInput.setValues(newConfig.getValueAttributePosition(), 0,
				255, 0, 1, 1);
		valueAttrInput.addSelectionListener(new SpinnerListener(newConfig,
				valueAttrInput, this) {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TracemapLayerConfiguration tracemapLayerConfig = (TracemapLayerConfiguration) layerConfig;
				tracemapLayerConfig.setValueAttributePosition((spinner
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
		markEndIfLineButton.addSelectionListener(new ButtonListener(newConfig,
				this, markEndIfLineButton) {

			@Override
			public void widgetSelected(SelectionEvent e) {
				TracemapLayerConfiguration tracemapLayerConfiguration = (TracemapLayerConfiguration) layerConfiguration;
				tracemapLayerConfiguration.setMarkEndpoint((correspondingButton
						.getSelection()));
				treeListener.updateParentConfig(tracemapLayerConfiguration);
			}
		});

		// Width of lines
		Label lineWidthLabel = new Label(settingsContainer, SWT.NONE);
		lineWidthLabel.setText("Width of lines: ");

		Spinner lineWidthInput = new Spinner(settingsContainer, SWT.NONE);
		lineWidthInput.setValues(newConfig.getLineWidth(), 0, 255, 0, 1, 1);
		lineWidthInput.addSelectionListener(new SpinnerListener(newConfig,
				lineWidthInput, this) {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TracemapLayerConfiguration tracemapLayerConfig = (TracemapLayerConfiguration) layerConfig;
				tracemapLayerConfig.setLineWidth((spinner.getSelection()));
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
		numElements
				.setText("Number of elements to show (if no auto-transparency): ");

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
		final Group statisticsContainer = new Group(tracemapContainer, SWT.NONE);
		statisticsContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, true, 1, 1));
		statisticsContainer.setText("Tracemap statistics");
		statisticsContainer.setLayout(new GridLayout(1, false));

		// Update-Button (for the statistics)
		// (has to be on top cause after click on update
		// it will be on the top)
		Button updateButton = new Button(statisticsContainer, SWT.PUSH);
		updateButton.setText("Update statistics");

		// Length and speed of the traces
		HashMap<Integer, Double> distances = tracemap.getAllLineDistances();
		HashMap<Integer, Double> speeds = tracemap.getAllLineSpeeds();

		for (Integer key : distances.keySet()) {
			Label lenTraceLabel = new Label(statisticsContainer, SWT.NONE);
			lenTraceLabel.setText("Length of trace " + key + ": "
					+ String.valueOf(distances.get(key)) + " km ("
					+ speeds.get(key) + " km/h)");
		}

		updateButton.addSelectionListener(new UpdateButtonListener(tracemap) {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TraceLayer tracemap = (TraceLayer) layer;
				// Remove all statistics
				for (Control control : statisticsContainer.getChildren()) {
					if (!(control instanceof Button)) {
						// But leave the updatebutton there
						control.dispose();
					}
				}

				// Add them again
				// Length and speed of the traces
				HashMap<Integer, Double> distances = tracemap
						.getAllLineDistances();
				HashMap<Integer, Double> speeds = tracemap.getAllLineSpeeds();

				for (Integer key : distances.keySet()) {
					Label lenTraceLabel = new Label(statisticsContainer,
							SWT.NONE);
					lenTraceLabel.setText("Length of trace " + key + ": "
							+ String.valueOf(distances.get(key)) + " km ("
							+ speeds.get(key) + " km/h)");
					statisticsContainer.layout();
				}
			}
		});

		scrollContainer.setContent(tracemapContainer);
		tracemapContainer.setSize(tracemapContainer.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));

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

		// New container for scrolling
		ScrolledComposite scrollContainer = new ScrolledComposite(container,
				SWT.V_SCROLL | SWT.BORDER);
		scrollContainer.setLayout(new GridLayout(1, false));
		scrollContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));

		// New container
		Composite heatmapContainer = new Composite(scrollContainer, SWT.NONE);
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

		// Position of value-Attribute
		Label valueAttrLabel = new Label(settingsContainer, SWT.NONE);
		valueAttrLabel.setText("Position Value-Attribute: ");

		Spinner valueAttrInput = new Spinner(settingsContainer, SWT.NONE);
		valueAttrInput.setValues(newConfig.getValueAttributePosition(), 0,
				255, 0, 1, 1);
		valueAttrInput.addSelectionListener(new SpinnerListener(newConfig,
				valueAttrInput, this) {
			@Override
			public void widgetSelected(SelectionEvent e) {
				HeatmapLayerConfiguration heatmapLayerConfig = (HeatmapLayerConfiguration) layerConfig;
				heatmapLayerConfig.setValueAttributePosition((spinner
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

		colorMinButton.addSelectionListener(new HeatColorListener(newConfig,
				this, minColorView) {
			@Override
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

		colorMaxButton.addSelectionListener(new HeatColorListener(newConfig,
				this, maxColorView) {
			@Override
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

		// Interpolation
		Label interpolationLabel = new Label(settingsContainer, SWT.NONE);
		interpolationLabel.setText("Interpolation: ");
		Button interpolationButton = new Button(settingsContainer, SWT.CHECK);
		interpolationButton.setEnabled(true);
		interpolationButton.setSelection(newConfig.isInterpolation());
		interpolationButton.addSelectionListener(new ButtonListener(newConfig,
				this, interpolationButton) {

			@Override
			public void widgetSelected(SelectionEvent e) {
				HeatmapLayerConfiguration heatmapLayerConfiguration = (HeatmapLayerConfiguration) layerConfiguration;
				heatmapLayerConfiguration.setInterpolation(correspondingButton
						.getSelection());
				treeListener.updateParentConfig(heatmapLayerConfiguration);
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
				double value = spinner.getSelection()
						/ (Math.pow(10, spinner.getDigits()));
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
				double value = spinner.getSelection()
						/ (Math.pow(10, spinner.getDigits()));
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
				double value = spinner.getSelection()
						/ (Math.pow(10, spinner.getDigits()));
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
				double value = spinner.getSelection()
						/ (Math.pow(10, spinner.getDigits()));
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
		final Label maxValueLabel = new Label(statisticsContainer, SWT.NONE);
		maxValueLabel.setText("Maximal value of a tile: "
				+ String.valueOf(heatmap.getMaxValue()));

		// Minimal value
		final Label minValueLabel = new Label(statisticsContainer, SWT.NONE);
		minValueLabel.setText("Minimal value of a tile: "
				+ String.valueOf(heatmap.getMinValue()));

		// Sum value
		final Label sumValueLabel = new Label(statisticsContainer, SWT.NONE);
		sumValueLabel.setText("Sum of all values: "
				+ String.valueOf(heatmap.getTotalValue()));

		// Number of elements
		final Label numElementsLabel = new Label(statisticsContainer, SWT.NONE);
		numElementsLabel.setText("Number of elements: "
				+ String.valueOf(heatmap.getTotalNumberOfElements()));

		// Average value
		final Label avgValueLabel = new Label(statisticsContainer, SWT.NONE);
		avgValueLabel.setText("Average value of an element: "
				+ String.valueOf(heatmap.getAverageValueOfElement()));

		// Number of Tiles
		final Label numTilesLabel = new Label(statisticsContainer, SWT.NONE);
		numTilesLabel.setText("Number of tiles: "
				+ String.valueOf(heatmap.getNumberOfTiles()));

		// Average sum of tile
		final Label avgTileValueLabel = new Label(statisticsContainer, SWT.NONE);
		avgTileValueLabel.setText("Average sum of a tile: "
				+ String.valueOf(heatmap.getAverageValueOfTile()));

		// Update-Button
		Button updateButton = new Button(statisticsContainer, SWT.PUSH);
		updateButton.setText("Update statistics");

		updateButton.addSelectionListener(new UpdateButtonListener(heatmap) {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Heatmap heatmap = (Heatmap) layer;
				maxValueLabel.setText("Maximal value of a tile: "
						+ String.valueOf(heatmap.getMaxValue()));

				minValueLabel.setText("Minimal value of a tile: "
						+ String.valueOf(heatmap.getMinValue()));

				sumValueLabel.setText("Sum of all values: "
						+ String.valueOf(heatmap.getTotalValue()));

				numElementsLabel.setText("Number of elements: "
						+ String.valueOf(heatmap.getTotalNumberOfElements()));

				avgValueLabel.setText("Average value of an element: "
						+ String.valueOf(heatmap.getAverageValueOfElement()));

				numTilesLabel.setText("Number of tiles: "
						+ String.valueOf(heatmap.getNumberOfTiles()));

				avgTileValueLabel.setText("Average sum of a tile: "
						+ String.valueOf(heatmap.getAverageValueOfTile()));
			}
		});

		scrollContainer.setContent(heatmapContainer);
		heatmapContainer.setSize(heatmapContainer.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));

		// Redraw the container
		container.layout();
	}
}
