package de.uniol.inf.is.odysseus.rcp.editor.parameters;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

public class ReadonlyComboParameterEditor extends ComboParameterEditor {

	@Override
	protected Combo createCombo(Composite parent) {
		// Combo, worin kein Text manuell eingegeben werden kann
		return new Combo(parent, SWT.BORDER | SWT.READ_ONLY);
	}
}
