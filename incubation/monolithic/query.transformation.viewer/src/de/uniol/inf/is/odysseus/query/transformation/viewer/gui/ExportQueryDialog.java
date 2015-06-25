package de.uniol.inf.is.odysseus.query.transformation.viewer.gui;

import java.io.IOException;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.uniol.inf.is.odysseus.query.transformation.compiler.TransformationCompiler;
import de.uniol.inf.is.odysseus.query.transformation.compiler.TransformationCompilerParameter;


public class ExportQueryDialog extends Dialog {
	private Text txtTragetDirectory;
	private Button btnChooseTargetDirectory;
	private Text txtTempDirectory;
	private Combo programLanguage;
	
	private int queryId;
	
  public ExportQueryDialog(Shell parentShell, int queryId) {
    super(parentShell);
    this.queryId = queryId;
  }

  @Override
  protected Control createDialogArea(Composite parent) {

    Composite container = (Composite) super.createDialogArea(parent);
    container.setLayout(null);
    
    Label label = new Label(container, SWT.NONE);
    label.setBounds(5, 101, 45, 15);
    label.setText("Sprache:");
    
    programLanguage = new Combo(container, SWT.DROP_DOWN | SWT.READ_ONLY);
    programLanguage.setBounds(99, 98, 68, 23);
    programLanguage.add("Java");
    programLanguage.add("C#");
    programLanguage.add("Python");
    programLanguage.select(0);
    
    txtTragetDirectory = new Text(container, SWT.BORDER);
    txtTragetDirectory.setBounds(99, 22, 261, 21);
    
    Label lblTragetDirectory = new Label(container, SWT.NONE);
    lblTragetDirectory.setBounds(5, 25, 89, 15);
    lblTragetDirectory.setText("Zielverzeichnis");
    
    btnChooseTargetDirectory = new Button(container, SWT.NONE);
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
    
    btnChooseTargetDirectory.setBounds(367, 20, 75, 25);
    btnChooseTargetDirectory.setText("...");
    
    Label lblTempverzeichnis = new Label(container, SWT.NONE);
    lblTempverzeichnis.setText("Tempverzeichnis");
    lblTempverzeichnis.setBounds(5, 62, 89, 15);
    
    txtTempDirectory = new Text(container, SWT.BORDER);
    txtTempDirectory.setBounds(99, 59, 261, 21);
    
    Button btnChooseTempDirectory = new Button(container, SWT.NONE);
    btnChooseTempDirectory.setText("...");
    btnChooseTempDirectory.setBounds(367, 57, 75, 25);
    
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
    
    
    return container;
  }

  @Override
  protected void configureShell(Shell newShell) {
   super.configureShell(newShell);
    newShell.setText("Export Query");
  }

  @Override
  protected Point getInitialSize() {
    return new Point(450, 300);
  }
  
  
  @Override
  protected void okPressed() {
    saveInput();
    super.okPressed();
  }
  
  
  private void saveInput(){
	  TransformationCompilerParameter compilerParameter = new TransformationCompilerParameter(programLanguage.getText(),txtTempDirectory.getText(), txtTragetDirectory.getText(), queryId);
	  try {
		TransformationCompiler.transformQueryToStandaloneSystem(compilerParameter);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
} 