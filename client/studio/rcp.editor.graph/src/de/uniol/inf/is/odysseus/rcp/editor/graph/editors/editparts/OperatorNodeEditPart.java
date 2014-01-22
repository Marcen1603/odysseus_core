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

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.figures.OperatorNodeFigure;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.Connection;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.OperatorNode;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.policies.OperatorNodeComponentEditPolicy;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.policies.OperatorNodeGraphicalNodeEditPolicy;

/**
 * @author DGeesen
 * 
 */
public class OperatorNodeEditPart extends AbstractGraphicalEditPart implements Observer, NodeEditPart {

	public OperatorNodeEditPart(OperatorNode node) {
		setModel(node);
	}

	@Override
	protected IFigure createFigure() {
		return new OperatorNodeFigure((OperatorNode) getModel());
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new OperatorNodeGraphicalNodeEditPolicy());
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new OperatorNodeComponentEditPolicy());
	}

	@Override
	public void refreshVisuals() {
		OperatorNodeFigure figure = (OperatorNodeFigure) getFigure();
		OperatorNode node = (OperatorNode) getModel();
		GraphEditPart parent = (GraphEditPart) getParent();
		Object v = node.getReadableName();
		Object addInfo = getValue(node, "PREDICATE");
		figure.getLabel().setText(v.toString());
		if (addInfo != null) {
			figure.updateToolTip(addInfo.toString());
		}
		figure.setSatisfied(node.isSatisfied());
		Rectangle r = new Rectangle(node.getConstraint().getLocation(), figure.getPreferredSize());
		parent.setLayoutConstraint(this, figure, r);
	}

	private Object getValue(OperatorNode node, String valueType) {
		Object v = null;
		LogicalParameterInformation param = node.getOperatorInformation().getParameter(valueType);
		if (param != null) {
			v = node.getParameterValue(param);
		}
		return v;
	}

	@Override
	public void activate() {
		if (!isActive())
			((OperatorNode) getModel()).addObserver(this);
		super.activate();
	}

	@Override
	public void deactivate() {
		if (isActive())
			((OperatorNode) getModel()).deleteObserver(this);
		super.deactivate();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		refreshVisuals();
		refreshSourceConnections();
		refreshTargetConnections();
	}

	@Override
	protected List<Connection> getModelSourceConnections() {
		return ((OperatorNode) getModel()).getSourceConnections();
	}

	@Override
	protected List<Connection> getModelTargetConnections() {
		return ((OperatorNode) getModel()).getTargetConnections();
	}

	// @Override
	// public void performRequest(Request req) {
	// OperatorNode operatorNode = ((OperatorNode) getModel());
	// if (req.getType() == RequestConstants.REQ_OPEN) {
	// // OperatorSettingsDialog dialog = new
	// OperatorSettingsDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
	// // dialog.setOperator(operatorNode.getOperatorInformation());
	// // dialog.setParameterValues(operatorNode.getParameterValues());
	// // if (Window.OK == dialog.open()) {
	// // operatorNode.setParameterValues(dialog.getParameterValues());
	// // refreshVisuals();
	// // }
	// }
	// super.performRequest(req);
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef
	 * .ConnectionEditPart)
	 */
	@Override
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
		return ((OperatorNodeFigure) getFigure()).getConnectionAnchor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef
	 * .ConnectionEditPart)
	 */
	@Override
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
		return ((OperatorNodeFigure) getFigure()).getConnectionAnchor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef
	 * .Request)
	 */
	@Override
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return ((OperatorNodeFigure) getFigure()).getConnectionAnchor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef
	 * .Request)
	 */
	@Override
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return ((OperatorNodeFigure) getFigure()).getConnectionAnchor();
	}

}
