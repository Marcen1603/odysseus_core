package de.uniol.inf.is.odysseus.rcp.viewer.nodeview.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.INodeView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.IOdysseusGraphView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.IOdysseusNodeView;

public class NodeViewContentProvider implements ITreeContentProvider {
	
	private IOdysseusGraphView activeGraph;
	
	@Override
	public Object[] getChildren(Object parentElement) {
		
		if( parentElement instanceof IOdysseusGraphView) {
			IOdysseusGraphView graph = (IOdysseusGraphView)parentElement;
			
			ArrayList<Object> list = new ArrayList<Object>();
			for( INodeView<IPhysicalOperator> node : graph.getViewedNodes()) {
				if( node.getModelNode() != null )
					list.add(node);
			}
			
			return list.toArray();
		}
		
		if( parentElement instanceof IOdysseusNodeView) {
			IOdysseusNodeView node = (IOdysseusNodeView)parentElement;
			
			Collection<Object> children = new ArrayList<Object>();
			for( String type : node.getModelNode().getProvidedMetadataTypes())
				children.add(node.getModelNode().getMetadataItem(type));

			return children.toArray();
		}
	
		
		return null;
	}

	@Override
	public Object getParent(Object element) {
		if( element instanceof IOdysseusNodeView ) {
			return activeGraph;
		}
		
		if( element instanceof IMonitoringData<?> ) {		
			Object tgt = ((IMonitoringData<?>)element).getTarget();
			for( INodeView<IPhysicalOperator> n : activeGraph.getViewedNodes()) {
				if( n.getModelNode() == tgt ) return n;
			}
		}

		System.out.println("getParent() mit " + element.getClass().getSimpleName() + " fehlgeschlagen");
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if( element instanceof IOdysseusNodeView ) {
			IOdysseusNodeView node = (IOdysseusNodeView)element;
			if( node.getModelNode() == null ) return false;
			return node.getModelNode().getProvidedMetadataTypes().size() > 0;
		}
		if( element instanceof Collection<?>) return true;
		
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
		activeGraph = (IOdysseusGraphView)newInput;
		
		viewer.refresh();
	}

}
