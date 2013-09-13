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
package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.command;

import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.EditorActionBarContributor;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.DashboardGraphicsPart;

/**
 * @author DGeesen
 * 
 */
public class GprahicsActionBarContributor extends EditorActionBarContributor {
	public void setActiveEditor(IEditorPart targetEditor) {
		IActionBars actionBars = getActionBars();
		if (actionBars == null)
			return;
		String undoId = ActionFactory.UNDO.getId();
		String redoId = ActionFactory.REDO.getId();
		String deleteId = ActionFactory.DELETE.getId();
		ActionRegistry actionRegistry = ((DashboardGraphicsPart) targetEditor).getActionRegistry();
		actionBars.setGlobalActionHandler(undoId, actionRegistry.getAction(undoId));
		actionBars.setGlobalActionHandler(redoId, actionRegistry.getAction(redoId));
		actionBars.setGlobalActionHandler(deleteId, actionRegistry.getAction(deleteId));
		actionBars.updateActionBars();
	}

}
