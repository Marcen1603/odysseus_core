package windperformancercp.views;


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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import windperformancercp.event.InputDialogEvent;
import windperformancercp.event.InputDialogEventType;
import windperformancercp.model.AttributeTable;



public class SourceDialog extends AbstractUIDialog {

	public static final String ID = "measure.windPerformanceRCP.sourceDialog";
	
	//private InputDialogEvent newSourceEvent = new InputDialogEvent(getInstance(),InputDialogEventType.NewSourceITem, new String[]{});
	
	private Text nameInputField;
	public static final String NAMELABEL = "Name:";
	private Text strInputField;
	public static final String STREAMLABEL = "Stream name:";
	private Text hostInputField;
	public static final String HOSTLABEL = "Host:";
	private Text portInputField;
	public static final String PORTLABEL = "Port:";
	private Text hhInputField;
	public static final String HHLABEL = "Hub height:";
	private Button btnWT;
	private Button btnMM;
	private Button btnRActive;
	private Button btnRPassive;
	//private ToolBar tb_attList;
	
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
		{
		SashForm upperComposite = new SashForm(area, SWT.FILL);
			{
			Group streamInfoGroup = new Group(upperComposite,SWT.NONE);
			streamInfoGroup.setLayout(new FormLayout());
			streamInfoGroup.setText("Stream Information");
		
			Composite nameComp = new Composite(streamInfoGroup,SWT.NONE);
			{
				FormData nameCompFD = new FormData();
				nameCompFD.top = new FormAttachment(streamInfoGroup,0);
				nameComp.setLayoutData(nameCompFD);
				nameComp.setLayout(new FillLayout());
				Label nameLabel = new Label(nameComp, SWT.BORDER);
				nameLabel.setText(NAMELABEL);
				nameLabel.setToolTipText("name for human readable identification");
				nameInputField = new Text(nameComp, SWT.SINGLE | SWT.BORDER);
			}

			Composite strInputComp = new Composite(streamInfoGroup,SWT.NONE);
			{
				FormData strInputCompFD = new FormData();
				strInputCompFD.top = new FormAttachment(nameComp,5);
				strInputComp.setLayoutData(strInputCompFD);
				strInputComp.setLayout(new FillLayout());
				Label strInputLabel = new Label(strInputComp, SWT.BORDER);
				strInputLabel.setText(STREAMLABEL);
				strInputLabel.setToolTipText("stream identification for DSMS");
				strInputField = new Text(strInputComp, SWT.SINGLE | SWT.BORDER);
			}
			
			Composite hostComp = new Composite(streamInfoGroup,SWT.NONE);
			{
				FormData hostCompFD = new FormData();
				hostCompFD.top = new FormAttachment(strInputComp,5);
				hostComp.setLayoutData(hostCompFD);
				hostComp.setLayout(new FillLayout());
				Label hostLabel = new Label(hostComp, SWT.NONE);
				hostLabel.setText(HOSTLABEL);
				hostInputField = new Text(hostComp, SWT.SINGLE | SWT.BORDER);
			}
			
			Composite portComp = new Composite(streamInfoGroup,SWT.NONE);
			{
				FormData portCompFD = new FormData();
				portCompFD.top = new FormAttachment(hostComp,5);
				portComp.setLayoutData(portCompFD);
				portComp.setLayout(new FillLayout());
				Label portLabel = new Label(portComp, SWT.NONE);
				portLabel.setText(PORTLABEL);
				portInputField = new Text(portComp, SWT.SINGLE | SWT.BORDER);
				/*portInputField.addListener(SWT.Verify, new Listener(){
				public void handleEvent(Event e){
					String string = e.text;
					char[] chars = new char[string.length()];
					string.getChars(0, chars.length, chars, 0);
					for(int i = 0;i<chars.length;i++){
						if(!('0'<= chars[i]&&chars[i]<='9')){
							e.doit = false;
							return;
						}
					}
				}
				});*/
			}
			}

		
		//## upper right composite: attribute table
			{
				Composite attributeComp = new AttributeTable(upperComposite, SWT.RIGHT);
			}
		}
		
		//### lower sash form with WT and MetMast Information
		{
		SashForm lowerSash = new SashForm(area, SWT.FILL);

			//## WindTurbine
			{
			//GridLayout llGgridLayout = new GridLayout();
			Group lowerLeftGroup = new Group(lowerSash, SWT.LEFT);
			lowerLeftGroup.setLayout(new FormLayout());
			lowerLeftGroup.setText("WindTurbine");
		
			//# hub height
			Composite hhComp = new Composite(lowerLeftGroup, SWT.FILL);
			{
				FormData hhCompFD = new FormData();
				hhCompFD.top = new FormAttachment(lowerLeftGroup,5);
				hhComp.setLayoutData(hhCompFD);
				hhComp.setLayout(new FillLayout());
		
				Label hubHeightLbl = new Label(hhComp, SWT.NONE);
				hubHeightLbl.setText(HHLABEL);
				hhInputField = new Text(hhComp, SWT.BORDER | SWT.FILL);
				Label mLabel = new Label(hhComp, SWT.NONE);
				mLabel.setText("m");
			}
		
			//# power control
			Composite pcComposite = new Composite(lowerLeftGroup, SWT.FILL);
			{
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
			}
			}	
		
			//## MetMast
			{
				GridLayout lrGgridLayout = new GridLayout();
				Group lowerRightGroup = new Group(lowerSash, SWT.RIGHT);
				lowerRightGroup.setText("MetMast");
				lowerRightGroup.setLayout(lrGgridLayout);
			}
		}
		return area;
	}
	
	Listener verifyListener = new Listener(){
		public void handleEvent(Event event){
			Text eText = (Text) event.widget;
			
			if(eText.getText().equals(NAMELABEL)){
				
			}
			if(eText.getText().equals(STREAMLABEL)){
				
			}

			if(eText.getText().equals(HOSTLABEL)){
				
			}
			if(eText.getText().equals(PORTLABEL)){
				
			}

			if(eText.getText().equals(HHLABEL)){
				
			}
			
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
	@Override
	public String[] getValues(){
		return new String[]{};
	}
	
	//TODO
	public void setType(int type){
	/*	if(type==MMId){
			
		}
		if(type==WTId){
			
		}
		*/
	}
	
	@Override
	public void resetView(){
		//TODO: reset fuer attributeComp einsetzen
		nameInputField.setText("");
		strInputField.setText("");
		hostInputField.setText("");
		portInputField.setText("");
		
		hhInputField.setText("");
		btnRActive.setSelection(false);
		btnRPassive.setSelection(false);
	}
	
/*	@Override
	public void okPressed(){
		//TODO: neuen Event erzeugen, der dafuer sorgt, dass die Werte abgeholt werden
		
		close();
	}
*/

}
