package de.uniol.inf.is.odysseus.parallelization.rcp.windows;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import com.google.common.base.Preconditions;

public class InterOperatorParallelizationBenchmarkerWindow {
	
	private static final String TITLE = "Create PQL-ACCESS Statement";
	private static final int WINDOW_WIDTH = 500;
	private static final int WINDOW_HEIGHT = 500;

	private final Shell parent;
	private Shell window;
	
	public InterOperatorParallelizationBenchmarkerWindow(Shell parent) {
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
		//insertContent(contentComposite);

		Composite buttonComposite = new Composite(window, SWT.NONE);
		buttonComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		//insertButtons(buttonComposite, window);

		window.pack();
		return window;
	}
}
