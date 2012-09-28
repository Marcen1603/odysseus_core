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
package de.uniol.inf.is.odysseus.rcp.commands;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.util.SelectionProvider;

/**
 * @author Dennis Geesen
 *
 */
public class ShowQueryTextCommand extends AbstractHandler implements IHandler {

	private static final Logger LOG = LoggerFactory.getLogger(QueryViewCopyCommand.class);

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		List<Object> selections = SelectionProvider.getSelection(event);		
		for (Object selection : selections) {
			IExecutor executor = OdysseusRCPPlugIn.getExecutor();
			if (executor != null) {
				int id = (Integer)selection;
				ILogicalQuery query = executor.getLogicalQueryById(id);
				String queryText = query.getQueryText();
				Shell shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
				MessageBox box = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
				box.setText("Query text for query "+id);
				box.setMessage(queryText);
				box.open();
			} else {
				LOG.error("Executor is not set");
			}						
		}				
		return null;
	}

}
