package de.uniol.inf.is.odysseus.rcp.dashboard.warningserrors.data;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class Error extends Entry {

	public Error(Composite parent, int style) {
		super(parent, style);
		lblValueType.setBackground(lblValueType.getDisplay().getSystemColor(
				SWT.COLOR_RED));
	}

}
