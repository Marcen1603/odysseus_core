package de.uniol.inf.is.odysseus.rcp.editor.navigator.view;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.rcp.editor.navigator.activator.Activator;

public class NavigatorLabelProvider implements ILabelProvider {

	@Override
	public void addListener(ILabelProviderListener listener) {

	}

	@Override
	public void dispose() {

	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {

	}

	@Override
	public Image getImage(Object element) {
		if( element instanceof IProject ) {
			return Activator.getDefault().getImageRegistry().get("project");
		}
		if( element instanceof IFolder ) 
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
		else if( element instanceof IFile ) {
			IFile file = (IFile)element;
			if(file.getName().endsWith(".pln"))
				return Activator.getDefault().getImageRegistry().get("plan");
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);
		}
		return null;
	}

	@Override
	public String getText(Object element) {
		if( element instanceof IResource ) {
			return ((IResource)element).getName();
		}

		return null;
	}

}
