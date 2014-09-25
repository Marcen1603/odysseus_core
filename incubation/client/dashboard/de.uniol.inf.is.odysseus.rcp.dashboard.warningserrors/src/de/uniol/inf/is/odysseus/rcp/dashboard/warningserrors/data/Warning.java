package de.uniol.inf.is.odysseus.rcp.dashboard.warningserrors.data;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class Warning extends Entry {

	public Warning(Composite parent, int style) {
		super(parent, style);
		lblValueType.setBackground(lblValueType.getDisplay().getSystemColor(
				SWT.COLOR_YELLOW));
	}

}
