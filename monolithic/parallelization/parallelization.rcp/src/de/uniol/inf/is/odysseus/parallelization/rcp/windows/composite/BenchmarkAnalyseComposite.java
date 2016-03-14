/********************************************************************************** 
 * Copyright 2015 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.parallelization.rcp.windows.composite;

import java.util.Date;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;
import org.ocpsoft.prettytime.PrettyTime;

import de.uniol.inf.is.odysseus.parallelization.rcp.windows.ParallelizationBenchmarkerWindow;

/**
 * Composite for execution of analysis (progressbar, progresslog and result of
 * the benchmarker)
 * 
 * @author ChrisToenjesDeye
 *
 */
public class BenchmarkAnalyseComposite extends AbstractBenchmarkComposite {

	private ProgressBar progressAnalyseQuery;
	private Text analysisProgressLog;
	private Label remainingTimeLabel;

	public BenchmarkAnalyseComposite(Composite parent, int style,
			ParallelizationBenchmarkerWindow window, int windowWidth) {
		super(parent, style);

		GridData contentGridData = new GridData(GridData.FILL_BOTH);
		contentGridData.widthHint = windowWidth;
		this.setLayoutData(contentGridData);
		GridLayout gridLayout = new GridLayout();
		this.setLayout(gridLayout);

		createContent();

		parent.pack();
		parent.setVisible(true);
	}

	/**
	 * create the content of this composite
	 */
	private void createContent() {
		createLabelWithSeperator(this,
				"Analyse parallelization of current query");

		// progres bar for the execution progress
		progressAnalyseQuery = new ProgressBar(this, SWT.SMOOTH);
		progressAnalyseQuery.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL));
		progressAnalyseQuery.setMinimum(0);
		progressAnalyseQuery.setMaximum(100);

		// label with the remaining time
		remainingTimeLabel = new Label(this, SWT.NULL);
		remainingTimeLabel.setText("Calculating remaining time...");
		remainingTimeLabel
				.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// text with log messages
		analysisProgressLog = new Text(this, SWT.MULTI | SWT.BORDER | SWT.WRAP
				| SWT.V_SCROLL | SWT.READ_ONLY);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.heightHint = 300;
		analysisProgressLog.setLayoutData(gridData);
		analysisProgressLog.setText("Analysis started... "
				+ System.lineSeparator());
		analysisProgressLog.addListener(SWT.Modify, new Listener() {
			@Override
			public void handleEvent(Event e) {
				analysisProgressLog.setTopIndex(analysisProgressLog
						.getLineCount() - 1);
			}
		});
	}

	/**
	 * updates the analysis progress. updates remaining time, progress bar and
	 * text log
	 * 
	 * @param progressProcent
	 * @param remainingTimeMillis
	 * @param progressString
	 */
	public void updateAnalysisProgress(int progressProcent,
			long remainingTimeMillis, String progressString) {
		// pretty time framework for nice remaining time
		PrettyTime p = new PrettyTime(new Locale("en"));

		// remaining time
		if (remainingTimeMillis > 0l) {
			remainingTimeLabel.setText("Approximately finished in "
					+ p.format(new Date(System.currentTimeMillis()
							+ remainingTimeMillis)));
		} else if (remainingTimeMillis == 0l) {
			remainingTimeLabel.setText("Finished !");
		}

		// progress log
		if (!progressString.isEmpty()) {
			analysisProgressLog.setText(analysisProgressLog.getText()
					+ progressString + System.lineSeparator());
		}

		// progress bar
		if (progressProcent != 0
				&& progressProcent >= progressAnalyseQuery.getSelection()) {
			progressAnalyseQuery.setSelection(progressProcent);
		}
	}

	/**
	 * shows the result if the benchmark is completely done
	 * 
	 * @param resultOdysseusScript
	 */
	public void showResult(String resultOdysseusScript) {
		createLabel(this,
				"Result of parallelization benchmarker. Put this Snippet in your script.");

		Text analysisResultScript = new Text(this, SWT.MULTI | SWT.BORDER
				| SWT.WRAP | SWT.V_SCROLL | SWT.READ_ONLY);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.heightHint = 100;
		analysisResultScript.setLayoutData(gridData);
		analysisResultScript.setText(resultOdysseusScript);

		pack();
		setVisible(true);
	}
}
