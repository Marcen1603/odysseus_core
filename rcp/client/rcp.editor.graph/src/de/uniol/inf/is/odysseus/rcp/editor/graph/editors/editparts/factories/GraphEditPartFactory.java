/*******************************************************************************
 * Copyright 2013 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.editor.graph.editors.editparts.factories;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.editparts.ConnectionEditPart;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.editparts.GraphEditPart;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.editparts.OperatorNodeEditPart;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.Connection;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.Graph;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.OperatorNode;

/**
 * @author DGeesen
 * 
 */
public class GraphEditPartFactory implements EditPartFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPartFactory#createEditPart(org.eclipse.gef.EditPart, java.lang.Object)
	 */
	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		if (model instanceof Graph) {
			return new GraphEditPart((Graph) model);
		} else if (model instanceof OperatorNode) {
			
			OperatorNodeEditPart part = new OperatorNodeEditPart((OperatorNode) model);
//			context.getViewer().appendSelection(part);
			return part;
		} else if (model instanceof Connection){
			return new ConnectionEditPart((Connection)model);
		} else {
			return null;
		}
	}

}
