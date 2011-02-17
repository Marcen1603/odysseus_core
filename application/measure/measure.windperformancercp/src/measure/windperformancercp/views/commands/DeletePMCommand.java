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
package measure.windperformancercp.views.commands;

import java.util.Iterator;

import measure.windperformancercp.event.InputDialogEvent;
import measure.windperformancercp.event.InputDialogEventType;
import measure.windperformancercp.views.IPresenter;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;

import measure.windperformancercp.model.query.IPerformanceQuery;
import measure.windperformancercp.views.performance.AssignPerformanceMeasView;

public class DeletePMCommand extends AbstractHandler implements
		IHandler {
	public static final String ID = "measure.windperformancercp.DeletePM";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell parent = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
		
		ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection();
		IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();
		IPresenter presenter = ((AssignPerformanceMeasView)page.findView("measure.windperformancercp.assignPMView")).getPresenter();

		
		if(selection != null & selection instanceof IStructuredSelection){
			
			IStructuredSelection strucSelection = (IStructuredSelection) selection;
			for (Iterator<Object> iterator = (Iterator<Object>) strucSelection.iterator(); 
				iterator.hasNext();) {
				Object element = iterator.next();
				
				IPerformanceQuery query = (IPerformanceQuery) element;
				boolean answerOK = MessageDialog.openConfirm(parent,"Delete Confirmation" , 
						"Do you really want to delete performance measure: "+query.getIdentifier()+"?\n");
				if(answerOK){
					presenter.fire(new InputDialogEvent(presenter,InputDialogEventType.DeletePerformanceItem,query));
				}
			}
		}
		
		//TODO: evtl. anzeigen, welche PMs dann davon betroffen sind
		

		return null;
	}
}
