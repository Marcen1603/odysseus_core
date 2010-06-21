package de.uniol.inf.is.odysseus.rcp.viewer.osgicommands;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.handlers.IHandlerService;

import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.rcp.viewer.osgicommands.activator.Activator;

public class ShowQueryDialogCommand extends AbstractHandler implements IHandler {

	public static final String START_QUERY_PARAMETER_ID = "de.uniol.inf.is.odysseus.rcp.viewer.osgicommands.StartQueryParameter";
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell parent = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
		final String startQuery = event.getParameter(START_QUERY_PARAMETER_ID);
		
		final Shell dialogShell = new Shell(parent);
		dialogShell.setSize(500,300);
		dialogShell.setText("Add Query");
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		dialogShell.setLayout(gridLayout);
		
		final Label queryLabel = new Label(dialogShell, SWT.None );
		queryLabel.setText("Query");

		final Text queryTextField = new Text(dialogShell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		queryTextField.setLayoutData(new GridData(GridData.FILL_BOTH));
		queryTextField.setText(startQuery != null ? startQuery : "");
					
		final Label parserLabel = new Label(dialogShell, SWT.None );
		parserLabel.setText("Parser");
		
		final Combo parserCombo = new Combo( dialogShell, SWT.BORDER );
		parserCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Button okButton = new Button(dialogShell, SWT.PUSH);
		okButton.setText("OK");
		okButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(IHandlerService.class);
				
				try {
					Map<String,String> map = new HashMap<String, String>();
					map.put(AddQueryCommand.PARSER_PARAMETER_ID, parserCombo.getText());
					map.put(AddQueryCommand.QUERY_PARAMETER_ID, queryTextField.getText());

					ICommandService cS = (ICommandService)PlatformUI.getWorkbench().getService(ICommandService.class);
					Command cmd = cS.getCommand(AddQueryCommand.COMMAND_ID);
					ParameterizedCommand parCmd = ParameterizedCommand.generateCommand(cmd, map);
					handlerService.executeCommand(parCmd, null);
					
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

		IAdvancedExecutor executor = Activator.getExecutor();
		try {
			for( String parserID : executor.getSupportedQueryParser() ) 
				parserCombo.add(parserID);
		} catch (PlanManagementException e1) {
			parserCombo.add("No parser available");
			parserCombo.setEnabled(false);
			okButton.setEnabled(false);
		}
		parserCombo.setText(parserCombo.getItem(0));

		dialogShell.open();
		
		return null;
	}

}
