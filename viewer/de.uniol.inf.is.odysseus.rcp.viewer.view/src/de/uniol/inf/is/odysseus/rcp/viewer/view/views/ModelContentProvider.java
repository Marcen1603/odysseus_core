package de.uniol.inf.is.odysseus.rcp.viewer.view.views;

import java.util.Collection;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.viewer.model.Model;
import de.uniol.inf.is.odysseus.rcp.viewer.model.create.IModelManager;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IGraphModel;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IOdysseusNodeModel;

public class ModelContentProvider implements ITreeContentProvider{
	
	public ModelContentProvider() {
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object[] getChildren(Object parentElement) {
		if( parentElement instanceof IModelManager<?>) {
			IModelManager<IPhysicalOperator> manager = (IModelManager<IPhysicalOperator>)parentElement;
			return manager.getModels().toArray();
		}
		
		if( parentElement instanceof IGraphModel<?>) {
			IGraphModel<IPhysicalOperator> graph = (IGraphModel<IPhysicalOperator>)parentElement;
			return graph.getNodes().toArray();
		}
		
		return null;
	}

	@Override
	public Object getParent(Object element) {
		if( element instanceof IOdysseusNodeModel) {
			IOdysseusNodeModel node = (IOdysseusNodeModel)element;
			
			Collection<IGraphModel<IPhysicalOperator>> graphs = Model.getInstance().getModelManager().getModels();
			for( IGraphModel<IPhysicalOperator> graph : graphs ) {
				if( graph.getNodes().contains(node))
					return graph;
			}
		}
		
		if( element instanceof IGraphModel<?>) {
			return Model.getInstance().getModelManager();
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if( element instanceof IOdysseusNodeModel) return false;
		if( element instanceof IGraphModel<?>) return true;
		if( element instanceof IModelManager<?>) return true;
		return false;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public void dispose() {}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}
}
