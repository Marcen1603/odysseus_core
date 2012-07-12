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

import java.util.Set;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilder;

public class PQLOperatorsLabelProvider implements ILabelProvider {

	@Override
	public void addListener(ILabelProviderListener listener) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
	}

	@Override
	public Image getImage(Object element) {
		if( element instanceof IOperatorBuilder ) {
			return PQLEditorTextPlugIn.getDefault().getImageRegistry().get("pqlOperator");
		}
		if( element instanceof IParameter ) {
			if( ((IParameter<?>)element).isMandatory() ) {
				return PQLEditorTextPlugIn.getDefault().getImageRegistry().get("pqlAttribute");
			} else {
				return PQLEditorTextPlugIn.getDefault().getImageRegistry().get("pqlOptionalAttribute");
			}
		}
		
		return null;
	}

	@Override
	public String getText(Object element) {
		if( element instanceof IOperatorBuilder ) {
			return ((IOperatorBuilder)element).getName().toUpperCase();
		}
		if( element instanceof IParameter ) {
			IParameter<?> param = (IParameter<?>)element;
			String name = param.getName().toUpperCase();
			if( !param.isMandatory() ) {
				name = "[Optional] " + name;
			}
			return name;
		}
		if( element instanceof String ) {
			return (String)element;
		}
		
		if( element instanceof Set ) {
			return "Set";
		}
		
		return null;
	}

}
