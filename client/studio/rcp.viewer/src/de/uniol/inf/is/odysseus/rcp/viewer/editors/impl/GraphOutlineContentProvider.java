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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.ISubscription;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFConstraint;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.viewer.view.INodeView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.IOdysseusGraphView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.IOdysseusNodeView;

// TODO: Double Code see also ResourceInformationContentProvider

public class GraphOutlineContentProvider implements ITreeContentProvider {

	private static final Logger LOG = LoggerFactory
			.getLogger(GraphOutlineContentProvider.class);
	private IOdysseusGraphView activeGraph;

	@Override
	public Object[] getChildren(Object parentElement) {

		if (parentElement instanceof IOdysseusGraphView) {
			IOdysseusGraphView graph = (IOdysseusGraphView) parentElement;

			ArrayList<Object> list = new ArrayList<Object>();
			for (INodeView<IPhysicalOperator> node : graph.getViewedNodes()) {
				if (node.getModelNode() != null)
					list.add(node);
			}

			return list.toArray();
		}

		if (parentElement instanceof IOdysseusNodeView) {
			IOdysseusNodeView node = (IOdysseusNodeView) parentElement;

			Collection<Object> children = new ArrayList<Object>();

			// Add Schemainformation
			if (node.getModelNode().getContent().getOutputSchemas() != null
					&& node.getModelNode().getContent().getOutputSchemas()
							.size() > 0) {
				for (Entry<Integer, SDFSchema> e : node.getModelNode()
						.getContent().getOutputSchemas().entrySet()) {
					if (e.getValue() != null) {
						children.add(e.getValue());
					}
				}
			} else {
				LOG.error("No output Schema for {}!", node.getModelNode()
						.getContent());
			}

			// StringBuffer owner = new StringBuffer("Part of Query: ");
			// for (IOperatorOwner o :
			// node.getModelNode().getContent().getOwner()) {
			// owner.append("#").append(o.getID()).append(" ").append("(#").append(o.hashCode()).append(")");
			// }
			// children.add(new StringWrapper(owner.toString()));

			NamedList n = new NamedList("Part of query");
			for (IOperatorOwner o : node.getModelNode().getContent().getOwner()) {
				n.addValue(o);
			}
			children.add(n);

			if (node.getModelNode().getContent().getUniqueIds().size() > 0) {
				StringBuffer ids = new StringBuffer("UIDs: ");
				for (Entry<IOperatorOwner, Resource> id : node.getModelNode()
						.getContent().getUniqueIds().entrySet()) {
					ids.append(id.getKey() + "-->" + id.getValue()).append(" ");
				}
				children.add(new StringWrapper(ids.toString()));
			}

			// Add Metadatainformation
			for (String type : node.getModelNode().getContent()
					.getProvidedMonitoringData()) {
				children.add(node.getModelNode().getContent()
						.getMonitoringData(type));
			}
			// Add Subscriptions to sources
			if (node.getModelNode().getContent().isSink()) {
				ISink<?> sink = (ISink<?>) node.getModelNode().getContent();
				Collection<? extends ISubscription<?>> subs = sink
						.getSubscribedToSource();
				children.addAll(subs);
			}

			// Add Subscriptions to sources
			if (node.getModelNode().getContent().isSource()) {
				ISource<?> source = (ISource<?>) node.getModelNode()
						.getContent();
				Collection<? extends ISubscription<?>> subs = source
						.getSubscriptions();
				children.addAll(subs);
			}

			// toString-Representation
			children.add(new StringNode(node.getModelNode().getContent()
					.toString()));

			// additional information
			if (!node.getModelNode().getContent().getParameterInfos().isEmpty()) {
				children.add(node.getModelNode().getContent()
						.getParameterInfos());
			}

			return children.toArray();
		}

		if (parentElement instanceof Map) {
			return ((Map<?, ?>) parentElement).entrySet().toArray();
		}

		if (parentElement instanceof StringNode) {
			return new Object[] { ((StringNode) parentElement).getContent() };
		}

		if (parentElement instanceof NamedList) {
			return ((NamedList) parentElement).getValues().toArray();
		}

		if (parentElement instanceof SDFSchema) {
			SDFSchema attributes = (SDFSchema) parentElement;
			Collection<Object> children = new ArrayList<Object>();
			if (attributes.getConstraints() != null) {
				for (SDFConstraint c : attributes.getConstraints()) {
					children.add(c);
				}
			}
			for (SDFAttribute a : attributes) {
				children.add(a);
			}
			return children.toArray();
		}

		if (parentElement instanceof SDFAttribute) {
			Collection<Object> children = new ArrayList<Object>();
			if (((SDFAttribute) parentElement).getDatatype().hasSchema()) {
				children.addAll(((SDFAttribute) parentElement).getDatatype()
						.getSchema().getAttributes());
			}
			if (((SDFAttribute) parentElement).getDtConstraints().size() > 0) {
				children.addAll(((SDFAttribute) parentElement)
						.getDtConstraints());
			}
			if (((SDFAttribute) parentElement).getUnit() != null) {
				children.add(((SDFAttribute) parentElement).getUnit());
			}
			return children.toArray();
		}

		return null;
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof IOdysseusNodeView) {
			return activeGraph;
		}

		if (element instanceof IMonitoringData<?>) {
			Object tgt = ((IMonitoringData<?>) element).getTarget();
			for (INodeView<IPhysicalOperator> n : activeGraph.getViewedNodes()) {
				if (n.getModelNode() == tgt)
					return n;
			}
		}

		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof NamedList) {
			return true;
		}
		if (element instanceof IOdysseusNodeView) {
			IOdysseusNodeView node = (IOdysseusNodeView) element;
			if (node.getModelNode() == null)
				return false;
			if (node.getModelNode().getContent() == null)
				return false;
			return true; // Every operator has an owner
			// if( node.getModelNode().getProvidedMetadataTypes().size() > 0)
			// return true;
			// if( node.getModelNode().getContent().getOutputSchema() != null )
			// return true;
		}
		if (element instanceof Collection<?>)
			return true;
		if (element instanceof Map)
			return true;
		if (element instanceof SDFSchema)
			return true;
		if (element instanceof SDFAttribute) {
			return ((SDFAttribute) element).getDatatype()
					.getSubattributeCount() > 0
					|| ((SDFAttribute) element).getDtConstraints().size() > 0
					|| ((SDFAttribute) element).getUnit() != null;
		}
		if (element instanceof StringNode) {
			return true;
		}
		return false;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public void dispose() {
		activeGraph = null;
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		activeGraph = (IOdysseusGraphView) newInput;

		viewer.refresh();
	}

}

class StringWrapper {
	public StringWrapper(String string) {
		this.content = string;
	}

	public String content;
}
