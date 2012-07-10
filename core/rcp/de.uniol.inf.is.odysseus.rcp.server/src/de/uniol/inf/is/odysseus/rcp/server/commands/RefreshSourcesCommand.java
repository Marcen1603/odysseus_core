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
package de.uniol.inf.is.odysseus.rcp.server.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

import de.uniol.inf.is.odysseus.rcp.server.OdysseusRCPServerPlugIn;
import de.uniol.inf.is.odysseus.rcp.server.util.ViewHelper;
import de.uniol.inf.is.odysseus.rcp.server.views.source.SourcesView;

public class RefreshSourcesCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		SourcesView viewer = ViewHelper.getView(OdysseusRCPServerPlugIn.SOURCES_VIEW_ID, event);
		if( viewer != null ) 
			viewer.refresh();
		else
			return false;
		return true;
	}
	
}
