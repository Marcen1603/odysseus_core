package windscadaanwendung.hd.ae;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class AEWarningComp extends AEEntryComp {

	public AEWarningComp(Composite parent, int style, AEEntry aeEntry) {
		super(parent, style, aeEntry);
		lblValueType.setBackground(lblValueType.getDisplay().getSystemColor(
				SWT.COLOR_YELLOW));
	}

}
