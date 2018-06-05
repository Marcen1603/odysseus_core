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
package de.uniol.inf.is.odysseus.rcp.editor.graph.editors;

import java.util.EventObject;
import java.util.Iterator;

import org.eclipse.gef.commands.CommandStackListener;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.UpdateAction;

/**
 * @author DGeesen
 * 
 */
@SuppressWarnings("deprecation")
public class GraphEditorListener implements CommandStackListener {
	private ActionRegistry actionRegistry;

	public GraphEditorListener(ActionRegistry registry) {
		this.actionRegistry = registry;
	}

	@Override
	public void commandStackChanged(EventObject event) {
		Iterator<?> iterator = actionRegistry.getActions();
		while (iterator.hasNext()) {
			Object action = iterator.next();
			if (action instanceof UpdateAction)
				((UpdateAction) action).update();
		}
	}

}
