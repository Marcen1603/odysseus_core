/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
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
