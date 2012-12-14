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

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilder;

public class PQLOperatorsLabelProvider extends CellLabelProvider {

	@Override
	public String getToolTipText(Object element) {
		if (element instanceof IOperatorBuilder) {
			return ((IOperatorBuilder) element).getDoc();
		}
		if (element instanceof IParameter) {
			return ((IParameter<?>) element).getDoc();
		}
		if (element instanceof String) {
			return (String) element;
		}

		if (element instanceof Set) {
			return "Set";
		}

		return null;
	}
	
	@Override
	public Point getToolTipShift(Object object) {
		return new Point(5,5);
	}
	
	@Override
	public int getToolTipDisplayDelayTime(Object object) {
		return 250;
	}
	
	@Override
	public void update(ViewerCell cell) {
		Object element = cell.getElement();
		String text = "";
		Image image = null;
		if (element instanceof IOperatorBuilder) {
			text = ((IOperatorBuilder) element).getName().toUpperCase();
			image = PQLEditorTextPlugIn.getImageManager().get("pqlOperator");
		}
		if (element instanceof IParameter) {
			IParameter<?> param = (IParameter<?>) element;
			String name = param.getName().toUpperCase();
			if (!param.isMandatory()) {
				name = "[Optional] " + name;
			}
			text = name;
			
			if( ((IParameter<?>)element).isMandatory() ) {
				image = PQLEditorTextPlugIn.getImageManager().get("pqlAttribute");
			} else {
				image = PQLEditorTextPlugIn.getImageManager().get("pqlOptionalAttribute");
			}
		}
		if (element instanceof String) {
			text = (String) element;
		}

		if (element instanceof Set) {
			text = "Set";
		}

		cell.setText(text);
		cell.setImage(image);
	}

}
