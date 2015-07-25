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

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * Label provider for strategy selection table
 * 
 * @author ChrisToenjesDeye
 *
 */
public class StrategyLabelProvider implements ITableLabelProvider {

	@Override
	public void addListener(ILabelProviderListener listener) {
		// nothing to do
	}

	@Override
	public void dispose() {
		// nothing to do
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// nothing to do
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// nothing to do
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		String result = "";
		StrategySelectionRow row = (StrategySelectionRow) element;
		switch (columnIndex) {
		case 0:
			// first column: operator type
			result = row.getLogicalOperator().getClass().getSimpleName();
			break;
		case 1:
			// second column: operator id
			result = row.getLogicalOperator().getUniqueIdentifier();
			break;
		case 2:
			// third column: end operator (editable)
			result = row.getEndOperatorId();
			break;
		case 3:
			// fourth column: custom degrees (editable)
			result = row.getCustomDegrees();
			break;
		case 4:
			// fifth column: strategy
			result = row.getStrategy().getName();
			break;
		case 5:
			// sixth column: fragmentation
			result = row.getFragmentType().getSimpleName();
			break;
		}
		return result;
	}

}
