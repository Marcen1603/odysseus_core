package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.action;

import java.util.Map.Entry;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.dialogs.ChangeSettingsDialog;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.IChartSettingChangeable;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.MethodSetting;

public class ChangeSettingsAction extends Action {
	
	private IChartSettingChangeable changeable;
	private Shell parentShell;

	public ChangeSettingsAction(Shell shell, IChartSettingChangeable changeable) {
		super();
		this.changeable = changeable;
		this.parentShell = shell;		

	}
	
	@Override
	public void run() {
		try {			
			this.changeable.getChartSettings();
			ChangeSettingsDialog dialog = new ChangeSettingsDialog(parentShell, this.changeable);
			if (dialog.open() == Window.OK) {
				for(Entry<MethodSetting, Object> en : dialog.getCurrentValues().entrySet()){					
					en.getKey().getSetter().invoke(this.changeable, en.getValue().toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

}
