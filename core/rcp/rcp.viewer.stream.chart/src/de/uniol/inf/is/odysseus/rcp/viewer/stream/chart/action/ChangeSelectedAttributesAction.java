package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.IAttributesChangeable;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.dialogs.ChangeAttributesDialog;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

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
			ChangeAttributesDialog dialog = new ChangeAttributesDialog(parentShell, this.changeable);
			if (dialog.open() == Window.OK) {
				SDFAttributeList attr = dialog.getSelectedAttributes();
				changeable.setVisibleSchema(attr.clone());
				changeable.chartSettingsChanged();
			}
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

}
