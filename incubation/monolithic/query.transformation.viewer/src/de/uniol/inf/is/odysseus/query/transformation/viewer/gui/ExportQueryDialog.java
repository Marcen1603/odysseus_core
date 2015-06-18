package de.uniol.inf.is.odysseus.query.transformation.viewer.gui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ExportQueryDialog extends Dialog {

  public ExportQueryDialog(Shell parentShell) {
    super(parentShell);
  }

  @Override
  protected Control createDialogArea(Composite parent) {
	
    Composite container = (Composite) super.createDialogArea(parent);
    
    Text myText = new Text( container, SWT.MULTI | SWT.READ_ONLY | SWT.WRAP );
    myText.setText("hier werden spätere Parameter abgefragt wie Sprache, Speicherort, Tempverzeichnis etc.");
    myText.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ));
    
    Button button = new Button(container, SWT.PUSH);
    button.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false,
        false));
    button.setText("Press me");
    button.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        System.out.println("Pressed");
      }
    });
    
    Label label = new Label(container, SWT.NONE);
    label.setText("Sprache:");
    
    Combo comboDropDown = new Combo(container, SWT.DROP_DOWN | SWT.BORDER);
    comboDropDown.add("Java");
    comboDropDown.add("C#");
    comboDropDown.add("Python");
    
    
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

} 