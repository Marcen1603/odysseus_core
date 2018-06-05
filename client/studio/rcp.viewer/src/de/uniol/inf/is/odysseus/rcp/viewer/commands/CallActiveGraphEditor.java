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
package de.uniol.inf.is.odysseus.rcp.viewer.commands;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.viewer.OdysseusRCPViewerPlugIn;
import de.uniol.inf.is.odysseus.rcp.viewer.editors.impl.PhysicalGraphEditorInput;
import de.uniol.inf.is.odysseus.rcp.viewer.model.create.IModelProvider;
import de.uniol.inf.is.odysseus.rcp.viewer.model.create.OdysseusModelProviderMultipleSinkOneWay;

public class CallActiveGraphEditor extends AbstractHandler implements IHandler {

	private static final Logger LOG = LoggerFactory.getLogger(CallActiveGraphEditor.class);

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		final IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		final IWorkbenchPage page = window.getActivePage();

		final IExecutor executor = OdysseusRCPViewerPlugIn.getExecutor();

		final Collection<Integer> queryIds = executor.getLogicalQueryIds(OdysseusRCPPlugIn.getActiveSession());
		try {
			final ArrayList<IPhysicalOperator> roots = new ArrayList<IPhysicalOperator>();
			for (final Integer queryId : queryIds) {
				roots.addAll(executor.getPhysicalRoots(queryId, OdysseusRCPPlugIn.getActiveSession()));
			}
			if (roots.isEmpty()) {
				return null;
			}

			final IModelProvider<IPhysicalOperator> provider = new OdysseusModelProviderMultipleSinkOneWay(roots);

			final PhysicalGraphEditorInput input = new PhysicalGraphEditorInput(provider);
			page.openEditor(input, OdysseusRCPViewerPlugIn.GRAPH_EDITOR_ID);

		} catch (final Exception ex) {
			LOG.error("Exception during opening editor for current execution graph", ex);
		}

		return null;
	}

}
