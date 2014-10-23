package windscadaanwendung.hd.ae;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * This class extends the AEEntryComp with special settings for the type warning
 * 
 * @author MarkMilster
 * 
 */
public class AEWarningComp extends AEEntryComp {

	/**
	 * Use this constructor if the aeEntry is a Warning.
	 * 
	 * @param parent
	 * @param style
	 * @param aeEntry
	 */
	public AEWarningComp(Composite parent, int style, AEEntry aeEntry) {
		super(parent, style, aeEntry);
		lblValueType.setBackground(lblValueType.getDisplay().getSystemColor(
				SWT.COLOR_YELLOW));
	}

}
