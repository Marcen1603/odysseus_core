package de.uniol.inf.is.odysseus.rcp.editor.script.blocks;

import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.script.IVisualOdysseusScriptBlock;
import de.uniol.inf.is.odysseus.rcp.editor.script.IVisualOdysseusScriptContainer;
import de.uniol.inf.is.odysseus.rcp.editor.script.VisualOdysseusScriptException;
import de.uniol.inf.is.odysseus.rcp.editor.script.service.ServicesBinder;

public class QueryVisualOdysseusScriptBlock implements IVisualOdysseusScriptBlock {

	private String queryText;
	private String parser;
	private String queryName;
	private boolean startQuery;

	private Text editingText;

	public QueryVisualOdysseusScriptBlock(String queryText, String parser, String queryName, boolean running) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(queryText), "queryText must not be null or empty!");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(parser), "parser must not be null or empty!");

		this.queryText = queryText;
		this.parser = parser;
		this.queryName = queryName;
		this.startQuery = running;
	}
	
	@Override
	public String getTitle() {
		return "Query";
	}

	@Override
	public void createPartControl(Composite parent, IVisualOdysseusScriptContainer container) {
		Composite topCompsite = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.verticalSpacing = 0;
		topCompsite.setLayout(gridLayout);
		
		Composite settingsComposite = new Composite(topCompsite, SWT.NONE);
		settingsComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		settingsComposite.setLayout(new GridLayout(3, false));

		// Parser DropDown
		Composite parserComposite = new Composite(settingsComposite, SWT.NONE);
		parserComposite.setLayout(new GridLayout(2, false));

		Label parserLabel = new Label(parserComposite, SWT.NONE);
		parserLabel.setText("Parser");

		Combo parserCombo = new Combo(parserComposite, SWT.DROP_DOWN);
		parserCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Set<String> parserNames = ServicesBinder.getServerExecutor().getSupportedQueryParsers(OdysseusRCPPlugIn.getActiveSession());
		parserCombo.setItems(parserNames.toArray(new String[0]));
		parserCombo.setText(parser);
		parserCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String selectedParser = parserCombo.getItem(parserCombo.getSelectionIndex());
				if (!selectedParser.equalsIgnoreCase(parser)) {
					parser = selectedParser;
					container.setDirty(true);
				}
			}
		});

		// QueryName Text
		Composite queryNameComposite = new Composite(settingsComposite, SWT.NONE);
		queryNameComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		queryNameComposite.setLayout(new GridLayout(2, false));

		Label queryNameLabel = new Label(queryNameComposite, SWT.NONE);
		queryNameLabel.setText("Name");

		Text queryNameText = new Text(queryNameComposite, SWT.BORDER);
		queryNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		if( queryName != null ) {
			queryNameText.setText(queryName);
		}
		queryNameText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!queryNameText.getText().equals(queryName)) {
					queryName = queryNameText.getText();
					container.setDirty(true);
				}
			}
		});

		// Running CheckBox
		Button runningCheck = new Button(settingsComposite, SWT.CHECK);
		runningCheck.setText("Start immediately");
		runningCheck.setSelection(startQuery);
		runningCheck.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				startQuery = runningCheck.getSelection();
				container.setDirty(true);
			}
		});

		editingText = new Text(topCompsite, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		editingText.setText(queryText);
		editingText.setLayoutData( new GridData(GridData.FILL_HORIZONTAL));
		editingText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				container.layoutAll();

				container.setDirty(true);
			}
		});
	}

	@Override
	public void dispose() {
		// nothing to do
	}

	@Override
	public String generateOdysseusScript() throws VisualOdysseusScriptException {
		StringBuilder scriptBuilder = new StringBuilder();
		
		scriptBuilder.append("#PARSER ").append(parser).append("\n");
		if( !Strings.isNullOrEmpty(queryName)) {
			scriptBuilder.append("#QNAME ").append(queryName).append("\n");
		}
		if( startQuery ) {
			scriptBuilder.append("#RUNQUERY\n");
		} else {
			scriptBuilder.append("#ADDQUERY\n");
		}
		scriptBuilder.append(queryText);
		
		return scriptBuilder.toString();
	}

}
