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

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;


public abstract class SimpleColumnDefinition<T> {

	private String title;
	
	public SimpleColumnDefinition( String title ) {
		setTitle(title);
	}
	
	public final String getTitle() {
		return title;
	}
	
	public final TableViewerColumn toTableViewerColumn( TableViewer viewer, TableColumnLayout layout ) {
		TableViewerColumn column = new TableViewerColumn(viewer, SWT.NONE);
		column.getColumn().setText(getTitle());
		layout.setColumnData(column.getColumn(), new ColumnWeightData(5, getWeight(), true));
		
		column.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				@SuppressWarnings("unchecked")
				T item = (T) cell.getElement();
				cell.setText(getStringValue(item));
			}
			
		});
		
		AbstractEditingSupport edSupport = createEditingSupport(viewer);
		if( edSupport != null )
			column.setEditingSupport(edSupport);
		
		return column;
	}
	
	protected int getWeight() {
		return 25;
	}
	
	protected AbstractEditingSupport createEditingSupport(TableViewer viewer) {
		return null; // no editing
	}
	
	private void setTitle( String title ) {
		if( title == null || title.length() == 0 )
			throw new IllegalArgumentException("title is invalid");
		this.title = title;
	}
	
	protected abstract String getStringValue( T element );

}
