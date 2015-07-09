package de.uniol.inf.is.odysseus.parallelization.rcp.windows;

import java.util.UUID;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.parallelization.rcp.threads.BenchmarkMainThread;
import de.uniol.inf.is.odysseus.parallelization.rcp.threads.InitializeQueryThread;
import de.uniol.inf.is.odysseus.parallelization.rcp.windows.composite.BenchmarkAnalyseComposite;
import de.uniol.inf.is.odysseus.parallelization.rcp.windows.composite.BenchmarkButtonComposite;
import de.uniol.inf.is.odysseus.parallelization.rcp.windows.composite.BenchmarkConfigureComposite;
import de.uniol.inf.is.odysseus.parallelization.rcp.windows.composite.BenchmarkStartComposite;

public class ParallelizationBenchmarkerWindow {

	public enum ParalleizationBenchmarkerPage {
		START, CONFIG, ANALYSE, ANALYSE_FINISHED
	}

	private static final String TITLE = "Parallelization Benchmarker";
	private static final int WINDOW_WIDTH = 1000;
	private static final int WINDOW_HEIGHT = 600;

	private final Shell parent;
	private Shell window;
	private Composite pageComposite;
	private Composite bottomComposite;

	private Label errorLabel;

	private UUID benchmarkProcessId;

	private BenchmarkMainThread benchmarkMainThread;

	private BenchmarkStartComposite benchmarkStartComposite;
	private BenchmarkConfigureComposite benchmarkConfigureComposite;
	private BenchmarkAnalyseComposite benchmarkAnalyseComposite;
	private BenchmarkButtonComposite benchmarkButtonComposite;
	private InitializeQueryThread initializeQueryThread;

	public ParallelizationBenchmarkerWindow(Shell parent) {
		this.parent = Preconditions.checkNotNull(parent,
				"Parent shell must not be null!");
	}

	public Shell getWindow() {
		return this.window;
	}

	public void show() {
		createWindow(parent);

		if (benchmarkStartComposite != null) {
			initializeQueryThread = new InitializeQueryThread(
					this);
			initializeQueryThread.setName("InitializeQueryThread");
			initializeQueryThread.setDaemon(true);
			initializeQueryThread.start();
		}
	}

	private void createWindow(Shell parent) {
		window = new Shell(parent, SWT.DIALOG_TRIM | SWT.RESIZE);
		window.setText(TITLE);
		window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		window.setLayout(new GridLayout());

		createStartContent();
	}

	public void createStartContent() {
		benchmarkStartComposite = new BenchmarkStartComposite(window, SWT.NONE,
				WINDOW_WIDTH);
		pageComposite = benchmarkStartComposite;
		createButtons(ParalleizationBenchmarkerPage.START, null);
	}

	public void createConfigContent() {
		this.clearContent();
		benchmarkConfigureComposite = new BenchmarkConfigureComposite(window,
				SWT.NONE, WINDOW_WIDTH, benchmarkProcessId, this);
		this.pageComposite = benchmarkConfigureComposite;

		createButtons(ParalleizationBenchmarkerPage.CONFIG, null);
	}

	public void createAnalysisContent() {
		clearContent();
		benchmarkAnalyseComposite = new BenchmarkAnalyseComposite(window,
				SWT.NONE, this, WINDOW_WIDTH);
		pageComposite = benchmarkAnalyseComposite;
		createButtons(ParalleizationBenchmarkerPage.ANALYSE, null);
	}

	public void clearContent() {
		pageComposite.dispose();
		bottomComposite.dispose();
	}

	public void createErrorMessage(Throwable ex) {
		if (errorLabel == null) {
			errorLabel = createLabel(pageComposite, "");
			errorLabel.setForeground(window.getDisplay().getSystemColor(
					SWT.COLOR_RED));
		}
		errorLabel.setText("An error occured: " + ex.getMessage());
		window.pack();
		window.setVisible(true);
	}

	public void startAnalysis() {
		createAnalysisContent();

		benchmarkMainThread = new BenchmarkMainThread(benchmarkProcessId, this);
		benchmarkMainThread.setName("BenchmarkThread");
		benchmarkMainThread.setDaemon(true);
		benchmarkMainThread.start();
	}
	
	public void analyseDone(String resultOdysseusScript){
		benchmarkAnalyseComposite.showResult(resultOdysseusScript);
		bottomComposite.dispose();
		createButtons(ParalleizationBenchmarkerPage.ANALYSE_FINISHED, resultOdysseusScript);
		window.pack();
		window.setVisible(true);
	}

	private void createButtons(ParalleizationBenchmarkerPage currentPage, String parameter) {
		benchmarkButtonComposite = new BenchmarkButtonComposite(window,
				SWT.NONE, this, currentPage, parameter);
		bottomComposite = benchmarkButtonComposite;
	}

	public void setBenchmarkProcessId(UUID uniqueIdentifier) {
		this.benchmarkProcessId = uniqueIdentifier;
	}

	private static Label createLabel(Composite generalComposite, String string) {
		Label label = new Label(generalComposite, SWT.WRAP | SWT.BORDER
				| SWT.LEFT);
		label.setText(string);
		return label;
	}

	public BenchmarkStartComposite getStartComposite() {
		return this.benchmarkStartComposite;
	}

	public BenchmarkConfigureComposite getConfigureComposite() {
		return this.benchmarkConfigureComposite;
	}

	public BenchmarkAnalyseComposite getAnalyseComposite() {
		return this.benchmarkAnalyseComposite;
	}

	public BenchmarkButtonComposite getButtonComposite() {
		return this.benchmarkButtonComposite;
	}

	public BenchmarkMainThread getBenchmarkMainThread() {
		return benchmarkMainThread;
	}
	
	public InitializeQueryThread getInitializeQueryThread() {
		return initializeQueryThread;
	}
}

