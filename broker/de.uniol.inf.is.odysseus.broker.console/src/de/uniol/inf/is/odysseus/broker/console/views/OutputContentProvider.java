package de.uniol.inf.is.odysseus.broker.console.views;

import java.util.Arrays;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class OutputContentProvider implements IStructuredContentProvider{
	 
	private Object[] values = new Object[0];
	
	public void addValue(Object newValue){
		Object[] newValues = Arrays.copyOf(values, values.length+1);
		for(int i=values.length;i>0;i--){
			newValues[i] = newValues[i-1];
		}
		newValues[0] = newValue;
		this.values = newValues;
	}
	
	
	@Override
	public Object[] getElements(Object inputElement) {		
		return values;
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		
	}


	public void clear() {
		this.values = new Object[0];
		
	}


	
	
}
