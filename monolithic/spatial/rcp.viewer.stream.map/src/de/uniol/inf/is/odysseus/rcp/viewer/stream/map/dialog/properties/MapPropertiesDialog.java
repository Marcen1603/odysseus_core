package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.dialog.properties;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Scanner;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
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
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.StreamMapEditorPart;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.dialog.AttributeListener;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.dialog.DialogUtils;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.ILayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.MapEditorModel;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.HeatmapLayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.LayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.TracemapLayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.Style;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.heatmap.Heatmap;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.tracemap.TraceLayer;

/**
 * 
 * Dialog to edit the properties of a map
 * 
 * @author Stefan Bothe
 * 
 */
public class MapPropertiesDialog extends TitleAreaDialog {

	private static final Logger LOG = LoggerFactory
			.getLogger(TitleAreaDialog.class);

	private TreeViewer treeViewer;
	private PropertiesLabelProvider lblProvider = new PropertiesLabelProvider();
	HashMap<Integer, Style> hashStyles;

	private MapEditorModel map;
	@SuppressWarnings("unused")
	private StreamMapEditorPart editor;

	private LayerConfiguration layerConfiguration = null;
	private Text txtSridInput;
	private Text txtQueriesInput;

	public LayerConfiguration getLayerConfiguration() {
		return layerConfiguration;
	}

	public void setLayerConfiguration(LayerConfiguration layerConfiguration) {
		this.layerConfiguration = layerConfiguration;
	}

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 * @param editor
	 */
	public MapPropertiesDialog(Shell parentShell,MapEditorModel map, StreamMapEditorPart editor) {
		super(parentShell);
		this.setShellStyle(SWT.MAX | SWT.RESIZE);
		this.map = map;
		this.editor = editor;
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {

		setTitle("Map Properties: " + map.getFile().getName());
		setMessage("Edit map parameters.", IMessageProvider.INFORMATION);
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		GridData gd_container = new GridData(GridData.FILL, SWT.FILL, true,
				true);
		gd_container.heightHint = 317;
		container.setLayoutData(gd_container);

		treeViewer = new TreeViewer(container, SWT.SINGLE | SWT.H_SCROLL
				| SWT.V_SCROLL);
		treeViewer.setContentProvider(new PropertiesContentProvider());
		treeViewer.setLabelProvider(lblProvider);
		treeViewer.setInput(new PropertiesModel(map.getLayers().toArray(), map
				.getFile()));
		treeViewer.refresh();
		treeViewer.expandAll();

		treeViewer.getTree().setSelection(treeViewer.getTree().getItem(0));
		treeViewer.setSelection(treeViewer.getSelection(), true);

		// Add a listener -> shows right settings for the layers
		treeViewer
				.addSelectionChangedListener(new TreeListener(container, this));

		Tree tree = treeViewer.getTree();
		tree.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));

		// Fill the right area with standard-content
		createStandardContent(container);

		return area;
	}

	/**
	 * Fills the container with standardcontent (srid and query) for
	 * non-thematic maps
	 * 
	 * @param container
	 */
	public void createStandardContent(Composite container) {
		Group grpMap = new Group(container, SWT.NONE);
		grpMap.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpMap.setText("Map");
		grpMap.setLayout(new GridLayout(2, false));

		Label lblSrid = new Label(grpMap, SWT.NONE);
		lblSrid.setText("Srid:");

		txtSridInput = new Text(grpMap, SWT.BORDER);
		txtSridInput.setText(Integer.toString(map.getSRID()));

		Label lblQueries = new Label(grpMap, SWT.NONE);
		lblQueries.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 2, 1));

		new Label(grpMap, SWT.NONE);

		txtQueriesInput = new Text(grpMap, SWT.MULTI | SWT.BORDER | SWT.WRAP
				| SWT.V_SCROLL);
		txtQueriesInput.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));

		if (!map.getQryFileList().isEmpty()) {
			lblQueries.setText("Query: "
					+ map.getQryFileList().getLast().getName());
			try {
				txtQueriesInput.setText(convertStreamToString(map
						.getQryFileList().getLast().getContents()));
			} catch (CoreException e) {
				e.printStackTrace();
			}
		} else {
			// Maybe the map has no queries
			lblQueries.setText("There are no queries in this map.");
		}
		container.layout();
	}

	/**
	 * Creates the menu for tracemaps with all the settings that can be made
	 * 
	 * @param tracemap
	 * @param container
	 */
	public void createTraceLayerMenu(TraceLayer tracemap, Composite container) {
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
		geoAttrLabel.setText("Geometry Attribute: ");

		CCombo geoAttrInput = new CCombo(settingsContainer, SWT.BORDER);
		geoAttrInput.setLayoutData(DialogUtils.getTextDataLayout());

		// Fill this combobox
		geoAttrInput.removeAll();
		SDFSchema schema = tracemap.getLayerUpdater().getConnection().getOutputSchema();

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

		geoAttrInput.addSelectionListener(new AttributeListener(newConfig,
				geoAttrInput, valueAttrInput, this));
		valueAttrInput.addSelectionListener(new AttributeListener(newConfig,
				geoAttrInput, valueAttrInput, this));

		// Mark end of line
		Label markEndIfLineLabel = new Label(settingsContainer, SWT.NONE);
		markEndIfLineLabel.setText("Mark end of traces: ");

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
				mapPropertiesDialog
						.setLayerConfiguration(tracemapLayerConfiguration);
			}
		});

		// Width of lines
		Label lineWidthLabel = new Label(settingsContainer, SWT.NONE);
		lineWidthLabel.setText("Width of traces: ");

		Spinner lineWidthInput = new Spinner(settingsContainer, SWT.NONE);
		lineWidthInput.setValues(newConfig.getLineWidth(), 0, 255, 0, 1, 1);
		lineWidthInput.addSelectionListener(new SpinnerListener(newConfig,
				lineWidthInput, this) {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TracemapLayerConfiguration tracemapLayerConfig = (TracemapLayerConfiguration) layerConfig;
				tracemapLayerConfig.setLineWidth((spinner.getSelection()));
				mapPropertiesDialog.setLayerConfiguration(tracemapLayerConfig);
			}
		});

		// Automatic transparency
		Label autoTransparencyLabel = new Label(settingsContainer, SWT.NONE);
		autoTransparencyLabel.setText("Automatic transparency: ");
		Button autoTransparencyButton = new Button(settingsContainer, SWT.CHECK);
		autoTransparencyButton.setEnabled(true);
		autoTransparencyButton.setSelection(newConfig.isAutoTransparency());

		// Number of elements to show
		Label numElements = new Label(settingsContainer, SWT.NONE);
		numElements
				.setText("Number of elements to show (if no auto-transparency): ");

		final Spinner numElementsInput = new Spinner(settingsContainer,
				SWT.NONE);
		numElementsInput.setValues(newConfig.getNumOfLineElements(), 1,
				Integer.MAX_VALUE, 0, 1, 1);
		numElementsInput.setEnabled(!autoTransparencyButton.getSelection());
		numElementsInput.addSelectionListener(new SpinnerListener(newConfig,
				numElementsInput, this) {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TracemapLayerConfiguration tracemapLayerConfig = (TracemapLayerConfiguration) layerConfig;
				tracemapLayerConfig.setNumOfLineElements(spinner.getSelection());
				mapPropertiesDialog.setLayerConfiguration(tracemapLayerConfig);
			}
		});

		// Listener for automatic transparency
		autoTransparencyButton.addSelectionListener(new ButtonListener(
				newConfig, this, autoTransparencyButton) {

			@Override
			public void widgetSelected(SelectionEvent e) {
				TracemapLayerConfiguration tracemapLayerConfiguration = (TracemapLayerConfiguration) layerConfiguration;
				tracemapLayerConfiguration
						.setAutoTransparency(correspondingButton.getSelection());
				numElementsInput.setEnabled(!correspondingButton.getSelection());
				mapPropertiesDialog
						.setLayerConfiguration(tracemapLayerConfiguration);
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

	/**
	 * Creates the menu for heatmaps with all the settings that can be made
	 * 
	 * @param heatmap
	 * @param container
	 */
	public void createHeatmapMenu(Heatmap heatmap, Composite container) {
		// Create a new configuration, that is used, if the user clicks
		// ok
		HeatmapLayerConfiguration newConfig = new HeatmapLayerConfiguration(
				heatmap.getConfig());

		// Remove everything except the Tree on the left
		removeContent(container);

		// New container for scrolling
		ScrolledComposite scrollContainer = new ScrolledComposite(container,
				SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
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
		settingsContainer.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true,
				true, 1, 1));
		settingsContainer.setText("Heatmap settings");
		settingsContainer.setLayout(new GridLayout(2, false));

		// Position of geometry-Attribute
		Label geoAttrLabel = new Label(settingsContainer, SWT.NONE);
		geoAttrLabel.setText("Geometry Attribute: ");

		CCombo geoAttrInput = new CCombo(settingsContainer, SWT.BORDER);
		geoAttrInput.setLayoutData(DialogUtils.getTextDataLayout());

		// Fill this combobox
		geoAttrInput.removeAll();
		SDFSchema schema = heatmap.getLayerUpdater().getConnection().getOutputSchema();

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

		geoAttrInput.addSelectionListener(new AttributeListener(newConfig,
				geoAttrInput, valueAttrInput, this));
		valueAttrInput.addSelectionListener(new AttributeListener(newConfig,
				geoAttrInput, valueAttrInput, this));

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
				mapPropertiesDialog
						.setLayerConfiguration(heatmapLayerConfiguration);
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
				mapPropertiesDialog
						.setLayerConfiguration(heatmapLayerConfiguration);
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
				mapPropertiesDialog.setLayerConfiguration(heatmapLayerConfig);
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
				mapPropertiesDialog.setLayerConfiguration(heatmapLayerConfig);
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
				mapPropertiesDialog.setLayerConfiguration(heatmapLayerConfig);
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
				mapPropertiesDialog
						.setLayerConfiguration(heatmapLayerConfiguration);
			}
		});

		// Hide tiles without information
		Label hideLabel = new Label(settingsContainer, SWT.NONE);
		hideLabel.setText("Hide tiles without information: ");
		Button hideButton = new Button(settingsContainer, SWT.CHECK);
		hideButton.setEnabled(true);
		hideButton.setSelection(newConfig.isHideWithoutInformation());
		hideButton.addSelectionListener(new ButtonListener(newConfig, this,
				hideButton) {

			@Override
			public void widgetSelected(SelectionEvent e) {
				HeatmapLayerConfiguration heatmapLayerConfiguration = (HeatmapLayerConfiguration) layerConfiguration;
				heatmapLayerConfiguration
						.setHideWithoutInformation((correspondingButton
								.getSelection()));
				mapPropertiesDialog
						.setLayerConfiguration(heatmapLayerConfiguration);
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

		final Spinner manPosSWLatInput = new Spinner(settingsContainer,
				SWT.NONE);
		manPosSWLatInput.setValues((int) newConfig.getLatSW() * 1000, -90000,
				90000, 3, 1000, 1000);
		manPosSWLatInput.setEnabled(!autoPositionButton.getSelection());
		manPosSWLatInput.addSelectionListener(new SpinnerListener(newConfig,
				manPosSWLatInput, this) {
			@Override
			public void widgetSelected(SelectionEvent e) {
				double value = spinner.getSelection()
						/ (Math.pow(10, spinner.getDigits()));
				HeatmapLayerConfiguration heatmapLayerConfig = (HeatmapLayerConfiguration) layerConfig;
				heatmapLayerConfig.setLatSW(value);
				mapPropertiesDialog.setLayerConfiguration(heatmapLayerConfig);
			}
		});

		// Longitude
		Label manPosSWLngLabel = new Label(settingsContainer, SWT.NONE);
		manPosSWLngLabel.setText("Southwest Longitude: ");

		final Spinner manPosSWLngInput = new Spinner(settingsContainer,
				SWT.NONE);
		manPosSWLngInput.setValues((int) newConfig.getLngSW() * 1000, -180000,
				180000, 3, 1000, 1000);
		manPosSWLngInput.setEnabled(!autoPositionButton.getSelection());
		manPosSWLngInput.addSelectionListener(new SpinnerListener(newConfig,
				manPosSWLngInput, this) {
			@Override
			public void widgetSelected(SelectionEvent e) {
				double value = spinner.getSelection()
						/ (Math.pow(10, spinner.getDigits()));
				HeatmapLayerConfiguration heatmapLayerConfig = (HeatmapLayerConfiguration) layerConfig;
				heatmapLayerConfig.setLngSW(value);
				mapPropertiesDialog.setLayerConfiguration(heatmapLayerConfig);
			}
		});

		// NorthEast
		// Latitude
		Label manPosNELatLabel = new Label(settingsContainer, SWT.NONE);
		manPosNELatLabel.setText("Northeast Latitude: ");

		final Spinner manPosNELatInput = new Spinner(settingsContainer,
				SWT.NONE);
		manPosNELatInput.setValues((int) newConfig.getLatNE() * 1000, -90000,
				90000, 3, 1000, 1000);
		manPosNELatInput.setEnabled(!autoPositionButton.getSelection());
		manPosNELatInput.addSelectionListener(new SpinnerListener(newConfig,
				manPosNELatInput, this) {
			@Override
			public void widgetSelected(SelectionEvent e) {
				double value = spinner.getSelection()
						/ (Math.pow(10, spinner.getDigits()));
				HeatmapLayerConfiguration heatmapLayerConfig = (HeatmapLayerConfiguration) layerConfig;
				heatmapLayerConfig.setLatNE(value);
				mapPropertiesDialog.setLayerConfiguration(heatmapLayerConfig);
			}
		});

		// Longitude
		Label manPosNELngLabel = new Label(settingsContainer, SWT.NONE);
		manPosNELngLabel.setText("Northeast Longitude: ");

		final Spinner manPosNELngInput = new Spinner(settingsContainer,
				SWT.NONE);
		manPosNELngInput.setValues((int) newConfig.getLngNE() * 1000, -180000,
				180000, 3, 1000, 1000);
		manPosNELngInput.setEnabled(!autoPositionButton.getSelection());
		manPosNELngInput.addSelectionListener(new SpinnerListener(newConfig,
				manPosNELngInput, this) {
			@Override
			public void widgetSelected(SelectionEvent e) {
				double value = spinner.getSelection()
						/ (Math.pow(10, spinner.getDigits()));
				HeatmapLayerConfiguration heatmapLayerConfig = (HeatmapLayerConfiguration) layerConfig;
				heatmapLayerConfig.setLngNE(value);
				mapPropertiesDialog.setLayerConfiguration(heatmapLayerConfig);
			}
		});

		autoPositionButton.addSelectionListener(new ButtonListener(newConfig,
				this, autoPositionButton) {

			@Override
			public void widgetSelected(SelectionEvent e) {
				HeatmapLayerConfiguration heatmapLayerConfiguration = (HeatmapLayerConfiguration) layerConfiguration;
				heatmapLayerConfiguration.setAutoPosition(correspondingButton
						.getSelection());
				manPosNELatInput.setEnabled(!correspondingButton.getSelection());
				manPosNELngInput.setEnabled(!correspondingButton.getSelection());
				manPosSWLatInput.setEnabled(!correspondingButton.getSelection());
				manPosSWLngInput.setEnabled(!correspondingButton.getSelection());
				mapPropertiesDialog
						.setLayerConfiguration(heatmapLayerConfiguration);
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

		// Sums of all tiles
		final Label sumTilesValuesLabel = new Label(statisticsContainer,
				SWT.NONE);
		sumTilesValuesLabel.setText("Values of single tiles:");

		final Table valueTable = new Table(statisticsContainer, SWT.BORDER);
		updateTableValues(valueTable, heatmap);

		valueTable.setHeaderVisible(true);

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

				updateTableValues(valueTable, heatmap);
			}
		});

		scrollContainer.setContent(heatmapContainer);
		heatmapContainer.setSize(heatmapContainer.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));

		// Redraw the container
		container.layout();
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

	@SuppressWarnings("resource")
	public static String convertStreamToString(InputStream is) {
		Scanner s = new Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createOkButton(parent, IDialogConstants.OK_ID,
				IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	protected Button createOkButton(Composite parent, int id, String label,
			boolean defaultButton) {
		((GridLayout) parent.getLayout()).numColumns++;
		Button button = new Button(parent, SWT.PUSH);
		button.setText(label);
		button.setFont(JFaceResources.getDialogFont());
		button.setData(new Integer(id));
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				// Check for valid input, if standard-properties are shown
				if (!txtSridInput.isDisposed() && isValidInput()) {
					// Non-thematic
					try {
						if (!map.getQryFileList().isEmpty()) {
							map.getQryFileList()
									.getLast()
									.setContents(
											new ByteArrayInputStream(
													txtQueriesInput.getText()
															.getBytes("UTF-8")),
											true, true, null);
						}
						map.setSrid(Integer.parseInt(txtSridInput.getText()));
					} catch (UnsupportedEncodingException | CoreException e) {
						LOG.debug("Changing Map Porperties failed");
						e.printStackTrace();
					}
				} else {

					IStructuredSelection selection = (IStructuredSelection) treeViewer
							.getSelection();
					if (selection.getFirstElement() instanceof Heatmap) {
						// OK-Options for the heatmap menu
						Heatmap heatmap = (Heatmap) selection.getFirstElement();
						// Should have been set in the TreeListener
						if (layerConfiguration != null
								&& layerConfiguration instanceof HeatmapLayerConfiguration)
							heatmap.setConfiguration((HeatmapLayerConfiguration) layerConfiguration);

					} else if (selection.getFirstElement() instanceof TraceLayer) {
						// OK-Options for the linemap menu
						TraceLayer tracemap = (TraceLayer) selection
								.getFirstElement();
						if (layerConfiguration != null
								&& layerConfiguration instanceof TracemapLayerConfiguration)
							tracemap.setConfiguration((TracemapLayerConfiguration) layerConfiguration);

					}
				}
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

	private boolean isValidInput() {
		boolean valid = true;

		if (txtSridInput.getText().isEmpty()
				|| txtSridInput.getText().contains(" ")) {
			super.setErrorMessage("Please enter a valid number: No whitespaces");
			valid = false;
		}

		try {
			Integer.parseInt(txtSridInput.getText());
		} catch (NumberFormatException e) {
			super.setErrorMessage("Please enter only valid numbers");
			valid = false;
		}

		return valid;
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(748, 522);
	}

	/**
	 * Selects the right element in the tree
	 * 
	 * @param selectedLayer
	 *            Layer for which the config should be openend
	 */
	public void selectLayer(ILayer selectedLayer) {
		Object[] path = new Object[2];
		path[0] = treeViewer.getTree().getItems()[0];
		path[1] = selectedLayer;
		org.eclipse.jface.viewers.TreePath newPath = new org.eclipse.jface.viewers.TreePath(
				path);
		TreeSelection newSelection = new TreeSelection(newPath);
		treeViewer.setSelection(newSelection);
	}
}
