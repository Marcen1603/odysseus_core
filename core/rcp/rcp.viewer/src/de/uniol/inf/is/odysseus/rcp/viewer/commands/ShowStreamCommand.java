/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.rcp.viewer.commands;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.rcp.util.SelectionProvider;
import de.uniol.inf.is.odysseus.rcp.viewer.OdysseusRCPViewerPlugIn;
import de.uniol.inf.is.odysseus.rcp.viewer.editors.StreamEditorInput;
import de.uniol.inf.is.odysseus.rcp.viewer.extension.IStreamEditorType;
import de.uniol.inf.is.odysseus.rcp.viewer.extension.StreamEditorRegistry;
import de.uniol.inf.is.odysseus.rcp.viewer.extension.StreamExtensionDefinition;
import de.uniol.inf.is.odysseus.rcp.viewer.view.IOdysseusNodeView;

public class ShowStreamCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		String editorTypeID = event.getParameter(OdysseusRCPViewerPlugIn.STREAM_EDITOR_TYPE_PARAMETER_ID);

		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		IWorkbenchPage page = window.getActivePage();

		ISelection selection = window.getActivePage().getSelection();
		if (selection == null)
			return null;

		IPhysicalOperator opForStream = null;
		if (selection instanceof IStructuredSelection) {
			List<Object> selections = SelectionProvider.getSelection(event);
			nextSelection: for (Object selectedObject : selections) {

				if (selectedObject instanceof IQuery) {
					IQuery query = (IQuery) selectedObject;
					if (query.getRoots().size() > 0)
						opForStream = query.getRoots().get(0);
					else
						opForStream = query.getPhysicalChilds().get(0);

				}

				if (selectedObject instanceof IOdysseusNodeView) {
					IOdysseusNodeView nodeView = (IOdysseusNodeView) selectedObject;

					// Auswahl holen
					opForStream = nodeView.getModelNode().getContent();
				}

				if (opForStream != null) {

					// schauen, ob Editor für den Graphen schon offen ist
					for (IEditorReference editorRef : page.getEditorReferences()) {
						try {
							IEditorInput i = editorRef.getEditorInput();
							if (i instanceof StreamEditorInput) {
								StreamEditorInput gInput = (StreamEditorInput) i;
								if (gInput.getPhysicalOperator() == opForStream && gInput.getEditorTypeID().equals(editorTypeID)) {
									page.activate(editorRef.getPart(false));
									continue nextSelection;
								}
							}
						} catch (PartInitException ex) {
							ex.printStackTrace();
							continue nextSelection;
						}
					}

					// Ediortyp holen und anzeigen
					for (StreamExtensionDefinition def : StreamEditorRegistry.getInstance().getStreamExtensionDefinitions()) {
						if (def.getID().equals(editorTypeID)) {
							try {
								final Object editorType = def.getConfigElement().createExecutableExtension("class");
								if (editorType instanceof IStreamEditorType) {
									IStreamEditorType editor = (IStreamEditorType) editorType;

									// ViewModell erzeugen
									StreamEditorInput input = new StreamEditorInput(opForStream, editor, editorTypeID, def.getLabel());

									try {
										page.openEditor(input, OdysseusRCPViewerPlugIn.STREAM_EDITOR_ID);
										continue nextSelection;
									} catch (PartInitException ex) {
										System.out.println(ex.getStackTrace());
									}
								}
							} catch (CoreException ex) {
								ex.printStackTrace();
							}
						}
					}
				}
			}
		}
		return null;
	}
}
