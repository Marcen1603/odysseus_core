package de.uniol.inf.is.odysseus.rcp.dashboard.warningserrors.data;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * This class extends the Entry, which represents a Message drawn as a
 * swt-Composite. You should use this class, if the Message represents a Waring,
 * with low priority
 * 
 * @author MarkMilster
 * 
 */
public class Warning extends Entry {

	/**
	 * Constructor to create this Error-Composite. The value_type of the Entry
	 * will have a red background
	 * 
	 * @param parent
	 *            The parent-Composite
	 * @param style
	 *            The swt-Style given as an int
	 */
	public Warning(Composite parent, int style) {
		super(parent, style);
		lblValueType.setBackground(lblValueType.getDisplay().getSystemColor(
				SWT.COLOR_YELLOW));
	}

}
