package de.uniol.inf.is.odysseus.rcp.editor.operatorview.impl;

import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.uniol.inf.is.odysseus.rcp.editor.operator.IOperatorExtensionDescriptor;

public class OperatorViewContentProvider implements ITreeContentProvider {

	private Map<String, OperatorGroup> input;
	
	public OperatorViewContentProvider( Map<String, OperatorGroup> input ) {
		this.input = input;
	}
	
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
		if( parentElement instanceof List ) {
			return input.values().toArray();
		}
		if( parentElement instanceof OperatorGroup ) {
			OperatorGroup group= (OperatorGroup)parentElement;
			return group.getExtensions().toArray();
		}
		
		return null;
	}

	@Override
	public Object getParent(Object element) {
		if( element instanceof IOperatorExtensionDescriptor) {
			IOperatorExtensionDescriptor desc = (IOperatorExtensionDescriptor)element;
			return input.get(desc.getGroup());
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if( element instanceof OperatorGroup) return true;
		return false;
	}

}
