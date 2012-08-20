package de.uniol.inf.is.odysseus.rcp.viewer.stream.map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class DialogPropertyWindow extends Dialog {

	/**
	 * @param parentShell
	 */
	public DialogPropertyWindow(Shell parentShell) {
		super(parentShell);

		// forces open method to block until window is closed.
		setBlockOnOpen(true);

	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);

	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite container = (Composite) super.createDialogArea(parent);

		Label nameLabel = new Label(container, SWT.NONE);
		nameLabel.setText("Name:");
		Text nameText = new Text(container, SWT.BORDER);

		return container;

	}
}
