package de.uniol.inf.is.odysseus.rcp.viewer.query.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
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
import de.uniol.inf.is.odysseus.rcp.viewer.query.IQueryConstants;
import de.uniol.inf.is.odysseus.rcp.viewer.query.ParameterTransformationConfigurationRegistry;

public class ShowQueryDialogCommand extends AbstractHandler implements IHandler {
	
	private static String lastQuery = "";
	private static String lastParser = "";
	private static String lastTransCfg = "";
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell parent = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
		
		final Shell dialogShell = new Shell(parent);
		dialogShell.setSize(600,500);
		dialogShell.setText("Add Query");
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		dialogShell.setLayout(gridLayout);
		
		final Label queryLabel = new Label(dialogShell, SWT.None );
		queryLabel.setText("Query");
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		queryLabel.setLayoutData(gd);

		final Text queryTextField = new Text(dialogShell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		queryTextField.setText(lastQuery);
		if(lastQuery.length()>0)
			queryTextField.selectAll();
		
		GridData gd1 = new GridData(GridData.FILL_BOTH);
		gd1.horizontalSpan = 2;
		queryTextField.setLayoutData(gd1);
					
		final Label parserLabel = new Label(dialogShell, SWT.None );
		parserLabel.setText("Parser");
		
		final Combo parserCombo = new Combo( dialogShell, SWT.BORDER );
		parserCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		final Label transCfgLabel = new Label(dialogShell, SWT.NONE);
		transCfgLabel.setText("TransformationCfg");
		
		final Combo transCfgCombo = new Combo( dialogShell, SWT.BORDER );
		transCfgCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Button okButton = new Button(dialogShell, SWT.PUSH);
		okButton.setText("OK");
		okButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(IHandlerService.class);
				
				try {
					Map<String,String> map = new HashMap<String, String>();
					map.put(IQueryConstants.PARSER_PARAMETER_ID, parserCombo.getText());
					map.put(IQueryConstants.QUERY_PARAMETER_ID, queryTextField.getText());
					map.put(IQueryConstants.PARAMETER_TRANSFORMATION_CONFIGURATION_NAME_PARAMETER_ID, transCfgCombo.getText());
					
//					lastParser = parserCombo.getText();
//					lastTransCfg = transCfgCombo.getText();
//					lastQuery = queryTextField.getText();
					
					ICommandService cS = (ICommandService)PlatformUI.getWorkbench().getService(ICommandService.class);
					Command cmd = cS.getCommand(IQueryConstants.ADD_QUERY_COMMAND_ID);
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

		dialogShell.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				lastParser = parserCombo.getText();
				lastTransCfg = transCfgCombo.getText();
				lastQuery = queryTextField.getText();
			}
			
		});
		
		try {
			IAdvancedExecutor executor = Activator.getExecutor();
			boolean lastFound = false;
			for( String parserID : executor.getSupportedQueryParser() ) { 
				parserCombo.add(parserID);
				if( parserID.equals(lastParser)) 
					lastFound = true;
			}
			if(lastFound) 
				parserCombo.setText(lastParser);
			else
				parserCombo.setText(parserCombo.getItem(0));
		} catch (PlanManagementException e1) {
			parserCombo.add("No parser available");
			parserCombo.setEnabled(false);
			okButton.setEnabled(false);
			parserCombo.setText("No parser available");
		} catch (NullPointerException ex ) {
			parserCombo.add("No executor available");
			parserCombo.setEnabled(false);
			okButton.setEnabled(false);
			parserCombo.setText("No executor available");
		}
		
		boolean lastFound = false;
		for( String name : ParameterTransformationConfigurationRegistry.getInstance().getTransformationConfigurationNames()) {
			transCfgCombo.add(name);
			if( name.equals(lastTransCfg)) 
				lastFound = true;
		}
		if( lastFound ) 
			transCfgCombo.setText(lastTransCfg);
		else
			transCfgCombo.setText(transCfgCombo.getItem(0));
			
		dialogShell.open();
		
		return null;
	}

}
