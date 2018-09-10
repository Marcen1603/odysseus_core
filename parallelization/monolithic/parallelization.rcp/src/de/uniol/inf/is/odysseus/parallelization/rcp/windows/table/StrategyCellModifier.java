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

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.TableItem;

/**
 * cell modifier which allows to enter specific degrees for strategies and
 * allows the definition of an end operatorId
 * 
 * @author ChrisToenjesDeye
 *
 */
public class StrategyCellModifier implements ICellModifier {

	private Viewer viewer;

	public StrategyCellModifier(Viewer viewer) {
		this.viewer = viewer;
	}

	/**
	 * checks if a given property can be modified
	 */
	@Override
	public boolean canModify(Object element, String property) {
		StrategySelectionRow row = null;
		if (element instanceof StrategySelectionRow) {
			row = (StrategySelectionRow) element;
		}
		if (row != null) {
			switch (property) {
			case StrategySelectionHelper.END_OPERATOR_ID:
				return true;
			case StrategySelectionHelper.DEGREES:
				return true;
			}
		}
		return false;
	}

	/**
	 * returns the value for a given property from a given row
	 */
	@Override
	public Object getValue(Object element, String property) {
		StrategySelectionRow row = null;
		if (element instanceof StrategySelectionRow) {
			row = (StrategySelectionRow) element;
		}
		if (row != null) {
			switch (property) {
			case StrategySelectionHelper.END_OPERATOR_ID:
				return row.getEndOperatorId();
			case StrategySelectionHelper.DEGREES:
				return row.getCustomDegrees();
			}
		}
		return "";
	}

	/**
	 * sets the value if the value is changed in UI
	 */
	@Override
	public void modify(Object element, String property, Object value) {
		StrategySelectionRow row = null;
		if (element instanceof TableItem) {
			TableItem tableItem = (TableItem) element;
			if (tableItem.getData() instanceof StrategySelectionRow) {
				row = (StrategySelectionRow) tableItem.getData();
			}
		}
		if (row != null) {
			switch (property) {
			case StrategySelectionHelper.END_OPERATOR_ID:
				row.setEndOperatorId(String.valueOf(value));
				break;
			case StrategySelectionHelper.DEGREES:
				row.setCustomDegrees(String.valueOf(value));
				break;
			}
		}

		viewer.refresh();
	}

}
