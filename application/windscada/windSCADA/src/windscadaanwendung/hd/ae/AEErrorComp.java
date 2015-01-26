package windscadaanwendung.hd.ae;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * This class extends the AEEntryComp with special settings for the type error
 * 
 * @author MarkMilster
 * 
 */
public class AEErrorComp extends AEEntryComp {

	/**
	 * Use this constructor if the aeEntry is a Error.
	 * 
	 * @param parent
	 * @param style
	 * @param aeEntry
	 */
	public AEErrorComp(Composite parent, int style, AEEntry aeEntry) {
		super(parent, style, aeEntry);
		lblValueType.setBackground(lblValueType.getDisplay().getSystemColor(
				SWT.COLOR_RED));
	}

}
