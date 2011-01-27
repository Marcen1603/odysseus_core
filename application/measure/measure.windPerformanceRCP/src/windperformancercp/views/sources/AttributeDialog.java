package windperformancercp.views.sources;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import windperformancercp.event.IEventListener;
import windperformancercp.event.IEventType;
import windperformancercp.views.AbstractUIDialog;

public class AttributeDialog extends AbstractUIDialog {

	public static final String ID = "measure.windPerformanceRCP.NewAttributeDialog";

	private SourceDialogPresenter presenter;
	Text nameInputField;
	Combo typeCombo;
	Object[] comboElements;
	
	public AttributeDialog(Shell parentShell, SourceDialogPresenter presenter, Object[] cElements) {
		super(parentShell);
		this.presenter = presenter;
		this.comboElements = cElements;
	}

	@Override
	protected void configureShell(Shell newShell){
		super.configureShell(newShell);
		newShell.setText("New Attribute Dialog");
		newShell.setMinimumSize(300, 250);
		newShell.setSize(300,250);
	}
	
	@Override
	protected Control createDialogArea(Composite parent){
		setMessage("Please enter name and type for the new attribute: ");
		Composite area = new Composite(parent, SWT.FILL);
		

		//###main composite
		area.setLayout(new FormLayout());
		
		//## attribute name
		Composite nameComp = new Composite(area,SWT.NONE);
		{
			FormData nameCompFD = new FormData();
			nameCompFD.top = new FormAttachment(area,0);
			nameComp.setLayoutData(nameCompFD);
			nameComp.setLayout(new GridLayout(2,false));
			Label nameLabel = new Label(nameComp, SWT.BORDER);
			nameLabel.setText("Name:");
			nameLabel.setToolTipText("name for human readable identification");
			nameLabel.setLayoutData(new GridData());
			nameInputField = new Text(nameComp, SWT.BORDER);
			nameInputField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			nameInputField.addFocusListener(new FocusListener(){
				public void focusGained(FocusEvent fe){}
				public void focusLost(FocusEvent fe){presenter.aDNameEntered();}
			});
			
		}	
		
		//## attribute type
		Composite typeComp = new Composite(area,SWT.NONE);
		{
			FormData typeCompFD = new FormData();
			typeCompFD.top = new FormAttachment(nameComp,5);
			typeComp.setLayoutData(typeCompFD);
			typeComp.setLayout(new GridLayout(1,false));
			typeCombo = new Combo(typeComp,SWT.READ_ONLY);
			typeCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			for(int i = 0; i<comboElements.length;i++){
				typeCombo.add(comboElements[i].toString());
			}
			typeCombo.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent e) { presenter.aDTypeSelected();}
				public void widgetDefaultSelected(SelectionEvent e) { presenter.aDTypeSelected();}
			});
			typeCombo.select(0);
			presenter.aDTypeSelected(); //irgendwie bewirkt das obige select keinen Event (nur bei dem, sonst gehts)
		}

		return area;
	}
	
	@Override
	public void subscribe(IEventListener listener, IEventType type) {
		presenter.subscribe(listener, type);
	}
	@Override
	public void unsubscribe(IEventListener listener, IEventType type) {
		presenter.unsubscribe(listener, type);
	}

	@Override
	public void subscribeToAll(IEventListener listener) {
		presenter.subscribeToAll(listener);
	}

	@Override
	public void unSubscribeFromAll(IEventListener listener) {
		presenter.unSubscribeFromAll(listener);
	}
	
	public String getNameValue(){
		return nameInputField.getText();
	}
	public void setNameValue(String newName){
		nameInputField.setText(newName);
	}

	public String getComboValue(){
		return typeCombo.getItem(typeCombo.getSelectionIndex());
	}
	
	public void setComboValue(String value){
		typeCombo.setText(value);
	}
	
	
	@Override
	public void resetView(){
		nameInputField.setText("");
		typeCombo.select(0);
	}
			
	@Override
	public void okPressed(){	
		presenter.aDOkPressed();	
	}
	
	@Override
	public void cancelPressed(){	
		presenter.aDCancelPressed();	
	}

	

}
