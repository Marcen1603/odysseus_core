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
package de.uniol.inf.is.odysseus.rcp.editor.parameters.editing;

import org.eclipse.jface.viewers.TableViewer;

public abstract class ComboEditingColumnDefinition<T> extends EditableColumnDefinition<T> {

	private String[] items;
	
	public ComboEditingColumnDefinition(String title, String[] items) {
		super(title);
		setItems(items);
	}

	@Override
	protected AbstractEditingSupport createEditingSupport(TableViewer viewer) {
		return new ComboEditingSupport(viewer, getItems()) {

			@SuppressWarnings("unchecked")
			@Override
			protected void doSetValue(Object element, Object value) {
				ComboEditingColumnDefinition.this.setValue((T)element, value.toString());
			}

			@SuppressWarnings("unchecked")
			@Override
			protected Object doGetValue(Object element) {
				return getStringValue((T)element);
			}
			
		};
	}
	
	public String[] getItems() {
		return items;
	}
	
	private void setItems( String[] items ) {
		if( items == null || items.length == 0)
			throw new IllegalArgumentException("items is invalid");
		this.items = items;
	}
}
