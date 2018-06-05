package de.uniol.inf.is.odysseus.rcp.evaluation.editor;

import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

import de.uniol.inf.is.odysseus.rcp.evaluation.model.EvaluationModel;
import de.uniol.inf.is.odysseus.rcp.evaluation.model.EvaluationVariable;

public class EvaluationVariableContentProvider implements IStructuredContentProvider, ITableLabelProvider, ICheckStateProvider {

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof EvaluationModel) {
			return ((EvaluationModel)inputElement).getVariables().toArray();
		}
		if(inputElement instanceof EvaluationVariable){
			return ((EvaluationVariable)inputElement).getValues().toArray();
		}
		return new Object[0];
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		switch (columnIndex) {
		case 0:
			if(element instanceof EvaluationVariable){
				return ((EvaluationVariable)element).getName();
			}			
			if(element instanceof String){
				return "";
			}
			return null;
		case 1:
			if (element instanceof String) {
				return element.toString();
			}
			return null;
		default:
			return null;
		}
	}


	@Override
	public boolean isChecked(Object element) {
		if(element instanceof EvaluationVariable){
			EvaluationVariable var = (EvaluationVariable) element;
			return var.isActive();
		}
		return false;
	}

	@Override
	public boolean isGrayed(Object element) {		
		return false;
	}}
