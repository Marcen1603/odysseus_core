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
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.rcp.viewer.OdysseusRCPViewerPlugIn;
import de.uniol.inf.is.odysseus.rcp.viewer.editors.impl.PhysicalGraphEditorInput;
import de.uniol.inf.is.odysseus.rcp.viewer.model.create.IModelProvider;
import de.uniol.inf.is.odysseus.rcp.viewer.model.create.OdysseusModelProviderSinkOneWay;

public class CallGraphEditorCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		IWorkbenchPage page = window.getActivePage();
		
		ISelection selection = window.getSelectionService().getSelection();
		if( selection instanceof IStructuredSelection ) {
			IStructuredSelection treeSelection = (IStructuredSelection)selection;
			Object obj = treeSelection.getFirstElement();
						
			// Auswahl holen
			List<IPhysicalOperator> graph = null;
			if( obj instanceof IQuery ) {
				IQuery query = (IQuery)obj;
				graph = query.getRoots();
				
				ISink<?> sink = (ISink<?>)graph.get(0); 
				IModelProvider<IPhysicalOperator> provider = new OdysseusModelProviderSinkOneWay(sink, query);
				PhysicalGraphEditorInput input = new PhysicalGraphEditorInput(provider, "Query " + query.getID());
				
				try {
					page.openEditor(input, OdysseusRCPViewerPlugIn.GRAPH_EDITOR_ID);
					
				} catch( PartInitException ex ) {
					System.out.println(ex.getStackTrace());
				}
			}
		}
		
		return null;
	}

}
