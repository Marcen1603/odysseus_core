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
package de.uniol.inf.is.odysseus.rcp.viewer.commands;

import java.util.Collection;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.util.SelectionProvider;
import de.uniol.inf.is.odysseus.rcp.viewer.OdysseusRCPViewerPlugIn;
import de.uniol.inf.is.odysseus.rcp.viewer.editors.ChooseOperatorWindow;
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
		if (selection == null) {
			return null;
		}
		
		Collection<IPhysicalOperator> optionalOpForStream = null;
		if (selection instanceof IStructuredSelection) {
			List<Object> selections = SelectionProvider.getSelection(event);
			for (Object selectedObject : selections) {

				if( selectedObject instanceof Integer ) {
                    Integer queryID = (Integer)selectedObject;
                    IExecutor executor = OdysseusRCPViewerPlugIn.getExecutor();
                    optionalOpForStream = chooseOperator( executor.getPhysicalRoots(queryID, OdysseusRCPPlugIn.getActiveSession()));
				}

				if (selectedObject instanceof IOdysseusNodeView) {
					IOdysseusNodeView nodeView = (IOdysseusNodeView) selectedObject;
					
					if( nodeView.getModelNode() == null || nodeView.getModelNode().getContent() == null ) {
						return null;
					}
					
					// Auswahl holen
					optionalOpForStream = Lists.newArrayList(nodeView.getModelNode().getContent());
				}

				
				if (optionalOpForStream != null && !optionalOpForStream.isEmpty()) {

					nextOperator: for( IPhysicalOperator opForStream : optionalOpForStream ) {
						
						for (IEditorReference editorRef : page.getEditorReferences()) {
							try {
								IEditorInput i = editorRef.getEditorInput();
								if (i instanceof StreamEditorInput) {
									StreamEditorInput gInput = (StreamEditorInput) i;
									if (gInput.getPhysicalOperator() == opForStream && gInput.getEditorTypeID().equals(editorTypeID)) {
										page.activate(editorRef.getPart(false));
										continue nextOperator;
									}
								}
							} catch (PartInitException ex) {
								ex.printStackTrace();
								continue nextOperator;
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
											continue nextOperator;
										} catch (PartInitException ex) {
											ex.printStackTrace();
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
		}
		return null;
	}
	
	private static Collection<IPhysicalOperator> chooseOperator( List<IPhysicalOperator> operators ) {
		if( operators.size() == 1 ) {
			return operators;
		}
		
		ChooseOperatorWindow wnd = new ChooseOperatorWindow(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), operators);
		if( wnd.open() == Window.OK) {
			return wnd.getSelectedOperator();
		}
		
		return Lists.newArrayList();
	}
	
}
