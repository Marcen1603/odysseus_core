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
package de.uniol.inf.is.odysseus.broker.console.views;

import java.util.Arrays;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class OutputContentProvider implements IStructuredContentProvider{
	 
	private Object[] values = new Object[0];
	private String[] attributenames;
		
	public OutputContentProvider(String[] attrubuteNames){
		this.attributenames = attrubuteNames;
	}
	
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

	public void setAttributeNames(String[] names){
		this.attributenames = names;
	}
	
	public String[] getAttributeNames(){
		return this.attributenames;
	}
	
	
}
