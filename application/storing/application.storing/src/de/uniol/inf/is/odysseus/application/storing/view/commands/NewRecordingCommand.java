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

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.application.storing.controller.RecordingController;
import de.uniol.inf.is.odysseus.application.storing.view.dialogs.StartNewRecordingDialog;
import de.uniol.inf.is.odysseus.database.connection.DatabaseConnectionDictionary;
import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.usermanagement.ISession;
import de.uniol.inf.is.odysseus.usermanagement.client.GlobalState;

/**
 * 
 * @author Dennis Geesen
 * Created at: 10.11.2011
 */
public class NewRecordingCommand extends AbstractRecordingCommand {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		IDataDictionary dd = GlobalState.getActiveDatadictionary();
		ISession user = GlobalState.getActiveSession(OdysseusRCPPlugIn.RCP_USER_TOKEN);

		if (DatabaseConnectionDictionary.getInstance().getConnections().isEmpty()) {
			MessageBox mb = new MessageBox(shell);
			mb.setText("No database found");
			mb.setMessage("There are no database connections!\nCreate one first!");
			mb.open();
			return null;
		}

		if (dd.getStreamsAndViews(user).size() <= 0) {
			MessageBox mb = new MessageBox(shell);
			mb.setText("No sources found");
			mb.setMessage("There are no sources that can be recorded!\nCreate one first!");
			mb.open();
			return null;
		}
		
	

		StartNewRecordingDialog dialog = new StartNewRecordingDialog(shell, dd, user);
		int ergebnis = dialog.open();
		if (ergebnis == Window.OK) {			
			RecordingController.getInstance().createRecording(dialog.getRecordingName(), dialog.getDatabaseConnection(), dialog.getTableName(), dialog.getFromStream());
		}
		return null;
	}

}
