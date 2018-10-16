package de.uniol.inf.is.odysseus.rcp.editor.script.blocks;

import java.util.List;

import org.eclipse.jface.resource.JFaceResources;
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
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.script.IVisualOdysseusScriptBlock;
import de.uniol.inf.is.odysseus.rcp.editor.script.IVisualOdysseusScriptContainer;
import de.uniol.inf.is.odysseus.rcp.editor.script.VisualOdysseusScriptException;
import de.uniol.inf.is.odysseus.rcp.editor.script.impl.VisualOdysseusScriptPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.script.service.ServicesBinder;
import de.uniol.inf.is.odysseus.rcp.exception.ExceptionWindow;
import de.uniol.inf.is.odysseus.rcp.util.ImageButton;

public class QueryVisualOdysseusScriptBlock implements IVisualOdysseusScriptBlock {

	private static final Logger LOG = LoggerFactory.getLogger(QueryVisualOdysseusScriptBlock.class);

	private String queryText;
	private String parser;
	private String queryName;
	private boolean startQuery;

	private Text editingText;
	private IEditorPart editor;

	public QueryVisualOdysseusScriptBlock(String queryText, String parser, String queryName, boolean running) {
		// Preconditions.checkArgument(!Strings.isNullOrEmpty(queryText), "queryText must not be null or empty!");
		// Preconditions.checkArgument(!Strings.isNullOrEmpty(parser), "parser must not be null or empty!");

		this.queryText = queryText;
		this.parser = parser;
		this.queryName = queryName;
		this.startQuery = running;
	}

	@Override
	public String getTitle() {
		if (Strings.isNullOrEmpty(queryName)) {
			return "Query";
		}

		return "Query '" + queryName + "'";
	}

	@Override
	public void createPartControl(Composite parent, IVisualOdysseusScriptContainer container) {
		Composite topCompsite = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.verticalSpacing = 0;
		topCompsite.setLayout(gridLayout);

		Composite settingsComposite = new Composite(topCompsite, SWT.NONE);
		settingsComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		settingsComposite.setLayout(new GridLayout(4, false));

		ImageButton editButton = new ImageButton(settingsComposite, VisualOdysseusScriptPlugIn.getImageManager().get("edit"), "Open in editor");
		editButton.getButton().setLayoutData(new GridData());
		editButton.getButton().addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				if( editor != null ) {
					activePage.closeEditor(editor, false);
					editor = null;
				}
				
				// from:
				// http://blog.eclipse-tips.com/2008/06/opening-editor-without-ifile.html
				try {
					StringEditorInput stringInput = new StringEditorInput(queryText, queryName, getParserExtension(parser), container.getFile());
					stringInput.addListener(new IStringEditorInputChangeListener() {
						@Override
						public void stringChanged(StringEditorInput sender, String from, String to) {
							if( !editingText.isDisposed() ) {
								editingText.setText(to);
								queryText = to;
							}
						}
					});

					editor = activePage.openEditor(stringInput, "de.uniol.inf.is.odysseus.rcp.editor.OdysseusScriptEditor");

				} catch (PartInitException e1) {
					LOG.error("Could not open editor", e1);

					new ExceptionWindow("Could not open editor", e1);
				}
			}

		});

		// Parser DropDown
		Composite parserComposite = new Composite(settingsComposite, SWT.NONE);
		parserComposite.setLayout(new GridLayout(2, false));

		Label parserLabel = new Label(parserComposite, SWT.NONE);
		parserLabel.setText("Parser");

		Combo parserCombo = new Combo(parserComposite, SWT.DROP_DOWN);
		parserCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		List<String> parserNames = Lists.newArrayList(ServicesBinder.getExecutor().getSupportedQueryParsers(OdysseusRCPPlugIn.getActiveSession()));
		parserNames.remove("OdysseusScript");
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
		if (queryName != null) {
			queryNameText.setText(queryName);
		}
		queryNameText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!queryNameText.getText().equals(queryName)) {
					queryName = queryNameText.getText();

					container.setDirty(true);
					container.setTitleText(getTitle());
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
		editingText.setFont(JFaceResources.getTextFont());
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.minimumHeight = 150;
		editingText.setLayoutData(gd);
		editingText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				queryText = editingText.getText();

				container.layoutAll();

				container.setDirty(true);
			}
		});
	}

	@Override
	public void dispose() {
		IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		if( editor != null ) {
			activePage.closeEditor(editor, false);
			editor = null;
		}
	}

	@Override
	public String generateOdysseusScript() throws VisualOdysseusScriptException {
		StringBuilder scriptBuilder = new StringBuilder();

		scriptBuilder.append("#PARSER ").append(parser).append("\n");
		if (!Strings.isNullOrEmpty(queryName)) {
			scriptBuilder.append("#QNAME ").append(queryName).append("\n");
		}
		if (startQuery) {
			scriptBuilder.append("#RUNQUERY\n");
		} else {
			scriptBuilder.append("#ADDQUERY\n");
		}
		scriptBuilder.append(queryText).append("\n");

		return scriptBuilder.toString();
	}

	private static String getParserExtension(String parser) {
		switch (parser.trim()) {
		case "PQL":
			return "pql";
		case "CQL":
			return "cql";
		default:
			throw new RuntimeException("Could not determine file extension for parser " + parser);
		}
	}

}
