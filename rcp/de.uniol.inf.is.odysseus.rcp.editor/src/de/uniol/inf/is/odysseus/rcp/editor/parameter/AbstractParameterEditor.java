package de.uniol.inf.is.odysseus.rcp.editor.parameter;

import de.uniol.inf.is.odysseus.logicaloperator.builder.IOperatorBuilder;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter;
import de.uniol.inf.is.odysseus.rcp.editor.view.ParameterViewPart;

public abstract class AbstractParameterEditor implements IParameterEditor {

	private IOperatorBuilder builder;
	private IParameter<?> parameter;
	
	@Override
	public void init(IOperatorBuilder builder, IParameter<?> parameter) {
		this.builder = builder;
		this.parameter = parameter;
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
		for( Exception ex : getParameter().getErrors()) {
			sb.append(ex.getMessage());
			sb.append("\n");
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
	
}
