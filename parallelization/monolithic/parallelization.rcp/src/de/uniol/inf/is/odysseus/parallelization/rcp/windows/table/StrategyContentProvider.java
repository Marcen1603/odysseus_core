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

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * content provider class which is needed for strategy selection in benchmeraker
 * UI
 * 
 * @author ChrisToenjesDeye
 *
 */
public class StrategyContentProvider implements IStructuredContentProvider {

	private List<StrategySelectionRow> strategySelectionRows;

	public StrategyContentProvider(
			List<StrategySelectionRow> strategySelectionRows) {
		this.strategySelectionRows = strategySelectionRows;
	}

	@Override
	public void dispose() {
		// nothing to do
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// nothing to do
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return this.strategySelectionRows.toArray();
	}
}