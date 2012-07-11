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

import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilder;

public class PQLOperatorsContentProvider implements ITreeContentProvider {

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

	@Override
	public Object[] getChildren(Object parentElement) {
		if( parentElement instanceof List ) {
			List<?> set = (List<?>)parentElement;
			return set.toArray();
		}
		if( parentElement instanceof IOperatorBuilder ) {
			IOperatorBuilder builder = (IOperatorBuilder)parentElement;
			List<IParameter<?>> manParams = Lists.newArrayList();
			List<IParameter<?>> optParams = Lists.newArrayList();
			for( IParameter<?> param : builder.getParameters()) {
				if( param.isMandatory() ) {
					manParams.add(param);
				} else {
					optParams.add(param);
				}
			}
			List<IParameter<?>> result = Lists.newArrayList();
			result.addAll(manParams);
			result.addAll(optParams);
			return result.toArray();
		}
		
		return null;
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if( element instanceof List ) {
			return !((List<?>)element).isEmpty();
		}
		if( element instanceof IOperatorBuilder) {
			return !((IOperatorBuilder)element).getParameters().isEmpty();
		}
		
		return false;
	}

}
