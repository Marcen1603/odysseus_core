package de.uniol.inf.is.odysseus.rcp.editor.parameter;

import de.uniol.inf.is.odysseus.logicaloperator.builder.AbstractParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IOperatorBuilder;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter;

public abstract class AbstractParameterEditor implements IParameterEditor {

	private IOperatorBuilder builder;
	private IParameter<?> parameter;
	private IParameterView view;
	
	@Override
	public void init(IOperatorBuilder builder, IParameter<?> parameter, IParameterView view) {
		this.builder = builder;
		this.parameter = parameter;
		this.view = view;
	}
	
	protected IOperatorBuilder getOperatorBuilder() {
		return builder;
	}
	
	protected IParameter<?> getParameter() {
		return parameter;
	}
	
	protected boolean validate() {
		return getParameter().validate();
	}
	
	protected String getErrorText() {
		StringBuilder sb = new StringBuilder();
		for( int i = 0; i < getParameter().getErrors().size(); i++ ) {
			Exception ex = getParameter().getErrors().get(i);
			if( i > 0 ) 
				sb.append("\n");
			sb.append(ex.getMessage());
		}
		return sb.toString();
	}
	
	protected void setValue(Object obj) {
		Object oldObj = ((AbstractParameter<?>)getParameter()).getInputValue();
		
		if( obj == null ) {
			if( oldObj != null ) {
				getParameter().clear();
				view.refresh();
			}
		} else if( !obj.equals(oldObj)) { 
			getParameter().setInputValue(obj);
			view.refresh();
		}
	}
	
	protected Object getValue() {
		try {
			return getParameter().getValue();
		} catch( RuntimeException ex ) {
			return null;
		}
	}

	protected IParameterView getView() {
		return view;
	}
}
