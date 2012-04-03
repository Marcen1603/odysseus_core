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
package de.uniol.inf.is.odysseus.rcp.viewer.stream.table;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.viewer.editors.StreamEditor;
import de.uniol.inf.is.odysseus.rcp.viewer.extension.IStreamEditorInput;
import de.uniol.inf.is.odysseus.rcp.viewer.extension.IStreamEditorType;
import de.uniol.inf.is.odysseus.relational.base.Tuple;

public class StreamTableEditor implements IStreamEditorType {

	private static final Logger LOG = LoggerFactory.getLogger(StreamTableEditor.class);
	
	private TableViewer viewer;
	private SDFSchema schema;

	private List<Tuple<?>> tuples = new ArrayList<Tuple<?>>();
	private int maxTuplesCount;

	public StreamTableEditor(int maxTuples) {
		setMaxTuplesCount(maxTuples);
	}

	@Override
	public void streamElementRecieved(Object element, int port) {
		if (!(element instanceof Tuple<?>)) {
			System.out.println("Warning: StreamTable is only for relational tuple!");
			return;
		}

		tuples.add(0, (Tuple<?>) element);
		if (tuples.size() > getMaxTuplesCount()) {
			tuples.remove(tuples.size() - 1);
		}

		if (hasTableViewer() && !getTableViewer().getTable().isDisposed()) {
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					if( !getTableViewer().getTable().isDisposed() )
						getTableViewer().refresh();
				}
			});
		}
	}

	@Override
	public void init(StreamEditor editorPart, IStreamEditorInput editorInput) {
		ISource<?>[] sources = editorInput.getStreamConnection().getSources().toArray(new ISource<?>[0]);
		setSchema(sources[0].getOutputSchema());
	}

	@Override
	public void createPartControl(Composite parent) {

		if (hasSchema() && getSchema().size() > 0) {
			TableColumnLayout tableColumnLayout = new TableColumnLayout();
			parent.setLayout(tableColumnLayout);

			setTableViewer(createTableViewer(parent, tableColumnLayout));
			getTableViewer().setContentProvider(createContentProvider());
			getTableViewer().setInput(tuples);

		} else {
			// Kein Schema vorhanden
			Label label = new Label(parent, SWT.NONE);
			label.setText("Operator provides no schema");
		}
	}

	@Override
	public void setFocus() {
		if (hasTableViewer())
			getTableViewer().getControl().setFocus();
	}

	@Override
	public void dispose() {
	}

	@Override
	public void punctuationElementRecieved(PointInTime point, int port) {
	}

	public final SDFSchema getSchema() {
		return schema;
	}

	public final int getMaxTuplesCount() {
		return maxTuplesCount;
	}

	protected final boolean hasSchema() {
		return getSchema() != null;
	}

	protected IContentProvider createContentProvider() {
		return ArrayContentProvider.getInstance();
	}

	protected TableViewer createTableViewer(Composite parent, TableColumnLayout layout) {
		TableViewer tableViewer = new TableViewer(parent, SWT.SINGLE | SWT.FULL_SELECTION | SWT.BORDER);
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);

		createTableColumns(tableViewer, layout);

		return tableViewer;
	}

	protected void createTableColumns(TableViewer tableViewer, TableColumnLayout layout) {
		final int weight = 1000 / getSchema().size();
		for (int i = 0; i < getSchema().size(); i++) {
			SDFAttribute attribute = getSchema().getAttribute(i);

			TableViewerColumn col = new TableViewerColumn(tableViewer, SWT.NONE);
			// col.getColumn().setText(attribute.getAttributeName());
			col.getColumn().setText(attribute.getURI());
			col.getColumn().setAlignment(SWT.CENTER);
			final int fi = i;
			col.setLabelProvider(new CellLabelProvider() {
				@Override
				public void update(ViewerCell cell) {
					try {
						Object attr = ((Tuple<?>) cell.getElement()).getAttribute(fi);
						if (attr != null){
							cell.setText(attr.toString());
						}else{
							cell.setText("<null>");
						}
					} catch( Throwable t ) {
						LOG.error("Could not retrieve attributeValue", t);
						cell.setText("###");
					}
				}
			});
			layout.setColumnData(col.getColumn(), new ColumnWeightData(weight, 25, true));
		}
	}

	protected final TableViewer getTableViewer() {
		return viewer;
	}

	protected final boolean hasTableViewer() {
		return getTableViewer() != null;
	}

	private void setTableViewer(TableViewer viewer) {
		this.viewer = viewer; // kann null sein
	}

	private void setSchema(SDFSchema schema) {
		this.schema = schema; // kann auch null sein!
	}

	private void setMaxTuplesCount(int maxTuples) {
		if (maxTuples > 0)
			this.maxTuplesCount = maxTuples;
		else
			this.maxTuplesCount = Integer.MAX_VALUE;
	}

}
