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

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import measure.windperformancercp.views.performance.PerformanceWizard;
import measure.windperformancercp.views.performance.PerformanceWizardDialog;

public class ShowNewQueryWizardDialogCommand extends AbstractHandler implements
		IHandler {
	public static final String ID = "measure.windperformancercp.ShowNewQueryWizardDialog";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		Shell parent = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
		final Shell dialogShell = new Shell(parent);
		
		//TODO: evtl. von AbstractUIDialog ableiten/IUserIDIalog
			PerformanceWizardDialog dialog = new PerformanceWizardDialog(dialogShell, new PerformanceWizard()); 
			
			//dialog.create();
			dialog.open();
			
		return null;
	}

}
