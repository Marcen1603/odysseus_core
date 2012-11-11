package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.dialog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
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
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.AttributeResolver;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.LayerUpdater;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.ILayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.LayerConfiguration;

public class PropertyTitleDialog extends TitleAreaDialog {

	private static final Logger LOG = LoggerFactory.getLogger(TitleAreaDialog.class);	
	
	private LinkedList<ILayer> layerOrder;
	private Collection<LayerUpdater> connections;

	private Composite configContainer;
	private Composite main;
	private Composite thematicContainer;

	/*
	 * @TODO Save the layer reference...
	 */
	private int layerType = 0;
	private Text layerName;
	private CCombo server = null;
	private int numberOfVisualizationAttributes = 1;

	private LayerConfiguration layerConfiguration = new LayerConfiguration("");

	public LayerConfiguration getLayerConfiguration() {
		return layerConfiguration;
	}

	public void setLayerConfiguration(LayerConfiguration layerConfiguration) {
		this.layerConfiguration = layerConfiguration;
	}

	public PropertyTitleDialog(Shell parentShell, LinkedList<ILayer> layerOrder, Collection<LayerUpdater> connections) {
		super(parentShell);
		this.layerOrder = layerOrder;
		this.connections = connections;
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
		layerConfiguration.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));

		Label layerNameLabel = new Label(layerConfiguration, SWT.NONE);
		layerNameLabel.setText("Name:");
		layerNameLabel.setLayoutData(DialogUtils.getLabelDataLayout());

		layerName = new Text(layerConfiguration, SWT.BORDER);
		layerName.setLayoutData(DialogUtils.getTextDataLayout());

		Label layerTypelabel = new Label(layerConfiguration, SWT.FLAT);
		layerTypelabel.setText("Type:");

		final Composite radioTypeSelection = new Composite(layerConfiguration, SWT.NONE);
		radioTypeSelection.setLayout(DialogUtils.getRadioSelectionLayout(3));
		radioTypeSelection.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));

		Listener listener = new Listener() {
			public void handleEvent(Event e) {
				Control[] children = radioTypeSelection.getChildren();
				if (((Button) e.widget).getText().endsWith("Raster")) {
					if (!(layerType == 0)) {
						configContainer.getChildren()[0].dispose();
						getRasterConfiguration(configContainer);
						configContainer.redraw();
						main.layout(true);
						layerType = 0;
					}
				}
				if (((Button) e.widget).getText().endsWith("Vector")) {
					if (!(layerType == 1)) {
						configContainer.getChildren()[0].dispose();
						getVectorConfiguration(configContainer);
						configContainer.redraw();
						main.layout(true);
						layerType = 1;
					}
				}
				if (((Button) e.widget).getText().endsWith("Thematic")) {
					if (!((layerType == 2)||(layerType == 3)||(layerType == 4))) {
						configContainer.getChildren()[0].dispose();
						getThematicConfiguration(configContainer);
						configContainer.redraw();
						main.layout(true);
						layerType = 2;
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
		
		Button radioTypeButtonChoropleth = new Button(radioTypeSelection, SWT.RADIO);
		radioTypeButtonChoropleth.setText("Thematic");
		radioTypeButtonChoropleth.addListener(SWT.Selection, listener);


		Label layerPlaceLabel = new Label(layerConfiguration, SWT.FLAT);
		layerPlaceLabel.setText("Placement (after):");

		CCombo layerPlace = new CCombo(layerConfiguration, SWT.BORDER);
		layerPlace.setLayoutData(DialogUtils.getTextDataLayout());

		for (ILayer layer : layerOrder) {
			layerPlace.add(layer.getName());
		}
		if(!layerOrder.isEmpty())
			layerPlace.setText(layerOrder.getFirst().getName());

		layerPlace.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {

			};
		});

		return layerConfiguration;
	}

	private Composite getRasterConfiguration(Composite parent) {
		Composite rasterLayer = new Composite(parent, SWT.NONE);
		rasterLayer.setLayout(DialogUtils.getGroupLayout());
		rasterLayer.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, true));
		rasterLayer.setVisible(true);

		Label serverTypeLabel = new Label(rasterLayer, SWT.NONE);
		serverTypeLabel.setText("Server Type:");
		serverTypeLabel.setLayoutData(DialogUtils.getLabelDataLayout());

		final Composite serverTypeSelection = new Composite(rasterLayer, SWT.NONE);
		serverTypeSelection.setLayout(DialogUtils.getRadioSelectionLayout(3));
		serverTypeSelection.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));

		Listener serverTypeListner = new Listener() {
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
			public void widgetSelected(SelectionEvent e) {
//					WMServiceTest wmsTest = new WMServiceTest();
//					String info = wmsTest.getInfo();
//					LOG.debug(info);
//					MessageDialog.openInformation(PlatformUI.getWorkbench().getDisplay().getActiveShell(), "WMS Info", info);
				MessageDialog.openInformation(PlatformUI.getWorkbench().getDisplay().getActiveShell(), "WMS Info", "This button is currently not working!");
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

		server.add("http://oatile2.mqcdn.com/tiles/1.0.0/sat/");
		server.add("http://otile2.mqcdn.com/tiles/1.0.0/osm/");
		server.add("http://tile.opencyclemap.org/cycle/");
		server.add("http://tile2.opencyclemap.org/transport/");
		server.add("http://tile.cloudmade.com/0636cabea78640328462d9d26e2a97e2/1/256/");
		server.add("http://tile.cloudmade.com/0636cabea78640328462d9d26e2a97e2/2/256/");
		server.add("http://tile.cloudmade.com/0636cabea78640328462d9d26e2a97e2/3/256/");
		server.add("http://otile1.mqcdn.com/tiles/1.0.0/osm/");
		server.add("http://oatile1.mqcdn.com/naip/");
		server.add("http://tile.openstreetmap.org/");
		
		server.add("http://oatile2.mqcdn.com/tiles/1.0.0/sat/");
		server.setText("http://oatile2.mqcdn.com/tiles/1.0.0/sat/");

		server.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//layerConfiguration.setUrl(server.getText());
				isValidInput();
			};
		});

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

//		serverType.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent e) {
//					WMServiceTest wmsTest = new WMServiceTest();
//					String info = wmsTest.getInfo();
//					LOG.debug(info);
//			};
//		});
		return rasterLayer;
	}

	private Composite getVectorConfiguration(Composite parent) {
		final Composite vectorLayer = new Composite(parent, SWT.NONE);
		vectorLayer.setLayout(DialogUtils.getGroupLayout());
		vectorLayer.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
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
			streamSelect.add(((LayerUpdater) connections.toArray()[i]).getQuery().getLogicalQuery().getQueryText(), i);
		}

		Label attributesLabel = new Label(vectorLayer, SWT.NONE);
		attributesLabel.setText("Attribute:");
		attributesLabel.setLayoutData(DialogUtils.getLabelDataLayout());

		final CCombo attributeSelect = new CCombo(vectorLayer, SWT.BORDER);
		attributeSelect.setLayoutData(DialogUtils.getTextDataLayout());

		streamSelect.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				attributeSelect.removeAll();
				layerConfiguration.setQuery(streamSelect.getText());
				LOG.debug("Set Query: " + layerConfiguration.getQuery());
				SDFSchema schema = ((LayerUpdater) connections.toArray()[streamSelect.getSelectionIndex()]).getConnection().getSubscriptions().get(0).getSchema();

				for (int i = 0; i < schema.size(); i++) {
					attributeSelect.add(schema.getAttribute(i).getAttributeName(), i);
				}

				AttributeResolver resolver = new AttributeResolver();
				resolver.addAttributes(schema);

				attributeSelect.setText(schema.getAttribute(0).getAttributeName());
				ArrayList<String> attributes = new ArrayList<>();
				attributes.add(attributeSelect.getText());
				layerConfiguration.setAttribute(attributes);
				
				attributeSelect.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						ArrayList<String> attributes = new ArrayList<>();
						attributes.add(attributeSelect.getText());
						layerConfiguration.setAttribute(attributes);
						for(int i=0;i<layerConfiguration.getAttribute().size();i++){
							LOG.debug("Set Attribute "+(i+1)+"/"+layerConfiguration.getAttribute().size()+": " + layerConfiguration.getAttribute().get(i));
						}
					};
				});

			};
		});

		return vectorLayer;
	}
	
	private Composite getThematicConfiguration(Composite parent){
		final Composite thematicLayer = new Composite(parent, SWT.NONE);
		thematicContainer = thematicLayer;
		thematicLayer.setLayout(DialogUtils.getGroupLayout());
		thematicLayer.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
//		thematicLayer.setLayout(new GridLayout(2, false));
		thematicLayer.setVisible(true);
		
		Label thematicLabel = new Label(thematicLayer, SWT.NONE);
		thematicLabel.setText("Type: ");
		thematicLabel.setLayoutData(DialogUtils.getLabelDataLayout());
		
		final Combo thematicCombo = new Combo(thematicLayer, SWT.READ_ONLY);
		thematicCombo.setItems(new String[] {"ChoroplethLayer","LocationLayer","DiagramLayer"});
		thematicCombo.setLayoutData(DialogUtils.getTextDataLayout());
		thematicCombo.select(0);
				
		thematicCombo.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if(thematicCombo.getText().equals("ChoroplethLayer")){
					thematicLayer.getChildren()[2].dispose();
					Composite config = getChoroplethConfiguration(thematicLayer);
					config.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
					thematicLayer.redraw();
					thematicLayer.layout(true);
					main.layout(true);
					getShell().pack();
					layerType = 2;
				}else if(thematicCombo.getText().equals("LocationLayer")){
					thematicLayer.getChildren()[2].dispose();
					Composite config = getLocationConfiguration(thematicLayer);
					config.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
					thematicLayer.redraw();
					thematicLayer.layout(true);
					main.layout(true);
					getShell().pack();
					layerType = 3;
				}else if(thematicCombo.getText().equals("DiagramLayer")){
					thematicLayer.getChildren()[2].dispose();
					Composite config = getDiagramConfiguration(thematicLayer, numberOfVisualizationAttributes);
					config.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
					thematicLayer.redraw();
					thematicLayer.layout(true);
					main.layout(true);
					getShell().pack();
					layerType = 4;
				}
			}
		});
		
		Composite config = getChoroplethConfiguration(thematicLayer);
		config.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		thematicLayer.redraw();
		main.layout(true);
		getShell().pack();
		layerType = 2;
		
		
		return thematicLayer;
	}
	
	private Composite getChoroplethConfiguration(Composite parent) {
		final Composite chropletheLayer = new Composite(parent, SWT.NONE);
		chropletheLayer.setLayout(DialogUtils.getGroupLayout());
		chropletheLayer.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
		chropletheLayer.setVisible(true);

		if (connections.isEmpty()) {
			Label streamLabel = new Label(chropletheLayer, SWT.NONE);
			streamLabel.setText("No Streams Available.");
			streamLabel.setLayoutData(DialogUtils.getLabelDataLayout());
			setErrorMessage("Please connect a stream to the Map.");
			return chropletheLayer;
		}

		Label streamLabel = new Label(chropletheLayer, SWT.NONE);
		streamLabel.setText("Stream:");
		streamLabel.setLayoutData(DialogUtils.getLabelDataLayout());

		final CCombo streamSelect = new CCombo(chropletheLayer, SWT.BORDER);
		streamSelect.setLayoutData(DialogUtils.getTextDataLayout());

		for (int i = 0; i < connections.toArray().length; i++) {
			streamSelect.add(((LayerUpdater) connections.toArray()[i]).getQuery().getLogicalQuery().getQueryText(), i);
		}

		Label geometrieLabel = new Label(chropletheLayer, SWT.NONE);
		geometrieLabel.setText("GeometryAttribute:");
		geometrieLabel.setLayoutData(DialogUtils.getLabelDataLayout());

		final CCombo geometrieSelect = new CCombo(chropletheLayer, SWT.BORDER);
		geometrieSelect.setLayoutData(DialogUtils.getTextDataLayout());
		
		Label visualizationLabel = new Label(chropletheLayer, SWT.NONE);
		visualizationLabel.setText("VisualizationAttribute:");
		visualizationLabel.setLayoutData(DialogUtils.getLabelDataLayout());

		final CCombo visualizationSelect = new CCombo(chropletheLayer, SWT.BORDER);
		visualizationSelect.setLayoutData(DialogUtils.getTextDataLayout());

		streamSelect.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				geometrieSelect.removeAll();
				layerConfiguration.setQuery(streamSelect.getText());
				LOG.debug("Set Query: " + layerConfiguration.getQuery());
				SDFSchema schema = ((LayerUpdater) connections.toArray()[streamSelect.getSelectionIndex()]).getConnection().getSubscriptions().get(0).getSchema();

				for (int i = 0; i < schema.size(); i++) {
					geometrieSelect.add(schema.getAttribute(i).getAttributeName(), i);
				}
				
				visualizationSelect.removeAll();
				for (int i = 0; i < schema.size(); i++) {
					visualizationSelect.add(schema.getAttribute(i).getAttributeName(), i);
				}

				AttributeResolver resolver = new AttributeResolver();
				resolver.addAttributes(schema);

				geometrieSelect.setText(schema.getAttribute(0).getAttributeName());
				visualizationSelect.setText(schema.getAttribute(1).getAttributeName());
				
				
				ArrayList<String> attributes = new ArrayList<>();
				attributes.add(geometrieSelect.getText());
				attributes.add(visualizationSelect.getText());
				layerConfiguration.setAttribute(attributes);
				
				geometrieSelect.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						ArrayList<String> attributes = new ArrayList<>();
						attributes.add(geometrieSelect.getText());
						attributes.add(visualizationSelect.getText());
						layerConfiguration.setAttribute(attributes);
						for(int i=0;i<layerConfiguration.getAttribute().size();i++){
							LOG.debug("Set Attribute "+(i+1)+"/"+layerConfiguration.getAttribute().size()+": " + layerConfiguration.getAttribute().get(i));
						}
					};
				});
				
				visualizationSelect.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						ArrayList<String> attributes = new ArrayList<>();
						attributes.add(geometrieSelect.getText());
						attributes.add(visualizationSelect.getText());
						layerConfiguration.setAttribute(attributes);
						for(int i=0;i<layerConfiguration.getAttribute().size();i++){
							LOG.debug("Set Attribute "+(i+1)+"/"+layerConfiguration.getAttribute().size()+": " + layerConfiguration.getAttribute().get(i));
						}
					};
				});

			};
		});

		return chropletheLayer;
	}
	private Composite getLocationConfiguration(Composite parent) {
		final Composite locationLayer = new Composite(parent, SWT.NONE);
		locationLayer.setLayout(DialogUtils.getGroupLayout());
		locationLayer.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
		locationLayer.setVisible(true);

		if (connections.isEmpty()) {
			Label streamLabel = new Label(locationLayer, SWT.NONE);
			streamLabel.setText("No Streams Available.");
			streamLabel.setLayoutData(DialogUtils.getLabelDataLayout());
			setErrorMessage("Please connect a stream to the Map.");
			return locationLayer;
		}

		Label streamLabel = new Label(locationLayer, SWT.NONE);
		streamLabel.setText("Stream:");
		streamLabel.setLayoutData(DialogUtils.getLabelDataLayout());

		final CCombo streamSelect = new CCombo(locationLayer, SWT.BORDER);
		streamSelect.setLayoutData(DialogUtils.getTextDataLayout());

		for (int i = 0; i < connections.toArray().length; i++) {
			streamSelect.add(((LayerUpdater) connections.toArray()[i]).getQuery().getLogicalQuery().getQueryText(), i);
		}

		Label geometrieLabel = new Label(locationLayer, SWT.NONE);
		geometrieLabel.setText("GeometryAttribute:");
		geometrieLabel.setLayoutData(DialogUtils.getLabelDataLayout());

		final CCombo geometrieSelect = new CCombo(locationLayer, SWT.BORDER);
		geometrieSelect.setLayoutData(DialogUtils.getTextDataLayout());
		
		Label visualizationLabel = new Label(locationLayer, SWT.NONE);
		visualizationLabel.setText("VisualizationAttribute:");
		visualizationLabel.setLayoutData(DialogUtils.getLabelDataLayout());

		final CCombo visualizationSelect = new CCombo(locationLayer, SWT.BORDER);
		visualizationSelect.setLayoutData(DialogUtils.getTextDataLayout());

		streamSelect.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				geometrieSelect.removeAll();
				layerConfiguration.setQuery(streamSelect.getText());
				LOG.debug("Set Query: " + layerConfiguration.getQuery());
				SDFSchema schema = ((LayerUpdater) connections.toArray()[streamSelect.getSelectionIndex()]).getConnection().getSubscriptions().get(0).getSchema();

				for (int i = 0; i < schema.size(); i++) {
					geometrieSelect.add(schema.getAttribute(i).getAttributeName(), i);
				}
				
				visualizationSelect.removeAll();
				for (int i = 0; i < schema.size(); i++) {
					visualizationSelect.add(schema.getAttribute(i).getAttributeName(), i);
				}

				AttributeResolver resolver = new AttributeResolver();
				resolver.addAttributes(schema);

				geometrieSelect.setText(schema.getAttribute(0).getAttributeName());
				visualizationSelect.setText(schema.getAttribute(1).getAttributeName());
				
				
				ArrayList<String> attributes = new ArrayList<>();
				attributes.add(geometrieSelect.getText());
				attributes.add(visualizationSelect.getText());
				layerConfiguration.setAttribute(attributes);
				
				geometrieSelect.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						ArrayList<String> attributes = new ArrayList<>();
						attributes.add(geometrieSelect.getText());
						attributes.add(visualizationSelect.getText());
						layerConfiguration.setAttribute(attributes);
						for(int i=0;i<layerConfiguration.getAttribute().size();i++){
							LOG.debug("Set Attribute "+(i+1)+"/"+layerConfiguration.getAttribute().size()+": " + layerConfiguration.getAttribute().get(i));
						}
					};
				});
				
				visualizationSelect.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						ArrayList<String> attributes = new ArrayList<>();
						attributes.add(geometrieSelect.getText());
						attributes.add(visualizationSelect.getText());
						layerConfiguration.setAttribute(attributes);
						for(int i=0;i<layerConfiguration.getAttribute().size();i++){
							LOG.debug("Set Attribute "+(i+1)+"/"+layerConfiguration.getAttribute().size()+": " + layerConfiguration.getAttribute().get(i));
						}
					};
				});

			};
		});

		return locationLayer;
	}
	private Composite getDiagramConfiguration(final Composite parent, final int numberOfValues) {
		final Composite diagramLayer = new Composite(parent, SWT.NONE);
		diagramLayer.setLayout(DialogUtils.getGroupLayout());
		diagramLayer.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
		diagramLayer.setVisible(true);

		if (connections.isEmpty()) {
			Label streamLabel = new Label(diagramLayer, SWT.NONE);
			streamLabel.setText("No Streams Available.");
			streamLabel.setLayoutData(DialogUtils.getLabelDataLayout());
			setErrorMessage("Please connect a stream to the Map.");
			return diagramLayer;
		}
		
		Label numberOfVisAttributesLabel = new Label(diagramLayer, SWT.NONE);
		numberOfVisAttributesLabel.setText("Number of DiagramAttributes: ");
		
		final Spinner numberOfVisAttributesSpinner = new Spinner(diagramLayer, SWT.NONE);
		numberOfVisAttributesSpinner.setMinimum(1);
		numberOfVisAttributesSpinner.setMaximum(10);
		numberOfVisAttributesSpinner.setSelection(numberOfValues);
		numberOfVisAttributesSpinner.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				numberOfVisualizationAttributes = numberOfVisAttributesSpinner.getSelection();
				thematicContainer.getChildren()[2].dispose();
				Composite config = getDiagramConfiguration(thematicContainer, numberOfVisualizationAttributes);
				config.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
				thematicContainer.redraw();
				thematicContainer.layout(true);
				main.layout(true);
				getShell().pack();
				layerType = 4;
			}
		});

		Label streamLabel = new Label(diagramLayer, SWT.NONE);
		streamLabel.setText("Stream:");
		streamLabel.setLayoutData(DialogUtils.getLabelDataLayout());

		final CCombo streamSelect = new CCombo(diagramLayer, SWT.BORDER);
		streamSelect.setLayoutData(DialogUtils.getTextDataLayout());

		for (int i = 0; i < connections.toArray().length; i++) {
			streamSelect.add(((LayerUpdater) connections.toArray()[i]).getQuery().getLogicalQuery().getQueryText(), i);
		}

		Label geometrieLabel = new Label(diagramLayer, SWT.NONE);
		geometrieLabel.setText("GeometryAttribute:");
		geometrieLabel.setLayoutData(DialogUtils.getLabelDataLayout());

		final CCombo geometrieSelect = new CCombo(diagramLayer, SWT.BORDER);
		geometrieSelect.setLayoutData(DialogUtils.getTextDataLayout());
		
		final ArrayList<CCombo> comboList = new ArrayList<>();
		for(int i=0; i<numberOfValues;i++){
			Label visualizationLabel = new Label(diagramLayer, SWT.NONE);
			visualizationLabel.setText("DiagramAttribute "+(i+1)+":");
			visualizationLabel.setLayoutData(DialogUtils.getLabelDataLayout());

			final CCombo visualizationSelect = new CCombo(diagramLayer, SWT.BORDER);
			visualizationSelect.setLayoutData(DialogUtils.getTextDataLayout());
			comboList.add(visualizationSelect);
		}

		streamSelect.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				geometrieSelect.removeAll();
				layerConfiguration.setQuery(streamSelect.getText());
				LOG.debug("Set Query: " + layerConfiguration.getQuery());
				SDFSchema schema = ((LayerUpdater) connections.toArray()[streamSelect.getSelectionIndex()]).getConnection().getSubscriptions().get(0).getSchema();

				for (int i = 0; i < schema.size(); i++) {
					geometrieSelect.add(schema.getAttribute(i).getAttributeName(), i);
				}
				
				
				for(int i=0;i<comboList.size();i++){
					CCombo visualizationSelect = comboList.get(i);
					visualizationSelect.removeAll();
					for (int j = 0; j < schema.size(); j++) {
						visualizationSelect.add(schema.getAttribute(j).getAttributeName(), j);
					}
				}
				

				AttributeResolver resolver = new AttributeResolver();
				resolver.addAttributes(schema);

				geometrieSelect.setText(schema.getAttribute(0).getAttributeName());
				
				for(int i=0;i<comboList.size();i++){
					CCombo visualizationSelect = comboList.get(i);
					visualizationSelect.setText(schema.getAttribute(1).getAttributeName());
				}
				
				
				
				ArrayList<String> attributes = new ArrayList<>();
				attributes.add(geometrieSelect.getText());
				for(int i=0;i<comboList.size();i++){
					CCombo visualizationSelect = comboList.get(i);
					attributes.add(visualizationSelect.getText());
				}
				layerConfiguration.setAttribute(attributes);
				
				geometrieSelect.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						ArrayList<String> attributes = new ArrayList<>();
						attributes.add(geometrieSelect.getText());
						for(int i=0;i<comboList.size();i++){
							CCombo visualizationSelect = comboList.get(i);
							attributes.add(visualizationSelect.getText());
						}
						layerConfiguration.setAttribute(attributes);
						for(int i=0;i<layerConfiguration.getAttribute().size();i++){
							LOG.debug("Set Attribute "+(i+1)+"/"+layerConfiguration.getAttribute().size()+": " + layerConfiguration.getAttribute().get(i));
						}
					};
				});
				
				for(int i=0;i<comboList.size();i++){
					CCombo visualizationSelect = comboList.get(i);
					visualizationSelect.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent e) {
							ArrayList<String> attributes = new ArrayList<>();
							attributes.add(geometrieSelect.getText());
							for(int i=0;i<comboList.size();i++){
								CCombo visualizationSelect = comboList.get(i);
								attributes.add(visualizationSelect.getText());
							}
							layerConfiguration.setAttribute(attributes);
							for(int i=0;i<layerConfiguration.getAttribute().size();i++){
								LOG.debug("Set Attribute "+(i+1)+"/"+layerConfiguration.getAttribute().size()+": " + layerConfiguration.getAttribute().get(i));
							}
						};
					});
				}
			};
		});
		return diagramLayer;
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
		configContainer.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));

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
		layerConfiguration.setType(layerType);
		
		if(layerConfiguration.getType() == 0){
			layerConfiguration.setUrl(server.getText());	
		}
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
