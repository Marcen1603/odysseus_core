/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.rcp.editor.editors;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.actions.ActionFactory;

public class MyContextMenuProvider extends ContextMenuProvider {

	private ActionRegistry registry;
	
	public MyContextMenuProvider(EditPartViewer viewer, ActionRegistry registry) {
		super(viewer);
		this.registry = registry;
	}

	@Override
	public void buildContextMenu(IMenuManager menu) {
		// Add standard action groups to the menu
		GEFActionConstants.addStandardActionGroups(menu);
		
		menu.appendToGroup(
				GEFActionConstants.GROUP_UNDO, // target group id
				getAction(ActionFactory.UNDO.getId())); // action to add
		menu.appendToGroup(
				GEFActionConstants.GROUP_UNDO, 
				getAction(ActionFactory.REDO.getId()));
		menu.appendToGroup(
				GEFActionConstants.GROUP_EDIT,
				getAction(ActionFactory.DELETE.getId()));
//		
//		EditPart plan = getViewer().getContents();
//		List<EditPart> children = plan.getChildren();
//		for( EditPart p : children ) {
//			OperatorEditPart part = (OperatorEditPart)p;
//			if( part.getSelected() == EditPart.SELECTED_PRIMARY ) {
//				
//				System.out.println( ((Operator)part.getModel()).getOperatorExtensionDescriptor().getLabel());
//			}
//		}
	}
	
	private IAction getAction(String actionId) {
		return registry.getAction(actionId);
	}
}
