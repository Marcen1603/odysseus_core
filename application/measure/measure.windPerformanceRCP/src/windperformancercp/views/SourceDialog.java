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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

public class SourceDialog extends Dialog {

	Text nameInputField;
	Text hostInputField;
	Text portInputField;
	Table attributeTable;
	Text hhInputField;
	Button btnRActive;
	Button btnRPassive;
	
	public static final int PC_ACTIVE = 0;
	public static final int PC_PASSIVE = 1;
	public static final int MMId = 0;
	public static final int WTId = 1;
	
	public SourceDialog(Shell parentShell) {
		super(parentShell);
		
	}

	public SourceDialog(IShellProvider parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
	}
	
//TODO ein clear-button waere noch schick!	
	@Override
	protected void configureShell(Shell newShell){
		super.configureShell(newShell);
		newShell.setText("New Source Dialog");
	}
	
	@Override
	protected Control createDialogArea(Composite parent){
		
		Composite area = (Composite) super.createDialogArea(parent);
		area.setLayout(new FillLayout(SWT.VERTICAL));
		
		//###upper composite
		
		SashForm upperComposite = new SashForm(area, SWT.FILL);
		
		Group streamInfoGroup = new Group(upperComposite,SWT.NONE);
		streamInfoGroup.setLayout(new FormLayout());
		streamInfoGroup.setText("Stream Information");
		
		Composite nameComp = new Composite(streamInfoGroup,SWT.NONE);
		FormData nameCompFD = new FormData();
		nameCompFD.top = new FormAttachment(streamInfoGroup,0);
		nameComp.setLayoutData(nameCompFD);
		nameComp.setLayout(new FillLayout());
		Label nameLabel = new Label(nameComp, SWT.BORDER);
		nameLabel.setText("Name:");
		nameInputField = new Text(nameComp, SWT.SINGLE | SWT.BORDER);
		
		Composite hostComp = new Composite(streamInfoGroup,SWT.NONE);
		FormData hostCompFD = new FormData();
		hostCompFD.top = new FormAttachment(nameComp,5);
		hostComp.setLayoutData(hostCompFD);
		hostComp.setLayout(new FillLayout());
		Label hostLabel = new Label(hostComp, SWT.NONE);
		hostLabel.setText("Host:");
		hostInputField = new Text(hostComp, SWT.SINGLE | SWT.BORDER);
		
		Composite portComp = new Composite(streamInfoGroup,SWT.NONE);
		FormData portCompFD = new FormData();
		portCompFD.top = new FormAttachment(hostComp,5);
		portComp.setLayoutData(portCompFD);
		portComp.setLayout(new FillLayout());
		Label portLabel = new Label(portComp, SWT.NONE);
		portLabel.setText("Port:");
		portInputField = new Text(portComp, SWT.SINGLE | SWT.BORDER);
		
		//### upper right composite: attribute table
		Composite attributeComp = new Composite(upperComposite, SWT.RIGHT);
		//GridLayout attributeCompL = new GridLayout();
		//attributeComp.setLayout(attributeCompL);
		attributeComp.setLayout(new FillLayout());
		
		attributeTable = new Table(attributeComp, SWT.MULTI|SWT.BORDER|SWT.FULL_SELECTION);
		attributeTable.setHeaderVisible(true);
		attributeTable.setLinesVisible(true);
		String[] titles ={"Attribute","Type"};
		for (int i=0;i<titles.length;i++){
			TableColumn col = new TableColumn(attributeTable, SWT.NONE);
			col.setText(titles[i]);
		}
		
				
		//### lower sash form with WT and MetMast Information
		SashForm lowerSash = new SashForm(area, SWT.FILL);
		//FillLayout llCompoLayout = new FillLayout();
		//lowerSash.setLayoutData(lowerSashLayout);
		
		//## WindTurbine
		//GridLayout llGgridLayout = new GridLayout();

		Group lowerLeftGroup = new Group(lowerSash, SWT.LEFT);
		lowerLeftGroup.setLayout(new FormLayout());
		lowerLeftGroup.setText("WindTurbine");
		
		//# hub height
		Composite hhComp = new Composite(lowerLeftGroup, SWT.FILL);
		FormData hhCompFD = new FormData();
		hhCompFD.top = new FormAttachment(lowerLeftGroup,5);
		hhComp.setLayoutData(hhCompFD);
		hhComp.setLayout(new FillLayout());
		
		Label hubHeightLbl = new Label(hhComp, SWT.NONE);
		hubHeightLbl.setText("Hub height:");
		hhInputField = new Text(hhComp, SWT.BORDER | SWT.FILL);
		Label mLabel = new Label(hhComp, SWT.NONE);
		mLabel.setText("m");
		
		//# power control
		Composite pcComposite = new Composite(lowerLeftGroup, SWT.FILL);
		FormData pcCompFD = new FormData();
		pcCompFD.top = new FormAttachment(hhComp,15);
		pcComposite.setLayoutData(pcCompFD);
		pcComposite.setLayout(new FormLayout());
		Label powerControlLbl = new Label(pcComposite, SWT.NONE);
		powerControlLbl.setLayoutData(new FormData());
		
		powerControlLbl.setText("Power Control Type:");
		btnRActive =	new Button(pcComposite, SWT.RADIO);
		FormData btnRActiveFD = new FormData();
		btnRActiveFD.top = new FormAttachment(powerControlLbl,7);
		btnRActiveFD.left = new FormAttachment(pcComposite,5,SWT.LEFT);
		btnRActive.setLayoutData(btnRActiveFD);
		btnRActive.setText("active(pitch)");

		btnRPassive = new Button(pcComposite, SWT.RADIO);
		FormData btnRPassiveFD = new FormData();
		btnRPassiveFD.left = new FormAttachment(btnRActive,5);
		btnRPassiveFD.top = new FormAttachment(btnRActive,0,SWT.TOP);
		btnRPassive.setLayoutData(btnRPassiveFD);
		btnRPassive.setText("passive(stall)");
		
		//## MetMast
		GridLayout lrGgridLayout = new GridLayout();
		Group lowerRightGroup = new Group(lowerSash, SWT.RIGHT);
		lowerRightGroup.setText("MetMast");
		lowerRightGroup.setLayout(lrGgridLayout);

		return area;
	}
	
	public void setNameValue(String newName){
		nameInputField.setText(newName);
	}
	
	public void setHostValue(String newHost){
		hostInputField.setText(newHost);
	}
	
	//TODO: bin ich korrekt?
	public void setPortValue(int newPort){
		portInputField.setText(Integer.toString(newPort));
	}
	
	public void setHubHeightValue(int newHubHeight){
		hhInputField.setText(Integer.toString(newHubHeight));
	}
	
	public void setPowerControl(int pc){
		if(pc == PC_ACTIVE){
			btnRActive.setSelection(true);
			btnRPassive.setSelection(false);
		}
		if(pc == PC_PASSIVE){
			btnRActive.setSelection(false);
			btnRPassive.setSelection(true);
		}
	}
	
	//TODO
	public void setType(int type){
	/*	if(type==MMId){
			
		}
		if(type==WTId){
			
		}
		*/
	}
	
	public void resetView(){
		nameInputField.setText("");
		hostInputField.setText("");
		portInputField.setText("");
		attributeTable.removeAll();
		hhInputField.setText("");
		btnRActive.setSelection(false);
		btnRPassive.setSelection(false);
	}
	
	@Override
	public void okPressed(){
		//TODO
		System.out.println("SourceDialog: Ok gedrueckt!");
		close();
	}
	
	@Override
	public void cancelPressed(){
		resetView();
		System.out.println("SourceDialog: cancel gedrueckt!");
		close();
	}


}
