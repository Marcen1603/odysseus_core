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
import org.eclipse.swt.SWT;

import de.uniol.inf.is.odysseus.application.storing.RecordingException;
import de.uniol.inf.is.odysseus.application.storing.controller.RecordEntry;
import de.uniol.inf.is.odysseus.application.storing.controller.RecordingController;

/**
 * 
 * @author Dennis Geesen Created at: 29.11.2011
 */
public class DropRecordingCommand extends AbstractRecordingCommand {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Object selected = super.getCurrentSelection(event);
		if (selected instanceof RecordEntry) {
			if (confirmDialog("Warning", "Do you really want to remove this recording?")) {
				boolean dropTable = confirmDialog("Drop Table?", "Do you also want to drop the corresponding table from database?", SWT.ICON_QUESTION);
				RecordEntry record = (RecordEntry) selected;
				try {
					RecordingController.getInstance().dropRecording(record.getName(), dropTable);
				} catch (RecordingException e) {
					showError(e.getMessage());
				}
			}
		} else {
			return null;
		}
		return null;
	}

}
