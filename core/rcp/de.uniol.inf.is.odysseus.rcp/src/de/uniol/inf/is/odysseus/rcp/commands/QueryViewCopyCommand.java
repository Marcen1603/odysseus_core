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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.handlers.HandlerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.util.SelectionProvider;

public class QueryViewCopyCommand extends AbstractHandler implements IHandler {

	private static final Logger LOG = LoggerFactory.getLogger(QueryViewCopyCommand.class);

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		List<Object> selections = SelectionProvider.getSelection(event);
		List<String> texts = new ArrayList<>();				
		for (Object selection : selections) {
			IExecutor executor = OdysseusRCPPlugIn.getExecutor();
			if (executor != null) {
				int id = (Integer)selection;
				ILogicalQuery query = executor.getLogicalQueryById(id);
				String queryText = query.getQueryText();
				texts.add(queryText);				
			} else {
				LOG.error("Executor is not set");
			}						
		}
		
		if(texts.size()==1){
			transferToClipboard(texts.get(0), event);
		}else{
			if(texts.size()>1){
				String all = "";
				String sep = "";
				for(String text : texts){
					all = all + sep + text;
					sep = "\n\n///next query\n\n";							
				}
				transferToClipboard(all, event);
			}
		}
		
		

		
		return null;
	}
	
	private void transferToClipboard(String text, ExecutionEvent event){
		Display display = HandlerUtil.getActiveWorkbenchWindow(event).getShell().getDisplay();
		Clipboard clipboard = new Clipboard(display);
		TextTransfer textTransfer = TextTransfer.getInstance();
		Transfer[] transfers = new Transfer[]{textTransfer};
		Object[] data = new Object[]{text};
		clipboard.setContents(data, transfers);
		clipboard.dispose();
	}
}
