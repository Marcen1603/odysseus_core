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
import java.util.List;
import java.util.Map;

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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.rcp.config.OdysseusRCPConfiguration;
import de.uniol.inf.is.odysseus.rcp.l10n.OdysseusNLS;
import de.uniol.inf.is.odysseus.report.IReport;

public class BugReportEditor extends Window {

	private static final String ODYSSEUS_BUG_REPORT_DEFAULT_TITLE = "Odysseus Bug Report";

	private static final Logger LOG = LoggerFactory.getLogger(BugReportEditor.class);
	
	private static final String NO_REPORT_AVAILABLE_TEXT = "<no report available>";
	private static final String NO_EXCEPTION_TEXT = "<no exception>";
	
	private static final String DIALOG_TITLE = "Send bug report";
	private static final int DEBUG_INFO_TEXT_HEIGHT_PIXELS = 150;

	private static final String[] QUESTIONS = new String[] { 
		"Title of your report",
		"If you want a reply please enter a valid e-mail adress", 
		"What led up to the situation?", 
		"What exactly did you do (or not do) that was effective (or ineffective)?", 
		"What was the outcome of this action?", 
		"What outcome did you expect instead?" 
	};

	private static final int[] ANSWER_TEXT_HEIGHTS = new int[] { 20, 20, 75, 75, 75, 75 }; // in pixels

	private final IReport baseReport;
	private final List<StyledText> reportTextList = Lists.newArrayList();

	private StyledText[] questionTexts;

	protected BugReportEditor(Shell parentShell, IReport report) {
		super(parentShell);
		Preconditions.checkNotNull(report, "Report must not be null!");
		
		setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | Window.getDefaultOrientation());

		baseReport = report;
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

	private void createTextArea(Composite parent) {
		createStyledLabel(parent, "Reporter, please consider answering these questions, where appropriate");

		questionTexts = new StyledText[QUESTIONS.length];
		for (int i = 0; i < QUESTIONS.length; i++) {
			createStyledLabel(parent, QUESTIONS[i]);
			questionTexts[i] = createStyledText(parent, ANSWER_TEXT_HEIGHTS[i]);
		}

		createStyledLabel(parent, "Report (can be modified, if needed):");
		Label label = createStyledLabel(parent, "Caution: Please be aware that this report may contain private or other confidential information! Change details if necessary!");
		label.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		
		reportTextList.addAll(createReportTabs(parent, baseReport));
	}

	private static List<StyledText> createReportTabs(Composite parent, IReport report) {
		List<StyledText> texts = Lists.newArrayList();
		
		TabFolder tabFolder = new TabFolder(parent, SWT.BORDER);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		tabFolder.setLayoutData(data);
		
		Optional<Throwable> optException = report.getException();
		texts.add(createReportTab(tabFolder, "Exception", optException.isPresent() ? getStackTraceReport(optException.get()) : NO_EXCEPTION_TEXT));
				
		for( String reportTitle : report.getTitles() ) {
			
			Optional<String> optReportText = report.getReportText(reportTitle);
			String reportText = optReportText.isPresent() ? optReportText.get() : NO_REPORT_AVAILABLE_TEXT;
			
			texts.add(createReportTab(tabFolder, reportTitle, reportText));
		}
		
		return texts;
	}
	
	private static String getStackTraceReport(Throwable e) {
		final StringBuilder report = new StringBuilder();
		report.append("Message:\n").append(e.getMessage()).append("\n\n");
		final StackTraceElement[] stack = e.getStackTrace();
		for (final StackTraceElement s : stack) {
			report.append("\tat ");
			report.append(s);
			report.append("\n");
		}
		report.append("\n");
		// cause
		Throwable throwable = e.getCause();
		while (throwable != null) {
			final StackTraceElement[] trace = throwable.getStackTrace();
			int m = trace.length - 1, n = stack.length - 1;
			while ((m >= 0) && (n >= 0) && trace[m].equals(stack[n])) {
				m--;
				n--;
			}
			final int framesInCommon = trace.length - 1 - m;

			report.append("Caused by: ").append(throwable.getClass().getSimpleName()).append(" - ").append(throwable.getMessage());
			report.append("\n\n");
			for (int i = 0; i <= m; i++) {
				report.append("\tat ").append(trace[i]).append("\n");
			}
			if (framesInCommon != 0) {
				report.append("\t... ").append(framesInCommon).append(" more\n");
			}
			throwable = throwable.getCause();
		}
		return report.toString();
	}

	private static StyledText createReportTab(TabFolder tabFolder, String title, String reportText) {
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText(title);
		
		Composite tabComposite = new Composite(tabFolder, SWT.NONE);
		tabItem.setControl(tabComposite);
		
		tabComposite.setLayout(new GridLayout(1, false));
		tabComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		StyledText text = createStyledText(tabComposite, DEBUG_INFO_TEXT_HEIGHT_PIXELS);
		text.setText(reportText);
		
		return text;
	}

	private static StyledText createStyledText(Composite parent, int heightHint) {
		StyledText styledText = new StyledText(parent, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		styledText.setEditable(true);

		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.heightHint = heightHint;
		data.widthHint = 600;
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
					String title = determineTitle();
					String questionsText = createQuestionsText();

					Map<String, String> reportMap = createReportMap();
					boolean result = BugReport.send(title, questionsText, reportMap);
					
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
	
	private Map<String, String> createReportMap() {
		
		Map<String, String> result = Maps.newHashMap();
		result.put("Exception", reportTextList.get(0).getText());		
		
		int index = 1;
		for( String title : baseReport.getTitles() ) {
			result.put(title, reportTextList.get(index).getText());
			index++;
		}
		
		return result;
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

	private String determineTitle() {
		String title = questionTexts[0].getText();
		if( Strings.isNullOrEmpty(title)) {
			title = ODYSSEUS_BUG_REPORT_DEFAULT_TITLE;
		}
		return title;
	}

	private String createQuestionsText() {
		StringBuilder sb = new StringBuilder();

		// index 0 contains title --> need special behaviour
		for (int i = 1; i < QUESTIONS.length; i++) {
			sb.append("*** ").append(QUESTIONS[i]).append(" ***\n");

			String answer = questionTexts[i].getText();
			sb.append(Strings.isNullOrEmpty(answer) ? "<no answer>" : answer);
			sb.append("\n\n");
		}

		return sb.toString();
	}

}
