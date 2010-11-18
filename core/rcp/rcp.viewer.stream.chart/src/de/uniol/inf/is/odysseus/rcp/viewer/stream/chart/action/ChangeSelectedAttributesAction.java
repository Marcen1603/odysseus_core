package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.action;

import java.util.Arrays;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.IAttributesChangeable;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.dialogs.ChangeAttributesDialog;

public class ChangeSelectedAttributesAction extends Action {

	private IAttributesChangeable changeable;
	private Shell parentShell;	

	public ChangeSelectedAttributesAction(Shell shell, IAttributesChangeable changeable) {
		super();
		this.changeable = changeable;
		this.parentShell = shell;		

	}

	@Override
	public void run() {
		try {
			System.out.println("open dialog...");
			ChangeAttributesDialog dialog = new ChangeAttributesDialog(parentShell, changeable.getSchema(),  this.changeable.getVisibleAttributes());
			if (dialog.open() == Window.OK) {
				boolean[] attr = dialog.getSelectedAttributes();
				changeable.setVisibleAttributes(Arrays.copyOf(attr, attr.length));
				changeable.visibleAttributesChanged();
			}
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

}
