package windperformancercp.views.performance;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
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
	private ArrayList<?> selectedSources;
	DialogSettings dialogSettings;
	
	
	public QueryWizard(){
		
		queryID = "";
		method = "";
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
	
	public void setMethod(String method){
		this.method = method;
	}
	
	public void setAvailableMethods(Object[] methods){
		this.availableMethods = methods;
		//System.out.println("set availabe methods: "+availableMethods.toString());	
	}
	
	public void setAvailableSources(ArrayList<String> sources){
		this.availableSources = sources;
		//System.out.println("set availabe sources: "+availableSources.toString());	
	}
	
	public void identifierEntered(){
		this.queryID = toqpage.getidInputValue();
		//System.out.println("id entered: "+queryID);
	}
	
	public void methodComboClick(){
		String methText = toqpage.getMethodComboValue();
		if(this.method != methText){
			this.method = methText;
		}

		//System.out.println("method entered: "+method);
	}
	
	public void sourceSelectionClick(String btnData, int[] indexes){
		
	}
	
	public void assignmentClick(){
		
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
	    }

	    public void createControl(Composite parent) {
	    	
	    	Composite container = new Composite(parent, SWT.NULL);
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
				idInputField.addFocusListener(new FocusListener(){
					public void focusGained(FocusEvent fe){}
					public void focusLost(FocusEvent fe){((QueryWizard)getWizard()).identifierEntered();}
				});
			}

	        typeCombo = new Combo(container, SWT.NONE);
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
	        
	        if(typeCombo.getSelectionIndex()!=-1)
	        	setPageComplete(true);
	    }
	    
	    public String getidInputValue(){
	    	return idInputField.getText();
	    }
	    
	    public String getMethodComboValue(){
	    	return typeCombo.getText();
	    }
	    
	    public void update(){
	    	idInputField.setText(queryID);
	    	typeCombo.removeAll();
	    	for(int i = 0; i<availableMethods.length;i++){
					typeCombo.add(availableMethods[i].toString());
			}
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
	    
	    public void update(){
			leftlv.setInput(availableSources);
			rightlv.setInput(selectedSources);
		} 
	}//end SourceSelectPage

	public class AssignAttributePage extends WizardPage {

	    protected AssignAttributePage() {
	        super("AssignAttributePage");
	        setTitle("Assign Attributes");
	        setMessage("Third Step of three: \n"+
	        		"At last assign the sources attributes to the variables of the performance measurement.");
	    }

	    public void createControl(Composite parent) {
	       
	    	Composite container = new Composite(parent, SWT.NULL);
	    	container.setLayoutData(new GridData(GridData.FILL_BOTH));
	    	//Fuer jede der assnattributes eine Combo mit verfuegbaren Sourceattributen
	        container.setLayout(new RowLayout(SWT.VERTICAL));
	       
	        String[] att_labels = {"Timestamp","Windspeed","Power","Pressure","Temperature"};
	        for(int i = 0; i<att_labels.length;i++){ //TODO: automatisch befuellen
	        	Composite attComp = new Composite(container,SWT.NONE);
	        	attComp.setLayoutData(new RowData());
	        	attComp.setLayout(new FillLayout());
				{
					Label attLabel = new Label(attComp, SWT.BORDER);
					attLabel.setText(att_labels[i]);
					Combo attCombo = new Combo(attComp,SWT.NONE);
					//attLabel.setToolTipText("name for human readable identification");
					
				}
	        }
	        
	        setControl(container);
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
