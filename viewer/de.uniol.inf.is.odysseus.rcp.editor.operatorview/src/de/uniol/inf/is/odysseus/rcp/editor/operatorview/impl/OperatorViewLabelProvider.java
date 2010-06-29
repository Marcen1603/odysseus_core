package de.uniol.inf.is.odysseus.rcp.editor.operatorview.impl;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import de.uniol.inf.is.odysseus.rcp.editor.model.cfg.IOperatorDescriptor;

public class OperatorViewLabelProvider implements ILabelProvider {

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
		return Activator.getDefault().getImageRegistry().get("operatorIcon");
	}

	@Override
	public String getText(Object element) {
		if( element instanceof IOperatorDescriptor ) {
			IOperatorDescriptor desc = (IOperatorDescriptor)element;
			if( desc.getLabel() != null && desc.getLabel().length() > 0 )
				return desc.getLabel();
			return desc.getName();
		}
		return null;
	}

}
