package de.uniol.inf.is.odysseus.parallelization.rcp.windows.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ProgressBar;

public class BenchmarkStartComposite extends AbstractBenchmarkComposite{

	private ProgressBar progressInitializeQuery;
	
	public BenchmarkStartComposite(Composite parent, int style, int windowWidth) {
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
		createLabel(this, "Initialize current query");

		progressInitializeQuery = new ProgressBar(this, SWT.SMOOTH);
		progressInitializeQuery.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL));
		progressInitializeQuery.setMinimum(0);
		progressInitializeQuery.setMaximum(100);

		createLabel(
				this,
				"Hint: Optimization is only possible if operators have an unique id. If #INTEROPERATORPARALLELIZATION \n keyword is used, only selected operators are used for benchmarking.");
	}
	

	public void updateProgress(int selectionValue) {
		progressInitializeQuery.setSelection(selectionValue);
	}
}
