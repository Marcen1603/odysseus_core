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

	private void createContent() {
		createLabel(this, "Analyse parallelization of current query");


		progressAnalyseQuery = new ProgressBar(this, SWT.SMOOTH);
		progressAnalyseQuery.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL));
		progressAnalyseQuery.setMinimum(0);
		progressAnalyseQuery.setMaximum(100);

		remainingTimeLabel = new Label(this, SWT.NULL);
		remainingTimeLabel.setText("Calculating remaining time...");
		remainingTimeLabel.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL));

		analysisProgressLog = new Text(this, SWT.MULTI | SWT.BORDER | SWT.WRAP
				| SWT.V_SCROLL | SWT.READ_ONLY);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.heightHint = 300;
		analysisProgressLog.setLayoutData(gridData);
		analysisProgressLog.setText("Analysis started... "
				+ System.lineSeparator());
		analysisProgressLog.addListener(SWT.Modify, new Listener() {
			public void handleEvent(Event e) {
				analysisProgressLog.setTopIndex(analysisProgressLog
						.getLineCount() - 1);
			}
		});
	}

	public void updateAnalysisProgress(int progressProcent,
			long remainingTimeMillis, String progressString) {
		PrettyTime p = new PrettyTime(new Locale("en"));

		if (remainingTimeMillis > 0l) {
			remainingTimeLabel.setText("Approximately finished in "
					+ p.format(new Date(System.currentTimeMillis()
							+ remainingTimeMillis)));
		} else if (remainingTimeMillis == 0l) {
			remainingTimeLabel.setText("Finished !");
		}

		if (!progressString.isEmpty()) {
			analysisProgressLog.setText(analysisProgressLog.getText()
					+ progressString + System.lineSeparator());
		}

		if (progressProcent != 0
				&& progressProcent >= progressAnalyseQuery.getSelection()) {
			progressAnalyseQuery.setSelection(progressProcent);
		}
	}

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