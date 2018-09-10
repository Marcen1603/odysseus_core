/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.uniol.inf.is.odysseus.rcp.editor.text.pql;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation;

public class PQLOperatorsContentProvider implements ITreeContentProvider {

	private boolean showOptionalParameters = true;

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof List) {
			List<LogicalOperatorInformation> set = (List<LogicalOperatorInformation>) parentElement;
			List<LogicalOperatorInformation> nonHiddenSet = selectNonHidden(set);
			return nonHiddenSet.toArray();
		}
		if (parentElement instanceof LogicalOperatorInformation) {
			LogicalOperatorInformation builder = (LogicalOperatorInformation) parentElement;
			List<LogicalParameterInformation> manParams = Lists.newArrayList();
			List<LogicalParameterInformation> optParams = Lists.newArrayList();
			for (LogicalParameterInformation param : builder.getParameters()) {
				if (param.isMandatory()) {
					manParams.add(param);
				} else if (showOptionalParameters) {
					optParams.add(param);
				}
			}
			List<LogicalParameterInformation> result = Lists.newArrayList();
			result.addAll(manParams);
			result.addAll(optParams);
			return result.toArray();
		}

		return null;
	}

	private List<LogicalOperatorInformation> selectNonHidden(
			List<LogicalOperatorInformation> builders) {
		List<LogicalOperatorInformation> nonHidden = Lists.newArrayList();
		for (LogicalOperatorInformation builder : builders) {
			if (!builder.isHidden()) {
				nonHidden.add(builder);
			}
		}
		return nonHidden;
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof List) {
			return !((List<?>) element).isEmpty();
		}
		if (element instanceof LogicalOperatorInformation) {
			return !((LogicalOperatorInformation) element).getParameters()
					.isEmpty();
		}

		return false;
	}

	public void showOptionalParameters(boolean show) {
		showOptionalParameters = show;
	}

}
