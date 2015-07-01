package de.uniol.inf.is.odysseus.parallelization.rcp.windows;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.parallelization.rcp.threads.InitializeQueryThread;

public class InterOperatorParallelizationBenchmarkerWindow {

	private static final String TITLE = "Analyse parallelization in current query";
	private static final int WINDOW_WIDTH = 500;
	private static final int WINDOW_HEIGHT = 500;
	private static final String CANCEL_BUTTON_TEXT = "Cancel";

	private final Shell parent;
	private Shell window;
	private Composite pageComposite;
	private ProgressBar progressAnalyseQuery;

	public InterOperatorParallelizationBenchmarkerWindow(Shell parent) {
		this.parent = Preconditions.checkNotNull(parent,
				"Parent shell must not be null!");
	}

	public void show() {
		createWindow(parent);
		
		InitializeQueryThread initializeQueryThread = new InitializeQueryThread(this, progressAnalyseQuery);
		initializeQueryThread.setName("AnalyseQueryThread");
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
		contentGridData.widthHint = 600;
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

		progressAnalyseQuery = new ProgressBar(pageComposite,
				SWT.SMOOTH);
		progressAnalyseQuery.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL));
		progressAnalyseQuery.setMinimum(0);
		progressAnalyseQuery.setMaximum(100);

		createLabel(
				pageComposite,
				"Hint: Optimization is only possible if operators have an unique id. If #INTEROPERATORPARALLELIZATION \n keyword is used, only selected operators are used for benchmarking.");
	}

	public void createConfigContent() {
		createLabel(pageComposite,
				"Configure parallelization for current query");
		
		window.pack();
		window.setVisible(true);
	}
	
	public void clearPageContent() {
		Control[] children = pageComposite.getChildren();
		for (int i = 0; i < children.length; i++) {
			children[i].dispose();
		}
	}
	
	public void createErrorMessage(Throwable ex){
		Label createLabel = createLabel(
				pageComposite,
				"An error occured: "+ex.getMessage());
		createLabel.setForeground(window.getDisplay().getSystemColor(SWT.COLOR_RED));
		window.pack();
		window.setVisible(true);
	}
	
	
	private static Label createLabel(Composite generalComposite, String string) {
		Label label = new Label(generalComposite, SWT.NONE);
		label.setText(string);
		return label;
	}

	private void insertCancelButton() {
		Composite buttonComposite = new Composite(window, SWT.NONE);
		buttonComposite.setLayoutData(new GridData(SWT.BEGINNING));
		buttonComposite.setLayout(new GridLayout());
		Button closeButton = new Button(buttonComposite, SWT.PUSH);
		closeButton.setText(CANCEL_BUTTON_TEXT);
		closeButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		closeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!window.isDisposed()) {
					window.dispose();
				}
			}
		});
	}
	
}
