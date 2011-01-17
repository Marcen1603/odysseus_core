package windperformancercp.views.performance;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class QueryWizard extends Wizard {
	
	private String queryID;
	private String method;
	
	private TypeOfQueryPage toqpage;
	private SourceSelectPage sspage;
	private AssignAttributePage aapage;
	
	private Object[] availableMethods;
	private ArrayList<String> availableSources;
	private ArrayList<String> selectedSources;
	private ArrayList<String> neededAssignments;
	private ArrayList<ArrayList<String>> assignmentComboElements; 
	
	DialogSettings dialogSettings;
	
	
	public QueryWizard(){
		
		queryID = "";
		method = "";
		neededAssignments = new ArrayList<String>();
		assignmentComboElements = new ArrayList<ArrayList<String>>();
		selectedSources = new ArrayList<String>();
		availableSources = new ArrayList<String>();
		setWindowTitle("Performance Measurement Wizard");
		
		toqpage = new TypeOfQueryPage();
		sspage = new SourceSelectPage();
		aapage = new AssignAttributePage();
		
		addPage(toqpage);
        addPage(sspage);
        addPage(aapage);
	}
	
	public void setQueryID(String id){
		queryID= id;
	}
	
	public String getQueryID(){
		return this.queryID;
	}
	
	public void setMethod(String method){
		this.method = method;
	}
	
	public String getMethod(){
		return this.method;
	}
	
	public void setAvailableMethods(Object[] methods){
		this.availableMethods = methods;
		//System.out.println("set availabe methods: "+availableMethods.toString());	
	}
	
	public void setAvailableSources(ArrayList<String> sources){
		this.availableSources = sources;
		//System.out.println("set availabe sources: "+availableSources.toString());	
	}
	
	public ArrayList<String> getSelectedSources(){
		return this.selectedSources;
	}
	
	public void setNeededAssignments(ArrayList<String> assigns){
		this.neededAssignments = assigns;
		//System.out.println("set availabe sources: "+availableSources.toString());	
	}
	
	public void setAssignmentComboElements(ArrayList<ArrayList<String>> aCE){
		this.assignmentComboElements = aCE;		
		aapage.createControl(aapage.getControl().getParent());
		//aapage.getControl().getParent().getAccessible();

	}
	
	public void identifierEntered(){
		this.queryID = toqpage.getidInputValue();
		try{getContainer().updateButtons();}
		catch(Exception e){}
		//System.out.println("id entered: "+queryID);
	}
	
	public void methodComboClick(){
		String methText = toqpage.getMethodComboValue();
		if(this.method != methText){
			this.method = methText;
			
			try{getContainer().updateButtons();}
			catch(Exception e){}
		}
		//System.out.println("method entered: "+method);
	}
	
	public void sourceSelectionClick(String btnData, int[] indexes){
		//System.out.println("source selected: left: "+indexes[0]+", right: "+ indexes[1]);
		try{getContainer().updateButtons();}
		catch(Exception e){}
		
		if((btnData.equals("right"))&& (indexes[0]>= 0)&&(indexes[0]< availableSources.size())){
			selectedSources.add(availableSources.get(indexes[0]));
			availableSources.remove(indexes[0]);
		}
		if((btnData.equals("left"))&& (indexes[1]>= 0)&&(indexes[1]< selectedSources.size())){
			availableSources.add(selectedSources.get(indexes[1]));
			selectedSources.remove(indexes[1]);
		}
		sspage.update();
		try{getContainer().updateButtons();}
		catch(Exception e){}
	}
	
	public void assignmentClick(){
		try{getContainer().updateButtons();}
		catch(Exception e){}
	}
	
	@Override
	public boolean performFinish() {
		
		// TODO Auto-generated method stub
		return false;
	}
	
	
	public void update(){
				
		toqpage.update();
		sspage.update();
		aapage.update();
		getContainer().updateButtons();
	}
	

	public class TypeOfQueryPage extends WizardPage {

		Text idInputField;
		Combo typeCombo;
		
	    protected TypeOfQueryPage() {
	        super("TypeOfQueryPage");
	 
	        setTitle("Select Performance Measure Method");
	        setMessage("This wizard will guide you through creation of a new performance measurement.\n" +
	        		"First Step of three: \n" +
	        		"Please select kind of measurement: ");
	        setPageComplete(false);
	    }

	    public void createControl(Composite parent) {
	    	
	    	Composite container = new Composite(parent, SWT.NONE);
	    	container.setLayout(new FormLayout());
	        Composite nameComp = new Composite(container,SWT.NONE);
			{
				FormData nameCompFD = new FormData();
				nameCompFD.top = new FormAttachment(parent,0);
				nameComp.setLayoutData(nameCompFD);
				nameComp.setLayout(new FillLayout());
				Label nameLabel = new Label(nameComp, SWT.BORDER);
				nameLabel.setText("Identifier: ");
				nameLabel.setToolTipText("name for human readable identification");
				idInputField = new Text(nameComp, SWT.SINGLE | SWT.BORDER);
				idInputField.addModifyListener(new ModifyListener(){
				      public void modifyText(ModifyEvent me) {((QueryWizard)getWizard()).identifierEntered();}});
			}

	        typeCombo = new Combo(container, SWT.READ_ONLY);
	        FormData typeComboFD = new FormData();
	        typeComboFD.top = new FormAttachment(nameComp, 5);
	        typeCombo.setLayoutData(typeComboFD);
	        typeCombo.setToolTipText("kind of measurement");
	        
	        for(int i = 0; i<availableMethods.length;i++){
				typeCombo.add(availableMethods[i].toString());
			}
	        
	        typeCombo.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent e) { ((QueryWizard)getWizard()).methodComboClick();}
				public void widgetDefaultSelected(SelectionEvent e) { ((QueryWizard)getWizard()).methodComboClick();}
			});
	        typeCombo.select(0);
			((QueryWizard)getWizard()).methodComboClick();
	        setControl(container);
	    }
	    
	    public String getidInputValue(){
	    	return idInputField.getText();
	    }
	    
	    public String getMethodComboValue(){
	    	return typeCombo.getText();
	    }
	    
	    @Override
	    public boolean canFlipToNextPage(){
	    	   if (getErrorMessage() != null) return false;
	    	   if ((typeCombo.getSelectionIndex()!=-1)&&(!idInputField.getText().equals("")))
	    	        return true;
	    	    return false;
	    }
	    
	    public void update(){
	    	idInputField.setText(queryID);
	    	typeCombo.removeAll();
	    	for(int i = 0; i<availableMethods.length;i++){
					typeCombo.add(availableMethods[i].toString());
			}
	    	typeCombo.select(typeCombo.indexOf(method));
	    }

	}//end toQPage
	
	public class SourceSelectPage extends WizardPage {

        ListViewer leftlv;
        ListViewer rightlv;
        Button btnLeft;
        Button btnRight;
        
	    protected SourceSelectPage() {
	        super("SourceSelectPage");
	        setTitle("Select Sources");
	        setMessage("Second Step of three: \n" +
	        		"Now select the sources that should participate.");
	        setPageComplete(false);
	    }

	    public void createControl(Composite parent) {
	    	
	    	//zwei listen mit pfeilen dazwischen, bei Bestätigung evtl. nachfragen ob nicht verbundene Sourcen jetzt verbunden werden sollen
	    	Composite container = new Composite(parent, SWT.BORDER);
	    	container.setLayoutData(new GridData(GridData.FILL_BOTH));
	    	
	    	container.setLayout(new GridLayout(3,false));

	        leftlv = new ListViewer(container, SWT.NONE);
	        List availableSourcesT = leftlv.getList();
	        availableSourcesT.setLayoutData(new GridData(GridData.FILL_BOTH));
	        leftlv.setContentProvider(new SourcesListContentProvider());

	        ToolBar tb_srcSelect = new ToolBar(container,SWT.BORDER|SWT.VERTICAL);
	        String[] ti_labels = {"->","<-"};
	        String[] ti_data = {"right","left"};
			for(int i =0;i<ti_labels.length;i++){
				ToolItem ti = new ToolItem(tb_srcSelect,SWT.PUSH);
				ti.setText(ti_labels[i]);
				ti.setData(ti_data[i]);
				ti.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) { 
						ToolItem btn = (ToolItem)e.widget;
						((QueryWizard)getWizard()).sourceSelectionClick(
								btn.getData().toString(),
								new int[]{leftlv.getList().getSelectionIndex(),rightlv.getList().getSelectionIndex()});
					}
				});
			}
			GridData gd_tb_selS = new GridData(60,SWT.DEFAULT);
			gd_tb_selS.horizontalAlignment = GridData.BEGINNING;
			tb_srcSelect.setLayoutData(gd_tb_selS);
	        
	        rightlv = new ListViewer(container, SWT.NONE);
	        rightlv.setContentProvider(new SourcesListContentProvider());
	        List selectedSourcesT = rightlv.getList();
	        selectedSourcesT.setLayoutData(new GridData(GridData.FILL_BOTH));
	        
	        setControl(container);
	    }
	    
	    public int getLeftLVSelection(){
	    	return leftlv.getList().getSelectionIndex();
	    }
	    
	    public int getRightLVSelection(){
	    	return rightlv.getList().getSelectionIndex();
	    }
	    
	    @Override
	    public boolean canFlipToNextPage(){
	    	   if (getErrorMessage() != null) return false;
	    	   if (!selectedSources.isEmpty())
	    	        return true;
	    	    return false;
	    }
	    
	    public void update(){
			leftlv.setInput(availableSources);
			rightlv.setInput(selectedSources);
		} 
	}//end SourceSelectPage

	public class AssignAttributePage extends WizardPage {
		
		ArrayList<Combo> comboList = new ArrayList<Combo>(); 
		
	    protected AssignAttributePage() {
	        super("AssignAttributePage");
	        setTitle("Assign Attributes");
	        setMessage("Third Step of three: \n"+	    	
	        		"At last assign the sources attributes to the variables of the performance measurement.");
	        setPageComplete(false);
	    }

	    public void createControl(Composite parent) {
	    	//Fuer jede der assignattributes eine Combo mit verfuegbaren Sourceattributen
	 
	    	Composite container = new Composite(parent, SWT.BORDER);
	    	container.setLayoutData(new GridData());
	        container.setLayout(new RowLayout(SWT.VERTICAL));
	       
	        for(int i = 0; i< neededAssignments.size(); i++){
	        
	        	Composite attComp = new Composite(container,SWT.NONE);
	        	attComp.setLayoutData(new RowData());
	        	attComp.setLayout(new FillLayout());
	        	
	        	{
					Label attLabel = new Label(attComp, SWT.BORDER);
					attLabel.setText(neededAssignments.get(i));
					
	//System.out.println("assign: "+attLabel.getText());
					Combo attCombo = new Combo(attComp,SWT.READ_ONLY);
					attCombo.setItems(assignmentComboElements.get(i).toArray(new String[]{}));
					
		for(String s:assignmentComboElements.get(i).toArray(new String[]{})){
			System.out.println(s);
		}
					comboList.add(attCombo);
	        	}
	        }
	        
	        setControl(container);
	    }
	    
	    @Override
	    public boolean canFlipToNextPage(){
	    	if (getErrorMessage() != null) return false;
	    	for(Combo co: comboList){
	    	   if(co.getSelectionIndex()== -1)
	    		   return false;
	    	}
	    	return true;
	    }
	    
	    public void update(){
	    	
	    }
	}//end AssignAttributePage
	
	class SourcesListContentProvider implements IStructuredContentProvider{

		@Override
		public void dispose() {
			// TODO Auto-generated method stub
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// TODO Auto-generated method stub
		}

		@Override
		public Object[] getElements(Object inputElement) {
			ArrayList<?> sources = (ArrayList<?>) inputElement;
        	return sources.toArray();
        }
		
	}
	
}
