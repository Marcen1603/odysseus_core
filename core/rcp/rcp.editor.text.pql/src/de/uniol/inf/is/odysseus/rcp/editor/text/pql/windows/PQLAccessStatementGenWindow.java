/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.uniol.inf.is.odysseus.rcp.editor.text.pql.windows;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.ProtocolHandlerRegistry;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.TransportHandlerRegistry;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.WrapperRegistry;

public class PQLAccessStatementGenWindow {

	private static final String TITLE = "Create PQL-ACCESS Statement";
	private static final int WINDOW_WIDTH = 500;
	private static final int WINDOW_HEIGHT = 500;
	private static final String CLOSE_BUTTON_TEXT = "Close";
	private static final String NONE_TEXT = "-- none --";

	private final Shell parent;
	private Shell window;

	private Text sourceText;
	private Combo wrapperCombo;
	private Text dateFormatText;
	private Combo transportHandlerCombo;
	private Combo protocolHandlerCombo;
	private Combo dataHandlerCombo;
	private OutputSchemaTableViewer outputSchemaTableViewer;
	private InputSchemaTableViewer inputSchemaTableViewer;
	private OptionsTableViewer optionsTableViewer;

	public PQLAccessStatementGenWindow(Shell parent) {
		this.parent = Preconditions.checkNotNull(parent, "Parent shell must not be null!");
	}

	public void show() {
		if (window == null) {
			window = createWindow(parent);
		}

		window.setVisible(true);
	}

	private Shell createWindow(Shell parent) {
		Shell window = new Shell(parent, SWT.DIALOG_TRIM | SWT.RESIZE);
		window.setText(TITLE);
		window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

		window.setLayout(new GridLayout());

		Composite contentComposite = new Composite(window, SWT.NONE);
		GridData contentGridData = new GridData(GridData.FILL_BOTH);
		contentGridData.widthHint = 600;
		contentComposite.setLayoutData(contentGridData);
		insertContent(contentComposite);

		Composite buttonComposite = new Composite(window, SWT.NONE);
		buttonComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		insertButtons(buttonComposite, window);

		window.pack();
		return window;
	}

	private void insertButtons(Composite buttonComposite, final Shell closingWindow) {
		buttonComposite.setLayout(new GridLayout());
		Button closeButton = new Button(buttonComposite, SWT.PUSH);
		closeButton.setText(CLOSE_BUTTON_TEXT);
		closeButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		closeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!closingWindow.isDisposed()) {
					closingWindow.dispose();
				}
			}
		});
	}

	private void insertContent(Composite contentComposite) {
		contentComposite.setLayout(new GridLayout());

		TabFolder tabFolder = new TabFolder(contentComposite, SWT.NONE);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
		tabFolder.setLayout(new GridLayout());

		Composite generalTab = createTabComposite(tabFolder, "General");
		Composite outputSchemaTab = createTabComposite(tabFolder, "Output Schema");
		Composite inputSchemaTab = createTabComposite(tabFolder, "InputSchema");
		Composite optionsTab = createTabComposite(tabFolder, "Options");
		Composite statementTab = createTabComposite(tabFolder, "Statement");

		insertGeneralContent(generalTab);
		insertOutputSchemaContent(outputSchemaTab);
		insertInputSchemaContent(inputSchemaTab);
		insertOptionsContent(optionsTab);
		insertStatementContent(statementTab);
	}

	private void insertGeneralContent(Composite generalComposite) {
		generalComposite.setLayout(new GridLayout(2, true));

		createLabel(generalComposite, "Source");

		sourceText = new Text(generalComposite, SWT.BORDER);
		sourceText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		createLabel(generalComposite, "Wrapper");

		wrapperCombo = new Combo(generalComposite, SWT.BORDER);
		wrapperCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		wrapperCombo.setItems(determineWrapperNameList());
		wrapperCombo.select(0);
		

		createLabel(generalComposite, "DateFormat");
		dateFormatText = new Text(generalComposite, SWT.BORDER);
		dateFormatText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		createLabel(generalComposite, "Transport Handler");
		transportHandlerCombo = createCombo(generalComposite, TransportHandlerRegistry.getHandlerNames());

		createLabel(generalComposite, "Protocol Handler");
		protocolHandlerCombo = createCombo(generalComposite, ProtocolHandlerRegistry.getHandlerNames());

		createLabel(generalComposite, "Data Handler");
		dataHandlerCombo = createCombo(generalComposite, DataHandlerRegistry.getHandlerNames());
	}

	private void insertOutputSchemaContent(Composite outputSchemaComposite) {
		outputSchemaTableViewer = new OutputSchemaTableViewer(outputSchemaComposite);
	}

	private void insertInputSchemaContent(Composite inputSchemaComposite) {
		inputSchemaTableViewer = new InputSchemaTableViewer(inputSchemaComposite);
	}

	private void insertOptionsContent(Composite optionsTab) {
		optionsTableViewer = new OptionsTableViewer(optionsTab);
	}

	private void insertStatementContent(Composite composite) {
		composite.setLayout(new GridLayout());

		final Text statementText = new Text(composite, SWT.BORDER | SWT.MULTI | SWT.WRAP);
		statementText.setLayoutData(new GridData(GridData.FILL_BOTH));
		statementText.setEditable(false);

		Button generateButton = createButton(composite, "Generate");
		generateButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				statementText.setText(generateStatement());
			}
		});
	}

	private String generateStatement() {
		StringBuilder sb = new StringBuilder();

		if (hasTextText(sourceText)) {
			sb.append(sourceText.getText().toLowerCase()).append(" = ");
		}
		sb.append("ACCESS({").append("\n");		
		sb.append("\tsource='").append(sourceText.getText()).append("'");
		
		appendTextText(sb, dateFormatText, "dateformat");
		
		appendComboText(sb, wrapperCombo, "wrapper");
		appendComboText(sb, transportHandlerCombo, "transport");
		appendComboText(sb, protocolHandlerCombo, "protocol");
		appendComboText(sb, dataHandlerCombo, "dataHandler");

		List<AttributeTypePair> attributes = outputSchemaTableViewer.getData();
		if (!attributes.isEmpty()) {
			sb.append(",\n\tschema=[\n");

			for (int i = 0; i < attributes.size(); i++) {
				AttributeTypePair attribute = attributes.get(i);
				sb.append("\t\t['").append(attribute.getAttributeName()).append("', '").append(outputSchemaTableViewer.getDataType(attribute.getTypeIndex())).append("']");

				if (i < attributes.size() - 1) {
					sb.append(",\n");
				} else {
					sb.append("\n");
				}
			}

			sb.append("\t]");
		}

		List<String> inputSchemaTypes = inputSchemaTableViewer.getData();
		if (!inputSchemaTypes.isEmpty()) {
			sb.append(",\n\tinputschema=[");
			for (int i = 0; i < inputSchemaTypes.size(); i++) {
				String type = inputSchemaTypes.get(i);
				sb.append("'").append(type).append("'");
				if (i < inputSchemaTypes.size() - 1) {
					sb.append(",");
				}
			}
			sb.append("\t]");
		}

		List<KeyValuePair> options = optionsTableViewer.getData();
		if (!options.isEmpty()) {
			sb.append(",\n\toptions=[\n");

			for (int i = 0; i < options.size(); i++) {
				KeyValuePair option = options.get(i);
				sb.append("\t\t['").append(option.getKey()).append("', '").append(option.getValue()).append("']");

				if (i < options.size() - 1) {
					sb.append(",\n");
				} else {
					sb.append("\n");
				}
			}

			sb.append("\t]");
		}

		sb.append("\n})");

		return sb.toString();
	}

	private static void appendTextText(StringBuilder sb, Text textControl, String string) {
		if (hasTextText(textControl)) {
			sb.append(",\n");
			sb.append("\t").append(string).append("='");
			sb.append(textControl.getText());
			sb.append("'");
		}
	}

	private static void appendComboText(StringBuilder sb, Combo combo, String string) {
		if( hasComboText(combo)) {
			sb.append(",\n");
			sb.append("\t").append(string).append("='");
			sb.append(getComboSelection(combo));
			sb.append("'");
		}
	}

	private static Label createLabel(Composite generalComposite, String string) {
		Label label = new Label(generalComposite, SWT.NONE);
		label.setText(string);
		return label;
	}

	private static Combo createCombo(Composite composite, List<String> items) {
		Combo c = new Combo(composite, SWT.BORDER | SWT.READ_ONLY);
		c.setItems(items.toArray(new String[items.size()]));
		c.add(NONE_TEXT);
		c.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		c.select(0);
		return c;
	}

	private static Button createButton(Composite composite, String title) {
		Button b = new Button(composite, SWT.PUSH);
		b.setText(title);
		return b;
	}

	private static Composite createTabComposite(TabFolder tabFolder, String title) {
		TabItem presentationTab = new TabItem(tabFolder, SWT.NULL);
		presentationTab.setText(title);

		Composite presentationTabComposite = new Composite(tabFolder, SWT.NONE);
		presentationTabComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		presentationTabComposite.setLayout(new GridLayout());

		presentationTab.setControl(presentationTabComposite);

		return presentationTabComposite;
	}

	private static String[] determineWrapperNameList() {
		return WrapperRegistry.getWrapperNames().toArray(new String[0]);
	}

	private static String getComboSelection(Combo c) {
		return c.getItems()[c.getSelectionIndex()];
	}

	private static boolean hasTextText(Text textControl) {
		return textControl != null && textControl.getText() != null && !textControl.getText().trim().isEmpty();
	}
	
	private static boolean hasComboText( Combo combo) {
		return combo != null && !combo.getItems()[combo.getSelectionIndex()].equals(NONE_TEXT);
	}
}
