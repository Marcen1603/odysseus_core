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

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;

public abstract class ComboEditingSupport extends AbstractEditingSupport {

	private String[] items;

	public ComboEditingSupport(TableViewer viewer, String[] items) {
		super(viewer);
		this.items = items;
		
		ComboBoxCellEditor boxCellEditor = ((ComboBoxCellEditor)getCellEditor());
		boxCellEditor.setItems(items);
		boxCellEditor.setStyle(SWT.READ_ONLY);
		boxCellEditor.create(viewer.getTable());
	}

	@Override
	protected CellEditor createCellEditor(TableViewer viewer) {
		return new ComboBoxCellEditor();
	}

	@Override
	protected void setValue(Object element, Object value) {
		doSetValue(element, items[Integer.valueOf(value.toString())]);
		getViewer().update(element, null);
	}

	@Override
	protected Object getValue(Object element) {
		String elementString = doGetValue(element) + "";
		int pos = findStringInList(elementString);

		return pos == -1 ? null : pos;
	}

	private int findStringInList(String string) {
		for (int i = 0; i < items.length; i++)
			if (items[i].equals(string))
				return i;
		return -1;
	}
}
