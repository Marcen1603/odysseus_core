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
package de.uniol.inf.is.odysseus.rcp.viewer.view.impl;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.INodeModel;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.INodeModelChangeListener;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IOdysseusNodeModel;
import de.uniol.inf.is.odysseus.rcp.viewer.view.IOdysseusNodeView;

public class OdysseusNodeView extends DefaultNodeView<IPhysicalOperator> implements IOdysseusNodeView, INodeModelChangeListener<IPhysicalOperator> {

	public OdysseusNodeView() {
		this(null);
	}
	
	public OdysseusNodeView(IOdysseusNodeModel data) {
		super(data);
	}

	@Override
	public void nodeModelChanged(INodeModel<IPhysicalOperator> sender) {
	}

	@Override
	public IOdysseusNodeModel getModelNode() {
		return (IOdysseusNodeModel) super.getModelNode();
	}

	@Override
	public void connect() {
		if (getModelNode() != null) {
			getModelNode().addNodeModelChangeListener(this);
		}
	}

	@Override
	public void disconnect() {
		if (getModelNode() != null) {
			getModelNode().removeNodeModelChangeListener(this);
		}
	}

	@Override
	public boolean isVisible() {
		boolean vis = super.isVisible();
		if (!vis) {
			return false;
		}

		if (getModelNode() != null) {
			return getModelNode().getContent().hasOwner();
		}

		return vis;
	}
}
