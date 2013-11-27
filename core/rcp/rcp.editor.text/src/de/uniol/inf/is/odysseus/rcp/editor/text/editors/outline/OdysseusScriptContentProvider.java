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
package de.uniol.inf.is.odysseus.rcp.editor.text.editors.outline;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.uniol.inf.is.odysseus.core.collection.NamedList;

public class OdysseusScriptContentProvider implements ITreeContentProvider {

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof ScriptNode) {
			
			try {
				List<Object> childs = new ArrayList<>();
				//TODO: really periodically sending the script to server?!
//				ISession user = OdysseusRCPPlugIn.getActiveSession();
//				String text = ((ScriptNode) parentElement).getString();
//				// replacements
//				NamedList<Entry<String, String>> replacements = new NamedList<>("Definitions");
//				replacements.addAll(OdysseusRCPEditorTextPlugIn.getScriptParser().getReplacements(text).entrySet());
//				childs.add(replacements);			
//				// statements				
//				childs.addAll(OdysseusRCPEditorTextPlugIn.getScriptParser().parseScript(text, user, Context.empty()));

				return childs.toArray();
			} catch (Exception e) {
				// if we cannot parse it yet, script has no children
				return new Object[] {};
			}
		}
		if (parentElement instanceof NamedList) {
			return ((NamedList<?>) parentElement).toArray();
		}
		// if we have any set, we want the children
		if (parentElement instanceof Set) {
			return ((Set<?>) parentElement).toArray();
		}
		// else we have no children
		return new Object[] {};
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof ScriptNode)
			return true;
		if (element instanceof NamedList)
			return true;
		return false;
	}

	@Override
	public void dispose() {
		// we don't need this
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// we don't need this
	}

}
