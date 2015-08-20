package de.uniol.inf.is.odysseus.rcp.dashboard.part.map.dialog.properties;

import java.util.HashMap;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.dashboard.MapDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.dialog.AttributeListener;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.dialog.DialogUtils;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.HeatmapLayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.LayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.style.Style;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.thematic.heatmap.Heatmap;

/**
 * 
 * Dialog to edit the properties of a map
 * 
 * @author Stefan Bothe
 * 
 */
public class MapPropertiesDialog extends TitleAreaDialog {



	HashMap<Integer, Style> hashStyles;
	
	private Heatmap heatmap;
	private IPhysicalOperator operator;
	private LayerConfiguration layerConfiguration = null;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 * @param mapDashboardPart
	 */
	public MapPropertiesDialog(Shell parentShell, MapDashboardPart mapDashboardPart, Heatmap heatmap,
			IPhysicalOperator operator) {
		super(parentShell);
		this.setShellStyle(SWT.MAX | SWT.RESIZE);
		this.heatmap = heatmap;
		this.operator = operator;
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {

		setTitle("HeatMap Properties");
		setMessage("Edit map parameters.", IMessageProvider.INFORMATION);
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		GridData gd_container = new GridData(GridData.FILL, SWT.FILL, true, true);
		gd_container.heightHint = 317;
		container.setLayoutData(gd_container);

		// Fill the area with heatmap-content
		createHeatmapMenu(heatmap, container);

		return area;
	}

	/**
	 * Creates the menu for heatmaps with all the settings that can be made
	 * 
	 * @param heatmap
	 * @param container
	 */
	public void createHeatmapMenu(Heatmap heatmap, Composite container) {
		// Create a new configuration, that is used, if the user clicks
		// ok
		HeatmapLayerConfiguration newConfig = new HeatmapLayerConfiguration(heatmap.getConfig());

		// Remove everything except the Tree on the left
		removeContent(container);

		// New container for scrolling
		ScrolledComposite scrollContainer = new ScrolledComposite(container, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		scrollContainer.setLayout(new GridLayout(1, false));
		scrollContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		// New container
		Composite heatmapContainer = new Composite(scrollContainer, SWT.NONE);
		heatmapContainer.setLayout(new GridLayout(2, false));
		heatmapContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		// Show the settings for the heatmap
		Group settingsContainer = new Group(heatmapContainer, SWT.NONE);
		settingsContainer.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, true, 1, 1));
		settingsContainer.setText("Heatmap settings");
		settingsContainer.setLayout(new GridLayout(2, false));

		// Position of geometry-Attribute
		Label geoAttrLabel = new Label(settingsContainer, SWT.NONE);
		geoAttrLabel.setText("Geometry Attribute: ");

		CCombo geoAttrInput = new CCombo(settingsContainer, SWT.BORDER);
		geoAttrInput.setLayoutData(DialogUtils.getTextDataLayout());

		// Fill this combobox
		geoAttrInput.removeAll();
		SDFSchema schema = operator.getOutputSchema();

		for (int i = 0; i < schema.size(); i++) {
			geoAttrInput.add(schema.getAttribute(i).getAttributeName(), i);
		}
		geoAttrInput.select(newConfig.getGeometricAttributePosition());

		// Position of value-Attribute
		Label valueAttrLabel = new Label(settingsContainer, SWT.NONE);
		valueAttrLabel.setText("Value-Attribute: ");

		CCombo valueAttrInput = new CCombo(settingsContainer, SWT.NONE);
		valueAttrInput.setLayoutData(DialogUtils.getTextDataLayout());

		for (int i = 0; i < schema.size(); i++) {
			valueAttrInput.add(schema.getAttribute(i).getAttributeName(), i);
		}

		valueAttrInput.select(newConfig.getValueAttributePosition());

		geoAttrInput.addSelectionListener(new AttributeListener(newConfig, geoAttrInput, valueAttrInput, this));
		valueAttrInput.addSelectionListener(new AttributeListener(newConfig, geoAttrInput, valueAttrInput, this));

		// Colors
		Label colorMinLabel = new Label(settingsContainer, SWT.NONE);
		colorMinLabel.setText("Color for min-value: ");

		// Show the chooser and a label with the selected color next to
		// it
		Composite minColorContainer = new Composite(settingsContainer, SWT.NONE);
		minColorContainer.setLayout(new GridLayout(2, false));
		minColorContainer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 0));

		Button colorMinButton = new Button(minColorContainer, SWT.PUSH);
		colorMinButton.setText("Change color ...");

		Label minColorView = new Label(minColorContainer, SWT.NONE);
		minColorView.setText("      ");
		minColorView.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
		minColorView.setBackground(newConfig.getMinColor());

		colorMinButton.addSelectionListener(new HeatColorListener(newConfig, this, minColorView) {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Shell s = new Shell(Display.getDefault());
				ColorDialog cd = new ColorDialog(s);
				cd.setText("Color for min-value");
				cd.setRGB(new RGB(heatmapLayerConfiguration.getMaxColor().getRed(),
						heatmapLayerConfiguration.getMaxColor().getGreen(),
						heatmapLayerConfiguration.getMaxColor().getBlue()));
				RGB newColor = cd.open();
				if (newColor == null) {
					return;
				}

				// The user has chosen a color
				Color color = new Color(Display.getDefault(), newColor.red, newColor.green, newColor.blue);
				correspondingLabel.setBackground(color);
				heatmapLayerConfiguration.setMinColor(color);
				mapPropertiesDialog.setLayerConfiguration(heatmapLayerConfiguration);
			}
		});

		Label colorMaxLabel = new Label(settingsContainer, SWT.NONE);
		colorMaxLabel.setText("Color for max-value: ");

		// Show the chooser and a label with the selected color next to
		// it
		Composite maxColorContainer = new Composite(settingsContainer, SWT.NONE);
		maxColorContainer.setLayout(new GridLayout(2, false));
		maxColorContainer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 0));

		Button colorMaxButton = new Button(maxColorContainer, SWT.PUSH);
		colorMaxButton.setText("Change color ...");

		Label maxColorView = new Label(maxColorContainer, SWT.NONE);
		maxColorView.setText("      ");
		maxColorView.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
		maxColorView.setBackground(newConfig.getMaxColor());

		colorMaxButton.addSelectionListener(new HeatColorListener(newConfig, this, maxColorView) {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Shell s = new Shell(Display.getDefault());
				ColorDialog cd = new ColorDialog(s);
				cd.setText("Color for max-value");
				cd.setRGB(new RGB(heatmapLayerConfiguration.getMaxColor().getRed(),
						heatmapLayerConfiguration.getMaxColor().getGreen(),
						heatmapLayerConfiguration.getMaxColor().getBlue()));
				RGB newColor = cd.open();
				if (newColor == null) {
					return;
				}

				// The user has chosen a color
				Color color = new Color(Display.getDefault(), newColor.red, newColor.green, newColor.blue);
				correspondingLabel.setBackground(color);
				heatmapLayerConfiguration.setMaxColor(color);
				mapPropertiesDialog.setLayerConfiguration(heatmapLayerConfiguration);
			}
		});

		// Transparency
		Label transparencyLabel = new Label(settingsContainer, SWT.NONE);
		transparencyLabel.setText("Opacity (0 = transparent): ");

		Spinner transparencyInput = new Spinner(settingsContainer, SWT.NONE);
		transparencyInput.setValues(newConfig.getAlpha(), 0, 255, 0, 1, 1);
		transparencyInput.addSelectionListener(new SpinnerListener(newConfig, transparencyInput, this) {
			@Override
			public void widgetSelected(SelectionEvent e) {
				HeatmapLayerConfiguration heatmapLayerConfig = (HeatmapLayerConfiguration) layerConfig;
				heatmapLayerConfig.setAlpha(spinner.getSelection());
				mapPropertiesDialog.setLayerConfiguration(heatmapLayerConfig);
			}
		});

		// Number of tiles
		Label numTilesWidthLabel = new Label(settingsContainer, SWT.NONE);
		numTilesWidthLabel.setText("Number of tiles horizontal: ");

		Spinner numTilesWidthInput = new Spinner(settingsContainer, SWT.NONE);
		numTilesWidthInput.setValues(newConfig.getNumTilesWidth(), 0, Integer.MAX_VALUE, 0, 1, 1);
		numTilesWidthInput.addSelectionListener(new SpinnerListener(newConfig, numTilesWidthInput, this) {
			@Override
			public void widgetSelected(SelectionEvent e) {
				HeatmapLayerConfiguration heatmapLayerConfig = (HeatmapLayerConfiguration) layerConfig;
				heatmapLayerConfig.setNumTilesWidth(spinner.getSelection());
				mapPropertiesDialog.setLayerConfiguration(heatmapLayerConfig);
			}
		});

		Label numTilesHeightLabel = new Label(settingsContainer, SWT.NONE);
		numTilesHeightLabel.setText("Number of tiles vertical: ");

		Spinner numTilesHeightInput = new Spinner(settingsContainer, SWT.NONE);
		numTilesHeightInput.setValues(newConfig.getNumTilesHeight(), 0, Integer.MAX_VALUE, 0, 1, 1);
		numTilesHeightInput.addSelectionListener(new SpinnerListener(newConfig, numTilesHeightInput, this) {
			@Override
			public void widgetSelected(SelectionEvent e) {
				HeatmapLayerConfiguration heatmapLayerConfig = (HeatmapLayerConfiguration) layerConfig;
				heatmapLayerConfig.setNumTilesHeight(spinner.getSelection());
				mapPropertiesDialog.setLayerConfiguration(heatmapLayerConfig);
			}
		});

		// Interpolation
		Label interpolationLabel = new Label(settingsContainer, SWT.NONE);
		interpolationLabel.setText("Interpolation: ");
		Button interpolationButton = new Button(settingsContainer, SWT.CHECK);
		interpolationButton.setEnabled(true);
		interpolationButton.setSelection(newConfig.isInterpolation());
		interpolationButton.addSelectionListener(new ButtonListener(newConfig, this, interpolationButton) {

			@Override
			public void widgetSelected(SelectionEvent e) {
				HeatmapLayerConfiguration heatmapLayerConfiguration = (HeatmapLayerConfiguration) layerConfiguration;
				heatmapLayerConfiguration.setInterpolation(correspondingButton.getSelection());
				mapPropertiesDialog.setLayerConfiguration(heatmapLayerConfiguration);
			}
		});

		// Hide tiles without information
		Label hideLabel = new Label(settingsContainer, SWT.NONE);
		hideLabel.setText("Hide tiles without information: ");
		Button hideButton = new Button(settingsContainer, SWT.CHECK);
		hideButton.setEnabled(true);
		hideButton.setSelection(newConfig.isHideWithoutInformation());
		hideButton.addSelectionListener(new ButtonListener(newConfig, this, hideButton) {

			@Override
			public void widgetSelected(SelectionEvent e) {
				HeatmapLayerConfiguration heatmapLayerConfiguration = (HeatmapLayerConfiguration) layerConfiguration;
				heatmapLayerConfiguration.setHideWithoutInformation((correspondingButton.getSelection()));
				mapPropertiesDialog.setLayerConfiguration(heatmapLayerConfiguration);
			}
		});

		// Position of layer
		Label autoPositionLabel = new Label(settingsContainer, SWT.NONE);
		autoPositionLabel.setText("Automatic positioning: ");
		Button autoPositionButton = new Button(settingsContainer, SWT.CHECK);
		autoPositionButton.setEnabled(true);
		autoPositionButton.setSelection(newConfig.isAutoPosition());

		// SouthWest
		// Latitude
		Label manPosSWLatLabel = new Label(settingsContainer, SWT.NONE);
		manPosSWLatLabel.setText("Southwest Latitude: ");

		final Spinner manPosSWLatInput = new Spinner(settingsContainer, SWT.NONE);
		manPosSWLatInput.setValues((int) newConfig.getLatSW() * 1000, -90000, 90000, 3, 1000, 1000);
		manPosSWLatInput.setEnabled(!autoPositionButton.getSelection());
		manPosSWLatInput.addSelectionListener(new SpinnerListener(newConfig, manPosSWLatInput, this) {
			@Override
			public void widgetSelected(SelectionEvent e) {
				double value = spinner.getSelection() / (Math.pow(10, spinner.getDigits()));
				HeatmapLayerConfiguration heatmapLayerConfig = (HeatmapLayerConfiguration) layerConfig;
				heatmapLayerConfig.setLatSW(value);
				mapPropertiesDialog.setLayerConfiguration(heatmapLayerConfig);
			}
		});

		// Longitude
		Label manPosSWLngLabel = new Label(settingsContainer, SWT.NONE);
		manPosSWLngLabel.setText("Southwest Longitude: ");

		final Spinner manPosSWLngInput = new Spinner(settingsContainer, SWT.NONE);
		manPosSWLngInput.setValues((int) newConfig.getLngSW() * 1000, -180000, 180000, 3, 1000, 1000);
		manPosSWLngInput.setEnabled(!autoPositionButton.getSelection());
		manPosSWLngInput.addSelectionListener(new SpinnerListener(newConfig, manPosSWLngInput, this) {
			@Override
			public void widgetSelected(SelectionEvent e) {
				double value = spinner.getSelection() / (Math.pow(10, spinner.getDigits()));
				HeatmapLayerConfiguration heatmapLayerConfig = (HeatmapLayerConfiguration) layerConfig;
				heatmapLayerConfig.setLngSW(value);
				mapPropertiesDialog.setLayerConfiguration(heatmapLayerConfig);
			}
		});

		// NorthEast
		// Latitude
		Label manPosNELatLabel = new Label(settingsContainer, SWT.NONE);
		manPosNELatLabel.setText("Northeast Latitude: ");

		final Spinner manPosNELatInput = new Spinner(settingsContainer, SWT.NONE);
		manPosNELatInput.setValues((int) newConfig.getLatNE() * 1000, -90000, 90000, 3, 1000, 1000);
		manPosNELatInput.setEnabled(!autoPositionButton.getSelection());
		manPosNELatInput.addSelectionListener(new SpinnerListener(newConfig, manPosNELatInput, this) {
			@Override
			public void widgetSelected(SelectionEvent e) {
				double value = spinner.getSelection() / (Math.pow(10, spinner.getDigits()));
				HeatmapLayerConfiguration heatmapLayerConfig = (HeatmapLayerConfiguration) layerConfig;
				heatmapLayerConfig.setLatNE(value);
				mapPropertiesDialog.setLayerConfiguration(heatmapLayerConfig);
			}
		});

		// Longitude
		Label manPosNELngLabel = new Label(settingsContainer, SWT.NONE);
		manPosNELngLabel.setText("Northeast Longitude: ");

		final Spinner manPosNELngInput = new Spinner(settingsContainer, SWT.NONE);
		manPosNELngInput.setValues((int) newConfig.getLngNE() * 1000, -180000, 180000, 3, 1000, 1000);
		manPosNELngInput.setEnabled(!autoPositionButton.getSelection());
		manPosNELngInput.addSelectionListener(new SpinnerListener(newConfig, manPosNELngInput, this) {
			@Override
			public void widgetSelected(SelectionEvent e) {
				double value = spinner.getSelection() / (Math.pow(10, spinner.getDigits()));
				HeatmapLayerConfiguration heatmapLayerConfig = (HeatmapLayerConfiguration) layerConfig;
				heatmapLayerConfig.setLngNE(value);
				mapPropertiesDialog.setLayerConfiguration(heatmapLayerConfig);
			}
		});

		autoPositionButton.addSelectionListener(new ButtonListener(newConfig, this, autoPositionButton) {

			@Override
			public void widgetSelected(SelectionEvent e) {
				HeatmapLayerConfiguration heatmapLayerConfiguration = (HeatmapLayerConfiguration) layerConfiguration;
				heatmapLayerConfiguration.setAutoPosition(correspondingButton.getSelection());
				manPosNELatInput.setEnabled(!correspondingButton.getSelection());
				manPosNELngInput.setEnabled(!correspondingButton.getSelection());
				manPosSWLatInput.setEnabled(!correspondingButton.getSelection());
				manPosSWLngInput.setEnabled(!correspondingButton.getSelection());
				mapPropertiesDialog.setLayerConfiguration(heatmapLayerConfiguration);
			}
		});

//		// Show the statistics for this layer
//		Group statisticsContainer = new Group(heatmapContainer, SWT.NONE);
//		statisticsContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
//		statisticsContainer.setText("Heatmap statistics");
//		statisticsContainer.setLayout(new GridLayout(1, false));
//
//		// Maximal value
//		final Label maxValueLabel = new Label(statisticsContainer, SWT.NONE);
//		maxValueLabel.setText("Maximal value of a tile: " + String.valueOf(heatmap.getMaxValue()));
//
//		// Minimal value
//		final Label minValueLabel = new Label(statisticsContainer, SWT.NONE);
//		minValueLabel.setText("Minimal value of a tile: " + String.valueOf(heatmap.getMinValue()));
//
//		// Sum value
//		final Label sumValueLabel = new Label(statisticsContainer, SWT.NONE);
//		sumValueLabel.setText("Sum of all values: " + String.valueOf(heatmap.getTotalValue()));
//
//		// Number of elements
//		final Label numElementsLabel = new Label(statisticsContainer, SWT.NONE);
//		numElementsLabel.setText("Number of elements: " + String.valueOf(heatmap.getTotalNumberOfElements()));
//
//		// Average value
//		final Label avgValueLabel = new Label(statisticsContainer, SWT.NONE);
//		avgValueLabel.setText("Average value of an element: " + String.valueOf(heatmap.getAverageValueOfElement()));
//
//		// Number of Tiles
//		final Label numTilesLabel = new Label(statisticsContainer, SWT.NONE);
//		numTilesLabel.setText("Number of tiles: " + String.valueOf(heatmap.getNumberOfTiles()));
//
//		// Average sum of tile
//		final Label avgTileValueLabel = new Label(statisticsContainer, SWT.NONE);
//		avgTileValueLabel.setText("Average sum of a tile: " + String.valueOf(heatmap.getAverageValueOfTile()));
//
//		// Sums of all tiles
//		final Label sumTilesValuesLabel = new Label(statisticsContainer, SWT.NONE);
//		sumTilesValuesLabel.setText("Values of single tiles:");
//
//		final Table valueTable = new Table(statisticsContainer, SWT.BORDER);
//		updateTableValues(valueTable, heatmap);
//
//		valueTable.setHeaderVisible(true);
//
//		// Update-Button
//		Button updateButton = new Button(statisticsContainer, SWT.PUSH);
//		updateButton.setText("Update statistics");
//
//		updateButton.addSelectionListener(new UpdateButtonListener(heatmap) {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				Heatmap heatmap = (Heatmap) layer;
//				maxValueLabel.setText("Maximal value of a tile: " + String.valueOf(heatmap.getMaxValue()));
//
//				minValueLabel.setText("Minimal value of a tile: " + String.valueOf(heatmap.getMinValue()));
//
//				sumValueLabel.setText("Sum of all values: " + String.valueOf(heatmap.getTotalValue()));
//
//				numElementsLabel.setText("Number of elements: " + String.valueOf(heatmap.getTotalNumberOfElements()));
//
//				avgValueLabel
//						.setText("Average value of an element: " + String.valueOf(heatmap.getAverageValueOfElement()));
//
//				numTilesLabel.setText("Number of tiles: " + String.valueOf(heatmap.getNumberOfTiles()));
//
//				avgTileValueLabel.setText("Average sum of a tile: " + String.valueOf(heatmap.getAverageValueOfTile()));
//
//				updateTableValues(valueTable, heatmap);
//			}
//		});

		scrollContainer.setContent(heatmapContainer);
		heatmapContainer.setSize(heatmapContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		// Redraw the container
		container.getParent().layout();
	}

	/**
	 * Removes everything from the container except the Tree (on the left)
	 * 
	 * @param container
	 */
	public void removeContent(Composite container) {
		// Remove old content
		for (Control child : container.getChildren()) {
			// But leave the TreeView there
			if (!child.getClass().equals(Tree.class))
				child.dispose();
		}
	}

	/**
	 * Updates the value-sum-table: Fills it with new values
	 * 
	 * @param t
	 * @param values
	 */
	private void updateTableValues(Table t, Heatmap heatmap) {

		// Remove old content, user should not see what we're doing
		t.setRedraw(false);

		t.removeAll();

		// Remove the old columns
		while (t.getColumnCount() > 0) {
			t.getColumns()[0].dispose();
		}

		double[][] values = heatmap.getVauesForTiles();

		// Build the columns
		for (int i = 0; i < values.length; i++) {
			TableColumn tc = new TableColumn(t, SWT.CENTER);
			tc.setText(Integer.toString(i));
			tc.setWidth(40);
		}

		for (int i = 0; i < values[0].length; i++) {
			String[] columnContents = new String[values.length];
			for (int j = 0; j < values.length; j++) {
				columnContents[j] = Double.toString(values[j][i]);
			}
			TableItem item = new TableItem(t, SWT.NONE);
			item.setText(columnContents);
		}
		t.setRedraw(true);
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createOkButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	protected Button createOkButton(Composite parent, int id, String label, boolean defaultButton) {
		((GridLayout) parent.getLayout()).numColumns++;
		Button button = new Button(parent, SWT.PUSH);
		button.setText(label);
		button.setFont(JFaceResources.getDialogFont());
		button.setData(new Integer(id));
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				if (layerConfiguration != null && layerConfiguration instanceof HeatmapLayerConfiguration)
					heatmap.setConfiguration((HeatmapLayerConfiguration) layerConfiguration);
				okPressed();
			}
		});
		if (defaultButton) {
			Shell shell = parent.getShell();
			if (shell != null) {
				shell.setDefaultButton(button);
			}
		}
		setButtonLayoutData(button);
		return button;
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(748, 522);
	}

	public LayerConfiguration getLayerConfiguration() {
		return layerConfiguration;
	}

	public void setLayerConfiguration(LayerConfiguration layerConfiguration) {
		this.layerConfiguration = layerConfiguration;
	}

}
