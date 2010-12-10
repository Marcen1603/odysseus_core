package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.action;

import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.IAttributesChangeable;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.dialogs.ChangeAttributesDialog;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.IViewableAttribute;

public class ChangeSelectedAttributesAction<T> extends Action {

	private IAttributesChangeable<T> changeable;
	private Shell parentShell;	

	public ChangeSelectedAttributesAction(Shell shell, IAttributesChangeable<T> changeable) {
		super();
		this.changeable = changeable;
		this.parentShell = shell;		

	}

	@Override
	public void run() {
		try {			
			ChangeAttributesDialog<T> dialog = new ChangeAttributesDialog<T>(parentShell, this.changeable);
			if (dialog.open() == Window.OK) {
				List<IViewableAttribute<T>> attr = dialog.getSelectedAttributes();
				changeable.setChoosenAttributes(attr);
				changeable.chartSettingsChanged();
			}
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

}
