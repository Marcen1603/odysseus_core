package de.uniol.inf.is.odysseus.rcp.viewer.nodeview;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.IGraphView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.INodeView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.IOdysseusNodeView;

public class NodeViewContentProvider implements ITreeContentProvider {
	
	private IGraphView<IPhysicalOperator> activeGraph;
	
	@SuppressWarnings("unchecked")
	@Override
	public Object[] getChildren(Object parentElement) {
		
		if( parentElement instanceof IGraphView<?>) {
			return ((IGraphView<IPhysicalOperator>)parentElement).getViewedNodes().toArray();
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
		
		if( element instanceof String ) {
			return activeGraph; // BAD
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if( element instanceof IOdysseusNodeView ) {
			return ((IOdysseusNodeView)element).getModelNode().getProvidedMetadataTypes().size() > 0;
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

	@SuppressWarnings("unchecked")
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		activeGraph = (IGraphView<IPhysicalOperator>)newInput;
		
		viewer.refresh();
	}

}
