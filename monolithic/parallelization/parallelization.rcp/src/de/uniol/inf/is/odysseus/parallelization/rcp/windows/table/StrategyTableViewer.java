/********************************************************************************** 
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.parallelization.rcp.windows.table;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

public class StrategyTableViewer extends CheckboxTableViewer {

	public StrategyTableViewer(Table table, List<StrategySelectionRow> strategySelectionRows) {
		super(table);
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
	}

	public static StrategyTableViewer newCheckList(Composite parent, int style, List<StrategySelectionRow> strategySelectionRows) {
        Table table = new Table(parent, SWT.CHECK | style);
        return new StrategyTableViewer(table, strategySelectionRows);
    }

	public Object[] getCheckedElements() {
		applyEditorValue();
		return super.getCheckedElements();
	}
	
	public List<StrategySelectionRow> getSelectedStratgies() {
		ArrayList<StrategySelectionRow> checkedElementResult = new ArrayList<StrategySelectionRow>();
		Object[] checkedElements = this.getCheckedElements();
		if (checkedElements.length > 0) {
			if (checkedElements[0] instanceof StrategySelectionRow) {
				for (int i = 0; i < checkedElements.length; i++) {
					StrategySelectionRow row = (StrategySelectionRow) checkedElements[i];
					checkedElementResult.add(row);
				}
			}
		}
		return checkedElementResult;
	}
	
}
