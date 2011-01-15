package windperformancercp.views.performance;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
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
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class QueryWizard extends Wizard implements INewWizard{
	
	
	
	public QueryWizard(){
		setWindowTitle("Performance Measurement Wizard");
		addPage(new TypeOfQueryPage());
        addPage(new SourceSelectPage());
        addPage(new AssignAttributePage());

	}

	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub
	}	

	public class TypeOfQueryPage extends WizardPage {

		Text nameInputField;
		
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
				nameInputField = new Text(nameComp, SWT.SINGLE | SWT.BORDER);
				nameInputField.addFocusListener(new FocusListener(){
					public void focusGained(FocusEvent fe){}
					public void focusLost(FocusEvent fe){}
				});
			}

	        Combo typeCombo = new Combo(container, SWT.NONE);
	        FormData typeComboFD = new FormData();
	        typeComboFD.top = new FormAttachment(nameComp, 5);
	        typeCombo.setLayoutData(typeComboFD);
	        typeCombo.setToolTipText("kind of measurement");
	        typeCombo.add("IEC");
	        typeCombo.add("Langevin");
	        typeCombo.select(0);
	        //TODO: automatisch befuellen lassen
	        setControl(container);
	        
	        if(typeCombo.getSelectionIndex()!=-1)
	        	setPageComplete(true);
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
	       
	    	Composite container = new Composite(parent, SWT.BORDER);
	    	container.setLayoutData(new GridData(GridData.FILL_BOTH));
	    	
	    	container.setLayout(new GridLayout(3,false));

	        leftlv = new ListViewer(container, SWT.NONE);
	        List availableSourcesT = leftlv.getList();
	        availableSourcesT.setLayoutData(new GridData(GridData.FILL_BOTH));

	        ToolBar tb_srcSelect = new ToolBar(container,SWT.BORDER|SWT.VERTICAL);
	        String[] ti_labels = {"->","<-"};
			for(int i =0;i<ti_labels.length;i++){
				ToolItem ti = new ToolItem(tb_srcSelect,SWT.PUSH);
				ti.setText(ti_labels[i]);
			}
			GridData gd_tb_selS = new GridData(60,SWT.DEFAULT);
			gd_tb_selS.horizontalAlignment = GridData.BEGINNING;
			tb_srcSelect.setLayoutData(gd_tb_selS);
	        
	        rightlv = new ListViewer(container, SWT.NONE);
	        List selectedSourcesT = rightlv.getList();
	        selectedSourcesT.setLayoutData(new GridData(GridData.FILL_BOTH));
	        
	        setControl(container);
	      
	        //zwei listen mit pfeilen dazwischen, bei Bestätigung evtl. nachfragen ob nicht verbundene Sourcen jetzt verbunden werden sollen
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
	}//end AssignAttributePage
	
}
