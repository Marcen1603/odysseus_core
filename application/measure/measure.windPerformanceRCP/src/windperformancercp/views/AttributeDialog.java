package windperformancercp.views;

import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
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

import windperformancercp.event.InputDialogEvent;
import windperformancercp.event.InputDialogEventType;

public class AttributeDialog extends AbstractUIDialog {

	public static final String ID = "measure.windPerformanceRCP.NewAttributeDialog";

//	private InputDialogEvent newAttributeEvent = new InputDialogEvent(getInstance(),InputDialogEventType.NewItem, new String[]{});
	
	Text nameInputField;
	Combo typeCombo;
	Object[] comboElements;
	
	public AttributeDialog(Shell parentShell, Object[] cElements) {
		super(parentShell);
		this.comboElements = cElements;
	}

	public AttributeDialog(IShellProvider parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void configureShell(Shell newShell){
		super.configureShell(newShell);
		newShell.setText("New Attribute Dialog");
	}
	
	@Override
	protected Control createDialogArea(Composite parent){
		
		Composite area = (Composite) super.createDialogArea(parent);

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
			typeCombo.select(0);
		}

		return area;
	}
	
	public void setNameValue(String newName){
		nameInputField.setText(newName);
	}

	@Override
	public void resetView(){
		nameInputField.setText("");
		typeCombo.select(0);
	}
	
	@Override
	public String[] getValues(){
		return new String[]{getName(),getComboValue()};
	}
	
	public String getName(){
		return nameInputField.getText();
	}
	
	public String getComboValue(){
		return typeCombo.getItem(typeCombo.getSelectionIndex());
	}
	
	@Override
	public void okPressed(){	
		//TODO: throw exception
			if(! this.getName().equals("")){
				fire(new InputDialogEvent(this, InputDialogEventType.NewAttributeItem, this.getValues()));
			}
		
		close();
	}
	
}
