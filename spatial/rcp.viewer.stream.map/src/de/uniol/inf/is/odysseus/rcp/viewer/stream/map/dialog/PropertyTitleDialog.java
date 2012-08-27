package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.dialog;

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

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.ILayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.LayerConfiguration;

public class PropertyTitleDialog extends TitleAreaDialog {

	private LinkedList<ILayer> layerOrder;
	private SDFSchema schema;

	private Composite configContainer; 
	private Composite main; 
	
	private boolean raster = true; 
	private Text layerName;

	private LayerConfiguration layerConfiguration = new LayerConfiguration();

	public PropertyTitleDialog(Shell parentShell,
			LinkedList<ILayer> layerOrder, SDFSchema schema) {
		super(parentShell);
		this.layerOrder = layerOrder;
		this.schema = schema;
	}

	@Override
	public void create() {
		super.create();
		setTitle("Map Layer");
		setMessage("Create or edit a Map Layer.", IMessageProvider.INFORMATION);
	}

	private GridLayout getMainLayout() {
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.horizontalSpacing = GridData.BEGINNING;
		layout.verticalSpacing = GridData.BEGINNING;
		return layout;
	}

	private GridLayout getGroupLayout() {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.horizontalSpacing = GridData.FILL;
		layout.verticalSpacing = GridData.FILL;
		return layout;
	}

	private GridData getLabelDataLayout() {
		GridData gridLabelLayout = new GridData();
		gridLabelLayout.widthHint = 150;
		gridLabelLayout.heightHint = 25;
		return gridLabelLayout;
	}

	private GridData getTextDataLayout() {
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.heightHint = 20;
		return gridData;
	}

	private GridLayout getRadioSelectionLayout(int colums) {
		GridLayout layout = new GridLayout();
		layout.numColumns = colums;
		layout.horizontalSpacing = GridData.FILL;
		layout.verticalSpacing = GridData.BEGINNING;
		// layout.setBackground(new Color(parent.getDisplay(), new RGB(100, 0, 0)));
		return layout;
	}

	private void separator(Composite parent) {
		GridData separatorgridData = new GridData();
		separatorgridData.horizontalAlignment = GridData.FILL;
		separatorgridData.grabExcessHorizontalSpace = true;
		separatorgridData.grabExcessVerticalSpace = false;
		separatorgridData.horizontalSpan = 2;

		Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		separator.setLayoutData(separatorgridData);
	}

	private Composite getBasicConfiguration(final Composite parent) {
		Composite layerConfiguration = new Composite(parent, SWT.NONE);
		layerConfiguration.setLayout(getGroupLayout());
		layerConfiguration.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));

		Label layerNameLabel = new Label(layerConfiguration, SWT.NONE);
		layerNameLabel.setText("Name:");
		layerNameLabel.setLayoutData(getLabelDataLayout());

		layerName = new Text(layerConfiguration, SWT.BORDER);
		layerName.setLayoutData(getTextDataLayout());

		Label layerTypelabel = new Label(layerConfiguration, SWT.FLAT);
		layerTypelabel.setText("Type:");

		final Composite radioTypeSelection = new Composite(layerConfiguration,
				SWT.NONE);
		radioTypeSelection.setLayout(getRadioSelectionLayout(2));
		radioTypeSelection.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));

		Listener listener = new Listener() {
			public void handleEvent(Event e) {
				Control[] children = radioTypeSelection.getChildren();
				if (((Button) e.widget).getText().endsWith("Raster")) {
					configContainer.getChildren()[0].dispose();
//					configContainer.getChildren()[0].redraw();
					getRasterConfiguration(configContainer).setVisible(true);
					configContainer.redraw();
					main.layout(true);
					raster = true;
				}
				if (((Button) e.widget).getText().endsWith("Vector")) {
					configContainer.getChildren()[0].dispose();
//					configContainer.getChildren()[0].redraw();
					getVectorConfiguration(configContainer).setVisible(true);
					configContainer.redraw();
					main.layout(true);
					raster = false;
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

		Label layerPlaceLabel = new Label(layerConfiguration, SWT.FLAT);
		layerPlaceLabel.setText("Placement (after):");

		CCombo layerPlace = new CCombo(layerConfiguration, SWT.BORDER);
		layerPlace.setLayoutData(getTextDataLayout());

		for (ILayer layer : layerOrder) {
			layerPlace.add(layer.getName());
		}
		layerPlace.setText(layerOrder.getFirst().getName());

		layerPlace.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {

			};
		});

		return layerConfiguration;
	}

	private Composite getRasterConfiguration(Composite parent) {
		Composite rasterLayer = new Composite(parent, SWT.NONE);
		rasterLayer.setLayout(getGroupLayout());
		rasterLayer.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
		rasterLayer.setVisible(true);

		Label serverTypeLabel = new Label(rasterLayer, SWT.NONE);
		serverTypeLabel.setText("Server Type:");
		serverTypeLabel.setLayoutData(getLabelDataLayout());

		final Composite serverTypeSelection = new Composite(rasterLayer,
				SWT.NONE);
		serverTypeSelection.setLayout(getRadioSelectionLayout(3));
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

		Button serverTypeButtonTile = new Button(serverTypeSelection, SWT.RADIO);
		serverTypeButtonTile.setText("Tile");
		serverTypeButtonTile.addListener(SWT.Selection, serverTypeListner);
		serverTypeButtonTile.setSelection(true);

		Button serverTypeButtonUD = new Button(serverTypeSelection, SWT.RADIO);
		serverTypeButtonUD.setText("User Defined");
		serverTypeButtonUD.addListener(SWT.Selection, serverTypeListner);

		Label serverLabel = new Label(rasterLayer, SWT.FLAT);
		serverLabel.setLayoutData(getLabelDataLayout());
		serverLabel.setText("Adresse:");

		CCombo server = new CCombo(rasterLayer, SWT.BORDER);
		server.setLayoutData(getTextDataLayout());

		server.add("http://tah.openstreetmap.org/Tiles/tile/");
		server.setText("http://tah.openstreetmap.org/Tiles/tile/");

		server.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				isValidInput();
			};
		});

		Label protocolTypeLabel = new Label(rasterLayer, SWT.NONE);
		protocolTypeLabel.setText("Protocol Type:");
		protocolTypeLabel.setLayoutData(getLabelDataLayout());

		CCombo serverType = new CCombo(rasterLayer, SWT.BORDER);
		serverType.setLayoutData(getTextDataLayout());

		serverType.add("RESTFUL Tile Server");
		serverType.add("WMS 1.0");
		serverType.add("WMS 1.1");
		serverType.add("WMS 1.1.1");

		serverType.setText("RESTFUL Tile Server");

		serverType.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {

			};
		});
		return rasterLayer;
	}

	private Composite getVectorConfiguration(Composite parent) {
		Composite vectorLayer = new Composite(parent, SWT.NONE);

		vectorLayer.setLayout(getGroupLayout());
		vectorLayer.setLayoutData(new GridData(GridData.FILL,
				GridData.BEGINNING, false, false));
		vectorLayer.setVisible(false);

		Label attributesLabel = new Label(vectorLayer, SWT.NONE);
		attributesLabel.setText("Attribute:");
		attributesLabel.setLayoutData(getLabelDataLayout());

		CCombo attributeSelect = new CCombo(vectorLayer, SWT.BORDER);
		attributeSelect.setLayoutData(getTextDataLayout());

		for (SDFAttribute attribute : schema.getAttributes()) {
			attributeSelect.add(attribute.getAttributeName());
		}

		attributeSelect.setText(schema.getAttribute(0).getAttributeName());

		attributeSelect.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				isValidInput();
			};
		});

		return vectorLayer;
	}

	private Composite getFlexArea(Composite parent) {
		GridLayout freespaceLayout = getGroupLayout();
		freespaceLayout.numColumns = 2;
		freespaceLayout.horizontalSpacing = GridData.FILL;
		freespaceLayout.verticalSpacing = GridData.FILL;

		final Composite freeSpace = new Composite(parent, SWT.NONE);
		freeSpace.setLayout(freespaceLayout);
		freeSpace.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		
		return freeSpace;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		parent.setLayout(getMainLayout());
		main = parent;
		
		getBasicConfiguration(parent);
		
		separator(parent);
		configContainer = new Composite(parent, SWT.NONE);
		configContainer.setLayout(getGroupLayout());
		configContainer.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
		
		getRasterConfiguration(configContainer);
		
		separator(parent);
		getFlexArea(parent);
		separator(parent);

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

	protected Button createOkButton(Composite parent, int id, String label,
			boolean defaultButton) {
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
		if(raster){
			layerConfiguration.setRaster(true);
			
		}
		else{
			
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
