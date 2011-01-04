package windperformancercp.views;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class AttributeDialog extends Dialog {

	public static final String ID = "measure.windPerformanceRCP.NewAttributeDialog";
	Text nameInputField;
	Button[] typeButton;

	
	public AttributeDialog(Shell parentShell) {
		super(parentShell);
		
	}

	public AttributeDialog(IShellProvider parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
	}
	
//TODO ein clear-button waere noch schick!	
	@Override
	protected void configureShell(Shell newShell){
		super.configureShell(newShell);
		newShell.setText("New Attribute Dialog");
	}
	
	@Override
	protected Control createDialogArea(Composite parent){
		
		Composite area = (Composite) super.createDialogArea(parent);
		area.setLayout(new FillLayout(SWT.VERTICAL));
		
		//###main composite
		area.setLayout(new FormLayout());
		
		//## attribute name
		Composite nameComp = new Composite(area,SWT.NONE);
		{
			FormData nameCompFD = new FormData();
			nameCompFD.top = new FormAttachment(area,0);
			nameComp.setLayoutData(nameCompFD);
			nameComp.setLayout(new FillLayout());
			Label nameLabel = new Label(nameComp, SWT.BORDER);
			nameLabel.setText("Name:");
			nameLabel.setToolTipText("name for human readable identification");
			nameInputField = new Text(nameComp, SWT.BORDER);
		}	
		
		//## attribute type
		Composite typeComp = new Composite(area,SWT.NONE);
		{
			FormData typeCompFD = new FormData();
			typeCompFD.top = new FormAttachment(nameComp,5);
			typeComp.setLayoutData(typeCompFD);
			GridLayout gl_typeComp = new GridLayout();
			gl_typeComp.numColumns = 2;
			typeComp.setLayout(gl_typeComp);
			String[] typeButtonNames = {"Timestamp", "Windspeed","Power","Air temperature","Air pressure","Wind direction","State","Various"};
			typeButton = new Button[typeButtonNames.length];
		
			for(int i=0 ; i<typeButton.length;i++){
				Button actButton = new Button(typeComp,SWT.RADIO);
				actButton.setText(typeButtonNames[i]);
			}
		}

		return area;
	}
	
	public void setNameValue(String newName){
		nameInputField.setText(newName);
	}

	public void resetView(){
		nameInputField.setText("");
		for(int i=0; i<typeButton.length;i++){
			typeButton[i].setSelection(false);
		}
	}
	
	@Override
	public void okPressed(){
		//TODO
		System.out.println("AttributeDialog: Ok gedrueckt!");
		close();
	}
	
	@Override
	public void cancelPressed(){
		System.out.println("AttributeDialog: cancel gedrueckt!");
		close();
	}


}
