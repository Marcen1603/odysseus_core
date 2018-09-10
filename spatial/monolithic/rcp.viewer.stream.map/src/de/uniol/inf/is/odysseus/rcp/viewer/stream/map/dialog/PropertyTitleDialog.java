package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.dialog;

import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.AttributeResolver;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.LayerUpdater;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.activator.OdysseusMapPlugIn;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.ILayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.LayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.RasterLayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.VectorLayerConfiguration;

public class PropertyTitleDialog extends TitleAreaDialog {

	private static final Logger LOG = LoggerFactory
			.getLogger(TitleAreaDialog.class);

	private LinkedList<ILayer> layerOrder;
	private Collection<LayerUpdater> connections;

	private Composite configContainer;
	private Composite main;

	/*
	 * @TODO Save the layer reference...
	 */
	private String layerType;
	private Text layerName;
	private CCombo server = null;

	private LayerConfiguration layerConfiguration = null;

	public LayerConfiguration getLayerConfiguration() {
		return layerConfiguration;
	}

	public void setLayerConfiguration(LayerConfiguration layerConfiguration) {
		this.layerConfiguration = layerConfiguration;
	}

	public PropertyTitleDialog(Shell parentShell,
			LinkedList<ILayer> layerOrder, Collection<LayerUpdater> connections) {
		super(parentShell);
		this.layerOrder = layerOrder;
		this.connections = connections;
		this.layerType = "RasterLayer";
	}

	@Override
	public void create() {
		super.create();
		setTitle("Map Layer");
		setMessage("Create or edit a Map Layer.", IMessageProvider.INFORMATION);
	}

	private Composite getBasicConfiguration(final Composite parent) {
		Composite layerConfiguration = new Composite(parent, SWT.NONE);
		layerConfiguration.setLayout(DialogUtils.getGroupLayout());
		layerConfiguration.setLayoutData(new GridData(GridData.FILL,
				GridData.BEGINNING, false, false));

		Label layerNameLabel = new Label(layerConfiguration, SWT.NONE);
		layerNameLabel.setText("Name:");
		layerNameLabel.setLayoutData(DialogUtils.getLabelDataLayout());

		layerName = new Text(layerConfiguration, SWT.BORDER);
		layerName.setLayoutData(DialogUtils.getTextDataLayout());
		layerName.setText("Layer" + layerOrder.size());

		Label layerTypelabel = new Label(layerConfiguration, SWT.FLAT);
		layerTypelabel.setText("Type:");

		final Composite radioTypeSelection = new Composite(layerConfiguration,
				SWT.NONE);
		radioTypeSelection.setLayout(DialogUtils.getRadioSelectionLayout(3));
		radioTypeSelection.setLayoutData(new GridData(GridData.FILL,
				GridData.BEGINNING, false, false));

		Listener listener = new Listener() {
			@Override
			public void handleEvent(Event e) {
				Control[] children = radioTypeSelection.getChildren();
				if (((Button) e.widget).getText().endsWith("Raster")) {
					if (!(layerType.equals("RasterLayer"))) {
						configContainer.getChildren()[0].dispose();
						getRasterConfiguration(configContainer);
						configContainer.redraw();
						main.layout(true);
						layerType = "RasterLayer";
					}
				}
				if (((Button) e.widget).getText().endsWith("Vector")) {
					if (!(layerType.equals("VectorLayer"))) {
						configContainer.getChildren()[0].dispose();
						getVectorConfiguration(configContainer);
						configContainer.layout(true);
						configContainer.redraw();
						main.layout(true);
						layerType = "VectorLayer";
					}
				}
				if (((Button) e.widget).getText().endsWith("Thematic")) {
					if (!(layerType.equals("ThematicLayer"))) {
						configContainer.getChildren()[0].dispose();
						getThematicConfiguration(configContainer);
						configContainer.layout(true);
						configContainer.redraw();
						main.layout(true);
						layerType = "ThematicLayer";
					}
				}
				for (Control child : children) {
					if (e.widget != child) {
						((Button) child).setSelection(false);
					}
				}
				((Button) e.widget).setSelection(true);
			}
		};

		Button radioTypeButtonRaster = new Button(radioTypeSelection, SWT.RADIO);
		radioTypeButtonRaster.setText("Raster");
		radioTypeButtonRaster.setSelection(true);
		radioTypeButtonRaster.addListener(SWT.Selection, listener);

		Button radioTypeButtonVector = new Button(radioTypeSelection, SWT.RADIO);
		radioTypeButtonVector.setText("Vector");
		radioTypeButtonVector.addListener(SWT.Selection, listener);

		Button radioTypeButtonThematic = new Button(radioTypeSelection,
				SWT.RADIO);
		radioTypeButtonThematic.setText("Thematic");
		radioTypeButtonThematic.addListener(SWT.Selection, listener);

		Label layerPlaceLabel = new Label(layerConfiguration, SWT.FLAT);
		layerPlaceLabel.setText("Placement (after):");

		CCombo layerPlace = new CCombo(layerConfiguration, SWT.BORDER);
		layerPlace.setLayoutData(DialogUtils.getTextDataLayout());

		for (ILayer layer : layerOrder) {
			layerPlace.add(layer.getName());
		}
		if (!layerOrder.isEmpty())
			layerPlace.setText(layerOrder.getFirst().getName());

		layerPlace.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

			};
		});

		return layerConfiguration;
	}

	private Composite getRasterConfiguration(Composite parent) {
		if (!(layerConfiguration instanceof RasterLayerConfiguration))
			this.layerConfiguration = new RasterLayerConfiguration("");
		final RasterLayerConfiguration layerConfiguration = (RasterLayerConfiguration) this.layerConfiguration;
		Composite rasterLayer = new Composite(parent, SWT.NONE);
		rasterLayer.setLayout(DialogUtils.getGroupLayout());
		rasterLayer.setLayoutData(new GridData(GridData.FILL,
				GridData.BEGINNING, true, true));
		rasterLayer.setVisible(true);

		Label serverTypeLabel = new Label(rasterLayer, SWT.NONE);
		serverTypeLabel.setText("(Server) Type:");
		serverTypeLabel.setLayoutData(DialogUtils.getLabelDataLayout());

		final Composite serverTypeSelection = new Composite(rasterLayer,
				SWT.NONE);
		serverTypeSelection.setLayout(DialogUtils.getRadioSelectionLayout(3));
		serverTypeSelection.setLayoutData(new GridData(GridData.FILL,
				GridData.BEGINNING, false, false));

		Listener serverTypeListner = new Listener() {
			@Override
			public void handleEvent(Event e) {
				Control[] children = serverTypeSelection.getChildren();

				if (((Button) e.widget).getText().endsWith("WMS")) {

				}
				if (((Button) e.widget).getText().endsWith("Tile")) {

				}
				if (((Button) e.widget).getText().endsWith("User Defined")) {

				}

				for (Control child : children) {
					if (e.widget != child) {
						((Button) child).setSelection(false);
					}
				}
				((Button) e.widget).setSelection(true);
			}

		};

		Button serverTypeButtonWMS = new Button(serverTypeSelection, SWT.RADIO);
		serverTypeButtonWMS.setText("WMS");
		serverTypeButtonWMS.addListener(SWT.Selection, serverTypeListner);
		serverTypeButtonWMS.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// WMServiceTest wmsTest = new WMServiceTest();
				// String info = wmsTest.getInfo();
				// LOG.debug(info);
				// MessageDialog.openInformation(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
				// "WMS Info", info);
			};
		});

		Button serverTypeButtonTile = new Button(serverTypeSelection, SWT.RADIO);
		serverTypeButtonTile.setText("Tile");
		serverTypeButtonTile.addListener(SWT.Selection, serverTypeListner);
		serverTypeButtonTile.setSelection(true);

		Button serverTypeButtonUD = new Button(serverTypeSelection, SWT.RADIO);
		serverTypeButtonUD.setText("User Defined");
		serverTypeButtonUD.addListener(SWT.Selection, serverTypeListner);

		Label serverLabel = new Label(rasterLayer, SWT.FLAT);
		serverLabel.setLayoutData(DialogUtils.getLabelDataLayout());
		serverLabel.setText("Adresse:");

		server = new CCombo(rasterLayer, SWT.BORDER);
		server.setLayoutData(DialogUtils.getTextDataLayout());
		server.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				OdysseusMapPlugIn.getProperties().getTileServer(
						server.getSelectionIndex(), layerConfiguration);
				layerConfiguration.setUrl(server.getText());
				isValidInput();
			};
		});

		String[] defaults = OdysseusMapPlugIn.getProperties().listTileServer();
		for (String string : defaults) {
			server.add(string);
		}
		server.select(0);// .setText("http://oatile2.mqcdn.com/tiles/1.0.0/sat/");
		layerConfiguration.setUrl(server.getText());
		OdysseusMapPlugIn.getProperties().getTileServer(
				server.getSelectionIndex(), layerConfiguration);

		Label protocolTypeLabel = new Label(rasterLayer, SWT.NONE);
		protocolTypeLabel.setText("Protocol Type:");
		protocolTypeLabel.setLayoutData(DialogUtils.getLabelDataLayout());

		CCombo serverType = new CCombo(rasterLayer, SWT.BORDER);
		serverType.setLayoutData(DialogUtils.getTextDataLayout());

		serverType.add("RESTFUL Tile Server");
		serverType.add("WMS 1.0");
		serverType.add("WMS 1.1");
		serverType.add("WMS 1.1.1");

		serverType.setText("RESTFUL Tile Server");

		serverType.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// WMServiceTest wmsTest = new WMServiceTest();
				// String info = wmsTest.getInfo();
				// LOG.debug(info);
			};
		});
		return rasterLayer;
	}

	private Composite getVectorConfiguration(Composite parent) {
		if (!(layerConfiguration instanceof VectorLayerConfiguration))
			this.layerConfiguration = new VectorLayerConfiguration("");
		final VectorLayerConfiguration layerConfiguration = (VectorLayerConfiguration) this.layerConfiguration;
		final Composite vectorLayer = new Composite(parent, SWT.NONE);
		vectorLayer.setLayout(DialogUtils.getGroupLayout());
		vectorLayer.setLayoutData(new GridData(GridData.FILL,
				GridData.BEGINNING, true, false));
		vectorLayer.setVisible(true);

		if (connections.isEmpty()) {
			Label streamLabel = new Label(vectorLayer, SWT.NONE);
			streamLabel.setText("No Streams Available.");
			streamLabel.setLayoutData(DialogUtils.getLabelDataLayout());
			setErrorMessage("Please connect a stream to the Map.");
			return vectorLayer;
		}

		Label streamLabel = new Label(vectorLayer, SWT.NONE);
		streamLabel.setText("Stream:");
		streamLabel.setLayoutData(DialogUtils.getLabelDataLayout());

		final CCombo streamSelect = new CCombo(vectorLayer, SWT.BORDER);
		streamSelect.setLayoutData(DialogUtils.getTextDataLayout());

		for (int i = 0; i < connections.toArray().length; i++) {
			streamSelect.add(((LayerUpdater) connections.toArray()[i])
					.getQuery().getQueryText(), i);
		}
		streamSelect.select(0);
		Label attributesLabel = new Label(vectorLayer, SWT.NONE);
		attributesLabel.setText("Attribute:");
		attributesLabel.setLayoutData(DialogUtils.getLabelDataLayout());

		final CCombo attributeSelect = new CCombo(vectorLayer, SWT.BORDER);
		attributeSelect.setLayoutData(DialogUtils.getTextDataLayout());

		streamSelect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				attributeSelect.removeAll();
				layerConfiguration.setQuery(streamSelect.getText());
				LOG.debug("Set Query: " + layerConfiguration.getQuery());
				SDFSchema schema = ((LayerUpdater) connections.toArray()[streamSelect
						.getSelectionIndex()]).getConnection().getOutputSchema();

				for (int i = 0; i < schema.size(); i++) {
					attributeSelect.add(schema.getAttribute(i)
							.getAttributeName(), i);
				}

				AttributeResolver resolver = new AttributeResolver();
				resolver.addAttributes(schema);

				attributeSelect.setText(schema.getAttribute(0)
						.getAttributeName());
				layerConfiguration.setAttribute(attributeSelect.getText());

				attributeSelect.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						layerConfiguration.setAttribute(attributeSelect
								.getText());
						LOG.debug("Set Attribute: "
								+ layerConfiguration.getAttribute());
					};
				});

			};
		});
		return vectorLayer;
	}

	/**
	 * Creates the content to select the thematic map and the stream for it
	 * 
	 * @param parent
	 * @return
	 */
	private Composite getThematicConfiguration(Composite parent) {

		Composite thematicLayer = new Composite(parent, SWT.NONE);
		thematicLayer.setLayout(DialogUtils.getGroupLayout());
		thematicLayer.setLayoutData(new GridData(GridData.FILL,
				GridData.BEGINNING, true, true));
		thematicLayer.setVisible(true);

		if (connections.isEmpty()) {
			Label streamLabel = new Label(thematicLayer, SWT.NONE);
			streamLabel.setText("No Streams Available.");
			streamLabel.setLayoutData(DialogUtils.getLabelDataLayout());
			setErrorMessage("Please connect a stream to the map.");
			return thematicLayer;
		}

		// Choose between the thematic maps
		Label mapTypeLabel = new Label(thematicLayer, SWT.NONE);
		mapTypeLabel.setText("Thematic map type:");
		mapTypeLabel.setLayoutData(DialogUtils.getLabelDataLayout());

		final CCombo mapTypeSelect = new CCombo(thematicLayer, SWT.BORDER);
		mapTypeSelect.setLayoutData(DialogUtils.getTextDataLayout());
		mapTypeSelect.add("Heatmap");
		mapTypeSelect.add("Tracemap");
		mapTypeSelect.select(0);

		// Choose the stream for the thematic map
		Label streamLabel = new Label(thematicLayer, SWT.NONE);
		streamLabel.setText("Stream:");
		streamLabel.setLayoutData(DialogUtils.getLabelDataLayout());

		final CCombo streamSelect = new CCombo(thematicLayer, SWT.BORDER);
		streamSelect.setLayoutData(DialogUtils.getTextDataLayout());

		// Add all available streams
		for (int i = 0; i < connections.toArray().length; i++) {
			streamSelect.add(((LayerUpdater) connections.toArray()[i])
					.getQuery().getQueryText(), i);
		}
		streamSelect.select(0);

		Label geometrieLabel = new Label(thematicLayer, SWT.NONE);
		geometrieLabel.setText("Geometry Attribute:");
		geometrieLabel.setLayoutData(DialogUtils.getLabelDataLayout());

		final CCombo geometrieSelect = new CCombo(thematicLayer, SWT.BORDER);
		geometrieSelect.setLayoutData(DialogUtils.getTextDataLayout());

		Label visualizationLabel = new Label(thematicLayer, SWT.NONE);
		visualizationLabel.setText("Value Attribute:");
		visualizationLabel.setLayoutData(DialogUtils.getLabelDataLayout());

		final CCombo visualizationSelect = new CCombo(thematicLayer, SWT.BORDER);
		visualizationSelect.setLayoutData(DialogUtils.getTextDataLayout());

		// Add a listener -> created right layerConfiguration
		ThematicSelectionListener thematicSelectionListener = new ThematicSelectionListener(
				layerConfiguration, mapTypeSelect, streamSelect,
				geometrieSelect, visualizationSelect, connections, this);

		StreamSelectionListener streamSelectionListener = new StreamSelectionListener(
				layerConfiguration, mapTypeSelect, streamSelect, geometrieSelect,
				visualizationSelect, connections, this);
		mapTypeSelect.addSelectionListener(thematicSelectionListener);
		streamSelect.addSelectionListener(streamSelectionListener);

		geometrieSelect.addSelectionListener(thematicSelectionListener);
		visualizationSelect.addSelectionListener(thematicSelectionListener);

		// Initialize selection
		thematicSelectionListener.widgetSelected(null);
		streamSelectionListener.widgetSelected(null);

		return thematicLayer;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		parent.setLayout(DialogUtils.getMainLayout());
		main = parent;

		getBasicConfiguration(parent);

		DialogUtils.separator(parent);
		configContainer = new Composite(parent, SWT.NONE);
		// GridLayout configContainerLayout = DialogUtils.getGroupLayout();
		// configContainerLayout.numColumns = 1;
		// configContainerLayout.horizontalSpacing = 1;
		// configContainer.setBackground(new Color(parent.getDisplay(), new
		// RGB(200, 200, 200)));
		configContainer.setLayout(DialogUtils.getGroupLayout());
		configContainer.setLayoutData(new GridData(GridData.FILL,
				GridData.BEGINNING, false, false));

		getRasterConfiguration(configContainer);

		DialogUtils.separator(parent);

		DialogUtils.getFlexArea(parent);
		DialogUtils.separator(parent);

		return parent;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 3;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = SWT.RIGHT;

		parent.setLayoutData(gridData);
		// Create Add button
		// Own method as we need to overview the SelectionAdapter
		createOkButton(parent, OK, "Add", true);
		// Add a SelectionListener

		// Create Cancel button
		Button cancelButton = createButton(parent, CANCEL, "Cancel", false);
		// Add a SelectionListener
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setReturnCode(CANCEL);
				close();
			}
		});
	}

	protected Button createOkButton(Composite parent, int id, String label,
			boolean defaultButton) {
		// increment the number of columns in the button bar
		((GridLayout) parent.getLayout()).numColumns++;
		Button button = new Button(parent, SWT.PUSH);
		button.setText(label);
		button.setFont(JFaceResources.getDialogFont());
		button.setData(new Integer(id));
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				if (isValidInput()) {
					okPressed();
				}
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

		if (layerName.getText().length() == 0) {
			setErrorMessage("Please maintain the layer name.");
			valid = false;
		}

		return valid;
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	private void saveInput() {
		layerConfiguration.setName(layerName.getText());
	}

	@Override
	protected void okPressed() {
		saveInput();
		super.okPressed();
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Layer Properties");
	}
}
