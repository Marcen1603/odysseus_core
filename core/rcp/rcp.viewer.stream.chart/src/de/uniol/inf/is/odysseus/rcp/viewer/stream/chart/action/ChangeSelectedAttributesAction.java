/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
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

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.IAttributesChangeable;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.dialogs.ChangeAttributesDialog;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.AbstractViewableAttribute;
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
				Map<Integer, List<IViewableAttribute>> attr = AbstractViewableAttribute.getAttributesAsPortMapList(dialog.getSelectedAttributes());
				for (Entry<Integer, List<IViewableAttribute>> e : attr.entrySet()) {
					changeable.setChoosenAttributes(e.getKey(), e.getValue());
				}
				changeable.chartSettingsChanged();
			}
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

}
