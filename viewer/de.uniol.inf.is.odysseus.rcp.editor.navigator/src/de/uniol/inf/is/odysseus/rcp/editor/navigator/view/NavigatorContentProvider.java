package de.uniol.inf.is.odysseus.rcp.editor.navigator.view;

import java.util.ArrayList;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class NavigatorContentProvider implements ITreeContentProvider {

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
		if( parentElement instanceof IContainer ) {
			IContainer c = (IContainer)parentElement;
			try {
				IResource[] res = c.members();
				ArrayList<IResource> ress = new ArrayList<IResource>();
				for( IResource r : res ) {
					if(!r.getName().startsWith("."))
						ress.add(r);
				}
				return ress.toArray();
			} catch (CoreException e) {}
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		if( element instanceof IResource ) {
			IResource r = (IResource)element;
			return r.getParent();
		}

		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if( element instanceof IContainer ) {
			try {
				return ((IContainer)element).members().length > 0;
			} catch (CoreException e) {} 
		}
		return false;
	}

}
