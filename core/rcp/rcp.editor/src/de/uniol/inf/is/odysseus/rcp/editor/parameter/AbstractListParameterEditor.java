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

import java.util.List;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilder;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ListParameter;
import de.uniol.inf.is.odysseus.rcp.editor.views.IParameterView;

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
