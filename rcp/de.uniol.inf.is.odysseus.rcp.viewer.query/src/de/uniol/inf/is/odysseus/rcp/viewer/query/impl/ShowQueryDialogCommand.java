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
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
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
import de.uniol.inf.is.odysseus.rcp.viewer.query.QueryHistory;

public class ShowQueryDialogCommand extends AbstractHandler implements IHandler {

	private static String lastQuery = "";
	private static String lastParser = "";
	private static String lastTransCfg = "";

	private static final String EMPTY_HISTORY = "[Nothing]";
	private static final String NO_PARSER = "No parser available";
	private static final String NO_EXECUTOR = "No executor available";

	private Combo historyCombo;
	private Combo parserCombo;
	private Combo transCfgCombo;

	private Button okButton;
	private Text queryTextField;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell parent = HandlerUtil.getActiveWorkbenchWindow(event).getShell();

		final Shell dialogShell = new Shell(parent);
		dialogShell.setSize(600, 500);
		dialogShell.setText("Fast Query");

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		dialogShell.setLayout(gridLayout);

		final Label queryLabel = new Label(dialogShell, SWT.None);
		queryLabel.setText("Query");
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		queryLabel.setLayoutData(gd);

		historyCombo = new Combo(dialogShell, SWT.BORDER | SWT.READ_ONLY);
		GridData gd2 = new GridData(GridData.FILL_HORIZONTAL);
		gd2.horizontalSpan = 3;
		historyCombo.setLayoutData(gd2);

		queryTextField = new Text(dialogShell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		queryTextField.setText(lastQuery);
		queryTextField.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				updateOkButtonEnabled();
			}
		});

		if (lastQuery.length() > 0)
			queryTextField.selectAll();

		GridData gd1 = new GridData(GridData.FILL_BOTH);
		gd1.horizontalSpan = 3;
		queryTextField.setLayoutData(gd1);

		final Label parserLabel = new Label(dialogShell, SWT.None);
		parserLabel.setText("Parser");

		parserCombo = new Combo(dialogShell, SWT.BORDER | SWT.READ_ONLY);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		parserCombo.setLayoutData(gd);

		final Label transCfgLabel = new Label(dialogShell, SWT.NONE);
		transCfgLabel.setText("TransformationCfg");

		transCfgCombo = new Combo(dialogShell, SWT.BORDER | SWT.READ_ONLY);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		transCfgCombo.setLayoutData(gd);

		okButton = new Button(dialogShell, SWT.PUSH);
		okButton.setText("OK");
		okButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(IHandlerService.class);

				try {

					Map<String, String> map = new HashMap<String, String>();
					map.put(IQueryConstants.PARSER_PARAMETER_ID, parserCombo.getText());
					map.put(IQueryConstants.QUERY_PARAMETER_ID, queryTextField.getText());
					map.put(IQueryConstants.PARAMETER_TRANSFORMATION_CONFIGURATION_NAME_PARAMETER_ID, transCfgCombo.getText());

					ICommandService cS = (ICommandService) PlatformUI.getWorkbench().getService(ICommandService.class);
					Command cmd = cS.getCommand(IQueryConstants.ADD_QUERY_COMMAND_ID);
					ParameterizedCommand parCmd = ParameterizedCommand.generateCommand(cmd, map);
					handlerService.executeCommand(parCmd, null);
					QueryHistory.getInstance().addQuery(parserCombo.getText(), queryTextField.getText());

				} catch (Exception ex) {
					ex.printStackTrace();
				}

				dialogShell.dispose();
			}
		});
		okButton.setLayoutData(new GridData(GridData.CENTER));

		Button cancelButton = new Button(dialogShell, SWT.PUSH);
		cancelButton.setText("Cancel");
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dialogShell.dispose();
			}
		});
		cancelButton.setLayoutData(new GridData(GridData.CENTER));

		Button clearHistoryButton = new Button(dialogShell, SWT.PUSH);
		clearHistoryButton.setText("Clear History");
		clearHistoryButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox box = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.ICON_WARNING | SWT.YES | SWT.NO);
				box.setMessage("Do you really want to clear the query-history?");
				box.setText("Delete history");

				if (box.open() == SWT.YES) {
					QueryHistory.getInstance().clear();
					fillHistoryCombo();
				}
			}
		});

		dialogShell.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				lastParser = parserCombo.getText();
				lastTransCfg = transCfgCombo.getText();
				lastQuery = queryTextField.getText();
			}

		});
		
		historyCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Combo combo = (Combo) e.getSource();
				if (combo.getSelectionIndex() == 0) {
					queryTextField.setText("");
				} else {
					queryTextField.setText(QueryHistory.getInstance().getQuery(combo.getSelectionIndex() - 1));
					parserCombo.setText(QueryHistory.getInstance().getParser(combo.getSelectionIndex() - 1));
				}
			}
		});

		fillParserCombo();
		fillHistoryCombo();
		fillTransformCfgCombo();

		dialogShell.open();
		queryTextField.setFocus();

		return null;
	}

	private void updateOkButtonEnabled() {
		if (parserCombo.getText() == NO_PARSER) {
			okButton.setEnabled(false);
			return;
		}

		if (parserCombo.getText() == NO_EXECUTOR) {
			okButton.setEnabled(false);
			return;
		}

		if (queryTextField.getText().length() == 0) {
			okButton.setEnabled(false);
			return;
		}

		okButton.setEnabled(true);
	}

	private void fillTransformCfgCombo() {
		transCfgCombo.removeAll();
		boolean lastFound = false;
		for (String name : ParameterTransformationConfigurationRegistry.getInstance().getTransformationConfigurationNames()) {
			transCfgCombo.add(name);
			if (name.equals(lastTransCfg))
				lastFound = true;
		}
		if (lastFound)
			transCfgCombo.setText(lastTransCfg);
		else
			transCfgCombo.setText(transCfgCombo.getItem(0));
	}

	private void fillParserCombo() {
		parserCombo.removeAll();
		try {
			IAdvancedExecutor executor = Activator.getExecutor();
			boolean lastFound = false;
			for (String parserID : executor.getSupportedQueryParser()) {
				parserCombo.add(parserID);
				if (parserID.equals(lastParser))
					lastFound = true;
			}
			if (lastFound)
				parserCombo.setText(lastParser);
			else
				parserCombo.select(0);
		} catch (PlanManagementException e1) {
			parserCombo.add(NO_PARSER);
			parserCombo.setEnabled(false);
			parserCombo.select(0);
		} catch (NullPointerException ex) {
			parserCombo.add(NO_EXECUTOR);
			parserCombo.setEnabled(false);
			parserCombo.select(0);
		}
		updateOkButtonEnabled();
	}

	private void fillHistoryCombo() {
		historyCombo.removeAll();
		historyCombo.add(EMPTY_HISTORY);
		for (String historyItem : QueryHistory.getInstance().getQueryHistory()) {
			String oneLineItem = getStringInOneLine(historyItem);
			historyCombo.add(oneLineItem);
		}
		historyCombo.select(0);

	}

	private String getStringInOneLine(String multiLineString) {
		String result = deleteChars(multiLineString, "\n");
		result = deleteChars(result, "\t");
		result = result.trim();

		if (result.length() > 80) {
			result = result.substring(0, 80) + "...";
		}
		return result;
	}

	private String deleteChars(String text, String ch) {
		String result = "";
		int lastPos = 0;
		int pos = text.indexOf(ch, 0);
		if (pos == -1)
			return text;

		while (pos >= 0) {
			result = result + text.substring(lastPos, pos);
			lastPos = pos + 1;
			pos = text.indexOf(ch, lastPos);
		}
		return result;
	}
}
