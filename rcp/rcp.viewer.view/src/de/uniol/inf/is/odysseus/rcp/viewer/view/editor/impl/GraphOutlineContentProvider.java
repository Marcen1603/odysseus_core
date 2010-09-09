package de.uniol.inf.is.odysseus.rcp.viewer.view.editor.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.INodeView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.IOdysseusGraphView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.IOdysseusNodeView;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class GraphOutlineContentProvider implements ITreeContentProvider {
	
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
			
			// Add Schemainformation
			if (node.getModelNode().getContent().getOutputSchema() != null){
//				for( SDFAttribute attr : node.getModelNode().getContent().getOutputSchema())
//					children.add(attr);
				children.add( node.getModelNode().getContent().getOutputSchema());
			}
			// Add Metadatainformation
			for( String type : node.getModelNode().getProvidedMetadataTypes())
				children.add(node.getModelNode().getMetadataItem(type));

			return children.toArray();
		}
		
		if (parentElement instanceof SDFAttributeList){
			SDFAttributeList attributes = (SDFAttributeList) parentElement;
			Collection<Object> children = new ArrayList<Object>();
			for (SDFAttribute a: attributes){
				children.add(a);
			}
			return children.toArray();
		}
		
		if( parentElement instanceof SDFAttribute ) {
			return ((SDFAttribute)parentElement).getSubattributes().toArray();
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
		
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if( element instanceof IOdysseusNodeView ) {
			IOdysseusNodeView node = (IOdysseusNodeView)element;
			if( node.getModelNode() == null ) return false;
			if( node.getModelNode().getContent() == null ) return false;
			if( node.getModelNode().getProvidedMetadataTypes().size() > 0) return true;
			if( node.getModelNode().getContent().getOutputSchema() != null ) return true;
		}
		if( element instanceof Collection<?>) return true;
		
		if( element instanceof SDFAttribute) {
			return ((SDFAttribute)element).getSubattributeCount() > 0;
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
		activeGraph = (IOdysseusGraphView)newInput;
		
		viewer.refresh();
	}

}
