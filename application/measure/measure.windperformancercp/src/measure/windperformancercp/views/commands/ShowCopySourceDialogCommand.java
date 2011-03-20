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

import measure.windperformancercp.model.sources.ISource;
import measure.windperformancercp.model.sources.MetMast;
import measure.windperformancercp.model.sources.WindTurbine;
import measure.windperformancercp.views.AbstractUIDialog;
import measure.windperformancercp.views.IPresenter;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import measure.windperformancercp.views.sources.SourceDialog;
import measure.windperformancercp.views.sources.SourceDialogPresenter;

/**
 * Encapsulates the opening of a new source dialog and fills in the values of the selected source
 * selection provider is the tabViewer in ManageSourceView
 * @author Diana von Gallera
 *
 */
public class ShowCopySourceDialogCommand extends AbstractHandler implements
		IHandler {
	public static final String ID = "measure.windperformancercp.ShowCopySourceDialog";
		 
	@SuppressWarnings(value="unchecked")
	    @Override
	    public Object execute(ExecutionEvent event) throws ExecutionException {
	    	
	    	ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection();
		
	    	if(selection != null & selection instanceof IStructuredSelection){
				IStructuredSelection strucSelection = (IStructuredSelection) selection;
							
				for (Iterator<Object> iterator = (Iterator<Object>) strucSelection.iterator(); iterator.hasNext();) {
					Object element = iterator.next();
					
					ISource source = (ISource) element;
					
					Shell parent = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
					final Shell dialogShell = new Shell(parent);
					AbstractUIDialog dialog = new SourceDialog(dialogShell);
					IPresenter presenter = dialog.getPresenter();
					dialog.create();
					
					if(source instanceof MetMast){					
						MetMast mm = new MetMast((MetMast)source);
						((SourceDialogPresenter) presenter).feedDialog(mm);
					}
					else{
						if(source instanceof WindTurbine){
							WindTurbine wt = new WindTurbine((WindTurbine)source);
							((SourceDialogPresenter) presenter).feedDialog(wt);
						}
						else return null;
					}

					dialog.open();
								
					}
				
				return null;
	        }
	        if(selection == null){
	        	System.out.println("ShowCopySourceCommand: Selection was null!");
	        } else{
	        	System.out.println("ShowCopySourceCommand: Selection was not null but did not match!");
	        }

	        return null;
	    }

}
