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
package de.uniol.inf.is.odysseus.rcp.editor.graph.editors.dnd;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.dnd.AbstractTransferDropTargetListener;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.jface.viewers.TreeSelection;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;
import de.uniol.inf.is.odysseus.core.planmanagement.ViewInformation;
import de.uniol.inf.is.odysseus.rcp.editor.graph.Activator;
import de.uniol.inf.is.odysseus.rcp.util.OperatorDragTransfer;

/**
 * @author DGeesen
 * 
 */
public class OperatorDropListener extends AbstractTransferDropTargetListener {

	public OperatorDropListener(GraphicalViewer viewer) {
		super(viewer, OperatorDragTransfer.getTransfer());
	}

	@Override
	protected Request createTargetRequest() {

		TreeSelection selection = (TreeSelection) OperatorDragTransfer
				.getTransfer().getSelection();
		if (selection != null) {
			CreateRequest request = new CreateRequest();
			Object fe = selection.getFirstElement();
			if (fe instanceof ViewInformation) {
				ViewInformation entry = (ViewInformation) selection
						.getFirstElement();
				String resourceName = entry.getName().toString();
				String operatorName = OperatorDragTransfer.getTransfer()
						.getOperatorName();
				LogicalOperatorInformation loi = Activator
						.getDefault()
						.getExecutor()
						.getOperatorInformation(operatorName,
								Activator.getDefault().getCaller());
				request.setFactory(new DropOperatorNodeFactory(loi,
						resourceName));
			}
			return request;
		}
		return super.createTargetRequest();
	}

	@Override
	protected void updateTargetRequest() {
		if (getTargetRequest() instanceof CreateRequest) {
			CreateRequest request = (CreateRequest) getTargetRequest();
			request.setLocation(getDropLocation());
		}
	}
}
