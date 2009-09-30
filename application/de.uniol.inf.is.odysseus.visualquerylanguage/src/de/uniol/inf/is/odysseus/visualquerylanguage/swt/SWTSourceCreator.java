package de.uniol.inf.is.odysseus.visualquerylanguage.swt;

import java.awt.Toolkit;
import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.visualquerylanguage.ISWTTreeChangedListener;
import de.uniol.inf.is.odysseus.visualquerylanguage.ReflectionException;

public class SWTSourceCreator{
	
	private final Logger log = LoggerFactory.getLogger(SWTSourceCreator.class);
	
	private Shell shell;

	public SWTSourceCreator(Shell baseWindow, final IAdvancedExecutor executor, final Collection<ISWTTreeChangedListener> listeners) {
		shell = new Shell(baseWindow, SWT.RESIZE | SWT.CLOSE | SWT.APPLICATION_MODAL);
		shell.setText("Neue Quelle");
		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		shell.setLayout(gl);
		
		final StyledText textArea = new StyledText(shell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		GridData gd = new GridData(GridData.FILL_BOTH);
		textArea.setLayoutData(gd);
		
		Button createSourceButton = new Button(shell, SWT.PUSH);
		createSourceButton.setText("Neue Quelle erstellen");
		createSourceButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					executor.addQuery(textArea.getText(), executor.getSupportedQueryParser().iterator()
							.next());
					for (ISWTTreeChangedListener listener : listeners) {
						listener.treeChanged();
					}
				} catch (PlanManagementException e1) {
					log.error("Error while trying to add a Query. Because of: ");
					e1.printStackTrace();
				} catch (ReflectionException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
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
