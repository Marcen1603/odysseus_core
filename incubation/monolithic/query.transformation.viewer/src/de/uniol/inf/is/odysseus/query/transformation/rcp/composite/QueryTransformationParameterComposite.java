package de.uniol.inf.is.odysseus.query.transformation.rcp.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;

import de.uniol.inf.is.odysseus.query.transformation.compiler.TransformationParameter;
import de.uniol.inf.is.odysseus.query.transformation.executor.registry.ExecutorRegistry;
import de.uniol.inf.is.odysseus.query.transformation.rcp.window.QueryTransformationWindow;
import de.uniol.inf.is.odysseus.query.transformation.target.platform.registry.TargetPlatformRegistry;

public class QueryTransformationParameterComposite extends Composite{
	

	
	private Text txtTragetDirectory;
	private Text txtTempDirectory;
	private Text txtFOdysseusCode;
	
	private Button btnChooseTargetDirectory;
	private Button  btnChooseTempDirectory;
	private Combo targetPlatform;
	private Combo comboExecutor;
	
	private Composite inputFieldComposite;
	private Composite inputField2GridComposite;
	private Composite buttonComposite;
	private QueryTransformationWindow window;

	public QueryTransformationParameterComposite(final QueryTransformationWindow window, Composite parent, int style, int windowWidth) {
		super(parent, style);
		this.window = window;
		
		GridData contentGridData = new GridData(GridData.FILL_BOTH);
		contentGridData.horizontalAlignment = SWT.FILL;
		contentGridData.widthHint = windowWidth;
		this.setLayoutData(contentGridData);
		setLayout(new GridLayout(1, false));

		inputFieldComposite = new Composite(this, SWT.FILL);
		inputFieldComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	
		
		inputField2GridComposite = new Composite(this, SWT.FILL);
		inputField2GridComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		createContent();
		
		// Create a horizontal separator
		Label separator = new Label(this, SWT.HORIZONTAL | SWT.SEPARATOR);
		separator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		buttonComposite = new Composite(this, SWT.NONE);
		GridData griDDatabuttonComposite = new GridData(SWT.BEGINNING);
		griDDatabuttonComposite.horizontalAlignment = SWT.FILL;
		buttonComposite.setLayoutData(griDDatabuttonComposite);
		buttonComposite.setLayout(new GridLayout(3, false));
		
		createButton();
		
		parent.pack();
		parent.setVisible(true);
	}
	
	private void createContent() {
		
	 inputFieldComposite.setLayout(new GridLayout(3, false));
	 inputField2GridComposite.setLayout(new GridLayout(2, false));
	 
		
	 Label lblTragetDirectory = new Label(inputFieldComposite, SWT.FILL);
	 lblTragetDirectory.setText("Zielverzeichnis");
		
	 txtTragetDirectory = new Text(inputFieldComposite, SWT.BORDER);
	 txtTragetDirectory.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	 txtTragetDirectory.setText("C:\\Users\\Marc\\Desktop\\target");

	 
	 btnChooseTargetDirectory = new Button(inputFieldComposite, SWT.NONE);
	 btnChooseTargetDirectory.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
	 btnChooseTargetDirectory.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent e) {
	    		
	    		DirectoryDialog dlg = new DirectoryDialog(btnChooseTargetDirectory.getShell(),  SWT.OPEN  );
				dlg.setText("Open");
				String path = dlg.open();
				if (path == null) return;
				txtTragetDirectory.setText(path);
	    	}
	    });
		    
	  btnChooseTargetDirectory.setText("...");
		
	
	  
	  
	  Label lblTempverzeichnis = new Label(inputFieldComposite, SWT.NONE);
	    lblTempverzeichnis.setText("Tempverzeichnis");
	
	    
	    txtTempDirectory = new Text(inputFieldComposite, SWT.BORDER);
	    txtTempDirectory.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	    txtTempDirectory.setText("C:\\Users\\Marc\\Desktop\\tmp");
	
	    
	    
	     btnChooseTempDirectory = new Button(inputFieldComposite, SWT.NONE);
	     btnChooseTempDirectory.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
	    btnChooseTempDirectory.setText("...");
	    
	    
	    btnChooseTempDirectory.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent e) {
	    		
	    		DirectoryDialog dlg = new DirectoryDialog(btnChooseTargetDirectory.getShell(),  SWT.OPEN  );
				dlg.setText("Open");
				String path = dlg.open();
				if (path == null) return;
				txtTempDirectory.setText(path);
	    	}
	    });
	  
	  
	    Label lblOdysseusCode = new Label(inputFieldComposite, SWT.NONE);
	    lblOdysseusCode.setText("Odysseus Code");
	    
	    txtFOdysseusCode = new Text(inputFieldComposite, SWT.BORDER);
	    txtFOdysseusCode.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	    txtFOdysseusCode.setText("F:\\Studium\\odysseus");
	    
	    Button buttonOdysseusCore = new Button(inputFieldComposite, SWT.NONE);
	    buttonOdysseusCore.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
	    buttonOdysseusCore.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent e) {
	    		
	    		DirectoryDialog dlg = new DirectoryDialog(btnChooseTargetDirectory.getShell(),  SWT.OPEN  );
				dlg.setText("Open");
				String path = dlg.open();
				if (path == null) return;
				txtFOdysseusCode.setText(path);
	    	}
	    });
	    buttonOdysseusCore.setText("...");
	
	    Label label = new Label(inputField2GridComposite, SWT.NONE);
	    label.setBounds(5, 130, 45, 15);
	    label.setText("Sprache:");
	    
	    targetPlatform = new Combo(inputField2GridComposite, SWT.DROP_DOWN | SWT.READ_ONLY);
	    targetPlatform.setBounds(99, 127, 68, 23);
	    
	    for(String platform : TargetPlatformRegistry.getAllTargetPlatform()){
	    	 targetPlatform.add(platform.toUpperCase());
	    }
	    targetPlatform.select(1);
	  
	    
	    Label labelExecutor = new Label(inputField2GridComposite, SWT.NONE);
	    labelExecutor.setBounds(5, 130, 45, 15);
	    labelExecutor.setText("Executor:");
	    
	    comboExecutor = new Combo(inputField2GridComposite, SWT.DROP_DOWN | SWT.READ_ONLY);
	    comboExecutor.setBounds(99, 127, 68, 23);
	    
	    for(String executor : ExecutorRegistry.getAllExecutor("Java")){
	    	comboExecutor.add(executor.toUpperCase());
	    }
	    comboExecutor.select(0);
	
	}
	
	
	private void createButton(){
		
		Button oklButton = new Button(buttonComposite, SWT.PUSH);
		oklButton.setText("OK");
		oklButton.setAlignment(SWT.CENTER);
		oklButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		oklButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				TransformationParameter parameter = new TransformationParameter(targetPlatform.getText(),txtTempDirectory.getText(), txtTragetDirectory.getText(), window.getQueryId(), txtFOdysseusCode.getText(), true,comboExecutor.getText());	
				window.startQueryTransformation(parameter);
			}
		});
	
		Button cancelButton = new Button(buttonComposite, SWT.PUSH);
		cancelButton.setText("Cancel");
		cancelButton.setAlignment(SWT.CENTER);
		cancelButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		new Label(buttonComposite, SWT.NONE);
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if(!window.getWindow().isDisposed()){
					window.getWindow().dispose();
				}
			
				
			}
		});
		
		
		
	}
	
	

}
