package de.uniol.inf.is.odysseus.rcp.dashboard.part.map.dialog;

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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.OwnProperties;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.layer.ILayer;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.LayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.RasterLayerConfiguration;

/**
 * Edit dialog for Basic and RasterLayers
 */
@SuppressWarnings("unused")
public class EditDialog extends TitleAreaDialog {

	private static final Logger LOG = LoggerFactory.getLogger(TitleAreaDialog.class);

	private LinkedList<ILayer> layerOrder;

	private OwnProperties ownProperties;

	private Composite configContainer;

	private Text layerName;
	private CCombo server = null;
	private int layerPosition;
	private int layerPositionAfter;

	private LayerConfiguration layerConfiguration = null;

	public EditDialog(Shell parentShell, LinkedList<ILayer> layerOrder, LayerConfiguration layerConfiguration,
			int layerPosition) {
		super(parentShell);
		this.layerOrder = layerOrder;
		this.ownProperties = new OwnProperties();
		this.layerConfiguration = layerConfiguration;
		this.layerPosition = layerPosition;
		this.layerPositionAfter = layerPosition;
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

		getBasicConfiguration(parent);

		DialogUtils.separator(parent);
		configContainer = new Composite(parent, SWT.NONE);
		configContainer.setLayout(DialogUtils.getGroupLayout());
		configContainer.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));

		if (layerConfiguration instanceof RasterLayerConfiguration) {
			getRasterLayerConfigurationComposite(configContainer);
			// }else if(layerConfiguration instanceof
			// HeatmapLayerConfiguration){
			// get
		}
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
		layerName.setText(layerConfiguration.getName());

		Label layerPlaceLabel = new Label(layerConfigurationComp, SWT.FLAT);
		layerPlaceLabel.setText("Placement (after):");

		final CCombo layerPlace = new CCombo(layerConfigurationComp, SWT.BORDER);
		layerPlace.setLayoutData(DialogUtils.getTextDataLayout());

		for (ILayer layer : layerOrder) {
			layerPlace.add(layer.getName());
		}
		if (!layerOrder.isEmpty())
			layerPlace.setText(layerOrder.get(layerPosition).getName());

		layerPlace.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				layerPositionAfter = layerPlace.getSelectionIndex();
			};
		});

		return layerConfigurationComp;
	}

	private Composite getRasterLayerConfigurationComposite(Composite parent) {
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
		RasterLayerConfiguration conf = layerConfiguration;

		boolean selectedPredefinedUrl = false;
		for (int i = 0; i < server.getItemCount(); i++) {
			if (server.getItem(i).equals(conf.getUrl())) {
				server.select(i);
				selectedPredefinedUrl = true;
			}
		}

		// If the user entered a tile server that is not in the predefined list,
		// show this url and select it
		if (!selectedPredefinedUrl) {
			server.add(layerConfiguration.getUrl());
			server.select(server.getItemCount() - 1);
		}
		ownProperties.getTileServer(server.getSelectionIndex(), layerConfiguration);

		return rasterLayerComp;
	}

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
		createOkButton(parent, OK, "Edit", true);
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

	public int getLayerPositionAfter() {
		return layerPositionAfter;
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

		if (layerConfiguration instanceof RasterLayerConfiguration) {
			((RasterLayerConfiguration) layerConfiguration).setUrl(server.getText());
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
