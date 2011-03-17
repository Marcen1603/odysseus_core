/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.rcp.editor.parameter;

import de.uniol.inf.is.odysseus.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.AbstractParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IOperatorBuilder;
import de.uniol.inf.is.odysseus.rcp.editor.views.IParameterView;

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
	
	protected void refreshView() {
		getView().layout();
		getView().refresh();
	}

	private IParameterView getView() {
		return view;
	}
}
