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
package de.uniol.inf.is.odysseus.rcp.viewer.editors.impl;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.viewer.OdysseusRCPViewerPlugIn;
import de.uniol.inf.is.odysseus.rcp.viewer.manage.impl.OdysseusGraphViewFactory;
import de.uniol.inf.is.odysseus.rcp.viewer.model.create.IModelProvider;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IOdysseusGraphModel;
import de.uniol.inf.is.odysseus.rcp.viewer.swt.symbol.SWTSymbolElementFactory;
import de.uniol.inf.is.odysseus.rcp.viewer.symbol.ISymbolElementFactory;
import de.uniol.inf.is.odysseus.rcp.viewer.view.IOdysseusGraphView;

public class PhysicalGraphEditorInput implements IEditorInput {

	private static final OdysseusGraphViewFactory GRAPH_VIEW_FACTORY = new OdysseusGraphViewFactory();
	private static final ISymbolElementFactory<IPhysicalOperator> SYMBOL_FACTORY = new SWTSymbolElementFactory<IPhysicalOperator>();

	private final String name;
	private final IOdysseusGraphModel model;
	private final IOdysseusGraphView view;
	private final int queryID;

	public PhysicalGraphEditorInput(IModelProvider<IPhysicalOperator> provider) {
		this(provider, -1);
	}

	public PhysicalGraphEditorInput(IModelProvider<IPhysicalOperator> provider, int queryID) {
		this.name = determineName(queryID);
		this.queryID = queryID;

		// Modell erzeugen
		model = (IOdysseusGraphModel) provider.get();

		// View erzezugen
		view = (IOdysseusGraphView) GRAPH_VIEW_FACTORY.createGraphView(model, OdysseusRCPViewerPlugIn.SYMBOL_CONFIGURATION, SYMBOL_FACTORY);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		return null;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		return name;
	}

	public int getQueryID() {
		return queryID;
	}
	
	public boolean hasQueryID() {
		return queryID >= 0;
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return name;
	}

	public IOdysseusGraphView getGraphView() {
		return view;
	}

	public IOdysseusGraphModel getGraphModel() {
		return model;
	}

	private static String determineName(int queryID) {
		return queryID < 0 ? "Current Plan" : "Query " + queryID;
	}
	
	@Override
	public boolean equals(Object obj) {
		if( !(obj instanceof PhysicalGraphEditorInput ) ) {
			return false;
		}
		
		if( obj == this ) {
			return true;
		}
		
		PhysicalGraphEditorInput other = (PhysicalGraphEditorInput)obj;
		return other.queryID == this.queryID;
	}
	
	@Override
	public int hashCode() {
		return this.queryID * 31 + 17;
	}
}
