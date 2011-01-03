package windperformancercp.views;


import java.util.Collections;
import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;


public class SourceDialog extends Dialog {

	public static final String ID = "measure.windPerformanceRCP.sourceDialog";
	
	private Text nameInputField;
	private Text strInputField;
	private Text hostInputField;
	private Text portInputField;
	private Table attributeTable;
	private Text hhInputField;
	private Button btnWT;
	private Button btnMM;
	private Button btnRActive;
	private Button btnRPassive;
	private ToolBar tb_attList;
	
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
		newShell.setMinimumSize(600, 400);
		newShell.setSize(700, 500);
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
		nameLabel.setToolTipText("name for human readable identification");
		nameInputField = new Text(nameComp, SWT.SINGLE | SWT.BORDER);
		
		Composite strInputComp = new Composite(streamInfoGroup,SWT.NONE);
		FormData strInputCompFD = new FormData();
		strInputCompFD.top = new FormAttachment(nameComp,5);
		strInputComp.setLayoutData(strInputCompFD);
		strInputComp.setLayout(new FillLayout());
		Label strInputLabel = new Label(strInputComp, SWT.BORDER);
		strInputLabel.setText("Stream name:");
		strInputLabel.setToolTipText("stream identification for DSMS");
		strInputField = new Text(strInputComp, SWT.SINGLE | SWT.BORDER);

		Composite hostComp = new Composite(streamInfoGroup,SWT.NONE);
		FormData hostCompFD = new FormData();
		hostCompFD.top = new FormAttachment(strInputComp,5);
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
		GridLayout attributeCompL = new GridLayout();
		attributeCompL.numColumns = 2;
		attributeComp.setLayout(attributeCompL);
				
		attributeTable = new Table(attributeComp, SWT.MULTI|SWT.BORDER|SWT.FULL_SELECTION);
		attributeTable.setHeaderVisible(true);
		attributeTable.setLinesVisible(true);
		GridData gd_table = new GridData(GridData.FILL_BOTH);

		attributeTable.setLayoutData(gd_table);
		String[] titles ={"Attribute","Type"};
		for (int i=0;i<titles.length;i++){
			TableColumn col = new TableColumn(attributeTable, SWT.FILL);
			col.setText(titles[i]);
		}
		
		tb_attList = new ToolBar(attributeComp,SWT.BORDER|SWT.VERTICAL);		
		String[] ti_labels = {"Add","Up","Down","Delete"};
		for(int i =0;i<ti_labels.length;i++){
			ToolItem ti = new ToolItem(tb_attList,SWT.PUSH);
			ti.setText(ti_labels[i]);
			ti.addListener(SWT.Selection, selectionListener);
		}
		GridData gd_tb_attL = new GridData(60,SWT.DEFAULT);
		gd_tb_attL.horizontalAlignment = GridData.BEGINNING;
		tb_attList.setLayoutData(gd_tb_attL);
				
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
		btnRPassiveFD.top = new FormAttachment(btnRActive,0,SWT.TOP);
		btnRPassiveFD.left = new FormAttachment(btnRActive,5);
		btnRPassive.setLayoutData(btnRPassiveFD);
		btnRPassive.setText("passive(stall)");
		
		//## MetMast
		GridLayout lrGgridLayout = new GridLayout();
		Group lowerRightGroup = new Group(lowerSash, SWT.RIGHT);
		lowerRightGroup.setText("MetMast");
		lowerRightGroup.setLayout(lrGgridLayout);

		return area;
	}
	
	Listener selectionListener = new Listener() {
	      public void handleEvent(Event event) {
	        ToolItem item = (ToolItem)event.widget;
	       
	        if(item.getText().equals("Add")){
	    	   
	        try {
	        	//TODO: das hier geht sicher eleganter mittels eines Commands!
	        	final Shell dialogShell = new Shell(event.display.getActiveShell());
				AttributeDialog dialog = new AttributeDialog(dialogShell);
	
				//TODO: generate
			if(dialog.open() == OK){
					System.out.println("New Attribute Handler says: Dialog says - ok button has been pressed!");
				}
			else{			
					System.out.println("New Attribute Handler says: Dialog says - cancel button has been pressed!");
				}
	    	  
	    	   }
	    	   catch(Exception ex){
	    		   System.out.println("Exception in pressing "+item.getText()+": "+ex+";;;"+ex.getCause()+";;;"+ex.getMessage());
	    	   }
 
	       }
	        System.out.println(item.getText() + " is selected");
	        if( (item.getStyle() & SWT.RADIO) != 0 || (item.getStyle() & SWT.CHECK) != 0 ) 
	        	System.out.println("Selection status: " + item.getSelection());
	      }
	    };
	    
	
	public void setNameValue(String newName){
		nameInputField.setText(newName);
	}
	
	public void setStrIdValue(String newStrIdent){
		strInputField.setText(newStrIdent);
	}
	
	public void setHostValue(String newHost){
		hostInputField.setText(newHost);
	}
	
	//TODO: bin ich korrekt?
	public void setPortValue(int newPort){
		portInputField.setText(String.valueOf(newPort));
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
		strInputField.setText("");
		hostInputField.setText("");
		portInputField.setText("");
		attributeTable.removeAll();
		hhInputField.setText("");
		btnRActive.setSelection(false);
		btnRPassive.setSelection(false);
	}
	
	@Override
	public void okPressed(){
		//TODO: neuen Event erzeugen, der dafuer sorgt, dass die Werte abgeholt werden
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
