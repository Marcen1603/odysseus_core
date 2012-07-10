/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.viewer.server.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.viewer.server.opbreak.OperatorBreak;
import de.uniol.inf.is.odysseus.rcp.viewer.server.opbreak.OperatorBreakManager;
import de.uniol.inf.is.odysseus.rcp.viewer.view.IOdysseusNodeView;

public class ResumeOperatorCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		ISelection selection = window.getActivePage().getSelection();

		if (selection == null)
			return null;

		if (selection instanceof IStructuredSelection) {

			IStructuredSelection structSelection = (IStructuredSelection) selection;
			
			Object selObject = structSelection.getFirstElement();
			OperatorBreak ob = null;
			
			if( selObject instanceof IOdysseusNodeView) {
				IOdysseusNodeView node = (IOdysseusNodeView) selObject;
	
				IPhysicalOperator operator = node.getModelNode().getContent();
	
				for( OperatorBreak o : OperatorBreakManager.getInstance().getAll()) {
					if( o.getOperator() == operator ) {  
						ob = o;
						break;
					}
				}
				
			} else if( selObject instanceof OperatorBreak ) {
				ob = (OperatorBreak)selObject;
				
			}

			if( ob != null ) {
				ob.endBreak();
//				OperatorBreakManager.getInstance().remove(ob);
			}
			else
				System.out.println("No OperatorBreak found!");
			
		}
		
		return false;
	}	

}
