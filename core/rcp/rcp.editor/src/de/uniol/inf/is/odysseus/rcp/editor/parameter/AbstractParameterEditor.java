package de.uniol.inf.is.odysseus.rcp.editor.parameter;

import de.uniol.inf.is.odysseus.logicaloperator.builder.IOperatorBuilder;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter;
import de.uniol.inf.is.odysseus.rcp.editor.view.ParameterViewPart;

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
		getParameter().setInputValue(obj);
		ParameterViewPart.getInstance().refresh();
	}
	
	protected Object getValue() {
		if( !validate() ) 
			return null;
		
		return getParameter().getValue();
	}

	protected IParameterView getView() {
		return view;
	}
}
