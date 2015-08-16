package de.uniol.inf.is.odysseus.rcp.dashboard.part.map.dialog;

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
import org.eclipse.swt.widgets.Combo;
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
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.LayerUpdater;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.OwnProperties;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.layer.ILayer;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.LayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.NullConfiguration;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.RasterLayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.VectorLayerConfiguration;

public class PropertyTitleDialog extends TitleAreaDialog {

	private static final Logger LOG = LoggerFactory.getLogger(TitleAreaDialog.class);

	private LinkedList<ILayer> layerOrder;
	private Collection<LayerUpdater> connections;

	private OwnProperties ownProperties;

	private Composite configContainer;
	private Composite main;

	private String layerType;
	private Text layerName;
	private CCombo server = null;

	private LayerConfiguration layerConfiguration = null;

	public PropertyTitleDialog(Shell parentShell, LinkedList<ILayer> layerOrder, Collection<LayerUpdater> connections) {
		super(parentShell);
		this.layerOrder = layerOrder;
		this.connections = connections;
		this.layerType = "RasterLayer";
		this.ownProperties = new OwnProperties();
	}

	@Override
	public void create() {
		super.create();
		setTitle("Map Layer");
		setMessage("Create or edit a Map Layer.", IMessageProvider.INFORMATION);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		parent.setLayout(DialogUtils.getMainLayout());
		main = parent;

		getBasicConfiguration(parent);

		DialogUtils.separator(parent);
		configContainer = new Composite(parent, SWT.NONE);
		configContainer.setLayout(DialogUtils.getGroupLayout());
		configContainer.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));

		getRasterLayerConfigurationComposite(configContainer);

		DialogUtils.separator(parent);

		DialogUtils.getFlexArea(parent);
		DialogUtils.separator(parent);

		return parent;
	}

	private Composite getBasicConfiguration(final Composite parent) {
		Composite layerConfigurationComp = new Composite(parent, SWT.NONE);
		layerConfigurationComp.setLayout(DialogUtils.getGroupLayout());
		layerConfigurationComp.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));

		Label layerNameLabel = new Label(layerConfigurationComp, SWT.NONE);
		layerNameLabel.setText("Name:");
		layerNameLabel.setLayoutData(DialogUtils.getLabelDataLayout());

		layerName = new Text(layerConfigurationComp, SWT.BORDER);
		layerName.setLayoutData(DialogUtils.getTextDataLayout());
		layerName.setText("Layer" + layerOrder.size());

		Label layerTypelabel = new Label(layerConfigurationComp, SWT.FLAT);
		layerTypelabel.setText("Type:");

		final Composite typeSelection = new Composite(layerConfigurationComp, SWT.NONE);
		typeSelection.setLayout(DialogUtils.getGroupLayout());
		typeSelection.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));

		String[] types = { "Basic", "Map", "HeatMap" };
		final Combo layerTypesCombo = new Combo(typeSelection, SWT.DROP_DOWN);
		layerTypesCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		layerTypesCombo.setItems(types);
		layerTypesCombo.select(1);// Map
		layerTypesCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(layerTypesCombo.getText().equals("Basic")){
					if (!(layerType.equals("Basic"))) {
						for(Control c: configContainer.getChildren()){
							c.dispose();
						}
						createNullConfiguration();
						configContainer.redraw();
						main.layout(true);
						layerType = "Basic";
					}	
				}else
					
					if (layerTypesCombo.getText().equals("Map")) {
					if (!(layerType.equals("RasterLayer"))) {
						for(Control c: configContainer.getChildren()){
							c.dispose();
						}
						getRasterLayerConfigurationComposite(configContainer);
						configContainer.redraw();
						main.layout(true);
						layerType = "RasterLayer";
					}
				} else if (layerTypesCombo.getText().equals("HeatMap")) {
					if (!(layerType.equals("HeatMap"))) {
						for(Control c: configContainer.getChildren()){
							c.dispose();
						}
						//getRasterLayerConfigurationComposite(configContainer);
						configContainer.redraw();
						main.layout(true);
						layerType = "HeatMap";
					}
				}
			}
		});

		// if (((Button) e.widget).getText().endsWith("Thematic")) {
		// if (!(layerType.equals("ThematicLayer"))) {
		// configContainer.getChildren()[0].dispose();
		// getThematicConfiguration(configContainer);
		// configContainer.layout(true);
		// configContainer.redraw();
		// main.layout(true);
		// layerType = "ThematicLayer";
		// }
		// }

		Label layerPlaceLabel = new Label(layerConfigurationComp, SWT.FLAT);
		layerPlaceLabel.setText("Placement (after):");

		CCombo layerPlace = new CCombo(layerConfigurationComp, SWT.BORDER);
		layerPlace.setLayoutData(DialogUtils.getTextDataLayout());

		for (ILayer layer : layerOrder) {
			layerPlace.add(layer.getName());
		}
		if (!layerOrder.isEmpty())
			layerPlace.setText(layerOrder.getFirst().getName());

		// layerPlace.addSelectionListener(new SelectionAdapter() {
		// @Override
		// public void widgetSelected(SelectionEvent e) {
		//
		// };
		// });

		return layerConfigurationComp;
	}
	
	private void createNullConfiguration() {
		if(!(layerConfiguration instanceof NullConfiguration ))
			this.layerConfiguration = new NullConfiguration();
	}

	private Composite getRasterLayerConfigurationComposite(Composite parent) {
		if (!(layerConfiguration instanceof RasterLayerConfiguration))
			this.layerConfiguration = new RasterLayerConfiguration("");
		final RasterLayerConfiguration layerConfiguration = (RasterLayerConfiguration) this.layerConfiguration;
		Composite rasterLayerComp = new Composite(parent, SWT.NONE);
		rasterLayerComp.setLayout(DialogUtils.getGroupLayout());
		rasterLayerComp.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, true));
		rasterLayerComp.setVisible(true);

		Label serverLabel = new Label(rasterLayerComp, SWT.FLAT);
		serverLabel.setLayoutData(DialogUtils.getLabelDataLayout());
		serverLabel.setText("Address:");

		server = new CCombo(rasterLayerComp, SWT.BORDER);
		server.setLayoutData(DialogUtils.getTextDataLayout());
		server.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ownProperties.getTileServer(server.getSelectionIndex(), layerConfiguration);
				layerConfiguration.setUrl(server.getText());
				isValidInput();
			};
		});

		String[] defaults = ownProperties.listTileServer();
		for (String string : defaults) {
			server.add(string);
		}
		server.select(1);// .setText("http://oatile2.mqcdn.com/tiles/1.0.0/osm/");
		layerConfiguration.setUrl(server.getText());
		ownProperties.getTileServer(server.getSelectionIndex(), layerConfiguration);

		return rasterLayerComp;
	}

	// private Composite getVectorConfiguration(Composite parent) {
	// if (!(layerConfiguration instanceof VectorLayerConfiguration))
	// this.layerConfiguration = new VectorLayerConfiguration("");
	// final VectorLayerConfiguration layerConfiguration =
	// (VectorLayerConfiguration) this.layerConfiguration;
	// final Composite vectorLayer = new Composite(parent, SWT.NONE);
	// vectorLayer.setLayout(DialogUtils.getGroupLayout());
	// vectorLayer.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING,
	// true, false));
	// vectorLayer.setVisible(true);
	//
	// if (connections.isEmpty()) {
	// Label streamLabel = new Label(vectorLayer, SWT.NONE);
	// streamLabel.setText("No Streams Available.");
	// streamLabel.setLayoutData(DialogUtils.getLabelDataLayout());
	// setErrorMessage("Please connect a stream to the Map.");
	// return vectorLayer;
	// }
	//
	// Label streamLabel = new Label(vectorLayer, SWT.NONE);
	// streamLabel.setText("Stream:");
	// streamLabel.setLayoutData(DialogUtils.getLabelDataLayout());
	//
	// final CCombo streamSelect = new CCombo(vectorLayer, SWT.BORDER);
	// streamSelect.setLayoutData(DialogUtils.getTextDataLayout());
	//
	// for (int i = 0; i < connections.toArray().length; i++) {
	// streamSelect.add(((LayerUpdater)
	// connections.toArray()[i]).getQuery().getQueryText(), i);
	// }
	// streamSelect.select(0);
	// Label attributesLabel = new Label(vectorLayer, SWT.NONE);
	// attributesLabel.setText("Attribute:");
	// attributesLabel.setLayoutData(DialogUtils.getLabelDataLayout());
	//
	// final CCombo attributeSelect = new CCombo(vectorLayer, SWT.BORDER);
	// attributeSelect.setLayoutData(DialogUtils.getTextDataLayout());
	//
	// streamSelect.addSelectionListener(new SelectionAdapter() {
	// @Override
	// public void widgetSelected(SelectionEvent e) {
	// attributeSelect.removeAll();
	// layerConfiguration.setQuery(streamSelect.getText());
	// LOG.debug("Set Query: " + layerConfiguration.getQuery());
	// SDFSchema schema = ((LayerUpdater)
	// connections.toArray()[streamSelect.getSelectionIndex()])
	// .getConnection().getOutputSchema();
	//
	// for (int i = 0; i < schema.size(); i++) {
	// attributeSelect.add(schema.getAttribute(i).getAttributeName(), i);
	// }
	//
	// AttributeResolver resolver = new AttributeResolver();
	// resolver.addAttributes(schema);
	//
	// attributeSelect.setText(schema.getAttribute(0).getAttributeName());
	// layerConfiguration.setAttribute(attributeSelect.getText());
	//
	// attributeSelect.addSelectionListener(new SelectionAdapter() {
	// @Override
	// public void widgetSelected(SelectionEvent e) {
	// layerConfiguration.setAttribute(attributeSelect.getText());
	// LOG.debug("Set Attribute: " + layerConfiguration.getAttribute());
	// };
	// });
	//
	// };
	// });
	// return vectorLayer;
	// }

	// /**
	// * Creates the content to select the thematic map and the stream for it
	// *
	// * @param parent
	// * @return
	// */
	// private Composite getThematicConfiguration(Composite parent) {
	//
	// Composite thematicLayer = new Composite(parent, SWT.NONE);
	// thematicLayer.setLayout(DialogUtils.getGroupLayout());
	// thematicLayer.setLayoutData(new GridData(GridData.FILL,
	// GridData.BEGINNING, true, true));
	// thematicLayer.setVisible(true);
	//
	// if (connections.isEmpty()) {
	// Label streamLabel = new Label(thematicLayer, SWT.NONE);
	// streamLabel.setText("No Streams Available.");
	// streamLabel.setLayoutData(DialogUtils.getLabelDataLayout());
	// setErrorMessage("Please connect a stream to the map.");
	// return thematicLayer;
	// }
	//
	// // Choose between the thematic maps
	// Label mapTypeLabel = new Label(thematicLayer, SWT.NONE);
	// mapTypeLabel.setText("Thematic map type:");
	// mapTypeLabel.setLayoutData(DialogUtils.getLabelDataLayout());
	//
	// final CCombo mapTypeSelect = new CCombo(thematicLayer, SWT.BORDER);
	// mapTypeSelect.setLayoutData(DialogUtils.getTextDataLayout());
	// mapTypeSelect.add("Heatmap");
	// mapTypeSelect.add("Tracemap");
	// mapTypeSelect.select(0);
	//
	// // Choose the stream for the thematic map
	// Label streamLabel = new Label(thematicLayer, SWT.NONE);
	// streamLabel.setText("Stream:");
	// streamLabel.setLayoutData(DialogUtils.getLabelDataLayout());
	//
	// final CCombo streamSelect = new CCombo(thematicLayer, SWT.BORDER);
	// streamSelect.setLayoutData(DialogUtils.getTextDataLayout());
	//
	// // Add all available streams
	// for (int i = 0; i < connections.toArray().length; i++) {
	// streamSelect.add(((LayerUpdater) connections.toArray()[i])
	// .getQuery().getQueryText(), i);
	// }
	// streamSelect.select(0);
	//
	// Label geometrieLabel = new Label(thematicLayer, SWT.NONE);
	// geometrieLabel.setText("Geometry Attribute:");
	// geometrieLabel.setLayoutData(DialogUtils.getLabelDataLayout());
	//
	// final CCombo geometrieSelect = new CCombo(thematicLayer, SWT.BORDER);
	// geometrieSelect.setLayoutData(DialogUtils.getTextDataLayout());
	//
	// Label visualizationLabel = new Label(thematicLayer, SWT.NONE);
	// visualizationLabel.setText("Value Attribute:");
	// visualizationLabel.setLayoutData(DialogUtils.getLabelDataLayout());
	//
	// final CCombo visualizationSelect = new CCombo(thematicLayer, SWT.BORDER);
	// visualizationSelect.setLayoutData(DialogUtils.getTextDataLayout());
	//
	// // Add a listener -> created right layerConfiguration
	// ThematicSelectionListener thematicSelectionListener = new
	// ThematicSelectionListener(
	// layerConfiguration, mapTypeSelect, streamSelect,
	// geometrieSelect, visualizationSelect, connections, this);
	//
	// StreamSelectionListener streamSelectionListener = new
	// StreamSelectionListener(
	// layerConfiguration, mapTypeSelect, streamSelect, geometrieSelect,
	// visualizationSelect, connections, this);
	// mapTypeSelect.addSelectionListener(thematicSelectionListener);
	// streamSelect.addSelectionListener(streamSelectionListener);
	//
	// geometrieSelect.addSelectionListener(thematicSelectionListener);
	// visualizationSelect.addSelectionListener(thematicSelectionListener);
	//
	// // Initialize selection
	// thematicSelectionListener.widgetSelected(null);
	// streamSelectionListener.widgetSelected(null);
	//
	// return thematicLayer;
	// }

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

	protected Button createOkButton(Composite parent, int id, String label, boolean defaultButton) {
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

	public LayerConfiguration getLayerConfiguration() {
		return layerConfiguration;
	}

	public void setLayerConfiguration(LayerConfiguration layerConfiguration) {
		this.layerConfiguration = layerConfiguration;
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
