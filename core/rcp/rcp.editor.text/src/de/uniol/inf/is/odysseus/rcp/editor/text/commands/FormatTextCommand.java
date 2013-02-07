/********************************************************************************** 
 * Copyright 2013 The Odysseus Team
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
package de.uniol.inf.is.odysseus.rcp.editor.text.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

import de.uniol.inf.is.odysseus.rcp.editor.text.editors.OdysseusScriptEditor;

/**
 * @author Dennis Geesen
 * 
 */
public class FormatTextCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IEditorPart part = HandlerUtil.getActiveEditor(event);
		if (part instanceof OdysseusScriptEditor) {
			ITextOperationTarget target = (ITextOperationTarget) part.getAdapter(ITextOperationTarget.class);
			if (target instanceof ISourceViewer) {
				ISourceViewer textViewer = (ISourceViewer) target;
				((ITextOperationTarget) textViewer).doOperation(ISourceViewer.FORMAT);
			}
		}
		return null;
	}
}
