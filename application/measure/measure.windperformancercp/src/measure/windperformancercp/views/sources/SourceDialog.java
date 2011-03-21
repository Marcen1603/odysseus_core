/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package measure.windperformancercp.views.sources;


import java.util.ArrayList;

import measure.windperformancercp.event.IEvent;
import measure.windperformancercp.event.IEventListener;
import measure.windperformancercp.event.UpdateEvent;
import measure.windperformancercp.event.UpdateEventType;
import measure.windperformancercp.model.sources.Attribute;
import measure.windperformancercp.views.AbstractUIDialog;
import measure.windperformancercp.views.IPresenter;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
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
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;




public class SourceDialog extends AbstractUIDialog {

	public static final String ID = "measure.windperformancercp.sourceDialog";
		
	private SourceDialogPresenter presenter;
	
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
	private Text cutinInputField;
	public static final String CUTINLABEL = "Cut in WS:";
	private Text eigthyfiveInputField;
	public static final String EIGTHYFIVELABEL = "85% P_rated WS:";
	private Text frequencyInputField;
	public static final String FREQUENCYLABEL = "Frequency:";
	private Button btnWT;
	Group lowerLeftGroup;
	private Button btnMM;
	Group lowerRightGroup;
	private Button btnRActive;
	private Button btnRPassive;
	private TableViewer attributeViewer;
	private ToolBar tb_attList;
	
	//Source Type
	public static final int MMId = 0;
	public static final int WTId = 1;
	//Power Control
	public static final int PC_ACTIVE = 0;
	public static final int PC_PASSIVE = 1;

	public SourceDialog(Shell parentShell) {
		super(parentShell);
		this.presenter = new SourceDialogPresenter(this);
	}
	

	/*public SourceDialog(IShellProvider parentShell) {
		super(parentShell);
		this.presenter = new SourceDialogPresenter(this);
	}*/
	
	
//TODO ein clear-button waere noch schick!	
	@Override
	protected void configureShell(Shell newShell){
		super.configureShell(newShell);
		newShell.setText("New Source Dialog");
		newShell.setMinimumSize(600, 400);
		newShell.setSize(750, 530);
	}
	
	
	public void create() {
		    super.create();
		    setMessage("Please enter the required fields for creating a new source: ");
	 }
	
	@Override
	protected Control createDialogArea(Composite parent){
		
		Composite area = (Composite) super.createDialogArea(parent);
		area.setLayout(new GridLayout(2, false));
		
		//###upper composite
		SashForm upperComposite = new SashForm(area, SWT.FILL);
		{
			{
			upperComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));	
			Group streamInfoGroup = new Group(upperComposite,SWT.NONE);
			streamInfoGroup.setLayout(new FormLayout());
			streamInfoGroup.setText("Stream Information");
		
			Composite nameComp = new Composite(streamInfoGroup,SWT.NONE);
			{
				FormData nameCompFD = new FormData();
				nameCompFD.top = new FormAttachment(streamInfoGroup,0);
				nameComp.setLayoutData(nameCompFD);
				//nameComp.setLayout(new FillLayout());
				nameComp.setLayout(new GridLayout(2,false));
				Label nameLabel = new Label(nameComp, SWT.BORDER);
				nameLabel.setText(NAMELABEL);
				nameLabel.setToolTipText("name for human readable identification");
				nameInputField = new Text(nameComp, SWT.SINGLE | SWT.BORDER);
				nameInputField.setLayoutData(new GridData(120, SWT.DEFAULT));
				nameInputField.addFocusListener(new FocusListener(){
					public void focusGained(FocusEvent fe){}
					public void focusLost(FocusEvent fe){presenter.nameEntered();}
				});
			}

			Composite strInputComp = new Composite(streamInfoGroup,SWT.NONE);
			{
				FormData strInputCompFD = new FormData();
				strInputCompFD.top = new FormAttachment(nameComp,5);
				strInputComp.setLayoutData(strInputCompFD);
				//strInputComp.setLayout(new FillLayout());
				strInputComp.setLayout(new GridLayout(2,false));
				Label strInputLabel = new Label(strInputComp, SWT.BORDER);
				strInputLabel.setText(STREAMLABEL);
				strInputLabel.setToolTipText("stream identification for DSMS");
				strInputField = new Text(strInputComp, SWT.SINGLE | SWT.BORDER);
				strInputField.setLayoutData(new GridData(120, SWT.DEFAULT));
				strInputField.addFocusListener(new FocusListener(){
					public void focusGained(FocusEvent fe){}
					public void focusLost(FocusEvent fe){presenter.streamIdEntered();}
				});
			}
			
			Composite hostComp = new Composite(streamInfoGroup,SWT.NONE);
			{
				FormData hostCompFD = new FormData();
				hostCompFD.top = new FormAttachment(strInputComp,5);
				hostComp.setLayoutData(hostCompFD);
				//hostComp.setLayout(new FillLayout());
				hostComp.setLayout(new GridLayout(2,false));
				Label hostLabel = new Label(hostComp, SWT.NONE);
				hostLabel.setText(HOSTLABEL);
				hostLabel.setToolTipText("name of the host where the stream is located, e.g. 'localhost'");
				hostInputField = new Text(hostComp, SWT.SINGLE | SWT.BORDER);
				hostInputField.setLayoutData(new GridData(120, SWT.DEFAULT));
				hostInputField.addFocusListener(new FocusListener(){
					public void focusGained(FocusEvent fe){}
					public void focusLost(FocusEvent fe){presenter.hostEntered();}
				});
			}
			
			Composite portComp = new Composite(streamInfoGroup,SWT.NONE);
			{
				FormData portCompFD = new FormData();
				portCompFD.top = new FormAttachment(hostComp,5);
				portComp.setLayoutData(portCompFD);
				portComp.setLayout(new FillLayout());
				Label portLabel = new Label(portComp, SWT.NONE);
				portLabel.setText(PORTLABEL);
				portLabel.setToolTipText("port under which the stream is accessible at the host");
				portInputField = new Text(portComp, SWT.SINGLE | SWT.BORDER);
				portInputField.addFocusListener(new FocusListener(){
					public void focusGained(FocusEvent fe){}
					public void focusLost(FocusEvent fe){presenter.portEntered();}
				});
			}
			
			Composite freqComp = new Composite(streamInfoGroup,SWT.NONE);
			{
				FormData freqCompFD = new FormData();
				freqCompFD.top = new FormAttachment(portComp,5);
				freqComp.setLayoutData(freqCompFD);
				freqComp.setLayout(new FillLayout());
				Label freqLabel = new Label(freqComp, SWT.NONE);
				freqLabel.setText(FREQUENCYLABEL);
				freqLabel.setToolTipText("maximum frequency of source in Hertz");
				frequencyInputField = new Text(freqComp, SWT.SINGLE | SWT.BORDER);
				frequencyInputField.addFocusListener(new FocusListener(){
					public void focusGained(FocusEvent fe){}
					public void focusLost(FocusEvent fe){presenter.frequencyEntered();}
				});
				Label hzLabel = new Label(freqComp, SWT.NONE);
				hzLabel.setText(" Hz");
				//hzLabel.setLayoutData(new GridData());
			}
			}

		
		//## upper right composite: attribute table
			Composite attributeComp = new Composite(upperComposite, SWT.NONE);
			{
				attributeComp.setLayout(new GridLayout(2,false));
				
				attributeViewer = new TableViewer(attributeComp);
				attributeViewer.setContentProvider(new AttributeContentProvider());
				attributeViewer.setLabelProvider(new AttributeLabelProvider());
				
				//the attribute list/table/whatever
				Table attributeTable = attributeViewer.getTable();
				{
					attributeTable.setLayoutData(new GridData(GridData.FILL_BOTH));
				
					String[] titles ={"Attribute","Type"};
					for (int i=0;i<titles.length;i++){
						TableColumn col = new TableColumn(attributeTable,SWT.LEFT);
						col.setText(titles[i]);
						col.pack();
					}
					attributeTable.setHeaderVisible(true);
					attributeTable.setLinesVisible(true);
				}
				//the toolbar for the attribute list
				tb_attList = new ToolBar(attributeComp,SWT.BORDER|SWT.VERTICAL);
				{
					String[] ti_labels = {"Add","Up","Down","Delete"};
					for(int i =0;i<ti_labels.length;i++){
						ToolItem ti = new ToolItem(tb_attList,SWT.PUSH);
						ti.setText(ti_labels[i]);
						ti.addSelectionListener(new SelectionAdapter() {
							public void widgetSelected(SelectionEvent e) { 
								ToolItem btn = (ToolItem)e.widget;
								
								presenter.attBtnClick(btn.getText(),attributeViewer.getTable().getSelectionIndex());}
						});
					}
					GridData gd_tb_attL = new GridData(60,SWT.DEFAULT);
					gd_tb_attL.horizontalAlignment = GridData.BEGINNING;
					tb_attList.setLayoutData(gd_tb_attL);
				}
			}
		}
		
		//### lower sash form with WT and MetMast Information
		Composite lowerSash = new Composite(area, SWT.FILL);
		{
			lowerSash.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));	
			lowerSash.setLayout(new GridLayout(4,false));

			//## WindTurbine
			{
			btnWT = new Button(lowerSash,SWT.RADIO);
			btnWT.setData(WTId);
			btnWT.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
			
			lowerLeftGroup = new Group(lowerSash, SWT.NONE);
			//lowerLeftGroup.setEnabled(false);
			lowerLeftGroup.setEnabled(btnWT.getSelection());
			
			lowerLeftGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
			lowerLeftGroup.setLayout(new FormLayout());
			lowerLeftGroup.setText("WindTurbine");
		
			//# hub height
			Composite hhComp = new Composite(lowerLeftGroup, SWT.FILL);
			{
				FormData hhCompFD = new FormData();
				hhCompFD.top = new FormAttachment(lowerLeftGroup,5);
				hhComp.setLayoutData(hhCompFD);
				hhComp.setLayout(new GridLayout(3, false));
		
				Label hubHeightLbl = new Label(hhComp, SWT.NONE);
				hubHeightLbl.setText(HHLABEL);
				hubHeightLbl.setLayoutData(new GridData());
				hhInputField = new Text(hhComp, SWT.BORDER);
				hhInputField.setLayoutData(new GridData(40, SWT.DEFAULT));
				hhInputField.addFocusListener(new FocusListener(){
					public void focusGained(FocusEvent fe){}
					public void focusLost(FocusEvent fe){presenter.hubheightEntered();}
				});
				Label mLabel = new Label(hhComp, SWT.NONE);
				mLabel.setText(" m");
				mLabel.setLayoutData(new GridData());
			}
			//# cut in and 85 percent of p rated
			Composite vComp = new Composite(lowerLeftGroup, SWT.FILL);
			{
				FormData vCompFD = new FormData();
				vCompFD.top = new FormAttachment(hhComp,5);
				vComp.setLayoutData(vCompFD);
				vComp.setLayout(new GridLayout(4,false));
		
				Label cutinLbl = new Label(vComp, SWT.NONE);
				cutinLbl.setText(CUTINLABEL);
				cutinLbl.setLayoutData(new GridData());
				cutinInputField = new Text(vComp, SWT.BORDER);
				cutinInputField.setLayoutData(new GridData(40, SWT.DEFAULT));
				cutinInputField.addFocusListener(new FocusListener(){
					public void focusGained(FocusEvent fe){}
					public void focusLost(FocusEvent fe){presenter.cutinEntered();}
				});
				Label eightyfiveLabel = new Label(vComp, SWT.NONE);
				eightyfiveLabel.setText(EIGTHYFIVELABEL);
				eightyfiveLabel.setLayoutData(new GridData());
				eigthyfiveInputField = new Text(vComp, SWT.BORDER);
				eigthyfiveInputField.setLayoutData(new GridData(40, SWT.DEFAULT));
				eigthyfiveInputField.addFocusListener(new FocusListener(){
					public void focusGained(FocusEvent fe){}
					public void focusLost(FocusEvent fe){presenter.eightyfiveEntered();}
				});
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
				btnRActive.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) { presenter.powerControlTypeClick();}
				});
				
				btnRPassive = new Button(pcComposite, SWT.RADIO);
				FormData btnRPassiveFD = new FormData();
				btnRPassiveFD.top = new FormAttachment(btnRActive,0,SWT.TOP);
				btnRPassiveFD.left = new FormAttachment(btnRActive,5);
				btnRPassive.setLayoutData(btnRPassiveFD);
				btnRPassive.setText("passive(stall)");
				btnRPassive.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) { presenter.powerControlTypeClick();}
				});
			}
			}	
		
			//## MetMast
			{
				btnMM = new Button(lowerSash,SWT.RADIO);
				btnMM.setData(MMId);
				btnMM.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
				
				lowerRightGroup = new Group(lowerSash, SWT.RIGHT);
				//lowerRightGroup.setEnabled(false);
				lowerRightGroup.setEnabled(btnMM.getSelection());
				
				lowerRightGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
				lowerLeftGroup.setLayout(new FormLayout());
				lowerRightGroup.setText("MetMast");
	
			}
			
			SelectionAdapter typeAdapter = new SelectionAdapter(){
				public void widgetSelected(SelectionEvent e) {
					//Button button = (Button) e.widget;
					Button button = (Button) e.getSource();
					
			    	  if((button.getData().equals(WTId))&&(button.getSelection())){
			    		  lowerLeftGroup.setEnabled(true);
			    		  lowerRightGroup.setEnabled(false);
			    		  presenter.srcTypeClick();
			    		  return;
			    	  }
			    	  if((button.getData().equals(MMId))&&(button.getSelection())){
			    		  lowerLeftGroup.setEnabled(false);
			    		  lowerRightGroup.setEnabled(true);
			    		  presenter.srcTypeClick();
			    		  return;
			    	  }
				}
			};

			btnMM.addSelectionListener(typeAdapter);
			btnWT.addSelectionListener(typeAdapter);
		}
		presenter.subscribeToAll(updateListener);
		return area;
	}

	
	IEventListener updateListener = new IEventListener(){
		public void eventOccured(IEvent<?, ?> idevent){
			//System.out.println(this.toString()+": got update event from"+idevent.getSender().toString());
			if(idevent.getEventType().equals(UpdateEventType.GeneralUpdate)){
				UpdateEvent updateInvoker = (UpdateEvent) idevent;
		//	System.out.println(this.toString()+": got general update invocation: ");
				update(updateInvoker.getValue());
			}
		}
	};
	
	@Override
	public IPresenter getPresenter(){
		return presenter;
	}
		
	@Override
	public void update(Object attList){
		ArrayList<?> al = (ArrayList<?>) attList; 
		setTableContent(al);
	}
	    
	public void setNameValue(String newName){
		nameInputField.setText(newName);
	}
	
	public String getNameValue(){
		return nameInputField.getText();
	}
	
	public void setStrIdValue(String newStrIdent){
		strInputField.setText(newStrIdent);
	}
	
	public String getStrIdValue(){
		return strInputField.getText();
	}
	
	public void setHostValue(String newHost){
		hostInputField.setText(newHost);
	}
	
	public String getHostValue(){
		return hostInputField.getText();
	}
	
	public void setPortValue(String newPort){
		portInputField.setText(newPort);
	}
	
	public String getPortValue(){
		if(portInputField.getText().equals("")) return "-1";
		return portInputField.getText();
	}
	
	public void setFrequencyValue(String newFreq){
		frequencyInputField.setText(newFreq);
	}
	
	public String getFrequencyValue(){
		if(frequencyInputField.getText().equals("")) return "-1";
		return frequencyInputField.getText();
	}
	
	public ArrayList<?> getTableContent(){
		if(attributeViewer!= null)
			return (ArrayList<?>) attributeViewer.getInput();
		else
			return null;
	}
	
	public void setTableContent(ArrayList<?> attList){
		attributeViewer.setInput(attList);
	}
	
	public void setHubHeightValue(String newHubHeight){
		hhInputField.setText(newHubHeight);
	}
	
	public String getHubHeightValue(){
		if(hhInputField.getText().equals("")) return "-1";
		return hhInputField.getText();
	}
	
	public void setEigthyfiveValue(String newEFV){
		eigthyfiveInputField.setText(newEFV);
	}
	
	public String getEigthyfiveValue(){
		if(eigthyfiveInputField.getText().equals("")) return "-1";
		return eigthyfiveInputField.getText();
	}
	
	public void setCutInValue(String newCutInV){
		cutinInputField.setText(newCutInV);
	}
	
	public String getCutInValue(){
		if(cutinInputField.getText().equals("")) return "-1";
		return cutinInputField.getText();
	}
	
	
	
	public String getStringSourceType(){
		if(btnWT.getSelection() & !btnMM.getSelection()) return Integer.toString(WTId);
		if(btnMM.getSelection() & !btnWT.getSelection()) return Integer.toString(MMId);
		else return Integer.toString(-1);
	}
	
	public int getSourceType(){
		if(btnWT.getSelection() & !btnMM.getSelection()) return WTId;
		if(btnMM.getSelection() & !btnWT.getSelection()) return MMId;
		else return -1;
	}
	
	public void setSourceType(int type){
		if(type==MMId){
			btnWT.setSelection(false);
			btnMM.setSelection(true);
			return;
			}
		if(type==WTId){
			btnWT.setSelection(true);
			btnMM.setSelection(false);
			return;
		}
		lowerLeftGroup.setEnabled(btnWT.getSelection());
		lowerRightGroup.setEnabled(btnMM.getSelection());
	}
	
	public void setPowerControl(int pc){
		
		if(pc == PC_ACTIVE){
			btnRActive.setSelection(true);
			btnRPassive.setSelection(false);
			return;
		}
		if(pc == PC_PASSIVE){
			btnRActive.setSelection(false);
			btnRPassive.setSelection(true);
			return;
		}
			btnRActive.setSelection(false);
			btnRPassive.setSelection(false);
	}
	
	public int getPowerControl(){
		if(btnRActive.getSelection()) return PC_ACTIVE;
		if(btnRPassive.getSelection()) return PC_PASSIVE;
		else return -1;
	}
	
	
	@Override
	public void resetView(){
		nameInputField.setText("");
		strInputField.setText("");
		hostInputField.setText("");
		portInputField.setText("");
		btnMM.setSelection(false);
		btnWT.setSelection(false);
		hhInputField.setText("");
		btnRActive.setSelection(false);
		btnRPassive.setSelection(false);
	}
	
	@Override
	public void okPressed(){
		presenter.okPressed();
	}
	
	@Override
	public void cancelPressed(){
		presenter.cancelPressed();
	}
	
	
	class AttributeLabelProvider implements ITableLabelProvider{

		@Override
		public void addListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub
		}

		@Override
		public void dispose() {
			// TODO Auto-generated method stub
		}

		@Override
		public boolean isLabelProperty(Object element, String property) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub
		}

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			//TODO
			Attribute attr = (Attribute) element;
			switch(columnIndex){
			case 0: return attr.getName();
			case 1:	return attr.getAttType().toString();
			}
			return null;
		}
		
	}

	class AttributeContentProvider implements IStructuredContentProvider{

		@Override
		public void dispose() {
			// TODO Auto-generated method stub
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// TODO Auto-generated method stub
		}

		@SuppressWarnings("unchecked")
		@Override
		public Object[] getElements(Object inputElement) {
			return ((ArrayList<Attribute>)inputElement).toArray();
		}
		
	}

	

}