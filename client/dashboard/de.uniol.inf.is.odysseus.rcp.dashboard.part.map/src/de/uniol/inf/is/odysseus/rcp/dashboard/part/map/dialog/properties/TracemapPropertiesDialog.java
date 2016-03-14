package de.uniol.inf.is.odysseus.rcp.dashboard.part.map.dialog.properties;

import java.util.Collection;
import java.util.HashMap;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.dialog.AttributeListener;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.dialog.DialogUtils;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.TracemapLayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.thematic.tracemap.TraceLayer;

/**
 * Dialog to change properties of a tracemap
 * 
 * @author Tobias Brandt
 *
 */
public class TracemapPropertiesDialog extends AbstractMapPropertiesDialog {

	private TraceLayer traceLayer;
	private Collection<IPhysicalOperator> operators;

	public TracemapPropertiesDialog(Shell parentShell, TraceLayer traceLayer, Collection<IPhysicalOperator> operators) {
		super(parentShell);
		this.traceLayer = traceLayer;
		this.operators = operators;
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {

		setTitle("Tracemap Properties");
		setMessage("Edit map parameters.", IMessageProvider.INFORMATION);
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		GridData gd_container = new GridData(GridData.FILL, SWT.FILL, true, true);
		gd_container.heightHint = 317;
		container.setLayoutData(gd_container);

		// Fill the area with tracemap-content
		createTraceLayerMenu(this.traceLayer, container);

		return area;
	}

	/**
	 * Creates the menu for tracemaps with all the settings that can be made
	 * 
	 * @param tracemap
	 * @param container
	 */
	public void createTraceLayerMenu(TraceLayer tracemap, Composite container) {
		// Remove everything except the Tree on the left
		// removeContent(container);
		TracemapLayerConfiguration newConfig = new TracemapLayerConfiguration(tracemap.getConfig());

		// New container for scrolling
		ScrolledComposite scrollContainer = new ScrolledComposite(container, SWT.V_SCROLL | SWT.BORDER);
		scrollContainer.setLayout(new GridLayout(1, false));
		scrollContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Composite tracemapContainer = new Composite(scrollContainer, SWT.NONE);
		tracemapContainer.setLayout(new GridLayout(1, false));
		tracemapContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		// Show the settings for the tracemap
		Group settingsContainer = new Group(tracemapContainer, SWT.NONE);
		settingsContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		settingsContainer.setText("Tracemap settings");
		settingsContainer.setLayout(new GridLayout(2, false));

		// Position of geometry-Attribute
		Label geoAttrLabel = new Label(settingsContainer, SWT.NONE);
		geoAttrLabel.setText("Geometry Attribute: ");

		CCombo geoAttrInput = new CCombo(settingsContainer, SWT.BORDER);
		geoAttrInput.setLayoutData(DialogUtils.getTextDataLayout());

		// TODO Choose the attribute before using it
		geoAttrInput.removeAll();
		SDFSchema schema = operators.iterator().next().getOutputSchema();
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

		geoAttrInput
				.addSelectionListener(new AttributeListener(newConfig, geoAttrInput, null, null, valueAttrInput, this));
		valueAttrInput
				.addSelectionListener(new AttributeListener(newConfig, geoAttrInput, null, null, valueAttrInput, this));

		// Mark end of line
		Label markEndIfLineLabel = new Label(settingsContainer, SWT.NONE);
		markEndIfLineLabel.setText("Mark end of traces: ");

		Button markEndIfLineButton = new Button(settingsContainer, SWT.CHECK);
		markEndIfLineButton.setEnabled(true);
		markEndIfLineButton.setSelection(newConfig.isMarkEndpoint());
		markEndIfLineButton.addSelectionListener(new ButtonListener(newConfig, this, markEndIfLineButton) {

			@Override
			public void widgetSelected(SelectionEvent e) {
				TracemapLayerConfiguration tracemapLayerConfiguration = (TracemapLayerConfiguration) layerConfiguration;
				tracemapLayerConfiguration.setMarkEndpoint((correspondingButton.getSelection()));
				propertiesDialog.setLayerConfiguration(tracemapLayerConfiguration);
			}
		});

		// Width of lines
		Label lineWidthLabel = new Label(settingsContainer, SWT.NONE);
		lineWidthLabel.setText("Width of traces: ");

		Spinner lineWidthInput = new Spinner(settingsContainer, SWT.NONE);
		lineWidthInput.setValues(newConfig.getLineWidth(), 0, 255, 0, 1, 1);
		lineWidthInput.addSelectionListener(new SpinnerListener(newConfig, lineWidthInput, this) {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TracemapLayerConfiguration tracemapLayerConfig = (TracemapLayerConfiguration) layerConfig;
				tracemapLayerConfig.setLineWidth((spinner.getSelection()));
				propertiesDialog.setLayerConfiguration(tracemapLayerConfig);
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
		numElements.setText("Number of elements to show (if no auto-transparency): ");

		final Spinner numElementsInput = new Spinner(settingsContainer, SWT.NONE);
		numElementsInput.setValues(newConfig.getNumOfLineElements(), 1, Integer.MAX_VALUE, 0, 1, 1);
		numElementsInput.setEnabled(!autoTransparencyButton.getSelection());
		numElementsInput.addSelectionListener(new SpinnerListener(newConfig, numElementsInput, this) {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TracemapLayerConfiguration tracemapLayerConfig = (TracemapLayerConfiguration) layerConfig;
				tracemapLayerConfig.setNumOfLineElements(spinner.getSelection());
				propertiesDialog.setLayerConfiguration(tracemapLayerConfig);
			}
		});

		// Listener for automatic transparency
		autoTransparencyButton.addSelectionListener(new ButtonListener(newConfig, this, autoTransparencyButton) {

			@Override
			public void widgetSelected(SelectionEvent e) {
				TracemapLayerConfiguration tracemapLayerConfiguration = (TracemapLayerConfiguration) layerConfiguration;
				tracemapLayerConfiguration.setAutoTransparency(correspondingButton.getSelection());
				numElementsInput.setEnabled(!correspondingButton.getSelection());
				propertiesDialog.setLayerConfiguration(tracemapLayerConfiguration);
			}
		});

		// Color settings
		// Show the chooser and a label with the selected color next to
		// it
		for (Integer key : newConfig.getColors().keySet()) {
			Label colorLabel = new Label(settingsContainer, SWT.NONE);
			colorLabel.setText("Choose color for id: " + key);

			Composite colorContainer = new Composite(settingsContainer, SWT.NONE);
			colorContainer.setLayout(new GridLayout(2, false));
			colorContainer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 0));

			Button colorButton = new Button(colorContainer, SWT.PUSH);
			colorButton.setText("Change color ...");

			Label colorView = new Label(colorContainer, SWT.NONE);
			colorView.setText("      ");
			colorView.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
			colorView.setBackground(newConfig.getColorForId(key));

			colorButton.addSelectionListener(new TraceColorListener(newConfig, key, this, colorView));
		}

		// Show the statistics for the tracemap
		final Group statisticsContainer = new Group(tracemapContainer, SWT.NONE);
		statisticsContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
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
			lenTraceLabel.setText("Length of trace " + key + ": " + String.valueOf(distances.get(key)) + " km ("
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
				HashMap<Integer, Double> distances = tracemap.getAllLineDistances();
				HashMap<Integer, Double> speeds = tracemap.getAllLineSpeeds();

				for (Integer key : distances.keySet()) {
					Label lenTraceLabel = new Label(statisticsContainer, SWT.NONE);
					lenTraceLabel.setText("Length of trace " + key + ": " + String.valueOf(distances.get(key)) + " km ("
							+ speeds.get(key) + " km/h)");
					statisticsContainer.layout();
				}
			}
		});

		scrollContainer.setContent(tracemapContainer);
		tracemapContainer.setSize(tracemapContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		// Redraw the container
		container.layout();
	}

}
