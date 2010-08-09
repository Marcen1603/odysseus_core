package de.uniol.inf.is.odysseus.rcp.sources.view.part;

import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

public class SourcesContentProvider implements ITreeContentProvider {

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if( parentElement instanceof Set ) {
			return ((Set<?>)parentElement).toArray();
		}
		if( parentElement instanceof Entry ) {
			@SuppressWarnings("unchecked")
			Entry<String, ILogicalOperator> entry = (Entry<String, ILogicalOperator>)parentElement;
			return entry.getValue().getOutputSchema().toArray();
		}
		if( parentElement instanceof SDFAttribute ) {
			return ((SDFAttribute)parentElement).getSubattributes().toArray();
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if( element instanceof Entry ) 
			return true;
		if( element instanceof SDFAttribute )
			return ((SDFAttribute)element).getSubattributeCount() > 0;
		return false;
	}

}
