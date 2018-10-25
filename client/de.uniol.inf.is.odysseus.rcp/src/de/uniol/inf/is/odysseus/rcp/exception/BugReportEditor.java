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
import java.util.Objects;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.bugreport.BugreportSender;
import de.uniol.inf.is.odysseus.rcp.config.OdysseusRCPConfiguration;
import de.uniol.inf.is.odysseus.rcp.l10n.OdysseusNLS;
import de.uniol.inf.is.odysseus.report.IReport;

public class BugReportEditor extends TitleAreaDialog {

	public static final String BUGREPORT_PASSWORD = "password";
	public static final String BUGREPORT_USER = "user";
	public static final String BUGREPORT_BASEURL = "baseurl";

	
	private static final String BUGREPORT_MAILADDRESS_CFGKEY = "bugreport.mailaddress";

	private static final String ODYSSEUS_BUG_REPORT_DEFAULT_TITLE = "Odysseus Bug Report";

	private static final Logger LOG = LoggerFactory.getLogger(BugReportEditor.class);
	
	private static final String NO_REPORT_AVAILABLE_TEXT = "<no report available>";
	private static final String NO_EXCEPTION_TEXT = "<no exception>";
	
	private static final String DIALOG_TITLE = "Send bug report";

	private static final String[] QUESTIONS = new String[] { 
		"Title of your report",
		"If you want a reply please enter a valid e-mail adress", 
		"What led up to the situation?", 
		"What exactly did you do (or not do) that was effective (or ineffective)?", 
		"What was the outcome of this action?", 
		"What outcome did you expect instead?" 
	};
	private static final int MAIL_QUESTION_INDEX = 1; // special behaviour: saving mail address in rcp-config

	private static final boolean[] ANSWER_TEXT_IS_ONE_LINE = new boolean[] { true, true, false, false, false, false }; 
	private static final String[] DEFAULT_ANSWERS = new String[] {
		ODYSSEUS_BUG_REPORT_DEFAULT_TITLE,
		"", 
		"", 
		"", 
		"", 
		"" 		
	};

	private final IReport baseReport;
	private final List<StyledText> reportTextList = Lists.newArrayList();

	private StyledText[] questionTexts;

	protected BugReportEditor(Shell parentShell, IReport report) {
		super(parentShell);
		Objects.requireNonNull(report, "Report must not be null!");

		baseReport = report;
	}
	
	static private String getUser() {
		return OdysseusRCPConfiguration.get(BUGREPORT_USER, "odysseus_studio");
	}

	static private String getPassword() {
		return OdysseusRCPConfiguration.get(BUGREPORT_PASSWORD, "jhf4hdds673");
	}

	static private String getJira() {
		return OdysseusRCPConfiguration.get(BUGREPORT_BASEURL,
				"https://jira.odysseus.informatik.uni-oldenburg.de/");
	}
	
	@Override
	public Control createDialogArea(Composite parent) {
		GridLayout layout = new GridLayout();
		parent.setLayout(layout);

		TabFolder tabFolder = new TabFolder(parent, SWT.NONE);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
		tabFolder.setLayout(new GridLayout());
		
		TabItem questionsTab = new TabItem(tabFolder, SWT.NONE);
		Composite questionsComposite = new Composite(tabFolder, SWT.NONE);
		questionsTab.setControl(questionsComposite);
		questionsTab.setText("Detail Information");
		questionsComposite.setLayout(new GridLayout());
		questionsComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		createQuestionsTab(questionsComposite);

		TabItem reportTab = new TabItem(tabFolder, SWT.NONE);
		Composite reportComposite = new Composite(tabFolder, SWT.NONE);
		reportComposite.setLayout(new GridLayout());
		reportTab.setControl(reportComposite);
		reportTab.setText("Report Information");
		createReportOverviewTab(reportComposite);

		setMessage("Please answer the questions and look through the generated report before sending the bug report.\n" +
				   "The more information you can provide, the more we can improve Odysseus.");
		setTitle("Odysseus Bug Report");
		getShell().setText(DIALOG_TITLE);

		return parent;
	}

	private void createQuestionsTab(Composite questionsComposite) {
		questionTexts = new StyledText[QUESTIONS.length];
		for (int i = 0; i < QUESTIONS.length; i++) {
			createStyledLabel(questionsComposite, QUESTIONS[i]);
			questionTexts[i] = createStyledText(questionsComposite, ANSWER_TEXT_IS_ONE_LINE[i]);
			
			if( i == MAIL_QUESTION_INDEX ) {
				questionTexts[i].setText(OdysseusRCPConfiguration.get(BUGREPORT_MAILADDRESS_CFGKEY, DEFAULT_ANSWERS[i]));
			} else {
				questionTexts[i].setText(DEFAULT_ANSWERS[i]);
			}
		}
	}

	private void createReportOverviewTab(Composite reportComposite) {
		Label label = createStyledLabel(reportComposite, "Caution: Please be aware that this report may contain private or other confidential information! Change details if necessary!");
		label.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		
		reportTextList.addAll(createReportTabs(reportComposite, baseReport));
	}

	private static List<StyledText> createReportTabs(Composite parent, IReport report) {
		List<StyledText> texts = Lists.newArrayList();
		
		TabFolder tabFolder = new TabFolder(parent, SWT.BORDER);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
		
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
		
		tabComposite.setLayout(new GridLayout());
		tabComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		StyledText text = createStyledText(tabComposite, false);
		text.setText(reportText);
		
		return text;
	}

	private static StyledText createStyledText(Composite parent,  boolean isLine) {
		StyledText styledText = new StyledText(parent, SWT.BORDER | (!isLine ? SWT.V_SCROLL : SWT.NONE));
		styledText.setEditable(true);

		GridData data = new GridData(!isLine ? GridData.FILL_BOTH : GridData.FILL_HORIZONTAL);
		data.widthHint = 600;
		styledText.setLayoutData(data);

		return styledText;
	}

	private static Label createStyledLabel(Composite parent, String text) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(text);

		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		label.setLayoutData(data);

		return label;
	}

	@Override
	public void createButtonsForButtonBar(Composite parent) {
		Button jiraButton = createButton(parent, 999, "Set JIRA account...", false);
		jiraButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String savedUsername = OdysseusRCPConfiguration.get(BUGREPORT_USER, "");
				String savedPassword = OdysseusRCPConfiguration.get(BUGREPORT_PASSWORD, "");

				UsernameAndPasswordDialog dlg = new UsernameAndPasswordDialog(getParentShell(), savedUsername, savedPassword, getJira());
				if (dlg.open() == Window.OK) {
					OdysseusRCPConfiguration.set(BUGREPORT_USER, dlg.getUsername());
					OdysseusRCPConfiguration.set(BUGREPORT_PASSWORD, dlg.getPassword());
					OdysseusRCPConfiguration.save();
				}
			}
		});
		
		super.createButtonsForButtonBar(parent); // creates OK and Cancel

		Button sendButton = getButton(OK);
		sendButton.setText(OdysseusNLS.SendBugReport); // replace "OK" with "Send"
	}
	
	@Override
	protected void okPressed() {
		Button button = getButton(OK);
		button.setText(OdysseusNLS.Sending);
		try {
			String title = determineTitle();
			String questionsText = createQuestionsText();

			Map<String, String> reportMap = createReportMap();
			boolean result = BugreportSender.send(getUser(), getPassword(), getJira(), title, questionsText, reportMap);
			
			setReturnCode(result ? Window.OK : Window.CANCEL);
			
			OdysseusRCPConfiguration.set(BUGREPORT_MAILADDRESS_CFGKEY, questionTexts[MAIL_QUESTION_INDEX].getText());
			OdysseusRCPConfiguration.save();
			
			close();
		} catch (IOException e) {
			setErrorMessage("Could not send the bug report");
			LOG.error(e.getMessage(), e);
			
			button.setText(OdysseusNLS.SendBugReport);
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
