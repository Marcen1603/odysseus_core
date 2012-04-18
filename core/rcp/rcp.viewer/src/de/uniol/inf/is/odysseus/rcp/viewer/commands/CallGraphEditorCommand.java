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
package de.uniol.inf.is.odysseus.rcp.viewer.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.rcp.util.SelectionProvider;
import de.uniol.inf.is.odysseus.rcp.viewer.OdysseusRCPViewerPlugIn;
import de.uniol.inf.is.odysseus.rcp.viewer.editors.impl.PhysicalGraphEditorInput;
import de.uniol.inf.is.odysseus.rcp.viewer.model.create.IModelProvider;
import de.uniol.inf.is.odysseus.rcp.viewer.model.create.OdysseusModelProviderMultipleSinkOneWay;
import de.uniol.inf.is.odysseus.rcp.viewer.model.create.OdysseusModelProviderSinkOneWay;

public class CallGraphEditorCommand extends AbstractHandler implements IHandler {

    private static final Logger LOG = LoggerFactory.getLogger(CallGraphEditorCommand.class);
    
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		Display.findDisplay(Thread.currentThread()).asyncExec(new Runnable() {
			@Override
            public void run() {
				IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
				IWorkbenchPage page = window.getActivePage();

				for (Object selection : SelectionProvider.getSelection(event)) {
					if (selection instanceof IPhysicalQuery) {
						openGraphEditor(page, (IPhysicalQuery)selection);
					} else if( selection instanceof Integer ) {
					    Integer queryID = (Integer)selection;
					    IExecutor executor = OdysseusRCPViewerPlugIn.getExecutor();
					    if( executor instanceof IServerExecutor ) {
					        IServerExecutor serverExecutor = (IServerExecutor)executor;
					        openGraphEditor(page, serverExecutor.getExecutionPlan().getQuery(queryID));
					    } else {
					        LOG.error("Could not show physical graphs outside server.");
					    }
					}
				}
			}

		});
		return null;

	}
	
	private static void openGraphEditor(IWorkbenchPage page, IPhysicalQuery query) {
        List<IPhysicalOperator> graph = query.getRoots();

        List<ISink<?>> sinks = new ArrayList<ISink<?>>();
        for( IPhysicalOperator op : graph ) {
        	sinks.add((ISink<?>)op);
        }
        
        IModelProvider<IPhysicalOperator> provider = null;
        if( sinks.size() == 1 ) {
        	provider = new OdysseusModelProviderSinkOneWay(sinks.get(0), query);
        } else {
        	provider = new OdysseusModelProviderMultipleSinkOneWay(sinks, query);
        }
        
        PhysicalGraphEditorInput input = new PhysicalGraphEditorInput(provider, "Query " + query.getID());

        try {
            page.openEditor(input, OdysseusRCPViewerPlugIn.GRAPH_EDITOR_ID);

        } catch (PartInitException ex) {
            System.out.println(ex.getStackTrace());
        }
	}

}
