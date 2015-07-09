package de.uniol.inf.is.odysseus.parallelization.rcp.windows.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public abstract class AbstractBenchmarkComposite extends Composite{

	public AbstractBenchmarkComposite(Composite parent, int style) {
		super(parent, style);
	}

	protected static Label createLabel(Composite generalComposite, String string) {
		Label label = new Label(generalComposite, SWT.WRAP | SWT.BORDER
				| SWT.LEFT);
		label.setText(string);
		return label;
	}
}
