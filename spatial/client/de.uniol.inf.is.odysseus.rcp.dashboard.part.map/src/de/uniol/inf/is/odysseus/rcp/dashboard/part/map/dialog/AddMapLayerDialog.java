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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.OwnProperties;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.layer.ILayer;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.HeatmapLayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.LayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.NullConfiguration;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.RasterLayerConfiguration;

public class AddMapLayerDialog extends TitleAreaDialog {

	private LinkedList<ILayer> layerOrder;

	private OwnProperties ownProperties;

	private Collection<IPhysicalOperator> operators;

	private Composite configContainer;
	private Composite main;

	private String layerType;
	private Text layerName;
	private int layerPositionAfter;
	private CCombo server = null;

	private LayerConfiguration layerConfiguration = null;

	public AddMapLayerDialog(Shell parentShell, Collection<IPhysicalOperator> operators,
			LinkedList<ILayer> layerOrder) {
		super(parentShell);
		this.operators = operators;
		this.layerOrder = layerOrder;
		this.layerType = "RasterLayer";
		this.ownProperties = new OwnProperties();
		this.layerPositionAfter = layerOrder.size() - 1;
	}

	@Override
	public void create() {
		super.create();
		setTitle("Map Layer");
		if (getMessage().length() == 0) {
			// If the length is greater than 0, there was already an error when
			// initializing a listener (e.g. StreamSelectionListener)
			setMessage("Create or edit a Map Layer.", IMessageProvider.INFORMATION);
		}
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

		// getRasterLayerConfigurationComposite(configContainer);
		getThematicConfiguration(configContainer);
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

		String[] types = { "Basic", "Map", "ThematicLayer" };
		final Combo layerTypesCombo = new Combo(typeSelection, SWT.DROP_DOWN);
		layerTypesCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		layerTypesCombo.setItems(types);
		layerTypesCombo.select(2);// Heatmap
		layerTypesCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (layerTypesCombo.getText().equals("Basic")) {
					if (!(layerType.equals("Basic"))) {
						for (Control c : configContainer.getChildren()) {
							c.dispose();
						}
						createNullConfiguration();
						configContainer.redraw();
						main.layout();
						layerType = "Basic";
					}
				} else

				if (layerTypesCombo.getText().equals("Map")) {
					if (!(layerType.equals("Map"))) {
						for (Control c : configContainer.getChildren()) {
							c.dispose();
						}
						getRasterLayerConfigurationComposite(configContainer);
						configContainer.redraw();
						main.layout();
						layerType = "RasterLayer";
					}
				} else if (layerTypesCombo.getText().equals("ThematicLayer")) {
					if (!(layerType.equals("ThematicLayer"))) {
						for (Control c : configContainer.getChildren()) {
							c.dispose();
						}
						getThematicConfiguration(configContainer);
						configContainer.redraw();
						main.layout();
						layerType = "ThematicLayer";
					}
				}
			}
		});

		Label layerPlaceLabel = new Label(layerConfigurationComp, SWT.FLAT);
		layerPlaceLabel.setText("Placement (after):");

		final CCombo layerPlace = new CCombo(layerConfigurationComp, SWT.BORDER);
		layerPlace.setLayoutData(DialogUtils.getTextDataLayout());

		for (ILayer layer : layerOrder) {
			layerPlace.add(layer.getName());
		}
		if (!layerOrder.isEmpty())
			layerPlace.setText(layerOrder.getLast().getName());

		layerPlace.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				layerPositionAfter = layerPlace.getSelectionIndex();
			};
		});

		return layerConfigurationComp;
	}

	private void createNullConfiguration() {
		if (!(layerConfiguration instanceof NullConfiguration))
			this.layerConfiguration = new NullConfiguration();
	}

	private Composite getRasterLayerConfigurationComposite(Composite parent) {
		if (!(layerConfiguration instanceof RasterLayerConfiguration)
				|| layerConfiguration instanceof HeatmapLayerConfiguration)
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
		layerConfiguration.setUrlNumber(server.getSelectionIndex());
		ownProperties.getTileServer(server.getSelectionIndex(), layerConfiguration);

		return rasterLayerComp;
	}

	/**
	 * Creates the content to select the thematic map and the stream for it
	 *
	 * @param parent
	 * @return
	 */
	private Composite getThematicConfiguration(Composite parent) {

		if (!(layerConfiguration instanceof HeatmapLayerConfiguration))
			this.layerConfiguration = new HeatmapLayerConfiguration("");
		final HeatmapLayerConfiguration layerConfiguration = (HeatmapLayerConfiguration) this.layerConfiguration;

		Composite thematicLayer = new Composite(parent, SWT.NONE);
		thematicLayer.setLayout(DialogUtils.getGroupLayout());
		thematicLayer.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, true));
		thematicLayer.setVisible(true);

		// Choose between the thematic maps
		Label mapTypeLabel = new Label(thematicLayer, SWT.NONE);
		mapTypeLabel.setText("Thematic map type:");
		mapTypeLabel.setLayoutData(DialogUtils.getLabelDataLayout());

		final CCombo mapTypeSelect = new CCombo(thematicLayer, SWT.BORDER);
		mapTypeSelect.setLayoutData(DialogUtils.getTextDataLayout());
		mapTypeSelect.add(HeatmapLayerConfiguration.HEATMAP_IDENTIFIER);
		mapTypeSelect.add("Tracemap");
		mapTypeSelect.select(0);

		Label dataSourceLabel = new Label(thematicLayer, SWT.NONE);
		dataSourceLabel.setText("Data source:");
		dataSourceLabel.setLayoutData(DialogUtils.getLabelDataLayout());

		final CCombo dataSourceSelect = new CCombo(thematicLayer, SWT.BORDER);
		dataSourceSelect.setLayoutData(DialogUtils.getTextDataLayout());
		// Add the possible sources to choose from
		for (IPhysicalOperator operator : operators) {
			dataSourceSelect.add(operator.getName());
		}
		if (operators.size() > 0) {
			dataSourceSelect.select(0);
		}

		Label geometrieLabel = new Label(thematicLayer, SWT.NONE);
		geometrieLabel.setText("Geometry Attribute:");
		geometrieLabel.setLayoutData(DialogUtils.getLabelDataLayout());

		final CCombo geometrieSelect = new CCombo(thematicLayer, SWT.BORDER);
		geometrieSelect.setLayoutData(DialogUtils.getTextDataLayout());

		Label latLabel = new Label(thematicLayer, SWT.NONE);
		latLabel.setText("Latitude Attribute:");
		latLabel.setLayoutData(DialogUtils.getLabelDataLayout());

		final CCombo latSelect = new CCombo(thematicLayer, SWT.BORDER);
		latSelect.setLayoutData(DialogUtils.getTextDataLayout());
		latSelect.setEnabled(false);

		Label lngLabel = new Label(thematicLayer, SWT.NONE);
		lngLabel.setText("Longitude Attribute:");
		lngLabel.setLayoutData(DialogUtils.getLabelDataLayout());

		final CCombo lngSelect = new CCombo(thematicLayer, SWT.BORDER);
		lngSelect.setLayoutData(DialogUtils.getTextDataLayout());
		lngSelect.setEnabled(false);

		Label geoTypeButtonLabel = new Label(thematicLayer, SWT.NONE);
		geoTypeButtonLabel.setText("Use Point?");
		geoTypeButtonLabel.setLayoutData(DialogUtils.getTextDataLayout());

		final Button geoSelectTypeButton = new Button(thematicLayer, SWT.CHECK);
		geoSelectTypeButton.setLayoutData(DialogUtils.getTextDataLayout());
		geoSelectTypeButton.setSelection(true);
		geoSelectTypeButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (geoSelectTypeButton.getSelection()) {
					geometrieSelect.setEnabled(true);
					latSelect.setEnabled(false);
					lngSelect.setEnabled(false);
					layerConfiguration.setUsePoint(true);
				} else {
					geometrieSelect.setEnabled(false);
					latSelect.setEnabled(true);
					lngSelect.setEnabled(true);
					layerConfiguration.setUsePoint(false);
				}
			}
		});

		Label visualizationLabel = new Label(thematicLayer, SWT.NONE);
		visualizationLabel.setText("Value Attribute:");
		visualizationLabel.setLayoutData(DialogUtils.getLabelDataLayout());

		final CCombo visualizationSelect = new CCombo(thematicLayer, SWT.BORDER);
		visualizationSelect.setLayoutData(DialogUtils.getTextDataLayout());

		// Add a listener -> created right layerConfiguration
		ThematicSelectionListener thematicSelectionListener = new ThematicSelectionListener(layerConfiguration,
				mapTypeSelect, geometrieSelect, latSelect, lngSelect, visualizationSelect, this);

		// TODO Choose the right operator first
		StreamSelectionListener streamSelectionListener = new StreamSelectionListener(operators, layerConfiguration,
				dataSourceSelect, mapTypeSelect, geometrieSelect, latSelect, lngSelect, visualizationSelect, this);
		mapTypeSelect.addSelectionListener(thematicSelectionListener);

		mapTypeSelect.addSelectionListener(thematicSelectionListener);
		geometrieSelect.addSelectionListener(thematicSelectionListener);
		latSelect.addSelectionListener(thematicSelectionListener);
		lngSelect.addSelectionListener(thematicSelectionListener);
		visualizationSelect.addSelectionListener(thematicSelectionListener);

		dataSourceSelect.addSelectionListener(streamSelectionListener);

		// Initialize selection
		thematicSelectionListener.widgetSelected(null);
		streamSelectionListener.widgetSelected(null);

		main.layout();
		return thematicLayer;
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

	public int getLayerPositionAfter() {
		return this.layerPositionAfter;
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
