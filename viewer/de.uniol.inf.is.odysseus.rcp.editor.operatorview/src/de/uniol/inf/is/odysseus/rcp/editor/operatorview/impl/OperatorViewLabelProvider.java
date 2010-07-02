package de.uniol.inf.is.odysseus.rcp.editor.operatorview.impl;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import de.uniol.inf.is.odysseus.rcp.editor.operator.IOperatorExtensionDescriptor;

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
		if( element instanceof IOperatorExtensionDescriptor)
			return Activator.getDefault().getImageRegistry().get("operatorIcon");
		if( element instanceof OperatorGroup ) 
			return Activator.getDefault().getImageRegistry().get("operatorGroupIcon");
			
		return null;
	}

	@Override
	public String getText(Object element) {
		if( element instanceof IOperatorExtensionDescriptor ) {
			IOperatorExtensionDescriptor desc = (IOperatorExtensionDescriptor)element;
			if( desc.getLabel() != null && desc.getLabel().length() > 0 )
				return desc.getLabel();
			return desc.getID();
		}
		if( element instanceof OperatorGroup ) 
			return ((OperatorGroup)element).getName();
		return null;
	}

}
