package de.uniol.inf.is.odysseus.parallelization.rcp.windows;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.parallelization.keyword.ParallelizationPreParserKeyword;
import de.uniol.inf.is.odysseus.parallelization.rcp.data.BenchmarkDataHandler;
import de.uniol.inf.is.odysseus.parallelization.rcp.data.BenchmarkerConfiguration;
import de.uniol.inf.is.odysseus.parallelization.rcp.threads.BenchmarkThread;
import de.uniol.inf.is.odysseus.parallelization.rcp.threads.InitializeQueryThread;

public class ParallelizationBenchmarkerWindow {

	private static final String TITLE = "Parallelization Benchmarker";
	private static final int WINDOW_WIDTH = 1000;
	private static final int WINDOW_HEIGHT = 600;
	private static final String CANCEL_BUTTON_TEXT = "Cancel";

	private final Shell parent;
	private Shell window;
	private Composite pageComposite;
	private ProgressBar progressInitializeQuery;
	private UUID benchmarkProcessId;
	private StrategySelectionTableViewer strategySelectionTableViewer;
	private Composite buttonComposite;
	private Text degreeText;
	private Text buffersizeText;
	private Label errorLabel;
	private ProgressBar progressAnalyseQuery;
	private Button startButton;
	private Text numberOfElementsText;
	private Text analysisProgressLog;
	private BenchmarkThread benchmarkThread;
	private Button closeButton;
	private Button allowPostOptimizationButton;
	private Text maxExecutionTimeText;

	public ParallelizationBenchmarkerWindow(Shell parent) {
		this.parent = Preconditions.checkNotNull(parent,
				"Parent shell must not be null!");
	}

	public void show() {
		createWindow(parent);

		InitializeQueryThread initializeQueryThread = new InitializeQueryThread(
				this, progressInitializeQuery);
		initializeQueryThread.setName("InitializeQueryThread");
		initializeQueryThread.setDaemon(true);
		initializeQueryThread.start();
	}

	private void createWindow(Shell parent) {
		window = new Shell(parent, SWT.DIALOG_TRIM | SWT.RESIZE);
		window.setText(TITLE);
		window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		window.setLayout(new GridLayout());

		createStartPage();
	}

	public Shell getWindow() {
		return this.window;
	}

	private void createStartPage() {
		pageComposite = new Composite(window, SWT.NONE);
		GridData contentGridData = new GridData(GridData.FILL_BOTH);
		contentGridData.widthHint = WINDOW_WIDTH;
		pageComposite.setLayoutData(contentGridData);
		GridLayout gridLayout = new GridLayout();
		pageComposite.setLayout(gridLayout);

		createStartContent();
		insertCancelButton();

		window.pack();
		window.setVisible(true);
	}

	private void createStartContent() {
		createLabel(pageComposite, "Initialize current query");

		progressInitializeQuery = new ProgressBar(pageComposite, SWT.SMOOTH);
		progressInitializeQuery.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL));
		progressInitializeQuery.setMinimum(0);
		progressInitializeQuery.setMaximum(100);

		createLabel(
				pageComposite,
				"Hint: Optimization is only possible if operators have an unique id. If #INTEROPERATORPARALLELIZATION \n keyword is used, only selected operators are used for benchmarking.");
	}

	public void createConfigContent() {
		BenchmarkDataHandler data = BenchmarkDataHandler
				.getExistingInstance(benchmarkProcessId);

		if (data.getBenchmarkInitializationResult().getStrategiesForOperator()
				.isEmpty()) {
			createErrorMessage(new Exception(
					"Running Benchmark not possible. No operator for parallelization found. \n Maybe you need to add unique ids to operators you want to parallelize."));
			return;
		}

		createLabel(pageComposite,
				"Configure parallelization benchmark for selected query");

		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.widthHint = 200;

		Composite configComposite = new Composite(pageComposite, SWT.NONE);
		GridLayout gridLayout = new GridLayout(2, false);
		configComposite.setLayout(gridLayout);

		Label degreeLabel = new Label(configComposite, SWT.NULL);
		degreeLabel.setText("Degrees (comma-seperated): ");
		degreeText = new Text(configComposite, SWT.SINGLE | SWT.BORDER);
		degreeText.setText("2,4,8");
		degreeText.setLayoutData(gridData);

		Label buffersizeLabel = new Label(configComposite, SWT.NULL);
		buffersizeLabel.setText("Buffersize: ");
		buffersizeText = new Text(configComposite, SWT.SINGLE | SWT.BORDER);
		buffersizeText.setText(String
				.valueOf(ParallelizationPreParserKeyword.AUTO_BUFFER_SIZE));
		buffersizeText.setLayoutData(gridData);

		Label numberOfElementsLabel = new Label(configComposite, SWT.NULL);
		numberOfElementsLabel.setText("Number of elements for analyse: ");
		numberOfElementsText = new Text(configComposite, SWT.SINGLE
				| SWT.BORDER);
		numberOfElementsText.setText(String
				.valueOf(BenchmarkerConfiguration.DEFAULT_NUMBER_OF_ELEMENTS));
		numberOfElementsText.setLayoutData(gridData);

		Label maxExecutionTimeLabel = new Label(configComposite, SWT.NULL);
		maxExecutionTimeLabel.setText("Maximum time for each analyse in ms: ");
		maxExecutionTimeText = new Text(configComposite, SWT.SINGLE
				| SWT.BORDER);
		maxExecutionTimeText.setText(String
				.valueOf(BenchmarkerConfiguration.DEFAULT_EXECUTION_TIME));
		maxExecutionTimeText.setLayoutData(gridData);

		allowPostOptimizationButton = new Button(pageComposite, SWT.CHECK);
		allowPostOptimizationButton.setText("Allow post optimization");
		allowPostOptimizationButton.setSelection(true);

		createLabel(
				pageComposite,
				"Following strategies are possible for the selected query. Please select at least one strategy for "
						+ "parallelization. \n If more than one strategy for one operator is compatible, each strategy is "
						+ "benchmarked. Note that executing \n the benchmark depends one the number of combinations.");

		strategySelectionTableViewer = new StrategySelectionTableViewer(
				pageComposite, data);

		startButton = new Button(buttonComposite, SWT.PUSH);
		startButton.setText("Start Analyse");
		startButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		startButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clearError();
				try {
					List<StrategySelectionRow> selectedStratgies = strategySelectionTableViewer
							.getSelectedStratgies();
					validateConfiguration(selectedStratgies);
					doParallelizationAnalysis(selectedStratgies);
				} catch (Exception ex) {
					createErrorMessage(ex);
				}
			}
		});

		window.pack();
		window.setVisible(true);
	}

	protected void doParallelizationAnalysis(
			List<StrategySelectionRow> selectedStratgies) {
		createConfiguration(selectedStratgies);

		clearPageContent();
		removeStartButton();
		showAnalysisContent();

		benchmarkThread = new BenchmarkThread(benchmarkProcessId, this);
		benchmarkThread.setName("BenchmarkThread");
		benchmarkThread.setDaemon(true);
		benchmarkThread.start();
	}

	protected void createConfiguration(
			List<StrategySelectionRow> selectedStratgies) {
		BenchmarkDataHandler data = BenchmarkDataHandler
				.getExistingInstance(benchmarkProcessId);
		BenchmarkerConfiguration configuration = new BenchmarkerConfiguration();
		configuration.setSelectedStratgies(selectedStratgies);

		List<Integer> degrees = new ArrayList<Integer>();
		String degreeString = this.degreeText.getText();
		String[] degreeValues = degreeString.trim().split(",");
		for (int i = 0; i < degreeValues.length; i++) {
			degrees.add(Integer.parseInt(degreeValues[i]));
		}
		configuration.setDegrees(degrees);

		String buffersizeString = this.buffersizeText.getText();
		configuration.setBuffersize(Integer.parseInt(buffersizeString));

		String numberOfElementsString = this.numberOfElementsText.getText();
		configuration.setNumberOfElements(Integer
				.parseInt(numberOfElementsString));

		String maxExecutionTimeString = this.maxExecutionTimeText.getText();
		configuration.setMaximumExecutionTime(Long
				.parseLong(maxExecutionTimeString));

		boolean allowPostOptimization = allowPostOptimizationButton
				.getSelection();
		configuration.setAllowPostOptimization(allowPostOptimization);

		data.setConfiguration(configuration);
	}

	protected void showAnalysisContent() {
		createLabel(pageComposite, "Analyse parallelization of current query");

		progressAnalyseQuery = new ProgressBar(pageComposite, SWT.SMOOTH);
		progressAnalyseQuery.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL));
		progressAnalyseQuery.setMinimum(0);
		progressAnalyseQuery.setMaximum(100);

		analysisProgressLog = new Text(pageComposite, SWT.MULTI | SWT.BORDER
				| SWT.WRAP | SWT.V_SCROLL | SWT.READ_ONLY);
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

		window.pack();
		window.setVisible(true);
	}

	public void updateAnalysisProgress(int progressProcent,
			String progressString) {
		if (!progressString.isEmpty()) {
			analysisProgressLog.setText(analysisProgressLog.getText()
					+ progressString + System.lineSeparator());
		}
		if (progressProcent != 0
				&& progressProcent >= progressAnalyseQuery.getSelection()) {
			progressAnalyseQuery.setSelection(progressProcent);
		}
	}

	protected void validateConfiguration(
			List<StrategySelectionRow> selectedStratgies) {
		// selected strategies
		if (selectedStratgies.isEmpty()) {
			throw new IllegalArgumentException("No strategy selected");
		}

		// degree values
		String degreeString = this.degreeText.getText();
		if (degreeString.isEmpty()) {
			throw new IllegalArgumentException("No degree definied");
		} else {
			String[] degreeValues = degreeString.trim().split(",");
			for (int i = 0; i < degreeValues.length; i++) {
				Integer.parseInt(degreeValues[i]);
			}
		}

		// buffersize
		String buffersizeString = this.buffersizeText.getText();
		if (buffersizeString.isEmpty()) {
			throw new IllegalArgumentException("No buffersize definied");
		} else {
			Integer.parseInt(buffersizeString);
		}

		// number of elements
		String numberOfElementsString = this.numberOfElementsText.getText();
		if (numberOfElementsString.isEmpty()) {
			throw new IllegalArgumentException("No number of elements definied");
		} else {
			int value = Integer.parseInt(numberOfElementsString);
			if (value < 1){
				throw new IllegalArgumentException(
						"Number of elements need to be greater than 0");
			}
			
		}

		// maximum execution time
		String maxExecutionTimeString = this.maxExecutionTimeText.getText();
		if (maxExecutionTimeString.isEmpty()) {
			throw new IllegalArgumentException(
					"No maximum execution time definied");
		} else {
			long value = Long.parseLong(maxExecutionTimeString);
			if (value < 1000){
				throw new IllegalArgumentException(
						"Maximum execution need to be greater equal 1000");
			}
		}
	}

	public void clearPageContent() {
		Control[] children = pageComposite.getChildren();
		for (int i = 0; i < children.length; i++) {
			children[i].dispose();
		}
	}

	public void createErrorMessage(Throwable ex) {
		errorLabel = createLabel(pageComposite,
				"An error occured: " + ex.getMessage());
		errorLabel.setForeground(window.getDisplay().getSystemColor(
				SWT.COLOR_RED));
		window.pack();
		window.setVisible(true);
	}

	protected void removeStartButton() {
		if (startButton != null) {
			startButton.dispose();
		}
		startButton = null;
	}

	public void clearError() {
		if (errorLabel != null) {
			errorLabel.dispose();
		}
		errorLabel = null;
	}

	private static Label createLabel(Composite generalComposite, String string) {
		Label label = new Label(generalComposite, SWT.WRAP | SWT.BORDER
				| SWT.LEFT);
		label.setText(string);
		return label;
	}

	private void insertCancelButton() {
		buttonComposite = new Composite(window, SWT.NONE);
		buttonComposite.setLayoutData(new GridData(SWT.BEGINNING));
		buttonComposite.setLayout(new GridLayout(2, false));
		closeButton = new Button(buttonComposite, SWT.PUSH);
		closeButton.setText(CANCEL_BUTTON_TEXT);
		closeButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		closeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (benchmarkThread != null) {
					// stop analyze if it is currently running
					benchmarkThread.interrupt();
				}
				if (!window.isDisposed()) {
					window.dispose();
				}
			}
		});
	}

	public void setBenchmarkProcessId(UUID uniqueIdentifier) {
		this.benchmarkProcessId = uniqueIdentifier;
	}

	public void showResult(String resultOdysseusScript) {
		createLabel(pageComposite,
				"Result of parallelization benchmarker. Put this Snippet in your script.");

		Text analysisResultScript = new Text(pageComposite, SWT.MULTI
				| SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.READ_ONLY);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.heightHint = 100;
		analysisResultScript.setLayoutData(gridData);
		analysisResultScript.setText(resultOdysseusScript);

		closeButton.setText("Done");

		window.pack();
		window.setVisible(true);
	}

}
