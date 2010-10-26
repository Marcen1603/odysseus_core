package de.uniol.inf.is.odysseus.rcp.editor.parameter;

import java.util.List;

import de.uniol.inf.is.odysseus.logicaloperator.builder.IOperatorBuilder;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.ListParameter;

public abstract class AbstractListParameterEditor<T> extends AbstractParameterEditor {

	private ListParameter<T> listParameter;
	
	@SuppressWarnings("unchecked")
	@Override
	public void init(IOperatorBuilder builder, IParameter<?> parameter, IParameterView view) {
		super.init(builder, parameter, view);
		
		if( parameter instanceof ListParameter ) {
			listParameter = (ListParameter<T>)parameter;
		} else {
			throw new IllegalArgumentException("parameter must be a ListParameter");
		}
	}
	
	protected ListParameter<T> getListParameter() {
		return listParameter;
	}
	
	@Override
	protected List<T> getValue() {
		try {
			return listParameter.getValue();
		} catch( RuntimeException ex ) {
			return null;
		}
	}
	
}
