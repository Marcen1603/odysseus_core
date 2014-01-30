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
package de.uniol.inf.is.odysseus.rcp.editor.graph.editors.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.editparts.OperatorNodeEditPart;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.OperatorNode;

/**
 * @author DGeesen
 * 
 */
public class CopyAction extends SelectionAction {

	/**
	 * @param part
	 */
	public CopyAction(IWorkbenchPart part) {
		super(part);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	@Override
	protected boolean calculateEnabled() {
		Command cmd = createCopyCommand(getSelectedObjects());
		if (cmd == null)
			return false;
		return cmd.canExecute();
	}

	@Override
	public void run() {
		execute(createCopyCommand(getSelectedObjects()));
	}

	/**
	 * @param selectedObjects
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Command createCopyCommand(List objects) {
		if (objects.isEmpty())
			return null;
		if (!(objects.get(0) instanceof OperatorNodeEditPart))
			return null;

		OperatorNodeCopyCommand command = new OperatorNodeCopyCommand();
		List<OperatorNode> nodes = new ArrayList<>();
		for (Object o : objects) {
			if (o instanceof OperatorNodeEditPart) {
				OperatorNodeEditPart editPart = (OperatorNodeEditPart) o;
				nodes.add((OperatorNode) editPart.getModel());
			}
		}
		command.setNodes(nodes);
		return command;
	}

	@Override
	protected void init() {
		super.init();
		setText("&Copy");
		setToolTipText("Copy");
		setId(ActionFactory.COPY.getId());
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
		setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY_DISABLED));
		setEnabled(false);
	}

}
