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
package de.uniol.inf.is.odysseus.rcp.editor.graph.editors.editparts;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.Graph;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.OperatorNode;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.policies.GraphXYLayoutEditPolicy;

/**
 * @author DGeesen
 * 
 */
public class GraphEditPart extends AbstractGraphicalEditPart implements Observer{
	public GraphEditPart(Graph graph) {
		setModel(graph);
	}

	@Override
	protected List<OperatorNode> getModelChildren() {
		ArrayList<OperatorNode> result = new ArrayList<OperatorNode>();
		result.addAll(((Graph)getModel()).getNodes());
		return result;
	}

	@Override
	protected IFigure createFigure() {
		FreeformLayer layer = new FreeformLayer();
		layer.setLayoutManager(new FreeformLayout());
		layer.setBorder(new LineBorder(1));
		return layer;
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new GraphXYLayoutEditPolicy());
	}
	
	
	@Override
	public void activate() {
		if (!isActive())
			((Graph) getModel()).addObserver(this);
		super.activate();
	}

	@Override
	public void deactivate() {
		if (isActive())
			((Graph) getModel()).deleteObserver(this);
		super.deactivate();
	}

	@Override
	public void update(Observable observable, Object message) {
		refreshChildren();
	}


}
