package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.dialog;

import java.util.LinkedList;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
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

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.ILayer;

public class PropertyTitleDialog extends TitleAreaDialog {

	private LinkedList<ILayer> layerOrder;

	public PropertyTitleDialog(Shell parentShell, LinkedList<ILayer> layerOrder) {
		super(parentShell);
		this.layerOrder = layerOrder;
	}

	@Override
	public void create() {
		super.create();
		// Set the title
		setTitle("Map Layer");
		// Set the message
		setMessage("Create or edit a Map Layer.", IMessageProvider.INFORMATION);
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		// Main Area
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.horizontalSpacing = GridData.BEGINNING;
		layout.verticalSpacing = GridData.BEGINNING;
		parent.setLayout(layout);

		// Group Area
		GridLayout groupLayout = new GridLayout();
		groupLayout.numColumns = 2;
		groupLayout.horizontalSpacing = GridData.FILL;
		groupLayout.verticalSpacing = GridData.FILL;

		//GridData gridDataLayout = new GridData(GridData.FILL, GridData.BEGINNING, false, false);
		//gridDataLayout.heightHint = 30;
		
		GridData gridLabelLayout = new GridData();
		gridLabelLayout.widthHint = 150;
		
		final Composite layerConfiguration = new Composite(parent, SWT.NONE);
		layerConfiguration.setLayout(groupLayout);
		layerConfiguration.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));

		// The text fields will grow with the size of the dialog
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.heightHint = 20;

		Label layerNameLabel = new Label(layerConfiguration, SWT.NONE);
		layerNameLabel.setText("Name:");
		layerNameLabel.setLayoutData(gridLabelLayout);
		
		Text layerName = new Text(layerConfiguration, SWT.BORDER);
		layerName.setLayoutData(gridData);

		Label layerTypelabel = new Label(layerConfiguration, SWT.FLAT);
		layerTypelabel.setText("Type:");

		final Composite radioTypeSelection = new Composite(layerConfiguration,SWT.NONE);
		radioTypeSelection.setLayout(groupLayout);
		radioTypeSelection.setLayoutData(new GridData(GridData.FILL,	GridData.BEGINNING, false, false));

		GridData separatorgridData = new GridData();
		separatorgridData.horizontalAlignment = GridData.FILL;
		separatorgridData.grabExcessHorizontalSpace = true;
		separatorgridData.grabExcessVerticalSpace = false;
		separatorgridData.horizontalSpan = 2;

		Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		separator.setLayoutData(separatorgridData);
		
		final Composite vectorLayer = new Composite(parent, SWT.NONE);
		final Composite rasterLayer = new Composite(parent, SWT.NONE);
		
		Listener listener = new Listener() {
			public void handleEvent(Event e) {
				Control[] children = radioTypeSelection.getChildren();
				if(((Button) e.widget).getText().endsWith("Raster")){
					vectorLayer.setVisible(false);
					rasterLayer.setVisible(true);
				}
				if(((Button) e.widget).getText().endsWith("Vector")){
					vectorLayer.setVisible(true);
					rasterLayer.setVisible(false);
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
		layerPlace.setLayoutData(gridData);

		for (ILayer layer : layerOrder) {
			layerPlace.add(layer.getName());
		}
		layerPlace.setText(layerOrder.getFirst().getName());

		layerPlace.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				
			};
		});

		
		
		// Inhalt Raster Layer
		
		GridLayout rasterLayout = new GridLayout();
		rasterLayout.numColumns = 2;
		//rasterLayout.horizontalSpacing = GridData.FILL;
		rasterLayout.verticalSpacing = GridData.BEGINNING;
		//rasterLayer.setBackground(new Color(parent.getDisplay(), new RGB(100, 0, 0)));
		
		rasterLayer.setLayout(groupLayout);
		rasterLayer.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
		rasterLayer.setVisible(true);

		
		Label serverTypeLabel = new Label(rasterLayer, SWT.NONE);
		serverTypeLabel.setText("Server Type:");
		serverTypeLabel.setLayoutData(gridLabelLayout);

		GridLayout radioGroupLayout = new GridLayout();
		radioGroupLayout.numColumns = 3;
		radioGroupLayout.horizontalSpacing = GridData.FILL;
		radioGroupLayout.verticalSpacing = GridData.FILL;
		
		final Composite serverTypeSelection = new Composite(rasterLayer,SWT.NONE);
		serverTypeSelection.setLayout(radioGroupLayout);
		serverTypeSelection.setLayoutData(new GridData(GridData.FILL,	GridData.BEGINNING, false, false));
		
		Listener serverTypeListner = new Listener() {
			
			public void handleEvent(Event e) {
				Control[] children = serverTypeSelection.getChildren();
				
				if(((Button) e.widget).getText().endsWith("WMS")){
				
				}
				if(((Button) e.widget).getText().endsWith("Tile")){
					
				}
				if(((Button) e.widget).getText().endsWith("User Defined")){
					
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
		serverLabel.setText("Adresse:");

		CCombo server = new CCombo(rasterLayer, SWT.BORDER);
		server.setLayoutData(gridData);

		server.add("http://tah.openstreetmap.org/Tiles/tile/");
		server.setText("http://tah.openstreetmap.org/Tiles/tile/");

		server.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//Check Adresse
				isValidInput();
			};
		});
		
		Label protocolTypeLabel = new Label(rasterLayer, SWT.NONE);
		protocolTypeLabel.setText("Protocol Type:");
		protocolTypeLabel.setLayoutData(gridLabelLayout);
		
		CCombo serverType = new CCombo(rasterLayer, SWT.BORDER);
		serverType.setLayoutData(gridData);

		serverType.add("RESTFUL Tile Server");
		serverType.add("WMS 1.0");
		serverType.add("WMS 1.1");
		serverType.add("WMS 1.1.1");

		serverType.setText("RESTFUL Tile Server");

		serverType.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				
			};
		});

//		Button radioTypeButton = new Button(rasterLayer, SWT.RADIO);
//		radioTypeButton.setText("Worldmap");
//		radioTypeButton.setSelection(true);
//		radioTypeButton.addListener(SWT.Selection, listener);
		
		
		
		GridLayout vectorLayout = new GridLayout();
		vectorLayout.numColumns = 2;
		vectorLayout.horizontalSpacing = GridData.FILL;
		vectorLayout.verticalSpacing = GridData.FILL;
		
		vectorLayer.setLayout(groupLayout);
		vectorLayer.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
		vectorLayer.setVisible(false);
		
		
		
		
		// Abschluss
		
		GridLayout freespaceLayout = new GridLayout();
		freespaceLayout.numColumns = 2;
		freespaceLayout.horizontalSpacing = GridData.FILL;
		freespaceLayout.verticalSpacing = GridData.FILL;
		
		
		final Composite freespace = new Composite(parent, SWT.NONE);
		freespace.setLayout(freespaceLayout);
		freespace.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		freespace.setVisible(true);
		
		
		Label separator2 = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		separator2.setLayoutData(separatorgridData);

		
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
	    Button cancelButton = 
	        createButton(parent, CANCEL, "Cancel", false);
	    // Add a SelectionListener
	    cancelButton.addSelectionListener(new SelectionAdapter() {
	      public void widgetSelected(SelectionEvent e) {
	        setReturnCode(CANCEL);
	        close();
	      }
	    });
	  }

	  protected Button createOkButton(Composite parent, int id, 
	      String label,
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
		 if (true) {
			setErrorMessage("Server is not active.");
		    valid = false;
		 }
		// if (lastNameText.getText().length() == 0) {
		// setErrorMessage("Please maintain the last name");
		// valid = false;
		// }
		return valid;
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	// Coyy textFields because the UI gets disposed
	// and the Text Fields are not accessible any more.
	private void saveInput() {
		// firstName = firstNameText.getText();
		// lastName = lastNameText.getText();
	}

	@Override
	protected void okPressed() {
		saveInput();
		super.okPressed();
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Layer Properties");
	}
}
