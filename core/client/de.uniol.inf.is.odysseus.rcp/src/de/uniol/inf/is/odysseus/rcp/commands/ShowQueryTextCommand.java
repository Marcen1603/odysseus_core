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

import java.io.ByteArrayInputStream;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
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
				ILogicalQuery query = executor.getLogicalQueryById(id, OdysseusRCPPlugIn.getActiveSession());
				String queryText = query.getQueryText();
				
				IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				
				try {
					IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("[Generated]");
					if( !project.exists() ) {
						project.create(null);
					}
					if( !project.isOpen() ) {
						project.open(null);
					}
					
					IFile file = project.getFile(query.getName() + "." + determineFileExtension(query));
					if( file.exists() ) {
						file.setContents(new ByteArrayInputStream(queryText.getBytes()), IResource.FORCE, null);
					} else {
						file.create(new ByteArrayInputStream(queryText.getBytes()), false, null);
					}
					IDE.openEditor(activePage, file, "de.uniol.inf.is.odysseus.rcp.editor.OdysseusScriptEditor");
					
				} catch (PartInitException e) {
					LOG.error("Could not open editor for showing the query text", e);
				} catch (CoreException e) {
					LOG.error("Could not open editor for showing the query text", e);
				}
				
			} else {
				LOG.error("Executor is not set");
			}						
		}				
		return null;
	}

	private static String determineFileExtension(ILogicalQuery query) {
		switch( query.getParserId() ) {
		case "PQL":
			return "pql";
		case "CQL":
			return "cql";
		case "OdysseusScript":
			return "qry";
		default:
			return "tmp";
		}
	}

}
