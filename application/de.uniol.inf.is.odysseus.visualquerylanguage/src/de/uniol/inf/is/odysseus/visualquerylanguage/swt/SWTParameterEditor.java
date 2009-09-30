package de.uniol.inf.is.odysseus.visualquerylanguage.swt;

import java.awt.Toolkit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

public class SWTParameterEditor {
	
	private final Logger log = LoggerFactory.getLogger(SWTSourceCreator.class);
	
	private Shell shell;
	
	public SWTParameterEditor(Shell baseWindow, final String type) {
		
		shell = new Shell(baseWindow, SWT.RESIZE | SWT.CLOSE | SWT.APPLICATION_MODAL);
		shell.setText("Neue Quelle");
		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		shell.setLayout(gl);
		
		final StyledText textArea = new StyledText(shell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		GridData gd = new GridData(GridData.FILL_BOTH);
		textArea.setLayoutData(gd);
		textArea.setEditable(false);
		
		final Composite comp = new Composite(shell, SWT.BORDER);
		GridLayout compLayout = new GridLayout();
		compLayout.numColumns = 6;
		comp.setLayout(compLayout);
		
		Button button;
		
		for (String func : SDFExpression.getFunctions()) {
			button = new Button(comp, SWT.PUSH);
			button.setText(func);
			button.addSelectionListener(new SelectionAdapter() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					if(e.getSource() instanceof Button) {
					textArea.setText(textArea.getText() + ((Button)e.getSource()).getText());
					}
				}
				
			});
		}
		
		Button applyButton = new Button(shell, SWT.PUSH);
		applyButton.setText("Parameter hinzufügen");
		applyButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(type.equals("Predicate")) {
					
				}
			}
		});
		
		Button cancelButton = new Button(shell, SWT.PUSH);
		cancelButton.setText("Abbrechen");
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
				shell.dispose();
			}
		});
		
		shell.setSize(400, 300);
		shell.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2 - shell.getSize().x/2, Toolkit.getDefaultToolkit().getScreenSize().height/2 - shell.getSize().y/2);
		shell.open();
}

}
