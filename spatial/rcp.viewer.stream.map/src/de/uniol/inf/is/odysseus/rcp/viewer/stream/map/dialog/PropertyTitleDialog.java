package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.dialog;

import java.util.LinkedList;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
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
   
	//Main Area
	GridLayout layout = new GridLayout();
	layout.numColumns = 1;
	layout.horizontalSpacing = GridData.HORIZONTAL_ALIGN_FILL;
	layout.verticalSpacing = GridData.VERTICAL_ALIGN_FILL;
    parent.setLayout(layout);
 
	//Group Area
	GridLayout groupLayout = new GridLayout();
	groupLayout.numColumns = 2;
	groupLayout.horizontalSpacing = GridData.FILL;
	groupLayout.verticalSpacing = GridData.FILL;
    
	
    final Composite layerConfiguration = new Composite(parent, SWT.NONE);
	layerConfiguration.setLayout(groupLayout);
	layerConfiguration.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
	
    // The text fields will grow with the size of the dialog
    GridData gridData = new GridData();
    gridData.grabExcessHorizontalSpace = true;
    gridData.horizontalAlignment = GridData.FILL;
    
	Label layerLeftSparator = new Label (layerConfiguration, SWT.NONE);
	layerLeftSparator.setText("                                     ");	
    
	Label layerRightSparator = new Label (layerConfiguration, SWT.NONE);
	layerRightSparator.setText("                                     ");	
	
	Label layerNameLabel = new Label (layerConfiguration, SWT.NONE);
	layerNameLabel.setText("Name:");	
	

	Text layerName = new Text(layerConfiguration, SWT.BORDER);
	layerName.setLayoutData(gridData);
	
	Label layerTypelabel = new Label (layerConfiguration, SWT.FLAT);
	layerTypelabel.setText("Type:");	
	
//	CCombo layerType = new CCombo(layerConfiguration, SWT.BORDER);
//	layerType.setLayoutData(gridData);
//
//	layerType.add("Raster");
//	layerType.add("Vector");
//	
//	layerType.setText("Raster");
//
//	layerType.setOrientation(SWT.CENTER);
//	
//	layerType.addSelectionListener(new SelectionAdapter() {
//		
//		public void widgetSelected(SelectionEvent e) {
//			
//			//rasterLayer.setVisible(!raster);
//		};
//	});

	 final Composite radioTypeSelection = new Composite(layerConfiguration, SWT.NONE);
	 	radioTypeSelection.setLayout(groupLayout);
		radioTypeSelection.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
	
	Listener listener = new Listener () {
		public void handleEvent (Event e) {
			Control [] children = radioTypeSelection.getChildren();
			
			System.out.println(radioTypeSelection.getChildren().toString());
			
			for (Control child : children) {
				System.out.println(e.widget.toString());
				if (e.widget != child) {
					((Button) child).setSelection(false);
				}
			}
			((Button) e.widget).setSelection(true);
		}
	};

	Button radioTypeButtonRaster = new Button (radioTypeSelection, SWT.RADIO);
	radioTypeButtonRaster.setText("Raster");
	radioTypeButtonRaster.addListener (SWT.Selection, listener);

	Button radioTypeButtonVector = new Button (radioTypeSelection, SWT.RADIO);
	radioTypeButtonVector.setText("Vector");	
	radioTypeButtonVector.addListener (SWT.Selection, listener);	
	
	Label layerPlaceLabel = new Label (layerConfiguration, SWT.FLAT);
	layerPlaceLabel.setSize(300, 30);
	layerPlaceLabel.setText("Placement (after):");	
	
	CCombo layerPlace = new CCombo(layerConfiguration, SWT.BORDER);
	layerPlace.setLayoutData(gridData);
	
	for (ILayer layer: layerOrder) {
		layerPlace.add( layer.getName());
	}
	layerPlace.setText(layerOrder.getFirst().getName());
	
	layerPlace.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			//rasterLayer.setVisible(!raster);
		};
	});

//    Label label1 = new Label(parent, SWT.NONE);
//    label1.setText("First Name");
//
//    firstNameText = new Text(parent, SWT.BORDER);
//    firstNameText.setLayoutData(gridData);
//    
//    Label label2 = new Label(parent, SWT.NONE);
//    label2.setText("Last Name");
//    // You should not re-use GridData
//    gridData = new GridData();
//    gridData.grabExcessHorizontalSpace = true;
//    gridData.horizontalAlignment = GridData.FILL;
//    lastNameText = new Text(parent, SWT.BORDER);
//    lastNameText.setLayoutData(gridData);
    
	gridData = new GridData();
    gridData.horizontalAlignment = GridData.FILL;
    gridData.verticalAlignment = GridData.FILL;
    gridData.grabExcessHorizontalSpace = true;
    gridData.grabExcessVerticalSpace = false;
    gridData.horizontalSpan = 2;
    
	Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
	separator.setLayoutData(gridData);

	gridData = new GridData();
	gridData.grabExcessHorizontalSpace = true;
	gridData.horizontalAlignment = GridData.FILL;
	
	final Composite rasterLayer = new Composite(parent, SWT.NONE);
	rasterLayer.setLayout(groupLayout);
	rasterLayer.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
	
	Label rasterLeftSparator = new Label (rasterLayer, SWT.NONE);
	rasterLeftSparator.setText("                                     ");	
	rasterLeftSparator.setSize(new Point(1000,10));
	
	Label rasterRightSparator = new Label (rasterLayer, SWT.NONE);
	rasterRightSparator.setText("                                     ");
	rasterRightSparator.setSize(new Point(1000,10));
	
	Label serverTypeLabel = new Label (rasterLayer, SWT.FLAT);
	serverTypeLabel.setText("Sever Type:");
	serverTypeLabel.setSize(300, 30);	
	
	CCombo serverType = new CCombo(rasterLayer, SWT.BORDER);
	serverType.setLayoutData(gridData);
	
	serverType.add("RESTFUL Tile Server");
	serverType.add("WMS 1.0");
	serverType.add("WMS 1.1");
	serverType.add("WMS 1.1.1");

	serverType.setText("RESTFUL Tile Server");
	
	serverType.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			System.out.println(e.data.toString());
		};
	});

	Label serverLabel = new Label (rasterLayer, SWT.FLAT);
	serverLabel.setText("URL:");	
	serverLabel.setSize(300, 30);
	
	CCombo server = new CCombo(rasterLayer, SWT.BORDER);
	server.setLayoutData(gridData);
	
	
	server.add("http://tah.openstreetmap.org/Tiles/tile/");
	server.setText("http://tah.openstreetmap.org/Tiles/tile/");
	
	server.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			System.out.println(e.data.toString());
		};
	});
	
    return parent;
  }

  @Override
  protected void createButtonsForButtonBar(Composite parent) {
    GridData gridData = new GridData();
    gridData.verticalAlignment = GridData.FILL;
    gridData.horizontalSpan = 3;
    gridData.grabExcessHorizontalSpace = true;
    gridData.grabExcessVerticalSpace = true;
    gridData.horizontalAlignment = SWT.CENTER;

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
//    if (firstNameText.getText().length() == 0) {
//      setErrorMessage("Please maintain the first name");
//      valid = false;
//    }
//    if (lastNameText.getText().length() == 0) {
//      setErrorMessage("Please maintain the last name");
//      valid = false;
//    }
    return valid;
  }
  
  @Override
  protected boolean isResizable() {
    return true;
  }

  // Coyy textFields because the UI gets disposed
  // and the Text Fields are not accessible any more.
  private void saveInput() {
//    firstName = firstNameText.getText();
//    lastName = lastNameText.getText();
  }

  @Override
  protected void okPressed() {
    saveInput();
    super.okPressed();
  }
//
//  public String getFirstName() {
////    return firstName;
//  }
//
//  public String getLastName() {
//    return lastName;
//  }
  
  	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Layer Properties");
	}
} 
