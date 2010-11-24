package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.action;

import java.lang.reflect.InvocationTargetException;
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
					invokeForType(en.getKey(), en.getValue());
				}
			}
			this.changeable.chartSettingsChanged();
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}
	
	
	private void invokeForType(MethodSetting ms, Object ob) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
//		Class<?> paramType = ms.getSetter().getParameterTypes()[0];						
//		Object castedOb = paramType.cast(ob);
//		ms.getSetter().invoke(this.changeable, castedOb);
		ms.getSetter().invoke(this.changeable, ob);
	}

}
