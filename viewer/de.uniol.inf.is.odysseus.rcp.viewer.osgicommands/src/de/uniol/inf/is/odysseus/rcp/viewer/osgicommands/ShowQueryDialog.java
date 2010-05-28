package de.uniol.inf.is.odysseus.rcp.viewer.osgicommands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.handlers.IHandlerService;

public class ShowQueryDialog extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell parent = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
		
		final Shell dialogShell = new Shell(parent);
		dialogShell.setSize(500,120);
		dialogShell.setText("Add Query");
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		dialogShell.setLayout(gridLayout);
		
		final Text textField = new Text(dialogShell, SWT.BORDER);
		GridData data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 2;
		textField.setLayoutData(data);
		textField.setText("SELECT b.auction, DolToEur(b.price) AS euroPrice, b.bidder, b.datetime FROM nexmark:bid2 [UNBOUNDED] AS b");
					
		Button okButton = new Button(dialogShell, SWT.PUSH);
		okButton.setText("OK");
		okButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String query = textField.getText();
				IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(IHandlerService.class);
				
				try {
					// BAD HACK
					AddQuery.queryToExecute = query;
					handlerService.executeCommand(AddQuery.COMMAND_ID, null);
				} catch( Exception ex ) {
					ex.printStackTrace();
				}
				
				dialogShell.dispose();
			}			
		});
		okButton.setLayoutData(new GridData(GridData.CENTER));
		
		Button cancelButton = new Button(dialogShell, SWT.PUSH);
		cancelButton.setText("Abbrechen");
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dialogShell.dispose();
			}
		});
		cancelButton.setLayoutData(new GridData(GridData.CENTER));
		
		dialogShell.open();
		
		return null;
	}

}
