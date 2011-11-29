/** Copyright 2011 The Odysseus Team
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

package de.uniol.inf.is.odysseus.application.storing.view.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * 
 * @author Dennis Geesen Created at: 10.11.2011
 */
public abstract class AbstractRecordingCommand extends AbstractHandler {

	public Object getCurrentSelection(ExecutionEvent event) {
		ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection();
		if (selection == null)
			return null;

		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structSelection = (IStructuredSelection) selection;
			return structSelection.getFirstElement();
		}
		return null;
	}

	protected boolean confirmDialog(String title, String message){
		return confirmDialog(title, message, SWT.ICON_WARNING);
	}
	
	protected Shell getParentShell() {
		return PlatformUI.getWorkbench().getDisplay().getActiveShell();
	}
	
	protected boolean confirmDialog(String title, String message, int icon) {		
		
		MessageBox mb = new MessageBox(getParentShell(), icon | SWT.YES | SWT.NO);
		mb.setText(title);
		mb.setMessage(message);
		int ans = mb.open();
		boolean answer = false;
		if (ans == SWT.YES) {
			answer = true;
		}
		return answer;

	}

}
