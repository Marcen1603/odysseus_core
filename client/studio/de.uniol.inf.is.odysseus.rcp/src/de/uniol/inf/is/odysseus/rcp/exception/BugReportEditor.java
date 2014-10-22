/*******************************************************************************
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.exception;

import java.io.IOException;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.rcp.config.OdysseusRCPConfiguration;
import de.uniol.inf.is.odysseus.rcp.l10n.OdysseusNLS;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class BugReportEditor extends Window {

	private static final Logger LOG = LoggerFactory.getLogger(BugReportEditor.class);

	private static final String DIALOG_TITLE = "Send bug report";
	private static final int DEBUG_INFO_TEXT_HEIGHT_PIXELS = 150;

	private static final String[] QUESTIONS = new String[] { 
		"If you want a reply please enter a valid e-mail adress", 
		"What led up to the situation?", 
		"What exactly did you do (or not do) that was effective (or ineffective)?", 
		"What was the outcome of this action?", 
		"What outcome did you expect instead?" 
	};

	private static final int[] ANSWER_TEXT_HEIGHTS = new int[] { 20, 75, 75, 75, 75 }; // in pixels

	private StyledText generatedTextArea;
	private StyledText[] questionTexts;
	private String reportText;

	protected BugReportEditor(Shell parentShell, String reportText) {
		super(parentShell);
		this.setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | Window.getDefaultOrientation());

		this.reportText = reportText != null ? reportText : "";
	}

	@Override
	protected void configureShell(Shell newShell) {
		newShell.setText(DIALOG_TITLE);

		super.configureShell(newShell);
	}

	@Override
	protected Control createContents(final Composite parent) {
		Composite composite = new Composite(parent, 0);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.verticalSpacing = 0;

		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		createDialogArea(composite);
		createButtonBar(composite);

		return composite;
	}

	private Control createDialogArea(final Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);

		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		createTextArea(parent);

		return composite;
	}

	private void createTextArea(final Composite parent) {
		createStyledLabel(parent, "Reporter, please consider answering these questions, where appropriate");

		questionTexts = new StyledText[QUESTIONS.length];
		for (int i = 0; i < QUESTIONS.length; i++) {
			createStyledLabel(parent, QUESTIONS[i]);
			questionTexts[i] = createStyledText(parent, ANSWER_TEXT_HEIGHTS[i]);
		}

		createStyledLabel(parent, "\nDebug Information:");
		generatedTextArea = createStyledText(parent, DEBUG_INFO_TEXT_HEIGHT_PIXELS);
		generatedTextArea.setText(reportText);
	}

	private static StyledText createStyledText(Composite parent, int heightHint) {
		StyledText styledText = new StyledText(parent, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		styledText.setEditable(true);

		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		data.widthHint = 600;
		data.heightHint = heightHint;
		styledText.setLayoutData(data);

		return styledText;
	}

	private static Label createStyledLabel(Composite parent, String text) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(text);

		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		data.widthHint = 600;
		label.setLayoutData(data);

		return label;
	}

	private Control createButtonBar(final Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 5;
		layout.makeColumnsEqualWidth = true;
		composite.setLayout(layout);
		composite.setFont(parent.getFont());

		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.VERTICAL_ALIGN_CENTER);
		composite.setLayoutData(data);

		createButtonsForButtonBar(composite);

		return composite;
	}

	private void createButtonsForButtonBar(Composite parent) {
		Button jiraButton = createButton(parent, "Set JIRA account...");
		jiraButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String savedUsername = OdysseusRCPConfiguration.get(BugReport.BUGREPORT_USER, "");
				String savedPassword = OdysseusRCPConfiguration.get(BugReport.BUGREPORT_PASSWORD, "");

				UsernameAndPasswordDialog dlg = new UsernameAndPasswordDialog(getParentShell(), savedUsername, savedPassword);
				if (dlg.open() == Dialog.OK) {
					OdysseusRCPConfiguration.set(BugReport.BUGREPORT_USER, dlg.getUsername());
					OdysseusRCPConfiguration.set(BugReport.BUGREPORT_PASSWORD, dlg.getPassword());
					OdysseusRCPConfiguration.save();
				}
			}
		});

		final Label errorLabel = createEmptyLabel(parent);

		Button cancelButton = createButton(parent, OdysseusNLS.Cancel);
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				setReturnCode(Window.CANCEL);
				close();
			}
		});

		Button sendButton = createButton(parent, OdysseusNLS.SendBugReport);
		sendButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				Button button = (Button) event.getSource();
				button.setText(OdysseusNLS.Sending);
				try {
					String questionsText = createQuestionsText();

					boolean result = BugReport.send(questionsText, generatedTextArea.getText());
					setReturnCode(result ? Window.OK : Window.CANCEL);
					close();
				} catch (IOException e) {
					errorLabel.setText(e.getMessage());
					LOG.error(e.getMessage(), e);
					button.setText(OdysseusNLS.SendBugReport);
				}
			}
		});

		Shell shell = parent.getShell();
		if (shell != null) {
			shell.setDefaultButton(sendButton);
		}
	}

	private static Label createEmptyLabel(final Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setText("");
		GridData errorLabelData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		errorLabelData.horizontalSpan = 2;
		label.setLayoutData(errorLabelData);
		return label;
	}

	private static Button createButton(Composite parent, String title) {
		Button button = new Button(parent, SWT.PUSH);
		button.setText(title);
		button.setFont(JFaceResources.getDialogFont());
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		Point minSize = button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		data.widthHint = minSize.x;
		button.setLayoutData(data);
		return button;
	}
	
	public String getQuestionsAndAnswersText() {
		return createQuestionsText();
	}

	private String createQuestionsText() {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < QUESTIONS.length; i++) {
			sb.append("*** ").append(QUESTIONS[i]).append(" ***\n");

			String answer = questionTexts[i].getText();
			sb.append(Strings.isNullOrEmpty(answer) ? "<no answer>" : answer);
			sb.append("\n\n");
		}

		return sb.toString();
	}

}
