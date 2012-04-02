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
package de.uniol.inf.is.odysseus.rcp.commands;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.ui.handlers.HandlerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.rcp.status.StatusBarManager;
import de.uniol.inf.is.odysseus.rcp.util.SelectionProvider;

public class QueryViewCopyCommand extends AbstractHandler implements IHandler {

	private static final Logger LOG = LoggerFactory.getLogger(QueryViewCopyCommand.class);
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		List<Object> selections = SelectionProvider.getSelection(event);
		String text = "";
		for (Object selection : selections) {
			LOG.debug("Selection for QueryViewCopy: " + selection);
			
			StatusBarManager.getInstance().setMessage("Copying querytexts not supported yet");
		}
		if (!text.isEmpty()) {
			Clipboard cb = new Clipboard(HandlerUtil.getActiveWorkbenchWindow(event).getShell().getDisplay());
			TextTransfer textTransfer = TextTransfer.getInstance();
			cb.setContents(new Object[] { text}, new Transfer[] { textTransfer });
			StatusBarManager.getInstance().setMessage("Copy successful");
		}
		return null;
	}
}
