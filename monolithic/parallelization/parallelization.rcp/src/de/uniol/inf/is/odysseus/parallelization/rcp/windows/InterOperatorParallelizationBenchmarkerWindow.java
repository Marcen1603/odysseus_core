package de.uniol.inf.is.odysseus.parallelization.rcp.windows;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

import com.google.common.base.Preconditions;

public class InterOperatorParallelizationBenchmarkerWindow {

	private static final String TITLE = "Analyse parallelization in current query";
	private static final int WINDOW_WIDTH = 500;
	private static final int WINDOW_HEIGHT = 500;
	private static final String CANCEL_BUTTON_TEXT = "Cancel";

	private final Shell parent;
	private Shell window;

	public InterOperatorParallelizationBenchmarkerWindow(Shell parent) {
		this.parent = Preconditions.checkNotNull(parent,
				"Parent shell must not be null!");
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
		GridLayout gridLayout = new GridLayout();
		contentComposite.setLayout(gridLayout);

		createLabel(contentComposite,
				"Analysing current selected query");

		ProgressBar progressAnalyseQuery = new ProgressBar(contentComposite,
				SWT.INDETERMINATE);
		progressAnalyseQuery.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL));

		createLabel(
				contentComposite,
				"Hint: Optimization is only possible if operators have ab unique id. If #INTEROPERATORPARALLELIZATION \n keyword is used, only selected operators are used for benchmarking.");

		Composite buttonComposite = new Composite(window, SWT.NONE);
		buttonComposite.setLayoutData(new GridData(SWT.BEGINNING));
		insertCancelButton(buttonComposite, window);

		window.pack();
		return window;
	}

	private static Label createLabel(Composite generalComposite, String string) {
		Label label = new Label(generalComposite, SWT.NONE);
		label.setText(string);
		return label;
	}
	
	private void insertCancelButton(Composite buttonComposite, final Shell closingWindow) {
		buttonComposite.setLayout(new GridLayout());
		Button closeButton = new Button(buttonComposite, SWT.PUSH);
		closeButton.setText(CANCEL_BUTTON_TEXT);
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
}
