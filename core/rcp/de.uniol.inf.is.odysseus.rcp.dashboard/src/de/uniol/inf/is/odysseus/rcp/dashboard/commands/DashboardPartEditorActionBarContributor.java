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
package de.uniol.inf.is.odysseus.rcp.dashboard.commands;

import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.DeleteRetargetAction;
import org.eclipse.gef.ui.actions.RedoRetargetAction;
import org.eclipse.gef.ui.actions.UndoRetargetAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionFactory;

/**
 * @author DGeesen
 * 
 */
public class DashboardPartEditorActionBarContributor extends ActionBarContributor {

	// public void setActiveEditor(IEditorPart targetEditor) {
	//
	// DashboardPartEditor editor = (DashboardPartEditor) targetEditor;
	// IDashboardActionBarContributor eabc = editor.getDashboardPart().getEditorActionBarContributor();
	// if (eabc != null) {
	// eabc.setActiveEditor(editor, editor.getDashboardPart(), getActionBars());
	// }
	//
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.ActionBarContributor#setActiveEditor(org.eclipse.ui.IEditorPart)
	 */
	@Override
	public void setActiveEditor(IEditorPart editor) {
		if (editor != null) {
			ActionRegistry registry = (ActionRegistry) editor.getAdapter(ActionRegistry.class);
			if(registry!=null){
				super.setActiveEditor(editor);
			}
		}
		
	}

	@Override
	protected void buildActions() {
		addRetargetAction(new UndoRetargetAction());
		addRetargetAction(new RedoRetargetAction());
		addRetargetAction(new DeleteRetargetAction());
	}

	@Override
	public void contributeToToolBar(IToolBarManager toolBarManager) {
		super.contributeToToolBar(toolBarManager);
		toolBarManager.add(getAction(ActionFactory.UNDO.getId()));
		toolBarManager.add(getAction(ActionFactory.REDO.getId()));
		toolBarManager.add(getAction(ActionFactory.DELETE.getId()));
	}

	@Override
	protected void declareGlobalActionKeys() {
		// Do nothing. This is an abstract function in the parent class.
	}

}
