package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.dialog;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class NewPictogramDialog extends TitleAreaDialog {

	private String location = "";
	private Text imgText;
	private Text predicateText;
	private String predicate = "";
	private boolean stretch = false;
	private Button stretchCheckButton;


	public NewPictogramDialog(Shell parentShell) {
		super(parentShell);
	}
	
	@Override
	protected Control createDialogArea(final Composite parent) {
		setMessage("Add a new pictogram");
		setTitle("New Pictogram");
	
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));		
		
		Label lblChooseAnImage = new Label(container, SWT.NONE);
		lblChooseAnImage.setText("Choose an image");
		new Label(container, SWT.NONE);
		
		imgText = new Text(container, SWT.BORDER);
		GridData gd_dataFolderText = new GridData(GridData.FILL_HORIZONTAL);
		gd_dataFolderText.widthHint = 287;
		imgText.setLayoutData(gd_dataFolderText);		
		imgText.setText(location);
		
		Button button = new Button(container, SWT.PUSH);
		button.setText("Browse...");
		
		Label lblPredicate = new Label(container, SWT.NONE);
		lblPredicate.setText("Predicate");
		new Label(container, SWT.NONE);
		
		predicateText = new Text(container, SWT.BORDER);
		predicateText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		predicateText.setText(predicate);
		
		new Label(container, SWT.NONE);
		
		stretchCheckButton = new Button(container, SWT.CHECK);
		stretchCheckButton.setText("Resize the image to fit the container size");
		stretchCheckButton.setSelection(stretch);
		
		new Label(container, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				FileDialog dlg = new FileDialog(parent.getShell());

				// Set the initial filter path according
				// to anything they've selected or typed in
				dlg.setFilterPath(imgText.getText());

				// Change the title bar text
				dlg.setText("Choose an image");
				String[] extensions = {"*.png; *.gif; *.jpg; *.jpeg; *.svg", "*.*"};
				dlg.setFilterExtensions(extensions);				
				String dir = dlg.open();
				if (dir != null) {
					// Set the text box to the new selection
					imgText.setText(dir);										
				}
			}
		});
		
		return parent;
	}
	
	@Override
	protected void okPressed() {
		this.location = imgText.getText();		
		this.predicate = predicateText.getText();
		this.stretch = stretchCheckButton.getSelection();
		super.okPressed();		
	}
	
	public String getPredicate(){
		return this.predicate;
	}
	
	public String getLocation(){
		return this.location;
	}
	
	public void setLocation(String location){
		this.location = location;
	}
	
	public void setPredicate(String predicate){
		this.predicate = predicate;
	}

	public boolean isStretch() {
		return stretch;
	}

	public void setStretch(boolean stretch) {
		this.stretch = stretch;
	}

}
